package com.s95ammar.weeklyschedule.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Schedule(
		var name: String,
		var isActive: Boolean
) {
	@PrimaryKey(autoGenerate = true)
	var id: Int =0

}