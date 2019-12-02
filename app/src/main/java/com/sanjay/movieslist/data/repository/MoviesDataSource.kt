/*
 * Created by Sanjay.Sah
 */

package com.sanjay.movieslist.data.repository

import com.sanjay.movieslist.data.repository.remote.model.Movie
import io.reactivex.Flowable

/**
 *
 *
 * @author Sanjay Sah
 */
interface MoviesDataSource {
    fun getNowPlayingMovies(apiKey: String, page: Int): Flowable<List<Movie>>

    fun searchMovies(apiKey: String, query: String, page: Int): Flowable<List<Movie>>
}