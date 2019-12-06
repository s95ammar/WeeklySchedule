package com.s95ammar.weeklyschedule.models.data

import android.util.Log
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index("id")])
data class Schedule(
		var name: String,
		var daysAmount: Int = 7,
		var isActive: Boolean = false
) {
	@PrimaryKey(autoGenerate = true)
	var id: Int = 0

	fun activate() {
		isActive = true
	}

	fun deactivate() {
		isActive = false
	}

	companion object {
		var activeScheduleId: Int = 0

		fun activeExists() = (activeScheduleId != 0)
		fun activeDoesntExist() = !activeExists() // just for better readability purposes
	}

	override fun toString(): String {
		return "Schedule(id = $id, name = $name, daysAmount = $daysAmount, isActive = $isActive)"
	}
}
