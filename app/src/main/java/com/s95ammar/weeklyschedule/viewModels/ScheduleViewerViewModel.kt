package com.s95ammar.weeklyschedule.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.s95ammar.weeklyschedule.models.Repository
import com.s95ammar.weeklyschedule.models.data.Event
import com.s95ammar.weeklyschedule.models.data.Schedule
import javax.inject.Inject

class ScheduleViewerViewModel @Inject constructor(private var repo: Repository) : ViewModel() {
	private val t = "log_${javaClass.simpleName}"

	private val _actionBarTitle = MutableLiveData<String>()

	val actionBarTitle: LiveData<String> = _actionBarTitle

	init {
		Log.d(t, "init: ")
	}

	fun insert(event: Event) = repo.insert(event)
	fun update(event: Event) = repo.update(event)
	fun delete(event: Event) = repo.delete(event)
	fun deleteAllEvents() = repo.deleteAllEvents()
	fun getEventById(id: Int) = repo.getEventById(id)
	fun getAllEvents() = repo.getAllEvents()

	fun getActiveSchedule() = repo.getScheduleById(Schedule.activeScheduleId)

	fun setActionBarTitle(title: String) {
		_actionBarTitle.value = title
	}
}
