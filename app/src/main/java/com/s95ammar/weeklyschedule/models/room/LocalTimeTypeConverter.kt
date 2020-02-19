package com.s95ammar.weeklyschedule.models.room

import androidx.room.TypeConverter
import com.google.gson.*
import org.joda.time.LocalTime
import org.joda.time.format.DateTimeFormatterBuilder
import org.joda.time.format.ISODateTimeFormat
import java.lang.reflect.Type


class LocalTimeTypeConverter {

	private val gson = GsonBuilder()
			.registerTypeAdapter(LocalTime::class.java, LocalTimeSerializer())
			.create()

	@TypeConverter
	fun stringToLocalTime(json: String): LocalTime = gson.fromJson(json, LocalTime::class.java)

	@TypeConverter
	fun localTimeToString(localTime: LocalTime): String = gson.toJson(localTime)

	private class LocalTimeSerializer : JsonDeserializer<LocalTime>, JsonSerializer<LocalTime> {
		private val FORMATTER = DateTimeFormatterBuilder()
				.append(ISODateTimeFormat.time().printer, ISODateTimeFormat.localTimeParser().parser)
				.toFormatter()

		override fun serialize(src: LocalTime, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
			return JsonPrimitive(src.toString(FORMATTER))
		}

		@Throws(JsonParseException::class)
		override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): LocalTime? {
			return if (json.asString == null || json.asString.isEmpty()) {
				null
			} else LocalTime.parse(json.asString, FORMATTER)

		}
	}

}
