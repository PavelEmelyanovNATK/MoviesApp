package com.emelyanov.moviesapp.shared.domain.models.responses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MoviesListResponse(
    @SerialName("films")
    val films: List<MovieResponse>
)