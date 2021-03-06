package com.s95ammar.weeklyschedule.di.main

import android.app.Activity
import androidx.lifecycle.ViewModel
import com.s95ammar.weeklyschedule.di.ViewModelKey
import com.s95ammar.weeklyschedule.viewModels.CategoriesListViewModel
import com.s95ammar.weeklyschedule.viewModels.EventEditorViewModel
import com.s95ammar.weeklyschedule.viewModels.ScheduleViewerViewModel
import com.s95ammar.weeklyschedule.viewModels.SchedulesListViewModel
import com.s95ammar.weeklyschedule.views.activities.MainActivity
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class MainActivityModule {

	@Binds
	@IntoMap
	@ViewModelKey(ScheduleViewerViewModel::class)
	abstract fun bindScheduleViewerViewModel(scheduleViewerViewModel: ScheduleViewerViewModel): ViewModel

	@Binds
	@IntoMap
	@ViewModelKey(SchedulesListViewModel::class)
	abstract fun bindSchedulesListViewModel(schedulesListViewModel: SchedulesListViewModel): ViewModel

	@Binds
	@IntoMap
	@ViewModelKey(CategoriesListViewModel::class)
	abstract fun bindCategoriesListViewModel(categoriesListViewModel: CategoriesListViewModel): ViewModel

	@Binds
	@IntoMap
	@ViewModelKey(EventEditorViewModel::class)
	abstract fun bindEventEditorViewModel(eventEditorViewModel: EventEditorViewModel): ViewModel

	@Binds
	abstract fun bindMainActivity(mainActivity: MainActivity): Activity

}