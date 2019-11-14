package com.s95ammar.weeklyschedule.di.main

import com.s95ammar.weeklyschedule.views.activities.MainActivity
import com.s95ammar.weeklyschedule.views.fragments.CategoriesListFragment
import com.s95ammar.weeklyschedule.views.fragments.ScheduleViewerFragment
import com.s95ammar.weeklyschedule.views.fragments.SchedulesListFragment
import dagger.BindsInstance
import dagger.Subcomponent
import dagger.android.AndroidInjector

@MainActivityScope
@Subcomponent(modules = [MainActivityModule::class])
interface MainActivitySubcomponent : AndroidInjector<MainActivity> {

    @Subcomponent.Factory
    interface Factory: AndroidInjector.Factory<MainActivity> {
        override fun create(@BindsInstance mainActivity: MainActivity): MainActivitySubcomponent
    }
}