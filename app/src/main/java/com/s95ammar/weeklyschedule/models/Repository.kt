package com.s95ammar.weeklyschedule.models

import com.s95ammar.weeklyschedule.models.data.Category
import com.s95ammar.weeklyschedule.models.data.Event
import com.s95ammar.weeklyschedule.models.data.Schedule
import com.s95ammar.weeklyschedule.models.room.CategoryDao
import com.s95ammar.weeklyschedule.models.room.EventDao
import com.s95ammar.weeklyschedule.models.room.ScheduleDao
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor(
		private var scheduleDao: ScheduleDao,
		private var categoryDao: CategoryDao,
		private var eventDao: EventDao
) {

	// Schedule CRUD

	suspend fun insert(schedule: Schedule) = withContext(IO) { scheduleDao.insert(schedule) }
	suspend fun update(vararg schedules: Schedule) = withContext(IO) { scheduleDao.update(*schedules) }
	suspend fun delete(schedule: Schedule) = withContext(IO) { scheduleDao.delete(schedule) }
	fun getScheduleById(id: Int) = scheduleDao.getScheduleById(id)
	fun getAllSchedules() = scheduleDao.getAllSchedules()

	// Category CRUD

	suspend fun insert(category: Category) = withContext(IO) { categoryDao.insert(category) }
	suspend fun update(category: Category) = withContext(IO) { categoryDao.update(category) }
	suspend fun delete(category: Category) = withContext(IO) { categoryDao.delete(category) }
	fun getCategoryById(id: Int) = categoryDao.getCategoryById(id)
	fun getAllCategories() = categoryDao.getAllCategories()

	// Event CRUD

	suspend fun insert(vararg events: Event) = withContext(IO) { eventDao.insert(*events) }
	suspend fun update(event: Event) = withContext(IO) { eventDao.update(event) }
	suspend fun delete(event: Event) = withContext(IO) { eventDao.delete(event) }
	fun getEventById(id: Int) = eventDao.getEventById(id)
	fun getEventsBy(scheduleId: Int) = eventDao.getEventsBy(scheduleId)
	fun getEventsBy(scheduleId: Int, day: String) = eventDao.getEventsBy(scheduleId, day)
	fun getEventsBy(categoryId: Int, scheduleId: Int) = eventDao.getEventsBy(categoryId, scheduleId)

}
