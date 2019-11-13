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

	@Query("SELECT * FROM event")
	fun getAllEvents(): LiveData<List<Event>>
}