package com.s95ammar.weeklyschedule.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.s95ammar.weeklyschedule.models.Repository
import com.s95ammar.weeklyschedule.models.data.Event
import com.s95ammar.weeklyschedule.util.*
import com.s95ammar.weeklyschedule.viewModels.viewModelHelpers.SingleLiveEvent
import javax.inject.Inject

class ScheduleViewerViewModel @Inject constructor(private var repo: Repository) : ViewModel() {
	private val t = "log_${javaClass.simpleName}"
	private val _actionBarTitle = MutableLiveData<String>()
	private val _scheduleMode = MutableLiveData<ScheduleMode>()
	private val _editedEvent = MutableLiveData<Event>()
	private val _showEventEditorFragment = SingleLiveEvent<Pair<String, Int>>()
	private val _showEventTimePicker = SingleLiveEvent<TimeDetails>()
	private val _eventEditorMode = MutableLiveData<Mode>()
	private val _showDaysMultiChoiceDialog = SingleLiveEvent<Pair<Days, IntArray>>()
	private val _onDaysSelected = SingleLiveEvent<IntArray>()
	private val _onEventTimeSet = SingleLiveEvent<TimeDetails>()

	val actionBarTitle: LiveData<String> = _actionBarTitle
	val scheduleMode: LiveData<ScheduleMode> = _scheduleMode
	val editedEvent: LiveData<Event> = _editedEvent
	val showEventEditorFragment: LiveData<Pair<String, Int>> = _showEventEditorFragment
	val showEventTimePicker: LiveData<TimeDetails> = _showEventTimePicker
	val eventEditorMode: LiveData<Mode> = _eventEditorMode
	val showDaysMultiChoiceDialog: LiveData<Pair<Days, IntArray>> = _showDaysMultiChoiceDialog
	val onDaysSelected: LiveData<IntArray> = _onDaysSelected
	val onEventTimeSet: LiveData<TimeDetails> = _onEventTimeSet

	init {
		Log.d(t, "init: ")
	}

	fun getScheduleById(id: Int) = repo.getScheduleById(id)

	fun insert(event: Event) = launchIO { repo.insert(event) }
	fun update(event: Event) = launchIO { repo.update(event) }
	fun delete(event: Event) = launchIO { repo.delete(event) }
	fun getEventById(id: Int) = repo.getEventById(id)
	fun getEventsBy(scheduleId: Int) = repo.getEventsBy(scheduleId)
	fun getEventsBy(scheduleId: Int, day: String) = repo.getEventsBy(scheduleId, day)
	fun getEventsBy(categoryId: Int, scheduleId: Int) = repo.getEventsBy(categoryId, scheduleId)

	fun getCategoryById(id: Int) = repo.getCategoryById(id)
	fun getAllCategories() = repo.getAllCategories()

	fun setActionBarTitle(title: String) {
		_actionBarTitle.value = title
	}

	fun setScheduleViewerMode(scheduleMode: ScheduleMode) {
		_scheduleMode.value = scheduleMode
	}

	fun setEditedEvent(event: Event) {
		_editedEvent.value = event
	}

	fun setEventEditorMode(mode: Mode) {
		_eventEditorMode.value = mode
	}

	fun showEventEditorFragment(keyToId: Pair<String, Int>) {
		_showEventEditorFragment.value = keyToId
	}

	fun showDaysMultiChoiceDialog(days: Days, selectionIndices: IntArray) {
		_showDaysMultiChoiceDialog.value = days to selectionIndices
	}

	fun displaySelectedDays(indices: IntArray) {
		_onDaysSelected.value = indices
	}

	fun getDaysAbbreviationsString(days: List<String>): String {
		val str = getDaysAbbreviations(days).toString()
		return str.substring(1, str.lastIndex)
	}

	fun showEventTimePicker(timeDetails: TimeDetails) {
		_showEventTimePicker.value = timeDetails
	}

	fun setEventTime(timeDetails: TimeDetails) {
		_onEventTimeSet.value = timeDetails
	}

}
