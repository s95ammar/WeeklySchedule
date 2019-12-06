package com.s95ammar.weeklyschedule.models.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.s95ammar.weeklyschedule.models.data.Schedule

@Dao
interface ScheduleDao {
	@Insert
	suspend fun insertAndReturnId(schedule: Schedule): Long

	@Update
	suspend fun update(vararg schedule: Schedule)

	@Delete
	suspend fun delete(schedule: Schedule)

	@Query("DELETE FROM schedule")
	suspend fun deleteAllSchedules()

	@Query("SELECT * FROM schedule WHERE id=:id")
	fun getScheduleById(id: Int): LiveData<Schedule>

	@Query("SELECT * FROM schedule")
	fun getAllSchedules(): LiveData<List<Schedule>>
}