package com.s95ammar.weeklyschedule.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.s95ammar.weeklyschedule.models.Repository
import com.s95ammar.weeklyschedule.models.data.Schedule
import com.s95ammar.weeklyschedule.util.LOG_TAG
import com.s95ammar.weeklyschedule.util.safeFetch
import com.s95ammar.weeklyschedule.viewModels.viewModelHelpers.SingleLiveEvent
import kotlinx.coroutines.launch
import javax.inject.Inject

class SchedulesListViewModel @Inject constructor(private var repo: Repository) : ViewModel() {

	val _onActiveScheduleChanged = SingleLiveEvent<Unit>()

	val onActiveScheduleIdChanged: LiveData<Unit> = _onActiveScheduleChanged

	init {
		Log.d(LOG_TAG, "init: ")
	}

	fun insertSchedule(schedule: Schedule) = viewModelScope.launch { repo.insert(schedule) }
	fun update(vararg schedule: Schedule) = viewModelScope.launch { repo.update(*schedule) }
	fun delete(schedule: Schedule) = viewModelScope.launch { repo.delete(schedule) }
	fun getScheduleById(id: Int) = repo.getScheduleById(id)
	fun getAllSchedules() = repo.getAllSchedules()

	fun renameSchedule(id: Int, newName: String) {
		getScheduleById(id).safeFetch {
			update(Schedule(newName, it.days, it.isActive).apply { this.id = it.id })
		}
	}

	private fun manageScheduleDeactivation(schedule: Schedule) {
		if (schedule.isActive) {
			schedule.isActive = false
			setActiveScheduleId(0)
			update(schedule)
		}
	}

	private fun manageScheduleActivation(schedule: Schedule) {
		schedule.isActive = true
		setActiveScheduleId(schedule.id)
		update(schedule)
	}

	private fun manageActiveScheduleReplacement(newActiveSchedule: Schedule) {
		getScheduleById(Schedule.activeScheduleId).safeFetch { activeSchedule ->
			if (activeSchedule != newActiveSchedule) { // true only when RecyclerView is being built
				activeSchedule.isActive = false
				newActiveSchedule.isActive = true
				setActiveScheduleId(newActiveSchedule.id)
				update(activeSchedule, newActiveSchedule)
			}
		}
	}

	private fun setActiveScheduleId(id: Int) {
		Schedule.activeScheduleId = id
		_onActiveScheduleChanged.call()
	}

	fun handleSwitchChange(modifiedSchedule: Schedule, isChecked: Boolean) {
		when {
			!isChecked -> manageScheduleDeactivation(modifiedSchedule)
			isChecked -> when {
				Schedule.activeExists() -> manageActiveScheduleReplacement(modifiedSchedule)
				Schedule.activeDoesntExist() -> manageScheduleActivation(modifiedSchedule)
			}
		}
	}

	override fun onCleared() {
		super.onCleared()
		Log.d(LOG_TAG, "onCleared: ")
	}
}