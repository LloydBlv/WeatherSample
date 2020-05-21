package ir.zinutech.android.weatherapp.core.di

import ir.zinutech.android.weatherapp.features.main.utils.WeatherExceptionsParser
import ir.zinutech.android.weatherapp.features.main.utils.WeatherExceptionsParserImpl
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

/**
 * Koin module for providing dependencies through out the app
 */
val appModule = module {
    single<WeatherExceptionsParser> { WeatherExceptionsParserImpl(androidApplication().resources) }
}