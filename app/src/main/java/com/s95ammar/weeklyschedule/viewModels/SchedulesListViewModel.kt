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

	private val _showScheduleEditorDialog = SingleLiveEvent<Int>()
	private val _onActiveScheduleChanged = SingleLiveEvent<Unit>()
	private val _onScheduleItemClick = SingleLiveEvent<Int>()

	val showScheduleEditorDialog: LiveData<Int> = _showScheduleEditorDialog
	val onActiveScheduleIdChanged: LiveData<Unit> = _onActiveScheduleChanged
	val onScheduleItemClick: LiveData<Int> = _onScheduleItemClick

	init {
		Log.d(LOG_TAG, "init: ")
	}

	fun insertSchedule(schedule: Schedule) {
		viewModelScope.launch { repo.insert(schedule) }
	}

	fun update(vararg schedule: Schedule) = viewModelScope.launch { repo.update(*schedule) }
	fun delete(schedule: Schedule) = viewModelScope.launch { repo.delete(schedule) }
	fun getScheduleById(id: Int) = repo.getScheduleById(id)
	fun getActiveSchedule() = repo.getScheduleById(Schedule.activeScheduleId)
	fun getAllSchedules() = repo.getAllSchedules()

	fun showScheduleEditorDialog(scheduleId: Int = 0) {
		_showScheduleEditorDialog.value = scheduleId
	}

	fun navigateToScheduleViewer(scheduleId: Int) {
		_onScheduleItemClick.value = scheduleId
	}

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
		getActiveSchedule().safeFetch { activeSchedule ->
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


}