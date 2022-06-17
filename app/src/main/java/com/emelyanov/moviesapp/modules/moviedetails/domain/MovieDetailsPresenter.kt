package com.emelyanov.moviesapp.modules.moviedetails.domain


import com.emelyanov.moviesapp.R
import com.emelyanov.moviesapp.shared.domain.BasePresenter
import com.emelyanov.moviesapp.shared.domain.BaseView
import com.emelyanov.moviesapp.shared.domain.services.moviesrepository.IMoviesRepository
import com.emelyanov.moviesapp.shared.domain.services.stringextractor.IStringExtractor
import com.emelyanov.moviesapp.shared.domain.services.stringextractor.StringExtractor
import com.emelyanov.moviesapp.shared.domain.utils.requestExceptionHandler
import kotlinx.coroutines.launch

private typealias VS = MovieDetailsPresenter.ViewState

class MovieDetailsPresenter(
    private val moviesRepository: IMoviesRepository,
    private val stringExtractor: IStringExtractor
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
                onServerNotResponding = { viewState = ViewState.Error(stringExtractor.getString(R.string.server_not_responding_message)) },
                onConnectionError = { viewState = ViewState.Error(stringExtractor.getString(R.string.connection_error_message)) },
                onNotFound = { viewState = ViewState.Error(stringExtractor.getString(R.string.movie_details_not_found)) },
                onBadRequest = { viewState = ViewState.Error(stringExtractor.getString(R.string.bad_request_message) + it.message) },
                onAnother = {
                    viewState = ViewState.Error(
                        stringExtractor.getString(R.string.undescribed_error_message) + "${it.javaClass.simpleName}, ${it.message}"
                    )
                },
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