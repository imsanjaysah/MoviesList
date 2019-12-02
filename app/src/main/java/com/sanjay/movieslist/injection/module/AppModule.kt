/*
 * AppModule.kt
 * Created by Sanjay.Sah
 */

package com.sanjay.movieslist.injection.module


import android.content.Context
import com.sanjay.movieslist.MoviesListApplication
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * @author Sanjay Sah
 */

@Module
open class AppModule(private val application: MoviesListApplication) {

    @Provides
    @Singleton
    fun providesApplicationContext(): Context = application

    @Provides
    @Singleton
    fun providesApplication(): MoviesListApplication = application

}