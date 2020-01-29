package com.s95ammar.weeklyschedule.util

import androidx.room.TypeConverter
import org.joda.time.LocalTime
import java.lang.RuntimeException

enum class DaysAmount(val value: Int) {
	OneWeek(7),
	TwoWeeks(14);

	companion object {
		fun fromInt(daysAmount: Int) = when (daysAmount) {
			OneWeek.value -> OneWeek
			TwoWeeks.value -> TwoWeeks
			else -> throw RuntimeException("daysAmount value is invalid")
		}

		fun toInt(daysAmount: DaysAmount) = when (daysAmount) {
			OneWeek -> OneWeek.value
			TwoWeeks -> TwoWeeks.value
		}
	}

	class Converter {
		@TypeConverter
		fun fromInt(daysAmount: Int): DaysAmount = DaysAmount.fromInt(daysAmount)

		@TypeConverter
		fun toInt(daysAmount: DaysAmount): Int = DaysAmount.toInt(daysAmount)
	}
}

val DAYS_OF_ONE_WEEK = arrayOf("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday")
val DAYS_OF_TWO_WEEKS = arrayOf(
		"Sunday I", "Monday I", "Tuesday I", "Wednesday I", "Thursday I", "Friday I", "Saturday I",
		"Sunday II", "Monday II", "Tuesday II", "Wednesday II", "Thursday II", "Friday II", "Saturday II"
)

fun getDaysAbbreviations(days: Array<String>): Array<String> {
	val isWeekNumbered = days[0].split(" ").size == 2
	val daysAbbrev = Array(days.size) { "" }
	days.forEachIndexed { i, day ->
		daysAbbrev[i] = StringBuilder().apply {
			append(day.substring(0..2))
			if (isWeekNumbered) append(" ${day.split(" ")[1]}")
		}.toString()
	}
	return daysAbbrev
}

fun getHoursStringArray(timePattern: String) = Array<String>(HOURS_IN_DAY) { i ->
	LocalTime.MIDNIGHT.plusHours(i).toString(timePattern)
}


const val HOURS_IN_DAY = 24
const val MINUTES_IN_HOUR = 60
val DEFAULT_TIME = LocalTime(12, 0)
const val TIME_PATTERN_12H = "hh:mm aa"
const val TIME_PATTERN_24H = "HH:mm"

