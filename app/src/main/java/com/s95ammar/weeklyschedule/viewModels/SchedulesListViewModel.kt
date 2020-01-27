package com.s95ammar.weeklyschedule.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.s95ammar.weeklyschedule.models.Repository
import com.s95ammar.weeklyschedule.models.data.Schedule
import com.s95ammar.weeklyschedule.util.launchIO
import com.s95ammar.weeklyschedule.util.fetchValue
import com.s95ammar.weeklyschedule.viewModels.viewModelHelpers.SingleLiveEvent
import javax.inject.Inject

class SchedulesListViewModel @Inject constructor(private var repo: Repository) : ViewModel() {
	private val t = "log_${javaClass.simpleName}"

	private val _editedSchedule = MutableLiveData<Schedule>()
	private val _showScheduleEditorDialog = SingleLiveEvent<Unit>()
	private val _onActiveScheduleChanged = SingleLiveEvent<Unit>()
	private val _onScheduleItemClick = SingleLiveEvent<Int>()

	val editedSchedule: LiveData<Schedule> = _editedSchedule
	val showScheduleEditorDialog: LiveData<Unit> = _showScheduleEditorDialog
	val onActiveScheduleIdChanged: LiveData<Unit> = _onActiveScheduleChanged
	val onScheduleItemClick: LiveData<Int> = _onScheduleItemClick

	init {
		Log.d(t, "init: ")
	}

	fun insertSchedule(schedule: Schedule) {
		launchIO { repo.insert(schedule) }
	}

	fun update(vararg schedule: Schedule) = launchIO { repo.update(*schedule) }
	fun delete(schedule: Schedule) = launchIO { repo.delete(schedule) }
	fun getActiveSchedule() = repo.getScheduleById(Schedule.activeScheduleId)
	fun getAllSchedules() = repo.getAllSchedules()

	fun setEditedSchedule(schedule: Schedule) {
		_editedSchedule.value = schedule
	}

	fun showScheduleEditorDialog() = _showScheduleEditorDialog.call()

	fun clearEditorDialogValues() {
		_editedSchedule.value = null
	}

	fun navigateToScheduleViewer(scheduleId: Int) {
		_onScheduleItemClick.value = scheduleId
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
		getActiveSchedule().fetchValue {
			it?.let { activeSchedule ->
				if (activeSchedule != newActiveSchedule) { // true only when RecyclerView is being built
					activeSchedule.isActive = false
					newActiveSchedule.isActive = true
					setActiveScheduleId(newActiveSchedule.id)
					update(activeSchedule, newActiveSchedule)
				}
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