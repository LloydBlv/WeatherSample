package ir.zinutech.android.weatherapp.core.utils

import com.squareup.moshi.*
import timber.log.Timber
import java.util.*

/**
 * A moshi adapter that converts epoch time in long to [Date] and vice-versa
 */
class DateConverter : JsonAdapter<Date>() {
    @FromJson
    override fun fromJson(reader: JsonReader): Date? {
        return try {
            val dateAsLong = reader.nextLong()
            Date(dateAsLong)
        } catch (ex: Exception) {
            Timber.e(ex)
            null
        }
    }

    @ToJson
    override fun toJson(writer: JsonWriter, value: Date?) {
        value?.let { date -> writer.value(date.time) }
    }
}