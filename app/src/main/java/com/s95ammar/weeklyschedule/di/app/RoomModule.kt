package com.s95ammar.weeklyschedule.di.app

import android.app.Application
import androidx.room.Room
import com.s95ammar.weeklyschedule.models.room.*
import com.s95ammar.weeklyschedule.util.DATABASE_NAME
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object RoomModule {
	@JvmStatic
	@Provides
	@Singleton
	fun provideDatabaseInstance(applictaion: Application): WeeklyScheduleDatabase {
		return Room.databaseBuilder(applictaion.applicationContext, WeeklyScheduleDatabase::class.java, DATABASE_NAME)
				.fallbackToDestructiveMigration()
				.build()
	}

	@JvmStatic
	@Provides
	@Singleton
	fun provideScheduleDao(db: WeeklyScheduleDatabase): ScheduleDao {
		return db.getScheduleDao()
	}

	@JvmStatic
	@Provides
	@Singleton
	fun provideDayDao(db: WeeklyScheduleDatabase): DayDao {
		return db.getDayDao()
	}

	@JvmStatic
	@Provides
	@Singleton
	fun provideCategoryDao(db: WeeklyScheduleDatabase): CategoryDao {
		return db.getCategoryDao()
	}

	@JvmStatic
	@Provides
	@Singleton
	fun provideEventDao(db: WeeklyScheduleDatabase): EventDao {
		return db.getEventDao()
	}
}