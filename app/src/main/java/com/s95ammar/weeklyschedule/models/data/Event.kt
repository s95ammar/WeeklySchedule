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
					entity = Day::class,
					parentColumns = ["id"],
					childColumns = ["day_id"],
					onDelete = ForeignKey.CASCADE
			)
		]
)
data class Event(
		var name: String,
		var startTime: LocalTime,
		var endTime: LocalTime,
		@ColumnInfo(name = "category_id", index = true) var categoryId: Int,
		@ColumnInfo(name = "day_id", index = true) var dayId: Int
) {

	@PrimaryKey(autoGenerate = true)
	var id: Int = 0

	object Validator {
		fun isTimeValid(startTime: LocalTime, endTime: LocalTime) = endTime.isAfter(startTime)
	}

	fun overlapsWith(other: Event): Boolean {
		return startTime.isEqual(other.startTime) ||
				startTime.isBefore(other.startTime) && endTime.isAfter(other.startTime) ||
				startTime.isAfter(other.startTime) && startTime.isBefore(other.endTime) ||
				endTime.isAfter(other.startTime) && endTime.isBefore(other.endTime)
	}

}