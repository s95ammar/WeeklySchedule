package com.s95ammar.weeklyschedule.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.s95ammar.weeklyschedule.models.Repository
import com.s95ammar.weeklyschedule.models.data.Event
import com.s95ammar.weeklyschedule.util.Mode
import com.s95ammar.weeklyschedule.util.ScheduleMode
import com.s95ammar.weeklyschedule.util.getDaysAbbreviations
import com.s95ammar.weeklyschedule.util.launchIO
import com.s95ammar.weeklyschedule.viewModels.viewModelHelpers.SingleLiveEvent
import javax.inject.Inject

class ScheduleViewerViewModel @Inject constructor(private var repo: Repository) : ViewModel() {
	private val t = "log_${javaClass.simpleName}"

	private val _actionBarTitle = MutableLiveData<String>()
	private val _scheduleMode = MutableLiveData<ScheduleMode>()
	private val _editedEvent = MutableLiveData<Event>()
	private val _showEventEditorFragment = SingleLiveEvent<Pair<String, Int>>()
	private val _eventEditorMode = MutableLiveData<Mode>()
	private val _showDaysMultiChoiceDialog = SingleLiveEvent<Pair<List<String>, IntArray>>()
	private val _onDaysSelected = SingleLiveEvent<Array<String>>()

	val actionBarTitle: LiveData<String> = _actionBarTitle
	val scheduleMode: LiveData<ScheduleMode> = _scheduleMode
	val editedEvent: LiveData<Event> = _editedEvent
	val showEventEditorFragment: LiveData<Pair<String, Int>> = _showEventEditorFragment
	val eventEditorMode: LiveData<Mode> = _eventEditorMode
	val showDaysMultiChoiceDialog: LiveData<Pair<List<String>, IntArray>> = _showDaysMultiChoiceDialog
	val onDaysSelected: LiveData<Array<String>> = _onDaysSelected

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

	fun showDaysMultiChoiceDialog(days: Array<String>, selectedDays: Array<String>) {
		val selectionIndices = ArrayList<Int>(selectedDays.size)
		selectedDays.forEach { day -> selectionIndices.add(days.indexOf(day)) }
		_showDaysMultiChoiceDialog.value = days.toList() to selectionIndices.toIntArray()
	}

	fun displaySelectedDays(selection: List<CharSequence>) {
		_onDaysSelected.value = Array(selection.size) { i -> selection[i].toString() }
	}

	fun getDaysAbbreviationsString(days: Array<String>): String {
		val daysAbbrevToString = getDaysAbbreviations(days).contentToString()
		return daysAbbrevToString.substring(1, daysAbbrevToString.lastIndex)
	}

}
