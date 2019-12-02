/*
 * Created by Sanjay.Sah
 */

package com.sanjay.movieslist.ui.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PageKeyedDataSource
import androidx.paging.PagedList
import com.sanjay.movieslist.BuildConfig
import com.sanjay.movieslist.constants.State
import com.sanjay.movieslist.data.repository.MoviesRepository
import com.sanjay.movieslist.data.repository.remote.model.Movie
import com.sanjay.movieslist.extensions.addToCompositeDisposable
import com.sanjay.movieslist.ui.BaseViewModel
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Action
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


/**
 * ViewModel class to handle the business logic. Activity will be updated using LiveData events
 */
class SearchedMoviesListViewModel @Inject constructor(
    private val repository: MoviesRepository
) : BaseViewModel() {

    //LiveData object for searched movies
    var moviesList: LiveData<PagedList<Movie>>? = null
    //LiveData object for state
    var state = MutableLiveData<State>()
    var searchedText = MutableLiveData<String>()
    //Completable required for retrying the API call which gets failed due to any error like no internet
    private var retryCompletable: Completable? = null

    init {
        //Setting up Paging for fetching the movies in pagination
        val config = PagedList.Config.Builder()
            .setPageSize(5)
            .setEnablePlaceholders(false)
            .build()

        moviesList = Transformations.switchMap(searchedText) { input ->
            LivePagedListBuilder<Int, Movie>(
                SearchedMoviesDataSourceFactory(input), config
            ).build()
        }
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

    inner class SearchedMoviesDataSourceFactory(val query: String) :
        DataSource.Factory<Int, Movie>() {

        override fun create(): DataSource<Int, Movie> {
            return SearchedMoviesDataSource(query)
        }
    }

    inner class SearchedMoviesDataSource(val query: String) :
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
            repository.searchMovies(BuildConfig.MOVIE_DB_API_KEY, query, currentPage)
                .subscribe(
                    { movies ->
                        updateState(State.DONE)
                        //if (movies.isNotEmpty())
                        Log.d("Movies Count", movies.size.toString())
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
            repository.searchMovies(BuildConfig.MOVIE_DB_API_KEY, query, currentPage).subscribe(
                { movies ->
                    updateState(State.DONE)
                    Log.d("Movies Count", movies.size.toString())
                    //if (movies.isNotEmpty())
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