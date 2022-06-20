package com.emelyanov.moviesapp.navigation.core

import android.util.Log
import androidx.core.os.bundleOf
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import com.emelyanov.moviesapp.R
import com.emelyanov.moviesapp.modules.core.presentation.MainActivity
import com.emelyanov.moviesapp.modules.moviedetails.presentation.MovieDetailsFragment
import kotlinx.coroutines.launch

/**
 * Запускает обработчик навигации.
 * @param coreNavProvider - провайдер навигации, который будет прослушиваться
 * @param coreNavController - контроллер навигации
 */
fun MainActivity.launchNavHost(
    coreNavProvider: CoreNavProvider,
    coreNavController: NavController
) {
    lifecycleScope.launch {
        lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            coreNavProvider.observeNavigationFlow(this@repeatOnLifecycle) { destination ->
                when (destination) {
                    is CoreDestinations.MoviesList -> coreNavController.navigate(R.id.moviesListFragment)
                    is CoreDestinations.MovieDetails -> coreNavController.navigate(
                            resId = R.id.navigateToDetails,
                            args = MovieDetailsFragment.createArguments(destination.id)
                        )
                }
            }
        }
    }
}
