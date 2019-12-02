/*
 * DaggerExtensions.kt
 * Created by Sanjay.Sah
 */

package com.sanjay.movieslist.extensions
import android.content.Context
import com.sanjay.movieslist.MoviesListApplication

/**
 * Created by Sanjay Sah
 */

val Context.appComponent
    get() = (applicationContext as MoviesListApplication).appComponent
