package com.s95ammar.weeklyschedule.di.app

import androidx.lifecycle.ViewModelProvider
import com.s95ammar.weeklyschedule.viewModels.viewModelHelpers.ViewModelFactory
import com.s95ammar.weeklyschedule.views.fragments.ScheduleViewerFragment
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
abstract class AppModule {

    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

}