package com.s95ammar.weeklyschedule.models.data

import androidx.room.Entity
import androidx.room.Ignore
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

	@Ignore
	val days = when (daysAmount) {
		OneWeek -> DAYS_OF_ONE_WEEK
		TwoWeeks -> DAYS_OF_TWO_WEEKS
	}

	fun getDayOfSchedule(dayNum: Int) = when (dayNum) {
		in 0 until daysAmount.value -> days[dayNum]
		else -> throw RuntimeException("dayNum must be in range of 0 until ${daysAmount.value}")
	}

	companion object {
		var activeScheduleId: Int = 0

		fun activeExists() = (activeScheduleId != 0)
		fun activeDoesntExist() = !activeExists() // just for better readability purposes
	}
}
