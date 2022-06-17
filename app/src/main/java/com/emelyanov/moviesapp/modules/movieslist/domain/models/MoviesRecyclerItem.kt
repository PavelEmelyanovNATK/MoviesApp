package com.emelyanov.moviesapp.modules.movieslist.domain.models

sealed interface MoviesRecyclerItem {
    data class Header(val resId: Int) : MoviesRecyclerItem
    data class Genre(
        val name: String,
        val isSelected: Boolean = false,
        val onClick: () -> Unit = {}
    ) : MoviesRecyclerItem
    data class Movie(
        val name: String,
        val imageUrl: String,
        val onClick: () -> Unit = {}
    ) : MoviesRecyclerItem
}