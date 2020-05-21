package ir.zinutech.android.weatherapp.features.main.data

import ir.zinutech.android.weatherapp.BuildConfig
import ir.zinutech.android.weatherapp.core.extensions.Coordination
import ir.zinutech.android.weatherapp.features.main.domain.Metrics
import ir.zinutech.android.weatherapp.features.main.domain.Weather

/**
 * Interface to make an api call to fetch weather info
 */
interface WeatherRepository {
    suspend fun getForecast(
        latitude: Coordination,
        longitude: Coordination,
        apiKey: String = BuildConfig.DARKSKY_API_KEY,
        units: Metrics = Metrics.SI
    ): Weather?
}