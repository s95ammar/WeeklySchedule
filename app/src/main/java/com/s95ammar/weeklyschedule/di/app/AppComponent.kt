package com.s95ammar.weeklyschedule.di.app

import android.app.Application
import com.s95ammar.weeklyschedule.App
import com.s95ammar.weeklyschedule.di.TimePattern
import com.s95ammar.weeklyschedule.di.main.MainActivitySubcomponent
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Singleton
@Component(
		modules = [
			AppModule::class,
			AndroidInjectionModule::class,
			RoomModule::class
		]
)
interface AppComponent : AndroidInjector<App> {
	fun getMainActivityComponentFactory(): MainActivitySubcomponent.Factory

	@Component.Factory
	interface Factory {
		fun create(
				@BindsInstance application: Application,
				@BindsInstance @TimePattern timePattern: String
		): AppComponent
	}
}