package ir.zinutech.android.weatherapp.features.main.utils

import android.content.res.Resources
import ir.zinutech.android.weatherapp.R
import ir.zinutech.android.weatherapp.features.main.domain.WeatherFetchFailedException

class WeatherExceptionsParserImpl(private val resources: Resources) : WeatherExceptionsParser {
    override fun parseException(exception: Exception): String =
        when (exception) {
            is WeatherFetchFailedException -> {
                resources.getString(R.string.weather_loading_failed)
            }

            else -> {
                resources.getString(R.string.connect_to_server_failed)
            }
        }
}