package com.s95ammar.weeklyschedule.di.app

import com.s95ammar.weeklyschedule.di.main.MainActivityFragmentsModule
import com.s95ammar.weeklyschedule.di.main.MainActivityModule
import com.s95ammar.weeklyschedule.views.activities.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivitiesModule {
	@ContributesAndroidInjector(modules = [MainActivityModule::class, MainActivityFragmentsModule::class])
	abstract fun contributesMainActivity(): MainActivity
}