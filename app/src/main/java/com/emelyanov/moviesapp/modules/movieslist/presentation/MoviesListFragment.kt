package com.emelyanov.moviesapp.modules.movieslist.presentation

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.emelyanov.moviesapp.databinding.FragmentMoviesListBinding
import com.emelyanov.moviesapp.modules.movieslist.domain.MoviesListPresenter
import com.emelyanov.moviesapp.shared.domain.BaseView

class MoviesListFragment : Fragment(), BaseView<MoviesListPresenter.ViewState> {
    lateinit var binding: FragmentMoviesListBinding
    private lateinit var presenter: MoviesListPresenter
    private val moviesAdapter = MoviesListAdapter()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMoviesListBinding.inflate(inflater, container, false)

        presenter = MoviesListPresenter()
        presenter.bindView(this)
        presenter.loadMovies()

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
                    addMovie(it.name, it.imageUrl) {}
                }
            }
        }
        binding.moviesProgressbar.visibility = if(viewState is MoviesListPresenter.ViewState.Loading) View.VISIBLE else View.GONE
    }

    override fun onStop() {
        super.onStop()
        presenter.unbindView()
        Log.d("Fragment", "Stop")
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
    }
}