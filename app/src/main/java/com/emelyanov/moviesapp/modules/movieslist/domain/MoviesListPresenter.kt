package com.emelyanov.moviesapp.modules.movieslist.domain

import android.util.Log
import com.emelyanov.moviesapp.modules.movieslist.domain.models.ViewGenre
import com.emelyanov.moviesapp.modules.movieslist.domain.models.ViewMovie
import com.emelyanov.moviesapp.modules.movieslist.domain.models.toViewGenre
import com.emelyanov.moviesapp.modules.movieslist.domain.models.toViewMovie
import com.emelyanov.moviesapp.shared.domain.BasePresenter
import com.emelyanov.moviesapp.shared.domain.BaseView
import com.emelyanov.moviesapp.shared.domain.services.moviesrepository.IMoviesRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch

private typealias VS = MoviesListPresenter.ViewState

class MoviesListPresenter(
    private val moviesRepository: IMoviesRepository
): BasePresenter<BaseView<VS>, VS>() {

    override var viewState: VS = ViewState.Loading
        set(value) {
            field = value
            view?.obtainState(value)
        }

    init {
        Log.d("Repository", "Presenter created")
    }

    fun onGenreClick(genre: String) {
        if(viewState is ViewState.Presentation) {
            val state = viewState as ViewState.Presentation
            viewState = ViewState.Loading
            val genres = moviesRepository.getGenres()
            val curGenre = genres.find { it.name == genre } ?: return
            val prevGenre = state.genres.find { it.isSelected }



            val movies = if(curGenre.name != prevGenre?.name)
                moviesRepository.getMovies(curGenre.name)
            else
                moviesRepository.getMovies()

            viewState = ViewState.Presentation(
                genres = genres.map {
                    it.toViewGenre(
                        isSelected = curGenre.name == it.name && curGenre.name != prevGenre?.name
                    )
                },
                movies = movies.map { it.toViewMovie() }
            )
        }
    }

    fun loadMovies() {
        presenterScope.launch {
            viewState = ViewState.Loading

            viewState = try {
                moviesRepository.refreshData()

                val movies = moviesRepository.getMovies()
                val genres = moviesRepository.getGenres()

                ViewState.Presentation(
                    genres = genres.map { it.toViewGenre() },
                    movies = movies.map { it.toViewMovie() }
                )
            } catch (ex: Exception) {
                ViewState.Error("")
            }
        }
    }

    sealed interface ViewState {
        object Loading : ViewState
        data class Presentation(
            val genres: List<ViewGenre>,
            val movies: List<ViewMovie>
        ) : ViewState
        data class Error(val message: String) : ViewState
    }
}