/*
 * Created by Sanjay.Sah
 */

package com.sanjay.movieslist.data.repository.local

import com.sanjay.movieslist.data.repository.MoviesDataSource
import com.sanjay.movieslist.data.repository.remote.model.Movie
import io.reactivex.Flowable
import javax.inject.Inject

/**
 * Class to handle local db operations
 *
 * @author Sanjay Sah
 */
class MoviesLocalDataSource @Inject constructor() : MoviesDataSource {
    override fun getNowPlayingMovies(apiKey: String, page: Int): Flowable<List<Movie>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun searchMovies(apiKey: String, query: String, page: Int): Flowable<List<Movie>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}