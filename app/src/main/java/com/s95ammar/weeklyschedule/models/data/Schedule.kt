package com.s95ammar.weeklyschedule.models.data

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.s95ammar.weeklyschedule.util.DAYS_OF_ONE_WEEK
import com.s95ammar.weeklyschedule.util.DAYS_OF_TWO_WEEKS
import com.s95ammar.weeklyschedule.util.DaysAmount
import com.s95ammar.weeklyschedule.util.DaysAmount.OneWeek
import com.s95ammar.weeklyschedule.util.DaysAmount.TwoWeeks
import com.s95ammar.weeklyschedule.util.HOURS_IN_DAY
import org.joda.time.LocalTime
import java.lang.RuntimeException

@Entity(indices = [Index("id")])
data class Schedule(
		var name: String,
		var daysAmount: DaysAmount = OneWeek,
		var isActive: Boolean = false
) {
	@PrimaryKey(autoGenerate = true)
	var id: Int = 0

	fun getDayOfSchedule(dayNum: Int) = when {
		daysAmount == OneWeek && (dayNum in 0 until OneWeek.value) -> DAYS_OF_ONE_WEEK[dayNum % OneWeek.value]
		daysAmount == TwoWeeks && (dayNum in 0 until TwoWeeks.value) -> DAYS_OF_TWO_WEEKS[dayNum % TwoWeeks.value]
		else -> throw RuntimeException("dayNum must be in range of 0 until $daysAmount")
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
