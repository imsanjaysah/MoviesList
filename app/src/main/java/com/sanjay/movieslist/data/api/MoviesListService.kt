/*
 * Created by Sanjay.Sah
 */

package com.sanjay.movieslist.data.api

import com.sanjay.movieslist.data.repository.remote.model.MoviesResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

/**Interface where all the api used in app are defined.
 * @author Sanjay.Sah
 */
interface MoviesListService {

    /**
     * Api for fetching Now Playing movies
     */
    @GET("movie/now_playing")
    fun getNowPlayingMovies(
        @Query("api_key") apiKey: String, @Query("language") language: String, @Query(
            "page"
        ) page: Int
    ): Single<MoviesResponse>


    /**
     * Api for searching movies
     */
    @GET("search/movie")
    fun searchMovies(
        @Query("api_key") apiKey: String, @Query("query") query: String, @Query(
            "page"
        ) page: Int
    ): Single<MoviesResponse>


}