package com.emelyanov.moviesapp.modules.moviedetails.domain


import com.emelyanov.moviesapp.shared.domain.BasePresenter
import com.emelyanov.moviesapp.shared.domain.BaseView
import com.emelyanov.moviesapp.shared.domain.services.moviesrepository.IMoviesRepository
import com.emelyanov.moviesapp.shared.domain.utils.requestExceptionHandler
import kotlinx.coroutines.launch

private typealias VS = MovieDetailsPresenter.ViewState

class MovieDetailsPresenter(
    private val moviesRepository: IMoviesRepository
) : BasePresenter<BaseView<VS>, VS>() {
    override var viewState: VS = ViewState.Loading
        set(value) {
            field = value
            view?.processState(value)
        }

    fun loadInfo(movieId: Int) {
        viewState = ViewState.Loading
        presenterScope.launch {
            requestExceptionHandler(
                onServerNotResponding = { viewState = ViewState.Error("Сервер не отвечает...") },
                onConnectionError = { viewState = ViewState.Error("Ошибка подключения.") },
                onNotFound = { viewState = ViewState.Error("Фильм не найден.") },
                onBadRequest = { viewState = ViewState.Error("Неверный запрос: ${it.message}") },
                onAnother = { viewState = ViewState.Error("Неизвестная ошибка: ${it.javaClass.simpleName}, ${it.message}") },
            ) {
                moviesRepository.refreshData()
                moviesRepository.getMovieDetails(movieId).apply {
                    viewState = ViewState.Presentation(
                        localizedName = localizedName,
                        name = name,
                        imageUrl = imageUrl,
                        year = year,
                        rating = rating,
                        description = description
                    )
                }
            }
        }
    }

    sealed interface ViewState {
        object Loading : ViewState
        data class Presentation(
            val localizedName: String,
            val name: String,
            val imageUrl: String,
            val year: Int,
            val rating: Float,
            val description: String
        ) : ViewState
        data class Error(val message: String) : ViewState
    }
}