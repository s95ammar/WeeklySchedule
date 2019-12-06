package com.s95ammar.weeklyschedule.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.s95ammar.weeklyschedule.models.Repository
import com.s95ammar.weeklyschedule.models.data.Day
import com.s95ammar.weeklyschedule.models.data.Schedule
import com.s95ammar.weeklyschedule.util.launchIO
import com.s95ammar.weeklyschedule.util.observeOnce
import com.s95ammar.weeklyschedule.viewModels.viewModelHelpers.SingleLiveEvent
import javax.inject.Inject

class SchedulesListViewModel @Inject constructor(private var repo: Repository) : ViewModel() {
	private val t = "log_${javaClass.simpleName}"

	private val _editedSchedule = MutableLiveData<Schedule>()
	private val _showScheduleEditorDialog = SingleLiveEvent<Unit>()
	private val _onActiveScheduleChanged = SingleLiveEvent<Unit>()

	val editedSchedule: LiveData<Schedule> = _editedSchedule
	val showScheduleEditorDialog: LiveData<Unit> = _showScheduleEditorDialog
	val onActiveScheduleIdChanged: LiveData<Unit> = _onActiveScheduleChanged

	init {
		Log.d(t, "init: ")
	}

	fun insertScheduleWithDays(schedule: Schedule) {
		launchIO {
			val scheduleId = repo.insertAndReturnId(schedule).toInt()
			for (dayNumInSchedule in 0 until schedule.daysAmount)
				insert(Day(Day.getDayOfWeek(dayNumInSchedule), scheduleId))
		}
	}

	fun update(vararg schedule: Schedule) = launchIO { repo.update(*schedule) }
	fun delete(schedule: Schedule) = launchIO { repo.delete(schedule) }
	fun getActiveSchedule() = repo.getScheduleById(Schedule.activeScheduleId)
	fun getAllSchedules() = repo.getAllSchedules()

	fun insert(day: Day) = launchIO { repo.insert(day) }
	fun update(day: Day) = launchIO { repo.update(day) }
	fun getDayById(id: Int) = repo.getDayById(id)
	fun getDaysByScheduleId(scheduleId: Int) = repo.getDaysByScheduleId(scheduleId)
	fun getAllDays() = repo.getAllDays()

	fun setEditedSchedule(schedule: Schedule) {
		_editedSchedule.value = schedule
	}

	fun showScheduleEditorDialog() = _showScheduleEditorDialog.call()

	fun clearEditorDialogValues() {
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
		schedule.activate()
		setActiveScheduleId(schedule.id)
		update(schedule)
	}

	private fun manageActiveScheduleReplacement(newActiveSchedule: Schedule) {
		getActiveSchedule().observeOnce(Observer { activeSchedule ->
			activeSchedule.deactivate()
			newActiveSchedule.activate()
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