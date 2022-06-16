package com.emelyanov.moviesapp.modules.movieslist.domain.models

sealed interface MoviesRecyclerItem {
    data class Header(val title: String) : MoviesRecyclerItem
    data class Genre(val name: String, val isSelected: Boolean = false) : MoviesRecyclerItem
    data class Movie(val name: String, val imageUrl: String) : MoviesRecyclerItem
}