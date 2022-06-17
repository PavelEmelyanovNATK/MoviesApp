package com.emelyanov.moviesapp.shared.domain.services.moviesrepository

import com.emelyanov.moviesapp.shared.domain.models.Genre
import com.emelyanov.moviesapp.shared.domain.models.Movie
import com.emelyanov.moviesapp.shared.domain.models.MovieDetails
import kotlinx.coroutines.flow.Flow

interface IMoviesRepository {
    suspend fun refreshData()
    fun getMovies(genre: String? = null): List<Movie>
    fun getGenres(): List<Genre>
    fun getMovieDetails(id: Int): MovieDetails
}