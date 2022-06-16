package com.emelyanov.moviesapp.shared.domain

interface BaseView<ViewState> {
    fun obtainState(viewState: ViewState)
}