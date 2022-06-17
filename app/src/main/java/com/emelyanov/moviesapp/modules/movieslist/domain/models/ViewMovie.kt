package com.emelyanov.moviesapp.modules.movieslist.domain.models

import com.emelyanov.moviesapp.shared.domain.models.Movie

data class ViewMovie(
    val id: Int,
    val localizedName: String,
    val imageUrl: String
)

fun Movie.toViewMovie() = ViewMovie(id, localizedName, imageUrl)