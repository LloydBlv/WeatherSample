package ir.zinutech.android.weatherapp.features.main.utils


/**
 * Used to parse exceptions thrown while loading weather data into human readable messages
 */
interface WeatherExceptionsParser {
    fun parseException(exception: Exception): String
}