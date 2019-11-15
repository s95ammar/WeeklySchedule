package com.s95ammar.weeklyschedule.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.s95ammar.weeklyschedule.models.Repository
import com.s95ammar.weeklyschedule.models.data.Schedule
import com.s95ammar.weeklyschedule.models.data.Day
import com.s95ammar.weeklyschedule.viewModels.viewModelHelpers.SingleLiveEvent
import javax.inject.Inject

class SchedulesListViewModel @Inject constructor(private var repo: Repository) : ViewModel() {
	private val t = "log_${javaClass.simpleName}"

	private val _onAddScheduleActionButtonClick = SingleLiveEvent<Unit>()
	private val _onsetScheduleColorButtonClick = SingleLiveEvent<Int>()


	val onAddScheduleActionButtonClick: LiveData<Unit> = _onAddScheduleActionButtonClick
	val onSetScheduleColorButtonClick: LiveData<Int> = _onsetScheduleColorButtonClick

	init {
		Log.d(t, "init: ")
	}

	fun insert(schedule: Schedule) = repo.insert(schedule)
	fun update(schedule: Schedule) = repo.update(schedule)
	fun delete(schedule: Schedule) = repo.delete(schedule)
	fun deleteAllSchedules() = repo.deleteAllSchedules()
	fun getScheduleById(id: Int) = repo.getScheduleById(id)
	fun getAllSchedules() = repo.getAllSchedules()

	fun insert(day: Day) = repo.insert(day)
	fun update(day: Day) = repo.update(day)
	fun delete(day: Day) = repo.delete(day)
	fun deleteAllDays() = repo.deleteAllDays()
	fun getDayById(id: Int) = repo.getDayById(id)
	fun getAllDays() = repo.getAllDays()


	fun showScheduleRefactorDialog() = _onAddScheduleActionButtonClick.call()

}