package com.sanjay.movieslist.data.repository.remote

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.sanjay.movieslist.BuildConfig
import com.sanjay.movieslist.data.api.MoviesListService
import com.sanjay.movieslist.data.repository.remote.model.Movie
import com.sanjay.movieslist.data.repository.remote.model.MoviesResponse
import io.reactivex.Single
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations


class MoviesRemoteDataSourceTest {

    @Mock
    private lateinit var apiService: MoviesListService

    var remoteDataSource: MoviesRemoteDataSource? = null

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        remoteDataSource = MoviesRemoteDataSource(apiService)

    }

    @After
    fun tearDown() {
        remoteDataSource = null
    }

    @Test
    fun getNowPlayingMovies() {
        val response = MoviesResponse(listOf(Movie("", "", "", "", 1.0f, "")), 100, 1)

        val observable = Single.just(response)

        whenever(apiService.getNowPlayingMovies(BuildConfig.MOVIE_DB_API_KEY, "en-US", 1)).thenReturn(
            observable
        )

        remoteDataSource?.getNowPlayingMovies(BuildConfig.MOVIE_DB_API_KEY, 1)
        verify(apiService).getNowPlayingMovies(BuildConfig.MOVIE_DB_API_KEY, "en-US", 1)


    }

    @Test
    fun searchMovies() {
        val response = MoviesResponse(listOf(Movie("", "", "", "", 1.0f, "")), 100, 1)

        val observable = Single.just(response)

        whenever(apiService.searchMovies(BuildConfig.MOVIE_DB_API_KEY, "Fast and Furious", 1)).thenReturn(
            observable
        )

        remoteDataSource?.searchMovies(BuildConfig.MOVIE_DB_API_KEY, "Fast and Furious", 1)
        verify(apiService).searchMovies(BuildConfig.MOVIE_DB_API_KEY, "Fast and Furious", 1)
    }
}