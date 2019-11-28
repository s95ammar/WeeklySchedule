package com.s95ammar.weeklyschedule

import com.s95ammar.weeklyschedule.di.app.DaggerAppComponent
import com.s95ammar.weeklyschedule.util.SYSTEM_TIME_PATTERN
import dagger.android.DaggerApplication

class App : DaggerApplication() {

	override fun applicationInjector() = DaggerAppComponent.factory().create(this, SYSTEM_TIME_PATTERN)
}
