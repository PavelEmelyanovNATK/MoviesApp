package com.emelyanov.moviesapp.modules.moviedetails.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import coil.load
import com.emelyanov.moviesapp.R
import com.emelyanov.moviesapp.databinding.FragmentMovieDetailsBinding
import com.emelyanov.moviesapp.modules.moviedetails.domain.MovieDetailsPresenter
import com.emelyanov.moviesapp.modules.moviedetails.domain.utils.MovieDetailsPresenterFactory
import com.emelyanov.moviesapp.shared.domain.BasePresenterFactory
import com.emelyanov.moviesapp.shared.domain.BaseView
import com.emelyanov.moviesapp.shared.domain.utils.supportActionBar
import com.emelyanov.moviesapp.shared.presentation.PresenterFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

private typealias VS = MovieDetailsPresenter.ViewState
private typealias V = BaseView<VS>
private typealias P = MovieDetailsPresenter

@AndroidEntryPoint
class MovieDetailsFragment : PresenterFragment<VS, V, P>(), V {
    lateinit var binding: FragmentMovieDetailsBinding

    @Inject
    lateinit var presenterFactory: MovieDetailsPresenterFactory

    private val movieId: Int
        get() = requireArguments().getInt(MOVIE_ID_KEY)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMovieDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        presenter.bindView(this)

        if(isFirstLoaded) {
            presenter.loadInfo(movieId)
        }
    }

    override fun onPause() {
        super.onPause()
        presenter.unbindView()
    }

    companion object {
        private const val MOVIE_ID_KEY = "movieIdKey123"

        fun createArguments(id: Int): Bundle {
            return bundleOf(
                MOVIE_ID_KEY to id
            )
        }
    }

    override fun createPresenterFactory(): BasePresenterFactory<P> = presenterFactory

    override fun processState(viewState: VS) {
        binding.movieProgressbar.visibility = if(viewState is MovieDetailsPresenter.ViewState.Loading) View.VISIBLE else View.GONE
        binding.movieContent.visibility = if(viewState is MovieDetailsPresenter.ViewState.Presentation) View.VISIBLE else View.GONE

        supportActionBar?.title = when(viewState) {
            is MovieDetailsPresenter.ViewState.Loading -> "Загрузка..."
            is MovieDetailsPresenter.ViewState.Presentation -> viewState.localizedName
            is MovieDetailsPresenter.ViewState.Error -> "Ошибка"
        }

        binding.movieName.text = if(viewState is MovieDetailsPresenter.ViewState.Presentation) viewState.name else null
        binding.movieYear.text = if(viewState is MovieDetailsPresenter.ViewState.Presentation) viewState.year.toString() else null
        binding.movieRate.text = if(viewState is MovieDetailsPresenter.ViewState.Presentation) viewState.rating.toString() else null
        binding.movieDescription.text = if(viewState is MovieDetailsPresenter.ViewState.Presentation) viewState.description else null

        if(viewState is MovieDetailsPresenter.ViewState.Presentation) {
            binding.movieImage.load(viewState.imageUrl) {
                this.listener(
                    onStart = { _ ->
                        binding.movieImageError.visibility = View.GONE
                    },
                    onError = { _, _ ->
                        binding.movieImageError.visibility = View.VISIBLE
                    }
                )
            }
        }

        binding.errorState.root.visibility = if(viewState is MovieDetailsPresenter.ViewState.Error) View.VISIBLE else View.GONE
        binding.errorState.errorMessage.text = if(viewState is MovieDetailsPresenter.ViewState.Error) viewState.message else null

        if(viewState is MovieDetailsPresenter.ViewState.Error) {
            binding.errorState.refreshButton.setOnClickListener { presenter.loadInfo(movieId) }
        }
    }
}