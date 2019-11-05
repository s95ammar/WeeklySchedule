package com.s95ammar.weeklyschedule.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.joda.time.LocalTime

@Entity
data class Event(
		var name: String,
		var startTime: LocalTime,
		var endTime: LocalTime,
		@ColumnInfo(name = "category_id") var categoryId: Int,
		@ColumnInfo(name = "day_id") var dayId: Int
) {

	@PrimaryKey(autoGenerate = true)
	var id: Int =0

	companion object {
		fun isTimeValid(startTime: LocalTime, endTime: LocalTime) = endTime.isAfter(startTime)
	}

	fun overlapsWith(other: Event): Boolean {
		return startTime.isEqual(other.startTime) ||
				startTime.isBefore(other.startTime) && endTime.isAfter(other.startTime) ||
				startTime.isAfter(other.startTime) && startTime.isBefore(other.endTime) ||
				endTime.isAfter(other.startTime) && endTime.isBefore(other.endTime)
	}

}