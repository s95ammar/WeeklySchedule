package com.s95ammar.weeklyschedule.di.app

import androidx.lifecycle.ViewModelProvider
import com.s95ammar.weeklyschedule.viewModels.viewModelHelpers.ViewModelFactory
import dagger.Binds
import dagger.Module

@Module
abstract class AppModule {

    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory
}