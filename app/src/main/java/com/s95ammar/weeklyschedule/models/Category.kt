package com.s95ammar.weeklyschedule.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.collections.ArrayList
import kotlin.collections.HashSet

@Entity
data class Category(
	var name: String,
	var fillColor: Int,
	var textColor: Int
) {
	@PrimaryKey(autoGenerate = true)
	var id: Int =0

}
