package com.s95ammar.weeklyschedule.util

import android.app.Application
import android.text.format.DateFormat
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

val Application.SYSTEM_TIME_PATTERN
	get() = if (DateFormat.is24HourFormat(this)) TIME_PATTERN_24H else TIME_PATTERN_12H


// Just to improve readability
fun DrawerLayout.isOpen() = isDrawerOpen(GravityCompat.START)
fun DrawerLayout.close() = closeDrawer(GravityCompat.START)

fun <T> LiveData<T>.observeOnce(lifecycleOwner: LifecycleOwner, onChanged: (T?) -> Unit) {
	observe(lifecycleOwner, Observer {
		removeObservers(lifecycleOwner)
		onChanged(it)
	})
}

val EditText.input
	get() = text.toString()