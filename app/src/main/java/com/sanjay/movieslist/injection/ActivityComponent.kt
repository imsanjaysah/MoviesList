/*
 * ActivityComponent.kt
 * Created by Sanjay.Sah
 */

package com.sanjay.movieslist.injection

import com.sanjay.movieslist.injection.annotations.PerActivity
import com.sanjay.movieslist.injection.module.ActivityModule
import com.sanjay.movieslist.ui.movies.MoviesListActivity
import com.sanjay.movieslist.ui.search.SearchMoviesActivity
import dagger.Subcomponent

/**
 * @author Sanjay Sah
 */

@PerActivity
@Subcomponent(modules = [ActivityModule::class])
interface ActivityComponent {
    fun inject(activity: MoviesListActivity)

    fun inject(activity: SearchMoviesActivity)

}