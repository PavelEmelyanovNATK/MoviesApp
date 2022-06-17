package com.emelyanov.moviesapp.navigation.core

sealed interface CoreDestinations {
    object MoviesList : CoreDestinations
    data class MovieDetails(val id: Int) : CoreDestinations
}