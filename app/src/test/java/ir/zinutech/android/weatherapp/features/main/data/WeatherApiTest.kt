package ir.zinutech.android.weatherapp.features.main.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import ir.zinutech.android.weatherapp.core.utils.DateConverter
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.buffer
import okio.source
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import org.junit.Assert.assertThat
import java.util.*


@RunWith(JUnit4::class)
class WeatherApiTest {

    companion object{
        private const val STOCKHOLM_LATITUDE = 59.337239
        private const val STOCKHOLM_LONGITUDE = 18.062381
        private const val STOCKHOLM_TIME_ZONE = "Europe/Stockholm"
        private const val SUMMARY = "Partly Cloudy"
        private const val TIME_IN_MILLIS = 1589627545L
        private const val TEMPERATURE = 10.05
        private const val APPARENT_TEMPERATURE = 7.9
    }
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
        enqueueResponse("weather_forecast_sample.json")
        val weatherForecast = weatherApi.getForecast(
            latitude = STOCKHOLM_LATITUDE,
            longitude = STOCKHOLM_LONGITUDE
        )

        assertThat(weatherForecast, notNullValue())
        assertThat(weatherForecast.latitude, `is`(STOCKHOLM_LATITUDE))
        assertThat(weatherForecast.longitude, `is`(STOCKHOLM_LONGITUDE))
        assertThat(weatherForecast.timezone, `is`(STOCKHOLM_TIME_ZONE))
        assertThat(weatherForecast.currently, notNullValue())
        assertThat(weatherForecast.currently?.time, `is`(Date(TIME_IN_MILLIS)))
        assertThat(weatherForecast.currently?.summary, `is`(SUMMARY))
        assertThat(weatherForecast.currently?.temperature, `is`(TEMPERATURE))
        assertThat(weatherForecast.currently?.apparentTemperature, `is`(APPARENT_TEMPERATURE))
    }


    private fun enqueueResponse(fileName: String, headers: Map<String, String> = emptyMap()) {
        val inputStream = javaClass.classLoader!!
            .getResourceAsStream(fileName)
        val source = inputStream.source().buffer()
        val mockResponse = MockResponse()
        for ((key, value) in headers) {
            mockResponse.addHeader(key, value)
        }
        mockWebServer.enqueue(
            mockResponse
                .setBody(source.readString(Charsets.UTF_8))
        )
    }
}