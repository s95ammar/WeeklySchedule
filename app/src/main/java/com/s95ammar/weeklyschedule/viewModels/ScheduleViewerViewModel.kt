package com.s95ammar.weeklyschedule.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.s95ammar.weeklyschedule.models.Repository
import com.s95ammar.weeklyschedule.models.data.Event
import com.s95ammar.weeklyschedule.models.data.Schedule
import com.s95ammar.weeklyschedule.util.ScheduleMode
import javax.inject.Inject

class ScheduleViewerViewModel @Inject constructor(private var repo: Repository) : ViewModel() {

	lateinit var schedule: Schedule
	lateinit var events: List<Event>

	private val _mode = MutableLiveData<ScheduleMode>()

	val scheduleMode: LiveData<ScheduleMode> = _mode

	fun getScheduleById(id: Int) = repo.getScheduleById(id)

	fun getEventsBy(scheduleId: Int) = repo.getEventsBy(scheduleId)
	fun getEventsBy(scheduleId: Int, day: String) = repo.getEventsBy(scheduleId, day)

	fun getCategoryById(id: Int) = repo.getCategoryById(id)
	fun getAllCategories() = repo.getAllCategories()

	fun setMode(scheduleMode: ScheduleMode) {
		_mode.value = scheduleMode
	}

}
