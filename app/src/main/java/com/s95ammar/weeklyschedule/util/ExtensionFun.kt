package com.s95ammar.weeklyschedule.util

import android.app.Application
import android.text.format.DateFormat
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

val Application.SYSTEM_TIME_PATTERN
	get() = if (DateFormat.is24HourFormat(this)) TIME_PATTERN_24H else TIME_PATTERN_12H

fun AppCompatActivity.addFragment(fragment: Fragment, container: ViewGroup, addToBackStack: Boolean) {
	supportFragmentManager.beginTransaction().add(container.id, fragment, null)
			.apply { if (addToBackStack) addToBackStack(null) }
			.commit()
	container.visibility = View.VISIBLE
}

fun AppCompatActivity.removeFragment(fragment: Fragment, container: ViewGroup) {
	if (fragment.isAdded) {
		supportFragmentManager.beginTransaction().remove(fragment).commit()
		container.visibility = View.GONE
	}
}

fun AppCompatActivity.replaceFragment(fragment: Fragment, container: ViewGroup) {
	supportFragmentManager.beginTransaction().replace(container.id, fragment).commit()
}


