package com.emelyanov.moviesapp.shared.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.loader.app.LoaderManager
import androidx.loader.content.Loader
import com.emelyanov.moviesapp.shared.domain.BasePresenter
import com.emelyanov.moviesapp.shared.domain.BasePresenterFactory
import com.emelyanov.moviesapp.shared.domain.BaseView
import com.emelyanov.moviesapp.shared.domain.utils.PresenterLoader

abstract class PresenterFragment<ViewState, View: BaseView<ViewState>, Presenter: BasePresenter<View, ViewState>>
    : Fragment(), LoaderManager.LoaderCallbacks<Presenter> {
    protected var isFirstLoaded: Boolean = true
    lateinit var presenter: Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isFirstLoaded = savedInstanceState?.getBoolean(FIRST_LOAD_KEY) ?: true
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        LoaderManager.getInstance(this).initLoader(PRESENTER_LOADER_KEY, savedInstanceState, this)
    }

    override fun onPause() {
        super.onPause()
        isFirstLoaded = false
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Presenter>
    = PresenterLoader(
        context = requireContext(),
        presenterFactory = createPresenterFactory()
    )

    override fun onLoadFinished(loader: Loader<Presenter>, data: Presenter) {
        presenter = data
    }

    override fun onLoaderReset(loader: Loader<Presenter>) { }

    abstract fun createPresenterFactory(): BasePresenterFactory<Presenter>

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(FIRST_LOAD_KEY, isFirstLoaded)
    }

    companion object {
        private const val PRESENTER_LOADER_KEY = 111
        private const val FIRST_LOAD_KEY = "firstLoadKey"
    }
}