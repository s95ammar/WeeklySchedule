package com.s95ammar.weeklyschedule.di.main

import com.s95ammar.weeklyschedule.views.fragments.CategoriesListFragment
import com.s95ammar.weeklyschedule.views.fragments.ScheduleViewerFragment
import com.s95ammar.weeklyschedule.views.fragments.SchedulesListFragment
import com.s95ammar.weeklyschedule.views.fragments.dialogs.CategoryRefactorDialog
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainActivityFragmentsModule {
	@ContributesAndroidInjector
	abstract fun contributeScheduleViewerFragment(): ScheduleViewerFragment

	@ContributesAndroidInjector
	abstract fun contributeSchedulesListFragment(): SchedulesListFragment

	@ContributesAndroidInjector
	abstract fun contributeCategoriesListFragment(): CategoriesListFragment

	@ContributesAndroidInjector
	abstract fun contributeCategoryRefactorDialog(): CategoryRefactorDialog

}