package com.s95ammar.weeklyschedule.models.data

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(indices = [Index("id")])
data class Category(
	var name: String,
	var fillColor: Int,
	var textColor: Int
) : Serializable{
	@PrimaryKey(autoGenerate = true)
	var id: Int =0

	override fun equals(other: Any?): Boolean {
		if (other !is Category) return false
		return name == other.name &&
				fillColor == other.fillColor &&
				textColor == other.textColor
	}
}
