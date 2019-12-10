package com.s95ammar.weeklyschedule.models.data

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.s95ammar.weeklyschedule.util.DAYS_OF_WEEK
import com.s95ammar.weeklyschedule.util.DaysAmount
import com.s95ammar.weeklyschedule.util.HOURS_IN_DAY
import org.joda.time.LocalTime
import java.lang.RuntimeException

@Entity(indices = [Index("id")])
data class Schedule(
		var name: String,
		var daysAmount: DaysAmount = DaysAmount.OneWeek,
		var isActive: Boolean = false
) {
	@PrimaryKey(autoGenerate = true)
	var id: Int = 0

	fun getDayOfSchedule(dayNumInSchedule: Int): String {
		if (dayNumInSchedule !in 0 until DaysAmount.TwoWeeks.value)
			throw RuntimeException("dayNumInSchedule must be in range of [0, 13]")

		val dayName = DAYS_OF_WEEK[dayNumInSchedule % DaysAmount.OneWeek.value]
		return when (daysAmount) {
			DaysAmount.OneWeek -> dayName
			DaysAmount.TwoWeeks -> dayName + if (dayNumInSchedule < DAYS_OF_WEEK.size) " I" else " II"
		}
	}

	companion object {
		var activeScheduleId: Int = 0

		fun activeExists() = (activeScheduleId != 0)
		fun activeDoesntExist() = !activeExists() // just for better readability purposes

		fun getHoursStringArray(timePattern: String) = Array<String>(HOURS_IN_DAY) { i ->
			LocalTime.MIDNIGHT.plusHours(i).toString(timePattern)
		}
	}
}
