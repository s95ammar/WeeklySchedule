package com.s95ammar.weeklyschedule.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.s95ammar.weeklyschedule.models.Repository
import com.s95ammar.weeklyschedule.models.data.Day
import com.s95ammar.weeklyschedule.models.data.Schedule
import com.s95ammar.weeklyschedule.util.observeOnce
import com.s95ammar.weeklyschedule.viewModels.viewModelHelpers.SingleLiveEvent
import javax.inject.Inject

class SchedulesListViewModel @Inject constructor(private var repo: Repository) : ViewModel() {
	private val t = "log_${javaClass.simpleName}"

	private val _editedSchedule = MutableLiveData<Schedule>()
	private val _showScheduleNamerDialog = SingleLiveEvent<Schedule?>()
	private val _onActiveScheduleChanged = SingleLiveEvent<Unit>()

	val editedSchedule: LiveData<Schedule> = _editedSchedule
	val showScheduleNamerDialog: LiveData<Schedule?> = _showScheduleNamerDialog
	val onActiveScheduleIdChanged: LiveData<Unit> = _onActiveScheduleChanged

	init {
		Log.d(t, "init: ")
	}

	fun insert(schedule: Schedule) = repo.insert(schedule)
	fun update(vararg schedule: Schedule) = repo.update(*schedule)
	fun delete(schedule: Schedule) = repo.delete(schedule)
	fun deleteAllSchedules() = repo.deleteAllSchedules()
	fun getScheduleById(id: Int) = repo.getScheduleById(id)
	fun getActiveSchedule() = repo.getScheduleById(Schedule.activeScheduleId)
	fun getAllSchedules() = repo.getAllSchedules()

	fun insert(day: Day) = repo.insert(day)
	fun update(day: Day) = repo.update(day)
	fun delete(day: Day) = repo.delete(day)
	fun deleteAllDays() = repo.deleteAllDays()
	fun getDayById(id: Int) = repo.getDayById(id)
	fun getAllDays() = repo.getAllDays()

	fun setEditedSchedule(schedule: Schedule) {
		_editedSchedule.value = schedule
	}

	fun showScheduleNamerDialog(schedule: Schedule? = null) {
		_showScheduleNamerDialog.value = schedule
	}

	fun clearNamerDialogValues() {
		_editedSchedule.value = null
	}

	private fun manageScheduleDeactivation(schedule: Schedule) {
		if (schedule.isActive) {
			schedule.deactivate()
			setActiveScheduleId(0)
			update(schedule)
		}
	}

	private fun manageScheduleActivation(schedule: Schedule) {
		schedule.selectAsTheActive()
		setActiveScheduleId(schedule.id)
		update(schedule)
	}

	private fun manageActiveScheduleReplacement(newActiveSchedule: Schedule) {
		getActiveSchedule().observeOnce(Observer { activeSchedule ->
			activeSchedule.deactivate()
			newActiveSchedule.selectAsTheActive()
			setActiveScheduleId(newActiveSchedule.id)
			update(activeSchedule, newActiveSchedule)
		})
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