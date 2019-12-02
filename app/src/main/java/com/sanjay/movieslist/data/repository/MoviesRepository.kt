/*
 * Created by Sanjay.Sah
 */

package com.sanjay.movieslist.data.repository

import com.sanjay.movieslist.data.repository.remote.model.Movie
import com.sanjay.movieslist.injection.annotations.Local
import com.sanjay.movieslist.injection.annotations.Remote
import io.reactivex.Flowable
import javax.inject.Inject

/**
 *
 * @author Sanjay Sah.
 */
open class MoviesRepository @Inject constructor(@Local private val localDataSource: MoviesDataSource, @Remote private val remoteDataSource: MoviesDataSource) :
    MoviesDataSource {
    override fun getNowPlayingMovies(apiKey: String, page: Int): Flowable<List<Movie>> =
        remoteDataSource.getNowPlayingMovies(apiKey, page)

    override fun searchMovies(apiKey: String, query: String, page: Int): Flowable<List<Movie>> =
        remoteDataSource.searchMovies(apiKey, query, page)
}