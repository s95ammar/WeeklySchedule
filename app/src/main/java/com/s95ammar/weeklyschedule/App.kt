package com.s95ammar.weeklyschedule

import android.app.Application
import android.text.format.DateFormat
import com.s95ammar.weeklyschedule.di.app.AppComponent
import com.s95ammar.weeklyschedule.di.app.DaggerAppComponent
import com.s95ammar.weeklyschedule.util.TIME_PATTERN_12H
import com.s95ammar.weeklyschedule.util.TIME_PATTERN_24H
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

class App : DaggerApplication() {
	lateinit var component: AppComponent

	override fun applicationInjector(): AndroidInjector<out DaggerApplication>? {
		return DaggerAppComponent.factory().create(this, getTimePattern()).also { component = it }
	}

	private fun getTimePattern() = if (DateFormat.is24HourFormat(this)) TIME_PATTERN_24H else TIME_PATTERN_12H

	override fun onCreate() {
		super.onCreate()

	}


}
