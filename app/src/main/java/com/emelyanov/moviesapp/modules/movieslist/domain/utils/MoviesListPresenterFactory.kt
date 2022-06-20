package com.emelyanov.moviesapp.modules.movieslist.domain.utils

import com.emelyanov.moviesapp.modules.movieslist.domain.MoviesListPresenter
import com.emelyanov.moviesapp.navigation.core.CoreNavProvider
import com.emelyanov.moviesapp.shared.domain.BasePresenterFactory
import com.emelyanov.moviesapp.shared.domain.services.moviesrepository.IMoviesRepository
import com.emelyanov.moviesapp.shared.domain.services.moviesrepository.MoviesRepository
import com.emelyanov.moviesapp.shared.domain.services.stringextractor.IStringExtractor
import com.emelyanov.moviesapp.shared.domain.services.stringextractor.StringExtractor
import dagger.hilt.android.scopes.FragmentScoped
import javax.inject.Inject

@FragmentScoped
class MoviesListPresenterFactory
@Inject
constructor(
    private val moviesRepository: IMoviesRepository,
    private val coreNavProvider: CoreNavProvider,
    private val stringExtractor: IStringExtractor
) : BasePresenterFactory<MoviesListPresenter> {
    override fun create(): MoviesListPresenter
    = MoviesListPresenter(moviesRepository, coreNavProvider, stringExtractor)
}