package com.s95ammar.weeklyschedule.models.data

import androidx.room.*

@Entity(
		foreignKeys = [
			ForeignKey(
					entity = Schedule::class,
					parentColumns = ["id"],
					childColumns = ["schedule_id"],
					onDelete = ForeignKey.CASCADE
			)
		],
		indices = [Index("id")]
)
data class Day(
		var name: String,
		@ColumnInfo(name = "schedule_id", index = true) var scheduleId: Int
) {
	@PrimaryKey(autoGenerate = true)
	var id: Int = 0

	companion object {
		val DAYS_OF_WEEK = arrayOf("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday")
		const val DAYS_AMOUNT_IN_A_WEEK = 7

		fun getDayOfWeek(dayNumInSchedule: Int) = DAYS_OF_WEEK[dayNumInSchedule % DAYS_AMOUNT_IN_A_WEEK]
	}
}
