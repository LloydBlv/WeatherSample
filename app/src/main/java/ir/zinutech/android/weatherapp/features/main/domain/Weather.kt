package ir.zinutech.android.weatherapp.features.main.domain

import ir.zinutech.android.weatherapp.core.extensions.Coordination
import java.util.*

data class Weather(
    val latitude: Coordination?,
    val longitude: Coordination?,
    val timezone: String?,
    val currently: Forecast?
){
    data class Forecast(
        val time: Date?,
        val summary: String?,
        val temperature: Double?,
        val apparentTemperature: Double?
    )
}