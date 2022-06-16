package com.emelyanov.moviesapp.modules.moviedetails.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.emelyanov.moviesapp.R
import com.emelyanov.moviesapp.databinding.FragmentMovieDetailsBinding

class MovieDetailsFragment : Fragment() {
    lateinit var binding: FragmentMovieDetailsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMovieDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }
}