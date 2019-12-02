/*
 * ViewModelModule.kt
 * Created by Sanjay.Sah
 */

package com.sanjay.movieslist.injection.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sanjay.movieslist.injection.ViewModelFactory
import com.sanjay.movieslist.ui.movies.MoviesListViewModel
import com.sanjay.movieslist.ui.search.SearchedMoviesListViewModel
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
open class ViewModelModule {

    @Provides
    fun provideViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory = factory

    @Provides
    @IntoMap
    @ViewModelFactory.ViewModelKey(MoviesListViewModel::class)
    fun providemovieslistViewModel(viewModel: MoviesListViewModel): ViewModel = viewModel

    @Provides
    @IntoMap
    @ViewModelFactory.ViewModelKey(SearchedMoviesListViewModel::class)
    fun providesSearchMoviesListViewModel(viewModel: SearchedMoviesListViewModel): ViewModel = viewModel


}