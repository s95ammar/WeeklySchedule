package com.s95ammar.weeklyschedule.di.main

import android.app.Activity
import androidx.lifecycle.ViewModel
import com.s95ammar.weeklyschedule.di.ViewModelKey
import com.s95ammar.weeklyschedule.viewModels.MainViewModel
import com.s95ammar.weeklyschedule.views.activities.MainActivity
import com.s95ammar.weeklyschedule.views.fragments.CategoriesListFragment
import com.s95ammar.weeklyschedule.views.fragments.ScheduleViewerFragment
import com.s95ammar.weeklyschedule.views.fragments.SchedulesListFragment
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
abstract class MainActivityModule {

	@Module
	// for static @Provides methods (which can't exist in an abstract class)
	companion object {
		@Provides
		@MainActivityScope
		@JvmStatic
		fun provideSchedulesFragment(): SchedulesListFragment {
			return SchedulesListFragment()
		}

		@Provides
		@MainActivityScope
		@JvmStatic
		fun provideCategoriesFragment(): CategoriesListFragment {
			return CategoriesListFragment()
		}

		@Provides
		@MainActivityScope
		@JvmStatic
		fun provideScheduleViewerFragment(): ScheduleViewerFragment {
			return ScheduleViewerFragment()
		}
	}

	@Binds
	@IntoMap
	@ViewModelKey(MainViewModel::class)
	abstract fun bindMainViewModel(mainViewModel: MainViewModel): ViewModel

	@Binds
	abstract fun bindMainActivity(mainActivity: MainActivity): Activity

}