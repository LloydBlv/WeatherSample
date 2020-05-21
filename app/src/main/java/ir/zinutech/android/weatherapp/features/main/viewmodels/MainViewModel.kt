package ir.zinutech.android.weatherapp.features.main.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import ir.zinutech.android.weatherapp.features.main.data.WeatherRepository
import ir.zinutech.android.weatherapp.features.main.domain.Location
import ir.zinutech.android.weatherapp.features.main.domain.Weather
import ir.zinutech.android.weatherapp.features.main.domain.WeatherFetchFailedException
import kotlinx.coroutines.Dispatchers
import timber.log.Timber

class MainViewModel(
    private val weatherRepository: WeatherRepository
) : ViewModel() {

    companion object {
        private const val STOCKHOLM_LATITUDE = 59.337239
        private const val STOCKHOLM_LONGITUDE = 18.062381
    }

    /**
     * Loading starts from here, by setting the value of this MutableLiveData
     */
    private val _weatherLocation = MutableLiveData<Location>()

    init {
        /**
         * In a real world example we probably start fetching user's location or get it from a stored preferences somewhere and then call loadWeather() function
         */
        loadWeather(location = Location(STOCKHOLM_LATITUDE, STOCKHOLM_LONGITUDE))
    }


    /**
     * Live data exposed to outside of this ViewModel which is the [Result] of loading the weather for a [Location]
     * This will get triggered when [_weatherLocation] value changes
     */
    val weather = _weatherLocation.switchMap { location ->
        liveData(Dispatchers.IO) {
            emit(ViewState.Loading)
            val latitude = location.latitude
            val longitude = location.longitude
            try {
                val forecast = weatherRepository.getForecast(
                    latitude,
                    longitude
                )
                if (forecast != null) {
                    emit(ViewState.WeatherLoaded(forecast))
                } else {
                    emit(ViewState.WeatherLoadFailed(WeatherFetchFailedException))
                }
            } catch (ex: Exception) {
                Timber.e(ex)
                emit(ViewState.WeatherLoadFailed(ex))
            }

        }
    }

    /**
     * starts the process of loading weather for a specified location
     * @param location the parameter to load location for
     */
    fun loadWeather(location: Location) {
        _weatherLocation.value = location
    }

    fun onRetryClicked() {
        _weatherLocation.value = _weatherLocation.value
    }

    /**
     * ViewState indicating current state for weather api load
     */
    sealed class ViewState {
        object Loading: ViewState()
        data class WeatherLoaded(val weather: Weather): ViewState()
        data class WeatherLoadFailed(val exception: Exception): ViewState()
    }
}