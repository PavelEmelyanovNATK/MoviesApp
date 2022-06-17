package com.emelyanov.moviesapp.shared.domain

import kotlinx.coroutines.*

/**
 * Базовый класс презентера. Имеет [presenterScope] для выполнения корутин. [presenterScope] отменяется при [onDestroy].
 * [View] - класс View для презентера, наследник [BaseView], объект вью хранится в поле [view].
 * [ViewState] - класс состояния экрана, объект состояния хранится в поле [viewState], при реализации рекомендуется
 * в методе set() уведомлять вью об изменении состояния (view.processState()).
 */
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