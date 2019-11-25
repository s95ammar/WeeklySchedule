package com.s95ammar.weeklyschedule.util

import android.app.Activity
import android.app.Application
import android.content.Context
import android.text.format.DateFormat
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.MenuRes
import androidx.annotation.StringRes
import androidx.appcompat.view.menu.MenuBuilder
import androidx.appcompat.view.menu.MenuPopupHelper
import androidx.appcompat.widget.PopupMenu
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

fun <T> LiveData<T>.observeOnce(lifecycleOwner: LifecycleOwner, observer: Observer<T>) {
	observe(lifecycleOwner, object : Observer<T> {
		override fun onChanged(t: T?) {
			removeObserver(this)
			observer.onChanged(t)
		}
	})
}

fun <T> LiveData<T>.observeOnce(observer: Observer<T>) {
	observeForever(object : Observer<T> {
		override fun onChanged(t: T?) {
			removeObserver(this)
			observer.onChanged(t)
		}
	})
}

val EditText.input
	get() = text.toString()

fun Fragment.toast(@StringRes stringRes: Int, length: Int = Toast.LENGTH_SHORT) {
	Toast.makeText(activity, stringRes, length).show()
}

fun Fragment.toast(msg: String, length: Int = Toast.LENGTH_SHORT) {
	Toast.makeText(activity, msg, length).show()
}
