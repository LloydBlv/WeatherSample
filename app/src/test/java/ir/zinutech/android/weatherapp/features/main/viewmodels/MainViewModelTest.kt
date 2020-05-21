package ir.zinutech.android.weatherapp.features.main.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.timeout
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import ir.zinutech.android.weatherapp.features.main.data.WeatherRepository
import ir.zinutech.android.weatherapp.features.main.domain.Location
import ir.zinutech.android.weatherapp.features.main.domain.Weather
import ir.zinutech.android.weatherapp.features.main.domain.WeatherFetchFailedException
import ir.zinutech.android.weatherapp.features.utils.INVALID_LATITUDE
import ir.zinutech.android.weatherapp.features.utils.INVALID_LONGITUDE
import ir.zinutech.android.weatherapp.features.utils.STOCKHOLM_LATITUDE
import ir.zinutech.android.weatherapp.features.utils.STOCKHOLM_LONGITUDE
import kotlinx.coroutines.*
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.util.*


@RunWith(JUnit4::class)
class MainViewModelTest {


    private lateinit var viewModel: MainViewModel
    private lateinit var weatherRepository: WeatherRepository
    private lateinit var weatherForecastObserver: Observer<MainViewModel.ViewState>
    private val successfulResponse = Weather(
        STOCKHOLM_LATITUDE, STOCKHOLM_LONGITUDE, "Europe/Stockholm", Weather.Forecast(
            time = Date(),
            summary = "Clear",
            temperature = 14.33,
            apparentTemperature = 14.34
        )
    )
    private val weatherLoadedState = MainViewModel.ViewState.WeatherLoaded(successfulResponse)
    private val failureException = WeatherFetchFailedException
    private val weatherLoadFailedState = MainViewModel.ViewState.WeatherLoadFailed(failureException)

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @ObsoleteCoroutinesApi
    private val mainThreadSurrogate = newSingleThreadContext("UI thread")


    @ObsoleteCoroutinesApi
    @Before
    fun setUp() {
        Dispatchers.setMain(mainThreadSurrogate)
        weatherRepository = mock()
        runBlocking {
            whenever(weatherRepository.getForecast(STOCKHOLM_LATITUDE, STOCKHOLM_LONGITUDE))
                .thenReturn(successfulResponse)
            whenever(weatherRepository.getForecast(INVALID_LATITUDE, INVALID_LONGITUDE))
                .thenReturn(null)
        }
        viewModel = MainViewModel(weatherRepository)
        weatherForecastObserver = mock()
    }

    @Test
    fun `when getWeather is called with valid location, then observer is updated with WeatherLoaded state`() =
        runBlocking {
            viewModel.loadWeather(Location(STOCKHOLM_LATITUDE, STOCKHOLM_LONGITUDE))
            viewModel.weather.observeForever(weatherForecastObserver)
            delay(10)
            verify(weatherRepository).getForecast(STOCKHOLM_LATITUDE, STOCKHOLM_LONGITUDE)
            verify(weatherForecastObserver, timeout(50)).onChanged(MainViewModel.ViewState.Loading)
            verify(weatherForecastObserver, timeout(50)).onChanged(weatherLoadedState)
        }

    @Test
    fun `when getWeather is called with invalid location, then observer is updated with failure`() =
        runBlocking {
            viewModel.loadWeather(Location(INVALID_LATITUDE, INVALID_LONGITUDE))
            viewModel.weather.observeForever(weatherForecastObserver)
            delay(10)
            verify(weatherRepository).getForecast(INVALID_LATITUDE, INVALID_LONGITUDE)
            verify(weatherForecastObserver, timeout(50)).onChanged(MainViewModel.ViewState.Loading)
            verify(weatherForecastObserver, timeout(50)).onChanged(weatherLoadFailedState)
        }


    @ObsoleteCoroutinesApi
    @ExperimentalCoroutinesApi
    @After
    fun tearDown() {
        Dispatchers.resetMain()
        mainThreadSurrogate.close()
    }
}