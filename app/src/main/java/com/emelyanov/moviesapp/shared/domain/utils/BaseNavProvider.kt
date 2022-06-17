package com.emelyanov.moviesapp.shared.domain.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

/**
 * Класс, реализующий поток точек навигации. [D] - класс точки навигации. [observeNavigationFlow] - метод для добавления
 * наблюдателя потока точек навигации.
 */
open class BaseNavProvider<D> {
    protected val destinationFlow: MutableStateFlow<D?> = MutableStateFlow(null)

    fun requestNavigate(destination: D?) {
        destinationFlow.tryEmit(destination)
    }

    fun observeNavigationFlow(scope: CoroutineScope, handler: (D?) -> Unit) {
        destinationFlow.onEach {
            handler(it)
            if(it != null) destinationFlow.tryEmit(null)
        }.launchIn(scope)
    }
}