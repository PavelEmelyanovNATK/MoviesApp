package com.emelyanov.moviesapp.shared.domain

/**
 * Базовый класс фабрики для создание презентеров.
 */
abstract class BasePresenterFactory<BasePresenter> {
    abstract fun create(): BasePresenter
}