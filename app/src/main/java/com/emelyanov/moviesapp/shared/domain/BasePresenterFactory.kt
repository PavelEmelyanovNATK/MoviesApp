package com.emelyanov.moviesapp.shared.domain

abstract class BasePresenterFactory<BasePresenter> {
    abstract fun create(): BasePresenter
}