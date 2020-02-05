package com.s95ammar.weeklyschedule.models.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import org.joda.time.LocalTime

@Entity(
		foreignKeys = [
			ForeignKey(
					entity = Category::class,
					parentColumns = ["id"],
					childColumns = ["category_id"],
					onDelete = ForeignKey.CASCADE
			),
			ForeignKey(
					entity = Schedule::class,
					parentColumns = ["id"],
					childColumns = ["schedule_id"],
					onDelete = ForeignKey.CASCADE
			)
		]
)
data class Event(
		var name: String,
		var day: String,
		var startTime: LocalTime,
		var endTime: LocalTime,
		@ColumnInfo(name = "category_id", index = true) var categoryId: Int,
		@ColumnInfo(name = "schedule_id", index = true) var scheduleId: Int
) {

	@PrimaryKey(autoGenerate = true)
	var id: Int = 0

	val startHour: Int
		get() = startTime.hourOfDay
	val endHour: Int
		get() = endTime.hourOfDay
	val startMinute: Int
		get() = startTime.minuteOfHour
	val endMinute: Int
		get() = endTime.minuteOfHour

	fun overlapsWith(other: Event): Boolean {
		return startTime.isEqual(other.startTime) ||
				startTime.isBefore(other.startTime) && endTime.isAfter(other.startTime) ||
				startTime.isAfter(other.startTime) && startTime.isBefore(other.endTime) ||
				endTime.isAfter(other.startTime) && endTime.isBefore(other.endTime)
	}

}