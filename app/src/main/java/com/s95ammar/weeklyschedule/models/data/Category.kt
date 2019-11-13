package com.s95ammar.weeklyschedule.models.data

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index("id")])
data class Category(
	var name: String,
	var fillColor: Int,
	var textColor: Int
) {
	@PrimaryKey(autoGenerate = true)
	var id: Int =0

}
