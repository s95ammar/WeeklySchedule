package com.s95ammar.weeklyschedule.viewModels

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.*
import com.s95ammar.weeklyschedule.R
import com.s95ammar.weeklyschedule.models.Repository
import com.s95ammar.weeklyschedule.models.data.Event
import com.s95ammar.weeklyschedule.util.*
import com.s95ammar.weeklyschedule.viewModels.viewModelHelpers.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.joda.time.LocalTime
import javax.inject.Inject

class ScheduleViewerViewModel @Inject constructor(private var repo: Repository) : ViewModel() {
	private val _actionBarTitle = MutableLiveData<String>()
	private val _scheduleMode = MutableLiveData<ScheduleMode>()
	private val _showEventEditorFragment = SingleLiveEvent<Pair<String, Int>>()
	private val _showEventTimePicker = SingleLiveEvent<TimeDetails>()
	private val _eventEditorMode = MutableLiveData<Mode>()
	private val _showDaysMultiChoiceDialog = SingleLiveEvent<Pair<Days, IntArray>>()
	private val _onDaysSelected = SingleLiveEvent<IntArray>()
	private val _onEventTimeSet = SingleLiveEvent<TimeDetails>()
	private val _onEventOperationAllowed = SingleLiveEvent<Result>()

	val actionBarTitle: LiveData<String> = _actionBarTitle
	val scheduleMode: LiveData<ScheduleMode> = _scheduleMode
	val showEventEditorFragment: LiveData<Pair<String, Int>> = _showEventEditorFragment
	val showEventTimePicker: LiveData<TimeDetails> = _showEventTimePicker
	val eventEditorMode: LiveData<Mode> = _eventEditorMode
	val showDaysMultiChoiceDialog: LiveData<Pair<Days, IntArray>> = _showDaysMultiChoiceDialog
	val onDaysSelected: LiveData<IntArray> = _onDaysSelected
	val onEventTimeSet: LiveData<TimeDetails> = _onEventTimeSet
	val onEventOperationAttempt: LiveData<Result> = _onEventOperationAllowed

	init {
		Log.d(LOG_TAG, "init: ")
	}

	fun getScheduleById(id: Int) = repo.getScheduleById(id)

	fun insert(vararg events: Event) = launchIO { repo.insert(*events) }
	fun update(event: Event) = launchIO { repo.update(event) }
	fun delete(event: Event) = launchIO { repo.delete(event) }
	fun deleteEventById(id: Int) = getEventById(id).safeFetch { delete(it) }
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

	fun setEventEditorMode(mode: Mode) {
		_eventEditorMode.value = mode
	}

	fun showEventEditorFragment(keyToEventOrScheduleId: Pair<String, Int>) {
		_showEventEditorFragment.value = keyToEventOrScheduleId
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

	fun tryInsert(eventsToInsert: Array<Event>, scheduleId: Int) {
		getEventsBy(scheduleId).safeFetch { scheduleEvents ->
			for (eventToInsert in eventsToInsert) {
				for (eventOfDay in scheduleEvents.filter { it.day == eventToInsert.day })
					if (eventToInsert.overlapsWith(eventOfDay)) {
						_onEventOperationAllowed.value = Result.Error(R.string.event_overlap_error)
						return@safeFetch
					}
			}

			insert(*eventsToInsert)
			_onEventOperationAllowed.value = Result.Success()
			return@safeFetch
		}
	}

	fun tryUpdate(eventToUpdate: Event) {
		getEventsBy(eventToUpdate.scheduleId, eventToUpdate.day).safeFetch { eventsOfDay ->
			for (eventOfDay in eventsOfDay) if (eventToUpdate.overlapsWith(eventOfDay) && eventToUpdate.id != eventOfDay.id) {
				_onEventOperationAllowed.value = Result.Error(R.string.event_overlap_error)
				return@safeFetch
			}
			update(eventToUpdate)
			_onEventOperationAllowed.value = Result.Success()
			return@safeFetch
		}
	}

	fun validateInput(input: Bundle): Result {
		val startTime = input[KEY_START_TIME] as LocalTime
		val endTime = input[KEY_END_TIME] as LocalTime

		if (!endTime.isAfter(startTime)) {
			return Result.Error(R.string.start_after_end_error)
		}

		val mode = input[KEY_MODE] as Mode
		val selectionDaysIndices = input[KEY_DAYS_INDICES] as IntArray

		if (mode == Mode.ADD && selectionDaysIndices.isEmpty()) {
			return Result.Error(R.string.no_days_selected_error)
		}

		return Result.Success()

	}

}
