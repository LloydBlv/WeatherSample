package ir.zinutech.android.weatherapp.core.di

import ir.zinutech.android.weatherapp.features.main.data.WeatherRepository
import ir.zinutech.android.weatherapp.features.main.data.WeatherRepositoryImpl
import org.koin.dsl.module

/**
 * Koin module which provides repositories
 */
val repositoriesModule = module {
    single<WeatherRepository> { WeatherRepositoryImpl(get()) }
}