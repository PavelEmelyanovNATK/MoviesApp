package com.emelyanov.moviesapp.shared.domain


/**
 * Базовый класс вью.
 */
interface BaseView<ViewState> {
    fun processState(viewState: ViewState)
}