package com.s95ammar.weeklyschedule.models.data

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index("id")])
data class Schedule(
		var name: String,
		var isActive: Boolean
) {
	@PrimaryKey(autoGenerate = true)
	var id: Int =0

}