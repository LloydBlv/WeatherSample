package ir.zinutech.android.weatherapp.core.di

import ir.zinutech.android.weatherapp.features.main.viewmodels.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


/**
 * Koin module that provides viewmodels used throughout the app
 */
val viewModelsModule = module {
    viewModel {
        MainViewModel(get())
    }
}