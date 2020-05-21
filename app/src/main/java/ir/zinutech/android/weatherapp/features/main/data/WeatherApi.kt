package ir.zinutech.android.weatherapp.features.main.data

import ir.zinutech.android.weatherapp.core.extensions.Coordination
import ir.zinutech.android.weatherapp.features.main.domain.Weather
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Retrofit interface to make suspended API calls using coroutines
 */
interface WeatherApi {

    companion object {
        const val BASE_URI = "https://api.darksky.net/"
    }

    @GET("forecast/{apiKey}/{lat},{lon}")
    suspend fun getForecast(
        @Path("lat") latitude: Coordination,
        @Path("lon") longitude: Coordination,
        @Path("apiKey") apiKey: String,
        @Query("units") unit: String
    ): Weather?
}