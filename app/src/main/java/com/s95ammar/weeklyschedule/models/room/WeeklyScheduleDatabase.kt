package com.s95ammar.weeklyschedule.models.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.s95ammar.weeklyschedule.models.Category
import com.s95ammar.weeklyschedule.models.Day
import com.s95ammar.weeklyschedule.models.Event
import com.s95ammar.weeklyschedule.models.Schedule

@Database(
		entities = [Schedule::class, Day::class, Category::class, Event::class],
		version = 1
)
abstract class WeeklyScheduleDatabase : RoomDatabase() {
	abstract fun getScheduleDao(): ScheduleDao
	abstract fun getDayDao(): DayDao
	abstract fun getCategoryDao(): CategoryDao
	abstract fun getEventDao(): EventDao
}