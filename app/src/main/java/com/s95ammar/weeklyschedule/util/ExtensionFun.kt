package com.s95ammar.weeklyschedule.util

import android.app.Application
import android.text.format.DateFormat
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

val Application.SYSTEM_TIME_PATTERN
	get() = if (DateFormat.is24HourFormat(this)) TIME_PATTERN_24H else TIME_PATTERN_12H

// Just to improve readability
fun DrawerLayout.isOpen() = isDrawerOpen(GravityCompat.START)
fun DrawerLayout.close() = closeDrawer(GravityCompat.START)
fun DrawerLayout.closeIfOpen() { if (isOpen()) close() }

fun <T> LiveData<T>.fetchAndIfExists(action: (t: T) -> Unit) {
	observeForever(object : Observer<T> {
		override fun onChanged(t: T?) {
			removeObserver(this)
			t?.let { action(it) }
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

fun launchIO(block: suspend () -> Unit): Job = CoroutineScope(IO).launch { block() }
fun lunchMain(block: suspend () -> Unit): Job = CoroutineScope(Main).launch { block() }
