package com.s95ammar.weeklyschedule.models.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.s95ammar.weeklyschedule.models.data.Event

@Dao
interface EventDao {
	@Insert
	suspend fun insert(event: Event)

	@Update
	suspend fun update(event: Event)

	@Delete
	suspend fun delete(event: Event)

	@Query("DELETE FROM event")
	suspend fun deleteAllEvents()

	@Query("SELECT * FROM event WHERE id=:id")
	fun getEventById(id: Int): LiveData<Event>

	@Query("SELECT * FROM event WHERE schedule_id=:scheduleId")
	fun getEventsBy(scheduleId: Int): LiveData<List<Event>>

	@Query("SELECT * FROM event WHERE schedule_id=:scheduleId AND day=:day")
	fun getEventsBy(scheduleId: Int, day: String): LiveData<List<Event>>

	@Query("SELECT * FROM event WHERE schedule_id=:scheduleId AND category_Id=:categoryId")
	fun getEventsBy(categoryId: Int, scheduleId: Int): LiveData<List<Event>>

	@Query("SELECT * FROM event")
	fun getAllEvents(): LiveData<List<Event>>
}
