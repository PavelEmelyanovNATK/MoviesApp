package com.emelyanov.moviesapp.shared.domain

interface BaseView<ViewState> {
    fun processState(viewState: ViewState)
}