package com.emelyanov.moviesapp.shared.domain.services.moviesrepository

import com.emelyanov.moviesapp.shared.domain.models.Genre
import com.emelyanov.moviesapp.shared.domain.models.Movie
import com.emelyanov.moviesapp.shared.domain.models.MovieDetails
import com.emelyanov.moviesapp.shared.domain.utils.BadRequestException
import com.emelyanov.moviesapp.shared.domain.utils.ConnectionErrorException
import com.emelyanov.moviesapp.shared.domain.utils.NotFoundException
import com.emelyanov.moviesapp.shared.domain.utils.ServerNotRespondingException
import kotlinx.coroutines.flow.Flow

interface IMoviesRepository {
    /**
     * @throws ServerNotRespondingException
     * @throws ConnectionErrorException
     * @throws NotFoundException
     * @throws BadRequestException
     * @throws Exception
     */
    suspend fun refreshData()
    fun getMovies(genre: String? = null): List<Movie>
    fun getGenres(): List<Genre>
    fun getMovieDetails(id: Int): MovieDetails
}