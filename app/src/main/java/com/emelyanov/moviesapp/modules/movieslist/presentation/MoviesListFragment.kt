package com.emelyanov.moviesapp.modules.movieslist.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.LinearLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.emelyanov.moviesapp.R
import com.emelyanov.moviesapp.databinding.FragmentMovieDetailsBinding
import com.emelyanov.moviesapp.databinding.FragmentMoviesListBinding
import com.emelyanov.moviesapp.modules.movieslist.domain.models.MoviesRecyclerItem

class MoviesListFragment : Fragment() {
    lateinit var binding: FragmentMoviesListBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMoviesListBinding.inflate(inflater, container, false)

        val adapter = MoviesListAdapter()
        val layoutManager = GridLayoutManager(requireContext(), 2, RecyclerView.VERTICAL, false).apply {
            spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return when (adapter.getItemViewType(position)) {
                        MoviesListAdapter.HEADER_VIEW_TYPE -> 2
                        MoviesListAdapter.GENRE_VIEW_TYPE -> 2
                        else -> 1
                    }
                }
            }
        }

        adapter.builder = MoviesListAdapter.MoviesRecyclerItemsBuilder().apply {
            repeat(7) {
                addGenre(it.toString(), false)
            }
            repeat(3) {
                addMovie(it.toString(), "")
            }
        }

        binding.moviesList.adapter = adapter
        binding.moviesList.layoutManager = layoutManager

        return binding.root
    }
}