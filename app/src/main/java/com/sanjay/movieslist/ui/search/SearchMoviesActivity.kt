package com.sanjay.movieslist.ui.search

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout.HORIZONTAL
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jakewharton.rxbinding3.widget.textChanges
import com.sanjay.movieslist.R
import com.sanjay.movieslist.constants.State
import com.sanjay.movieslist.ui.BaseActivity
import com.sanjay.movieslist.ui.movies.MovieDetailActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_searched_movies_list.*
import kotlinx.android.synthetic.main.content_searched_movie_list.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class SearchMoviesActivity : BaseActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: SearchedMoviesListViewModel
    private lateinit var moviesListAdapter: MoviesListAdapter

    private var searchSubscription: Disposable? = null

    private var isQueryChanged: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_searched_movies_list)
        setSupportActionBar(toolbar)
        toolbar.title = getString(R.string.search_movie)
        activityComponent.inject(this)
        viewModel =
            ViewModelProvider(this, viewModelFactory).get(SearchedMoviesListViewModel::class.java)
        recycler_view_searched_movies.layoutManager = LinearLayoutManager(
            this, LinearLayoutManager.VERTICAL,
            false
        )
        initAdapter()
        initState()
    }

    /**
     * Initializing the adapter
     */
    private fun initAdapter() {
        moviesListAdapter = MoviesListAdapter({ movie ->
            //Opening detail activity. Sending ImageView for shared element transition
            MovieDetailActivity.start(this, movie)

        }, {
            //On click of retry textview call the api again
            viewModel.retry()
        })
        recycler_view_searched_movies.layoutManager =
            LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        //set the adapter
        recycler_view_searched_movies.adapter = moviesListAdapter
        //Observing live data for changes, new changes are submitted to PagedAdapter
        viewModel.moviesList?.observe(this, Observer {
            moviesListAdapter.submitList(if (it.isNotEmpty()) it else null)
            //Workaround to fix this issue
            //https://stackoverflow.com/questions/30220771/recyclerview-inconsistency-detected-invalid-item-position
            if (isQueryChanged){
                moviesListAdapter.notifyDataSetChanged()
                isQueryChanged = false
            }
        })
    }

    /**
     * Initializing the state
     */
    private fun initState() {
        //On click of retry textview call the api again
        txt_error.setOnClickListener { viewModel.retry() }
        //Observing the different states of the API calling, and updating the UI accordingly
        viewModel.state.observe(this, Observer { state ->
            progress_bar.visibility =
                if (viewModel.listIsEmpty() && state == State.LOADING) View.VISIBLE else View.GONE
            txt_error.visibility =
                if (viewModel.listIsEmpty() && state == State.ERROR) View.VISIBLE else View.GONE
            if (!viewModel.listIsEmpty()) {
                moviesListAdapter.setState(state ?: State.DONE)
            }
        })

        searchSubscription =
            et_search_movie.textChanges()
                .skip(1)
                .debounce(500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread()).subscribe {
                    isQueryChanged = true
                    viewModel.searchedText.value = it.toString()
                }
    }

    override fun onDestroy() {
        searchSubscription?.dispose()
        super.onDestroy()
    }

    companion object {
        fun start(activity: Activity) {
            activity.startActivity(Intent(activity, SearchMoviesActivity::class.java))
        }
    }
}