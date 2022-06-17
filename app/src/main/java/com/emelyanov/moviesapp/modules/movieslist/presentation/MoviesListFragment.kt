package com.emelyanov.moviesapp.modules.movieslist.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.emelyanov.moviesapp.databinding.FragmentMoviesListBinding
import com.emelyanov.moviesapp.modules.movieslist.domain.MoviesListPresenter
import com.emelyanov.moviesapp.modules.movieslist.domain.utils.MoviesListPresenterFactory
import com.emelyanov.moviesapp.shared.domain.BasePresenterFactory
import com.emelyanov.moviesapp.shared.domain.BaseView
import com.emelyanov.moviesapp.shared.presentation.PresenterFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

typealias VS = MoviesListPresenter.ViewState
typealias V = BaseView<VS>
typealias P = MoviesListPresenter

@AndroidEntryPoint
class MoviesListFragment : PresenterFragment<VS, V, P>(), V {
    lateinit var binding: FragmentMoviesListBinding
    private val moviesAdapter = MoviesListAdapter()

    @Inject
    lateinit var presenterFactory: MoviesListPresenterFactory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMoviesListBinding.inflate(inflater, container, false)

        val layoutManager = GridLayoutManager(requireContext(), 2, RecyclerView.VERTICAL, false).apply {
            spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return when (moviesAdapter.getItemViewType(position)) {
                        MoviesListAdapter.HEADER_VIEW_TYPE -> 2
                        MoviesListAdapter.GENRE_VIEW_TYPE -> 2
                        else -> 1
                    }
                }
            }
        }

        binding.moviesList.adapter = moviesAdapter
        binding.moviesList.layoutManager = layoutManager

        return binding.root
    }

    override fun obtainState(viewState: MoviesListPresenter.ViewState) {
        binding.moviesList.visibility = if(viewState is MoviesListPresenter.ViewState.Presentation) View.VISIBLE else View.GONE
        moviesAdapter.builder = MoviesListAdapter.MoviesRecyclerItemsBuilder().apply {
            if(viewState is MoviesListPresenter.ViewState.Presentation) {
                viewState.genres.forEach {
                    addGenre(it.name, it.isSelected) { presenter.onGenreClick(it.name) }
                }
                viewState.movies.forEach {
                    addMovie(it.localizedName, it.imageUrl) {}
                }
            }
        }
        binding.moviesProgressbar.visibility = if(viewState is MoviesListPresenter.ViewState.Loading) View.VISIBLE else View.GONE
    }

    override fun onResume() {
        super.onResume()
        presenter.bindView(this)

        if(isFirstLoaded) {
            presenter.loadMovies()
        }
    }

    override fun onPause() {
        super.onPause()
        presenter.unbindView()
    }

    override fun createPresenterFactory(): BasePresenterFactory<P> {
        return presenterFactory
    }
}