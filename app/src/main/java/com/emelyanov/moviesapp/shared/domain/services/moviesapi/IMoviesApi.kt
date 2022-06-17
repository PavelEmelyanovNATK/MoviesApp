package com.emelyanov.moviesapp.shared.domain.services.moviesapi

import com.emelyanov.moviesapp.shared.domain.models.responses.MovieResponse
import com.emelyanov.moviesapp.shared.domain.models.responses.MoviesListResponse
import retrofit2.http.GET

interface IMoviesApi {
    @GET("sequeniatesttask/films.json")
    suspend fun getRawMoviesList(): MoviesListResponse
}