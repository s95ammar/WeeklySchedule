package com.s95ammar.weeklyschedule.di.main

import androidx.appcompat.widget.Toolbar
import androidx.annotation.StringRes
import androidx.drawerlayout.widget.DrawerLayout
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
    fun getSchedulesFragment(): SchedulesListFragment
    fun getCategoriesFragment(): CategoriesListFragment
    fun getScheduleViewerFragment(): ScheduleViewerFragment

    @Subcomponent.Factory
    interface Factory: AndroidInjector.Factory<MainActivity> {
        override fun create(@BindsInstance mainActivity: MainActivity): MainActivitySubcomponent
//        fun create(): MainActivitySubcomponent
    }
}