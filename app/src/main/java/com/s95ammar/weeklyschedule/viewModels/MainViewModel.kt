package com.s95ammar.weeklyschedule.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import javax.inject.Inject

class MainViewModel @Inject constructor() : ViewModel() {
    private val t = "log_${javaClass.simpleName}"

    init {
        Log.d(t, "init: ")
    }
}