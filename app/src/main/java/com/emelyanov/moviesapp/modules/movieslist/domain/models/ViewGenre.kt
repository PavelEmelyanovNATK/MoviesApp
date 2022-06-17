package com.emelyanov.moviesapp.modules.movieslist.domain.models

import com.emelyanov.moviesapp.shared.domain.models.Genre

data class ViewGenre(
    val name: String,
    val isSelected: Boolean = false
)

fun Genre.toViewGenre(isSelected: Boolean = false): ViewGenre = ViewGenre(name, isSelected)