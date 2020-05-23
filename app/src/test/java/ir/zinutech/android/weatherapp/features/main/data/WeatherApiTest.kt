package ir.zinutech.android.weatherapp.features.main.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import ir.zinutech.android.weatherapp.core.utils.DateConverter
import ir.zinutech.android.weatherapp.features.main.domain.Metrics
import ir.zinutech.android.weatherapp.features.utils.*
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.CoreMatchers.*
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.*


@RunWith(JUnit4::class)
class WeatherApiTest {

    @Rule
    @JvmField
    val instantExecuteRole = InstantTaskExecutorRule()

    private lateinit var weatherApi: WeatherApi
    private lateinit var mockWebServer: MockWebServer

    @Before
    fun initService() {
        mockWebServer = MockWebServer()

        weatherApi = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(
                MoshiConverterFactory.create(
                    Moshi.Builder()
                        .add(KotlinJsonAdapterFactory())
                        .add(DateConverter())
                        .build()
                )
            )
            .build()
            .create(WeatherApi::class.java)
    }

    @After
    fun terminateService() {
        mockWebServer.shutdown()
    }

    @Test
    fun `fetch stockholm weather forecast test`() = runBlocking {
        TestHelpers.enqueueResponse("weather_forecast_sample.json", mockWebServer)
        val weatherForecast = weatherApi.getForecast(
            latitude = STOCKHOLM_LATITUDE,
            longitude = STOCKHOLM_LONGITUDE,
            apiKey = "",
            unit = Metrics.SI.toString()
        )

        assertThat(weatherForecast, notNullValue())
        assertThat(weatherForecast?.latitude, equalTo(STOCKHOLM_LATITUDE))
        assertThat(weatherForecast?.longitude, equalTo(STOCKHOLM_LONGITUDE))
        assertThat(weatherForecast?.timezone, equalTo(STOCKHOLM_TIME_ZONE))
        assertThat(weatherForecast?.currently, notNullValue())
        assertThat(weatherForecast?.currently?.time, equalTo(Date(TIME_IN_MILLIS)))
        assertThat(weatherForecast?.currently?.summary, equalTo(SUMMARY))
        assertThat(weatherForecast?.currently?.temperature, equalTo(TEMPERATURE))
        assertThat(weatherForecast?.currently?.apparentTemperature, equalTo(APPARENT_TEMPERATURE))
    }
}