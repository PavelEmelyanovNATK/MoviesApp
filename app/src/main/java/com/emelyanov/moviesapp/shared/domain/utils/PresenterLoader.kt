package com.emelyanov.moviesapp.shared.domain.utils

import android.content.Context
import androidx.loader.content.Loader
import com.emelyanov.moviesapp.shared.domain.BasePresenter
import com.emelyanov.moviesapp.shared.domain.BasePresenterFactory
import com.emelyanov.moviesapp.shared.domain.BaseView

class PresenterLoader<ViewState, View: BaseView<ViewState>, Presenter: BasePresenter<View, ViewState>>(
    context: Context,
    private val presenterFactory: BasePresenterFactory<Presenter>
) : Loader<Presenter>(context) {
    private var presenter: Presenter? = null

    override fun onStartLoading() {
        if(presenter == null)
            forceLoad()
        else
            deliverResult(presenter)
    }

    override fun onForceLoad() {
        presenter = presenterFactory.create()
        deliverResult(presenter)
    }

    override fun onReset() {
        presenter?.onDestroy()
        presenter = null
    }
}