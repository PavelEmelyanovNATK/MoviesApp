package com.emelyanov.moviesapp.shared.domain

import kotlinx.coroutines.*

abstract class BasePresenter<View: BaseView<ViewState>, ViewState>() {
    protected abstract var viewState: ViewState
    protected var view: View? = null
    protected val presenterScope: CoroutineScope = CoroutineScope(Dispatchers.Main.immediate + SupervisorJob())

    open fun bindView(view: View) {
        this.view = view
        view.processState(viewState)
    }

    open fun unbindView() {
        view = null
    }

    open fun onDestroy() {
        presenterScope.cancel()
    }
}