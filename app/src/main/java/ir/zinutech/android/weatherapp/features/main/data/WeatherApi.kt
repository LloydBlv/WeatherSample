package ir.zinutech.android.weatherapp.features.main.data

import ir.zinutech.android.weatherapp.BuildConfig
import ir.zinutech.android.weatherapp.core.extensions.Coordination
import ir.zinutech.android.weatherapp.features.main.domain.Metrics
import ir.zinutech.android.weatherapp.features.main.domain.Weather
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface WeatherApi {

    companion object {
        const val BASE_URI = "https://api.darksky.net/forecast/"
    }

    @GET("forecast/{apiKey}/{lat},{lon}")
    suspend fun getForecast(
        @Path("lat") latitude: Coordination,
        @Path("lon") longitude: Coordination,
        @Path("apiKey") apiKey: String = BuildConfig.DARKSKY_API_KEY,
        @Query("units") units: Metrics = Metrics.SI
    ): Weather
}