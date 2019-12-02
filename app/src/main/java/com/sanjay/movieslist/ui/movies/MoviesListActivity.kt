/*
 * Created by Sanjay.Sah
 */

package com.sanjay.movieslist.ui.movies


import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import com.sanjay.movieslist.R
import com.sanjay.movieslist.constants.State
import com.sanjay.movieslist.ui.BaseActivity
import com.sanjay.movieslist.ui.movies.MoviesListAdapter.Companion.DATA_VIEW_TYPE
import com.sanjay.movieslist.ui.movies.MoviesListAdapter.Companion.FOOTER_VIEW_TYPE
import com.sanjay.movieslist.ui.search.SearchMoviesActivity
import kotlinx.android.synthetic.main.activity_movies_list.*
import kotlinx.android.synthetic.main.content_movie_list.*
import javax.inject.Inject


/**
 * Activity where list of all Now Playing Movies will be displayed. MVVM architecture is used to separate UI and business logic
 */
class MoviesListActivity : BaseActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: MoviesListViewModel
    private lateinit var moviesListAdapter: MoviesListAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movies_list)
        setSupportActionBar(toolbar)
        toolbar.title = getString(R.string.app_name)
        activityComponent.inject(this)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MoviesListViewModel::class.java)
        initAdapter()
        initState()
    }

    /**
     * Initializing the adapter
     */
    private fun initAdapter() {
        moviesListAdapter = MoviesListAdapter({ movie, imageView ->
            //Opening detail activity. Sending ImageView for shared element transition
            MovieDetailActivity.start(this, movie, imageView)

        }, {
            //On click of retry textview call the api again
            viewModel.retry()
        })
        recycler_view_movies.layoutManager =
            GridLayoutManager(this, 3)

        (recycler_view_movies.layoutManager as GridLayoutManager).spanSizeLookup = object : SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when (moviesListAdapter.getItemViewType(position)) {
                    DATA_VIEW_TYPE -> 1
                    FOOTER_VIEW_TYPE -> 3 //number of columns of the grid
                    else -> -1
                }
            }
        }
        //set the adapter
        recycler_view_movies.adapter = moviesListAdapter
        //Observing live data for changes, new changes are submitted to PagedAdapter
        viewModel.moviesList?.observe(this, Observer {
            moviesListAdapter.submitList(it)
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
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_activity_movies, menu)
        return super.onCreateOptionsMenu(menu)

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.action_search) {
            SearchMoviesActivity.start(this)
        }
        return super.onOptionsItemSelected(item)
    }
}