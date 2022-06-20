package com.emelyanov.moviesapp.shared.domain

/**
 * Базовый класс фабрики для создание презентеров.
 */
interface BasePresenterFactory<BasePresenter> {
    fun create(): BasePresenter
}