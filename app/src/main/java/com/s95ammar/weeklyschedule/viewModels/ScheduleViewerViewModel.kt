package com.s95ammar.weeklyschedule.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.s95ammar.weeklyschedule.models.Repository
import com.s95ammar.weeklyschedule.models.data.Event
import com.s95ammar.weeklyschedule.models.data.Schedule
import com.s95ammar.weeklyschedule.util.ScheduleMode
import com.s95ammar.weeklyschedule.util.launchIO
import javax.inject.Inject

class ScheduleViewerViewModel @Inject constructor(private var repo: Repository) : ViewModel() {
	private val t = "log_${javaClass.simpleName}"

	private val _actionBarTitle = MutableLiveData<String>()
	private val _mode = MutableLiveData<ScheduleMode>()
	private val _schedule = MutableLiveData<Schedule?>()

	val actionBarTitle: LiveData<String> = _actionBarTitle
	val mode: LiveData<ScheduleMode> = _mode
	val schedule: LiveData<Schedule?> = _schedule

	init {
		Log.d(t, "init: ")
	}

	fun insert(event: Event) = launchIO { repo.insert(event) }
	fun update(event: Event) = launchIO { repo.update(event) }
	fun delete(event: Event) = launchIO { repo.delete(event) }
	fun deleteAllEvents() = launchIO { repo.deleteAllEvents() }
	fun getEventsOfSchedule(scheduleId: Int) = repo.getEventsOfSchedule(scheduleId)
	fun getAllEvents() = repo.getAllEvents()

	fun getCategoryById(id: Int) = repo.getCategoryById(id)

	fun setSchedule(schedule: Schedule?) {
		_schedule.value = schedule
	}

	fun setActionBarTitle(title: String) {
		_actionBarTitle.value = title
	}

	fun setMode(mode: ScheduleMode) {
		_mode.value = mode
	}
}
