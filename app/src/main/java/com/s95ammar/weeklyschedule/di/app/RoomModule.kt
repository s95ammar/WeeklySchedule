package com.s95ammar.weeklyschedule.di.app

import android.app.Application
import androidx.room.Room
import com.s95ammar.weeklyschedule.models.room.WeeklyScheduleDatabase
import com.s95ammar.weeklyschedule.util.DATABASE_NAME
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object RoomModule {
	@JvmStatic
	@Provides
	@Singleton
	fun provideDatabaseInstance(application: Application): WeeklyScheduleDatabase {
		return Room.databaseBuilder(application.applicationContext, WeeklyScheduleDatabase::class.java, DATABASE_NAME)
				.fallbackToDestructiveMigration()
				.build()
	}

	@JvmStatic
	@Provides
	@Singleton
	fun provideScheduleDao(db: WeeklyScheduleDatabase) = db.getScheduleDao()

	@JvmStatic
	@Provides
	@Singleton
	fun provideCategoryDao(db: WeeklyScheduleDatabase) = db.getCategoryDao()

	@JvmStatic
	@Provides
	@Singleton
	fun provideEventDao(db: WeeklyScheduleDatabase) = db.getEventDao()
}