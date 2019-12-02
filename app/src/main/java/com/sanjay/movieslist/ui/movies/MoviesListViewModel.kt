/*
 * Created by Sanjay.Sah
 */

package com.sanjay.movieslist.ui.movies

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.*
import com.sanjay.movieslist.constants.State
import com.sanjay.movieslist.data.repository.MoviesRepository
import com.sanjay.movieslist.data.repository.remote.model.Movie
import com.sanjay.movieslist.extensions.addToCompositeDisposable
import com.sanjay.movieslist.ui.BaseViewModel
import com.sanjay.movieslist.BuildConfig


import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Action
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * ViewModel class to handle the business logic. Activity will be updated using LiveData events
 */
class MoviesListViewModel @Inject constructor(
    private val repository: MoviesRepository
) : BaseViewModel() {

    //LiveData object for movies
    var moviesList: LiveData<PagedList<Movie>>? = null
    //LiveData object for state
    var state = MutableLiveData<State>()
    //Completable required for retrying the API call which gets failed due to any error like no internet
    private var retryCompletable: Completable? = null

    init {
        //Setting up Paging for fetching the movies in pagination
        val config = PagedList.Config.Builder()
            .setPageSize(5)
            .setEnablePlaceholders(false)
            .build()
        moviesList = LivePagedListBuilder<Int, Movie>(
            MoviesDataSourceFactory(), config
        ).build()
    }

    fun listIsEmpty(): Boolean {
        return moviesList?.value?.isEmpty() ?: true
    }

    private fun updateState(state: State) {
        this.state.postValue(state)
    }

    //Retrying the API call
    fun retry() {
        if (retryCompletable != null) {
            disposable.add(
                retryCompletable!!
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe()
            )
        }
    }

    /**
     * Creating the observable for specific page to call the API
     */
    private fun setRetry(action: Action?) {
        retryCompletable = if (action == null) null else Completable.fromAction(action)
    }

    inner class MoviesDataSourceFactory() :
        DataSource.Factory<Int, Movie>() {

        override fun create(): DataSource<Int, Movie> {
            return MoviesDataSource()
        }
    }

    inner class MoviesDataSource() :
        PageKeyedDataSource<Int, Movie>() {

        override fun loadInitial(
            params: LoadInitialParams<Int>,
            callback: LoadInitialCallback<Int, Movie>
        ) {
            updateState(State.LOADING)
            val currentPage = 1
            val nextPage = currentPage + 1
            Log.d("Movies loadInitial", "$currentPage")
            //Call api
            repository.getNowPlayingMovies(BuildConfig.MOVIE_DB_API_KEY, currentPage)
                .subscribe(
                    { movies ->
                        updateState(State.DONE)
                        callback.onResult(movies, null, nextPage)

                    },
                    {
                        updateState(State.ERROR)
                        setRetry(Action { loadInitial(params, callback) })
                    }
                ).addToCompositeDisposable(disposable)
        }

        override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {
            updateState(State.LOADING)
            val currentPage = params.key
            val nextPage = currentPage + 1
            Log.d("Movies loadAfter", "$currentPage")
            //Call api
            repository.getNowPlayingMovies(BuildConfig.MOVIE_DB_API_KEY, currentPage).subscribe(
                { movies ->
                    updateState(State.DONE)
                    callback.onResult(movies, nextPage)

                },
                {
                    updateState(State.ERROR)
                    setRetry(Action { loadAfter(params, callback) })
                }
            ).addToCompositeDisposable(disposable)
        }

        override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {
        }
    }

}