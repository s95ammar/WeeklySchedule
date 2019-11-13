package com.s95ammar.weeklyschedule.models.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.s95ammar.weeklyschedule.models.data.Category
import com.s95ammar.weeklyschedule.models.data.Day
import com.s95ammar.weeklyschedule.models.data.Event
import com.s95ammar.weeklyschedule.models.data.Schedule

@Database(
		entities = [Schedule::class, Day::class, Category::class, Event::class],
		version = 1
)
@TypeConverters(LocalTimeTypeConverter::class)
abstract class WeeklyScheduleDatabase : RoomDatabase() {
	abstract fun getScheduleDao(): ScheduleDao
	abstract fun getDayDao(): DayDao
	abstract fun getCategoryDao(): CategoryDao
	abstract fun getEventDao(): EventDao
}