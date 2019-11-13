package com.s95ammar.weeklyschedule.models.room

import androidx.lifecycle.LiveData
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.s95ammar.weeklyschedule.models.Day

interface DayDao {
	@Insert
	suspend fun insert(day: Day)

	@Update
	suspend fun update(day: Day)

	@Delete
	suspend fun delete(day: Day)

	@Query("DELETE FROM day")
	suspend fun deleteAllDays()

	@Query("SELECT * FROM day WHERE id=:id")
	fun getDayById(id: Int): LiveData<Day>

	@Query("SELECT * FROM day")
	fun getAllDays(): LiveData<List<Day>>
}