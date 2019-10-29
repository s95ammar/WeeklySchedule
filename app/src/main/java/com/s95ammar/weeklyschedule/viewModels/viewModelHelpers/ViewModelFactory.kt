package com.s95ammar.weeklyschedule.viewModels.viewModelHelpers

import androidx.lifecycle.ViewModel
import javax.inject.Inject
import androidx.lifecycle.ViewModelProvider
import javax.inject.Provider

class ViewModelFactory @Inject constructor(private val creatorsMap: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T = creatorsMap[modelClass]?.get() as T


/*
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        var provider = creatorsMap[modelClass]
        if (provider == null) { // if the viewmodel has not been created

            // loop through the allowable keys (aka allowed classes with the @ViewModelKey)
            for ((keyClass, valueProvider) in creatorsMap) {

                // if it's allowed, set the Provider<ViewModel>
                if (modelClass.isAssignableFrom(keyClass)) {
                    provider = valueProvider
                    break
                }
            }
        }

        // if this is not one of the allowed keys, throw exception
        requireNotNull(provider) { "unknown model class $modelClass" }

        // return the Provider
        try {
            return provider.get() as T
        } catch (e: Exception) {
            throw RuntimeException(e)
        }

    }
*/



}
