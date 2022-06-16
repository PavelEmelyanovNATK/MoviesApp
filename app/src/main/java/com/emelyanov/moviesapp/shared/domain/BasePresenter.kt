package com.emelyanov.moviesapp.shared.domain

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel

abstract class BasePresenter<View: BaseView<ViewState>, ViewState>() {
    protected abstract var viewState: ViewState
    protected var view: View? = null
    protected val presenterScope: CoroutineScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    open fun bindView(view: View) {
        this.view = view
    }

    open fun unbindView() {
        view = null
    }

    open fun onDestroy() {
        presenterScope.cancel()
    }
}