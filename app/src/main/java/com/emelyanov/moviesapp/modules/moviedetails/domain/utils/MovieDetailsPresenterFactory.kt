package com.emelyanov.moviesapp.modules.moviedetails.domain.utils

import com.emelyanov.moviesapp.modules.moviedetails.domain.MovieDetailsPresenter
import com.emelyanov.moviesapp.shared.domain.BasePresenterFactory
import com.emelyanov.moviesapp.shared.domain.BaseView
import com.emelyanov.moviesapp.shared.domain.services.moviesrepository.IMoviesRepository
import com.emelyanov.moviesapp.shared.domain.services.moviesrepository.MoviesRepository
import com.emelyanov.moviesapp.shared.domain.services.stringextractor.IStringExtractor
import com.emelyanov.moviesapp.shared.domain.services.stringextractor.StringExtractor
import dagger.hilt.android.scopes.FragmentScoped
import javax.inject.Inject


@FragmentScoped
class MovieDetailsPresenterFactory
@Inject
constructor(
    private val moviesRepository: IMoviesRepository,
    private val stringExtractor: IStringExtractor
) : BasePresenterFactory<MovieDetailsPresenter>() {
    override fun create(): MovieDetailsPresenter
    = MovieDetailsPresenter(moviesRepository, stringExtractor)
}