package ir.zinutech.android.weatherapp.core.utils

import com.squareup.moshi.*
import timber.log.Timber
import java.util.*

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