package com.s95ammar.weeklyschedule.models

import com.s95ammar.weeklyschedule.models.data.Category
import com.s95ammar.weeklyschedule.models.data.Day
import com.s95ammar.weeklyschedule.models.data.Event
import com.s95ammar.weeklyschedule.models.data.Schedule
import com.s95ammar.weeklyschedule.models.room.CategoryDao
import com.s95ammar.weeklyschedule.models.room.DayDao
import com.s95ammar.weeklyschedule.models.room.EventDao
import com.s95ammar.weeklyschedule.models.room.ScheduleDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor(
		private var scheduleDao: ScheduleDao,
		private var dayDao: DayDao,
		private var categoryDao: CategoryDao,
		private var eventDao: EventDao
) {

	// Schedule CRUD
	fun insert(schedule: Schedule) = CoroutineScope(Dispatchers.IO).launch { scheduleDao.insert(schedule) }
	fun update(schedule: Schedule) = CoroutineScope(Dispatchers.IO).launch { scheduleDao.update(schedule) }
	fun delete(schedule: Schedule) = CoroutineScope(Dispatchers.IO).launch { scheduleDao.delete(schedule) }
	fun deleteAllSchedules() = CoroutineScope(Dispatchers.IO).launch { scheduleDao.deleteAllSchedules() }
	fun getScheduleById(id: Int) = scheduleDao.getScheduleById(id)
	fun getAllSchedules() = scheduleDao.getAllSchedules()

	// Day CRUD
	fun insert(day: Day) = CoroutineScope(Dispatchers.IO).launch { dayDao.insert(day) }
	fun update(day: Day) = CoroutineScope(Dispatchers.IO).launch { dayDao.update(day) }
	fun delete(day: Day) = CoroutineScope(Dispatchers.IO).launch { dayDao.delete(day) }
	fun deleteAllDays() = CoroutineScope(Dispatchers.IO).launch { dayDao.deleteAllDays() }
	fun getDayById(id: Int) = dayDao.getDayById(id)
	fun getAllDays() = dayDao.getAllDays()

	// Category CRUD
	fun insert(category: Category) = CoroutineScope(Dispatchers.IO).launch { categoryDao.insert(category) }
	fun update(category: Category) = CoroutineScope(Dispatchers.IO).launch { categoryDao.update(category) }
	fun delete(category: Category) = CoroutineScope(Dispatchers.IO).launch { categoryDao.delete(category) }
	fun deleteAllCategories() = CoroutineScope(Dispatchers.IO).launch { categoryDao.deleteAllCategories() }
	fun getCategoryById(id: Int) = categoryDao.getCategoryById(id)
	fun getAllCategories() = categoryDao.getAllCategories()

	// Event CRUD
	fun insert(event: Event) = CoroutineScope(Dispatchers.IO).launch { eventDao.insert(event) }
	fun update(event: Event) = CoroutineScope(Dispatchers.IO).launch { eventDao.update(event) }
	fun delete(event: Event) = CoroutineScope(Dispatchers.IO).launch { eventDao.delete(event) }
	fun deleteAllEvents() = CoroutineScope(Dispatchers.IO).launch { eventDao.deleteAllEvents() }
	fun getEventById(id: Int) = eventDao.getEventById(id)
	fun getAllEvents() = eventDao.getAllEvents()

}