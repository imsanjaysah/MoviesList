/*
 * Created by Sanjay.Sah
 */

package com.sanjay.movieslist.injection.module

import com.sanjay.movieslist.data.repository.MoviesDataSource
import com.sanjay.movieslist.data.repository.local.MoviesLocalDataSource
import com.sanjay.movieslist.data.repository.remote.MoviesRemoteDataSource
import com.sanjay.movieslist.injection.annotations.Local
import com.sanjay.movieslist.injection.annotations.Remote
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * @author Sanjay Sah on 14/11/2017.
 */

@Module
class RepositoryModule {

    @Provides
    @Singleton
    @Local
    fun providesLocalDataSource(localDataSource: MoviesLocalDataSource): MoviesDataSource = localDataSource

    @Provides
    @Singleton
    @Remote
    fun providesRemoteDataSource(remoteDataSource: MoviesRemoteDataSource): MoviesDataSource = remoteDataSource

}