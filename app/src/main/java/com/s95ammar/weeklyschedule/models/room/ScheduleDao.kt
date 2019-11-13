package com.s95ammar.weeklyschedule.models.room

import androidx.lifecycle.LiveData
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.s95ammar.weeklyschedule.models.Schedule

interface ScheduleDao {
	@Insert
	suspend fun insert(schedule: Schedule)

	@Update
	suspend fun update(schedule: Schedule)

	@Delete
	suspend fun delete(schedule: Schedule)

	@Query("DELETE FROM schedule")
	suspend fun deleteAllSchedules()

	@Query("SELECT * FROM schedule WHERE id=:id")
	fun getScheduleById(id: Int): LiveData<Schedule>

	@Query("SELECT * FROM schedule")
	fun getAllSchedules(): LiveData<List<Schedule>>
}