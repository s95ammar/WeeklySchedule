package com.s95ammar.weeklyschedule.models;

import com.google.gson.*;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;
import org.joda.time.format.ISODateTimeFormat;

import java.lang.reflect.Type;

public class LocalTimeSerializer implements JsonDeserializer<LocalTime>, JsonSerializer<LocalTime> {

	private static final DateTimeFormatter TIME_FORMAT = ISODateTimeFormat.timeNoMillis();

	private static final DateTimeFormatter FORMATTER = new DateTimeFormatterBuilder()
			.append(ISODateTimeFormat.time().getPrinter(),
					ISODateTimeFormat.localTimeParser().getParser())
			.toFormatter();

	@Override
	public JsonElement serialize(LocalTime src, Type typeOfSrc, JsonSerializationContext context) {
		return new JsonPrimitive(src.toString(FORMATTER));
	}

	@Override
	public LocalTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
			throws JsonParseException {
		if (json.getAsString() == null || json.getAsString().isEmpty()) {
			return null;
		}

		return LocalTime.parse(json.getAsString(), FORMATTER);
	}

}
