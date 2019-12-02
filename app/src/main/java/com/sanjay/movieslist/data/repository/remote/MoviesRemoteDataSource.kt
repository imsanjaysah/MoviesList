/*
 * Created by Sanjay.Sah
 */

package com.sanjay.movieslist.data.repository.remote

import com.sanjay.movieslist.data.api.MoviesListService
import com.sanjay.movieslist.data.repository.MoviesDataSource
import com.sanjay.movieslist.data.repository.remote.model.Movie
import io.reactivex.Flowable
import javax.inject.Inject

/**
 * Class to handle remote operations
 *
 * @author Sanjay.Sah
 */
class MoviesRemoteDataSource @Inject constructor(private var remoteService: MoviesListService) :
    MoviesDataSource {
    override fun getNowPlayingMovies(apiKey: String, page: Int): Flowable<List<Movie>> {
        return remoteService.getNowPlayingMovies(apiKey,"en-US", page).map {
            it.movies
        }.toFlowable().take(1)
    }

    override fun searchMovies(apiKey: String, query: String, page: Int): Flowable<List<Movie>> {
        return remoteService.searchMovies(apiKey, query, page).map {
            it.movies
        }.toFlowable().take(1)
    }

}