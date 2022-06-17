package com.emelyanov.moviesapp.shared.domain.models

data class MovieDetails(
    val id: Int,
    val localizedName: String,
    val name: String,
    val imageUrl: String,
    val year: Int,
    val rating: Float,
    val description: String,
)