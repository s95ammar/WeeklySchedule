package com.s95ammar.weeklyschedule.models.data

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.s95ammar.weeklyschedule.util.Days
import com.s95ammar.weeklyschedule.util.Days.OneWeek

@Entity(indices = [Index("id")])
data class Schedule(
		var name: String,
		var days: Days = OneWeek,
		var isActive: Boolean = false
) {
	@PrimaryKey(autoGenerate = true)
	var id: Int = 0

	fun getDayOfSchedule(dayNum: Int) = when (dayNum) {
		in 0 until days.amount -> days.array[dayNum]
		else -> throw RuntimeException("dayNum must be in range of 0 until ${days.amount}")
	}

	companion object {
		var activeScheduleId: Int = 0

		fun activeExists() = (activeScheduleId != 0)
		fun activeDoesntExist() = !activeExists() // just for better readability purposes
	}
}
