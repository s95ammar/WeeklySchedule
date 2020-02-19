package com.s95ammar.weeklyschedule.util

import androidx.room.TypeConverter
import org.joda.time.LocalTime
import java.util.*
import kotlin.collections.ArrayList

const val HOURS_IN_DAY = 24
const val MINUTES_IN_HOUR = 60
val DEFAULT_TIME = LocalTime(12, 0)
const val TIME_PATTERN_12H = "hh:mm aa"
const val TIME_PATTERN_24H = "HH:mm"

enum class Days(val amount: Int, val array: Array<String>) {
	OneWeek(7, arrayOf("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday")),
	TwoWeeks(14, arrayOf(
			"Sunday I", "Monday I", "Tuesday I", "Wednesday I", "Thursday I", "Friday I", "Saturday I",
			"Sunday II", "Monday II", "Tuesday II", "Wednesday II", "Thursday II", "Friday II", "Saturday II"
	));

	companion object {
		fun fromInt(daysAmount: Int) = when (daysAmount) {
			OneWeek.amount -> OneWeek
			TwoWeeks.amount -> TwoWeeks
			else -> throw RuntimeException("daysAmount value is invalid")
		}
	}

	fun toInt() = when (this) {
		OneWeek -> OneWeek.amount
		TwoWeeks -> TwoWeeks.amount
	}

	class Converter {
		@TypeConverter
		fun fromInt(daysAmount: Int): Days = Days.fromInt(daysAmount)

		@TypeConverter
		fun toInt(days: Days): Int = days.toInt()
	}
}

enum class TimeTarget { START_TIME, END_TIME }

fun getDaysAbbreviations(days: List<String>): List<String> {
	if (days.isEmpty()) return emptyList()
	val isWeekNumbered = when {
		days.all { Days.OneWeek.array.contains(it) } -> { false }
		days.all { Days.TwoWeeks.array.contains(it) } -> { true }
		else -> throw RuntimeException("Not a list of days")
	}
	val daysAbbrev = ArrayList<String>(days.size)
	days.forEach { day ->
		daysAbbrev.add(StringBuilder().apply {
			append(day.substring(0..2))
			if (isWeekNumbered) append(" ${day.split(" ")[1]}")
		}.toString())
	}
	return daysAbbrev
}

fun getHoursStringArray(timePattern: String) = Array<String>(HOURS_IN_DAY) { i ->
	LocalTime.MIDNIGHT.plusHours(i).toString(timePattern)
}

fun LocalTime.toCalendarInstance(): Calendar = Calendar.getInstance().apply {
	set(Calendar.HOUR_OF_DAY, this@toCalendarInstance.hourOfDay)
	set(Calendar.MINUTE, this@toCalendarInstance.minuteOfHour)
}
