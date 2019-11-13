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
		@ColumnInfo(name = "schedule_id") var scheduleId: Int
) {
	@PrimaryKey(autoGenerate = true)
	var id: Int = 0


}
