package com.s95ammar.weeklyschedule.models

import com.s95ammar.weeklyschedule.models.data.Category
import com.s95ammar.weeklyschedule.models.data.Event
import com.s95ammar.weeklyschedule.models.data.Schedule
import com.s95ammar.weeklyschedule.models.room.CategoryDao
import com.s95ammar.weeklyschedule.models.room.EventDao
import com.s95ammar.weeklyschedule.models.room.ScheduleDao
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor(
		private var scheduleDao: ScheduleDao,
		private var categoryDao: CategoryDao,
		private var eventDao: EventDao
) {

	// Schedule CRUD
	suspend fun insert(schedule: Schedule) = scheduleDao.insert(schedule)
	suspend fun update(vararg schedules: Schedule) = scheduleDao.update(*schedules)
	suspend fun delete(schedule: Schedule) = scheduleDao.delete(schedule)
	suspend fun deleteAllSchedules() = scheduleDao.deleteAllSchedules()
	fun getScheduleById(id: Int) = scheduleDao.getScheduleById(id)
	fun getAllSchedules() = scheduleDao.getAllSchedules()

	// Category CRUD
	suspend fun insert(category: Category) = categoryDao.insert(category)
	suspend fun update(category: Category) = categoryDao.update(category)
	suspend fun delete(category: Category) = categoryDao.delete(category)
	suspend fun deleteAllCategories() = categoryDao.deleteAllCategories()
	fun getCategoryById(id: Int) = categoryDao.getCategoryById(id)
	fun getAllCategories() = categoryDao.getAllCategories()

	// Event CRUD
	suspend fun insert(vararg events: Event) = eventDao.insert(*events)
	suspend fun update(event: Event) = eventDao.update(event)
	suspend fun delete(event: Event) = eventDao.delete(event)
	suspend fun deleteAllEvents() = eventDao.deleteAllEvents()
	fun getEventById(id: Int) = eventDao.getEventById(id)
	fun getEventsBy(scheduleId: Int) = eventDao.getEventsBy(scheduleId)
	fun getEventsBy(scheduleId: Int, day: String) = eventDao.getEventsBy(scheduleId, day)
	fun getEventsBy(categoryId: Int, scheduleId: Int) = eventDao.getEventsBy(categoryId, scheduleId)

	fun getAllEvents() = eventDao.getAllEvents()

}