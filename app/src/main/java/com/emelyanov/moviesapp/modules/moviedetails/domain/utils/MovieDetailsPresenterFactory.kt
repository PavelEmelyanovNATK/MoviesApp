package com.emelyanov.moviesapp.modules.moviedetails.domain.utils

import com.emelyanov.moviesapp.modules.moviedetails.domain.MovieDetailsPresenter
import com.emelyanov.moviesapp.shared.domain.BasePresenterFactory
import com.emelyanov.moviesapp.shared.domain.BaseView
import com.emelyanov.moviesapp.shared.domain.services.moviesrepository.IMoviesRepository
import com.emelyanov.moviesapp.shared.domain.services.moviesrepository.MoviesRepository
import dagger.hilt.android.scopes.FragmentScoped
import javax.inject.Inject


@FragmentScoped
class MovieDetailsPresenterFactory
@Inject
constructor(
    private val moviesRepository: IMoviesRepository
) : BasePresenterFactory<MovieDetailsPresenter>() {
    override fun create(): MovieDetailsPresenter
    = MovieDetailsPresenter(moviesRepository)
}