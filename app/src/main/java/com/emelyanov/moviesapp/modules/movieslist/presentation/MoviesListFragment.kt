package com.emelyanov.moviesapp.modules.movieslist.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.emelyanov.moviesapp.R
import com.emelyanov.moviesapp.databinding.FragmentMovieDetailsBinding
import com.emelyanov.moviesapp.databinding.FragmentMoviesListBinding

class MoviesListFragment : Fragment() {
    lateinit var binding: FragmentMoviesListBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMoviesListBinding.inflate(inflater, container, false)
        return binding.root
    }
}