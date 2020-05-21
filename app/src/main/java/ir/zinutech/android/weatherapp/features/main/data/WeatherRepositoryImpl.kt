package ir.zinutech.android.weatherapp.features.main.data

import ir.zinutech.android.weatherapp.core.extensions.Coordination
import ir.zinutech.android.weatherapp.features.main.domain.Metrics
import ir.zinutech.android.weatherapp.features.main.domain.Weather

class WeatherRepositoryImpl(private val weatherApi: WeatherApi) : WeatherRepository {
    override suspend fun getForecast(
        latitude: Coordination,
        longitude: Coordination,
        apiKey: String,
        units: Metrics
    ): Weather? {
        return weatherApi.getForecast(latitude, longitude, apiKey, units.toString().toLowerCase())
    }
}