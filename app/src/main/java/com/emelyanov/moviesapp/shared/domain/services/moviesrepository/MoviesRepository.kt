package com.emelyanov.moviesapp.shared.domain.services.moviesrepository

import com.emelyanov.moviesapp.shared.domain.models.Genre
import com.emelyanov.moviesapp.shared.domain.models.Movie
import com.emelyanov.moviesapp.shared.domain.models.MovieDetails
import com.emelyanov.moviesapp.shared.domain.models.responses.MovieResponse
import com.emelyanov.moviesapp.shared.domain.services.moviesapi.IMoviesApi

class MoviesRepository(
    private val moviesApi: IMoviesApi
) : IMoviesRepository {
    private var rawMovies: List<MovieResponse> = listOf()

    override suspend fun refreshData() {
        rawMovies = moviesApi.getRawMoviesList().films
    }

    override fun getMovies(genre: String?): List<Movie> {
        return rawMovies.filter { movie ->
            genre.isNullOrEmpty() || movie.genres.contains(genre)
        }.map {
            Movie(
                id = it.id,
                localizedName = it.localizedName,
                imageUrl = it.imageUrl ?: ""
            )
        }.sortedBy { it.localizedName }
    }

    override fun getGenres(): List<Genre>{
        val genres = mutableSetOf<String>()

        rawMovies.forEach { movie ->
            genres.addAll(movie.genres)
        }

        return genres.map { Genre(it) }
    }

    override fun getMovieDetails(id: Int): MovieDetails {
        return rawMovies.first { it.id == id }
            .let { movie ->
                MovieDetails(
                    id = movie.id,
                    localizedName = movie.localizedName,
                    name = movie.name,
                    imageUrl = movie.imageUrl ?: "",
                    rating = movie.rating ?: 0f,
                    description = movie.description ?: ""
                )
            }
    }
}