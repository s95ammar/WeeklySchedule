package com.s95ammar.weeklyschedule.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.s95ammar.weeklyschedule.models.Repository
import com.s95ammar.weeklyschedule.models.data.Category
import com.s95ammar.weeklyschedule.models.data.Day
import com.s95ammar.weeklyschedule.models.data.Event
import com.s95ammar.weeklyschedule.models.data.Schedule
import com.s95ammar.weeklyschedule.viewModels.viewModelHelpers.SingleLiveEvent
import javax.inject.Inject

class MainViewModel @Inject constructor(private var repo: Repository) : ViewModel() {
	private val t = "log_${javaClass.simpleName}"

	private val _onAddCategoryActionButtonClick = SingleLiveEvent<Unit>()
	private val _onsetCategoryColorButtonClick = SingleLiveEvent<Int>()


	val onAddCategoryActionButtonClick: LiveData<Unit> = _onAddCategoryActionButtonClick
	val onSetCategoryColorButtonClick: LiveData<Int> = _onsetCategoryColorButtonClick

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

	fun insert(category: Category) = repo.insert(category)
	fun update(category: Category) = repo.update(category)
	fun delete(category: Category) = repo.delete(category)
	fun deleteAllCategories() = repo.deleteAllCategories()
	fun getCategoryById(id: Int) = repo.getCategoryById(id)
	fun getAllCategories() = repo.getAllCategories()

	fun insert(event: Event) = repo.insert(event)
	fun update(event: Event) = repo.update(event)
	fun delete(event: Event) = repo.delete(event)
	fun deleteAllEvents() = repo.deleteAllEvents()
	fun getEventById(id: Int) = repo.getEventById(id)
	fun getAllEvents() = repo.getAllEvents()

	fun showCategoryRefactorDialog() = _onAddCategoryActionButtonClick.call()

	fun showColorPickerDialog(viewId: Int) {
		_onsetCategoryColorButtonClick.value = viewId
	}

}