package com.s95ammar.weeklyschedule.models.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.s95ammar.weeklyschedule.models.data.Schedule

@Dao
interface ScheduleDao {
	@Insert
	suspend fun insert(schedule: Schedule)

	@Update
	suspend fun update(vararg schedule: Schedule)

	@Delete
	suspend fun delete(schedule: Schedule)

	@Query("SELECT * FROM schedule WHERE id=:id")
	fun getScheduleById(id: Int): LiveData<Schedule>

	@Query("SELECT * FROM schedule")
	fun getAllSchedules(): LiveData<List<Schedule>>
}
