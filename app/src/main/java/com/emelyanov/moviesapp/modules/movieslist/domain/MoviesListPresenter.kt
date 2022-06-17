package com.emelyanov.moviesapp.modules.movieslist.domain

import android.util.Log
import com.emelyanov.moviesapp.R
import com.emelyanov.moviesapp.modules.movieslist.domain.models.ViewGenre
import com.emelyanov.moviesapp.modules.movieslist.domain.models.ViewMovie
import com.emelyanov.moviesapp.modules.movieslist.domain.models.toViewGenre
import com.emelyanov.moviesapp.modules.movieslist.domain.models.toViewMovie
import com.emelyanov.moviesapp.navigation.core.CoreDestinations
import com.emelyanov.moviesapp.navigation.core.CoreNavProvider
import com.emelyanov.moviesapp.shared.domain.BasePresenter
import com.emelyanov.moviesapp.shared.domain.BaseView
import com.emelyanov.moviesapp.shared.domain.services.moviesrepository.IMoviesRepository
import com.emelyanov.moviesapp.shared.domain.services.stringextractor.IStringExtractor
import com.emelyanov.moviesapp.shared.domain.utils.requestExceptionHandler
import kotlinx.coroutines.launch

private typealias VS = MoviesListPresenter.ViewState

class MoviesListPresenter(
    private val moviesRepository: IMoviesRepository,
    private val coreNavProvider: CoreNavProvider,
    private val stringExtractor: IStringExtractor
): BasePresenter<BaseView<VS>, VS>() {

    override var viewState: VS = ViewState.Loading
        set(value) {
            field = value
            view?.processState(value)
        }

    init {
        Log.d("Repository", "Presenter created")
    }

    fun onGenreClick(genre: String) {
        if(viewState is ViewState.Presentation) {
            presenterScope.launch {
                listExceptionHandler {
                    val state = viewState as ViewState.Presentation
                    viewState = ViewState.Loading
                    val genres = moviesRepository.getGenres()
                    val clickedGenre = genres.find { it.name == genre }
                    if(clickedGenre == null) {
                        loadMovies()
                        return@listExceptionHandler
                    }
                    val prevGenre = state.genres.find { it.isSelected }

                    //Если предыдущий жанр равен текущему, то выделение снимается, фильмы загружаются без фильтра
                    val movies = if(clickedGenre.name == prevGenre?.name)
                        moviesRepository.getMovies()
                    else
                        moviesRepository.getMovies(clickedGenre.name)

                    viewState = ViewState.Presentation(
                        genres = genres.map {
                            it.toViewGenre(
                                //Выделяем совпадающий жанр только в случе, если он не был выделен, т.е. не равен предыдущему выделенному
                                isSelected = clickedGenre.name == it.name && clickedGenre.name != prevGenre?.name
                            )
                        },
                        movies = movies.map { it.toViewMovie() }
                    )
                }
            }
        }
    }

    fun onMovieClick(id: Int) {
        Log.d("Navigation id", id.toString())
        coreNavProvider.requestNavigate(CoreDestinations.MovieDetails(id))
    }

    fun loadMovies() {
        Log.d("Loading", "Started")
        viewState = ViewState.Loading

        presenterScope.launch {
            listExceptionHandler {
                    moviesRepository.refreshData()

                    val movies = moviesRepository.getMovies()
                    val genres = moviesRepository.getGenres()

                    viewState = ViewState.Presentation(
                        genres = genres.map { it.toViewGenre() },
                        movies = movies.map { it.toViewMovie() }
                    )
            }
        }
    }

    private suspend fun listExceptionHandler(block: suspend () -> Unit) = requestExceptionHandler(
        onServerNotResponding = { viewState = ViewState.Error(stringExtractor.getString(R.string.server_not_responding_message)) },
        onConnectionError = { viewState = ViewState.Error(stringExtractor.getString(R.string.connection_error_message)) },
        onNotFound = { viewState = ViewState.Error(stringExtractor.getString(R.string.movies_list_not_found_message)) },
        onBadRequest = { viewState = ViewState.Error(stringExtractor.getString(R.string.bad_request_message) + it.message) },
        onAnother = {
            viewState = ViewState.Error(
                stringExtractor.getString(R.string.undescribed_error_message) + "${it.javaClass.simpleName}, ${it.message}"
            )
        },
        block = block
    )

    sealed interface ViewState {
        object Loading : ViewState
        data class Presentation(
            val genres: List<ViewGenre>,
            val movies: List<ViewMovie>
        ) : ViewState
        data class Error(val message: String) : ViewState
    }
}