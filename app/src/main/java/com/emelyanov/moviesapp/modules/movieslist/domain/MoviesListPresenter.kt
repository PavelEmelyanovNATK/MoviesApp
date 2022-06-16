package com.emelyanov.moviesapp.modules.movieslist.domain

import com.emelyanov.moviesapp.modules.movieslist.domain.models.Genre
import com.emelyanov.moviesapp.modules.movieslist.domain.models.Movie
import com.emelyanov.moviesapp.shared.domain.BasePresenter
import com.emelyanov.moviesapp.shared.domain.BaseView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private typealias VS = MoviesListPresenter.ViewState

class MoviesListPresenter : BasePresenter<BaseView<VS>, VS>() {
    override var viewState: VS = ViewState.Loading
        set(value) {
            field = value
            view?.obtainState(value)
        }

    fun onGenreClick(genre: String) {
        if(viewState is ViewState.Presentation) {
            val state = viewState as ViewState.Presentation
            val curGenre = state.genres.find { it.name == genre } ?: return
            val prevGenre = state.genres.find { it.isSelected }
            viewState = state.copy(
                genres = state.genres.toMutableList().apply {
                    if(curGenre != prevGenre)
                        set(indexOf(curGenre), curGenre.copy(isSelected = true))
                    prevGenre?.let {
                        set(indexOf(prevGenre), prevGenre.copy(isSelected = false))
                    }
                }
            )
        }
    }

    fun loadMovies() {
        view?.let { v ->
            presenterScope.launch(Dispatchers.Main) {
                v.obtainState(ViewState.Loading)
                delay(5000)
                viewState = ViewState.Presentation(
                    genres = listOf(
                        Genre("genre 1"),
                        Genre("genre 2"),
                        Genre("genre 3")
                    ),
                    movies = listOf(
                        Movie("movie 1", ""),
                        Movie("movie 2", "")
                    )
                )
            }
        }
    }

    sealed interface ViewState {
        object Loading : ViewState
        data class Presentation(
            val genres: List<Genre>,
            val movies: List<Movie>
        ) : ViewState
        data class Error(val message: String) : ViewState
    }
}