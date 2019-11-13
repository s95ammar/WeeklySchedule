package com.s95ammar.weeklyschedule

import com.s95ammar.weeklyschedule.di.app.AppComponent
import com.s95ammar.weeklyschedule.di.app.DaggerAppComponent
import com.s95ammar.weeklyschedule.util.SYSTEM_TIME_PATTERN
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

class App : DaggerApplication() {
	lateinit var component: AppComponent

	override fun applicationInjector(): AndroidInjector<out DaggerApplication>? {
		return DaggerAppComponent.factory().create(this, SYSTEM_TIME_PATTERN).also { component = it }
	}


}
