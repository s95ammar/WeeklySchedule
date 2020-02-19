package com.s95ammar.weeklyschedule

import com.s95ammar.weeklyschedule.di.app.DaggerAppComponent
import com.s95ammar.weeklyschedule.models.data.Schedule
import com.s95ammar.weeklyschedule.util.ACTIVE_SCHEDULE_ID_KEY
import com.s95ammar.weeklyschedule.util.SHARED_PREFERENCES
import com.s95ammar.weeklyschedule.util.SYSTEM_TIME_PATTERN
import dagger.android.DaggerApplication

class App : DaggerApplication() {

	override fun applicationInjector() = DaggerAppComponent.factory().create(this, SYSTEM_TIME_PATTERN)

	override fun onCreate() {
		super.onCreate()
		Schedule.activeScheduleId = loadActiveScheduleId()
	}

	private fun loadActiveScheduleId() = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE)
			.getInt(ACTIVE_SCHEDULE_ID_KEY, 0)

}
