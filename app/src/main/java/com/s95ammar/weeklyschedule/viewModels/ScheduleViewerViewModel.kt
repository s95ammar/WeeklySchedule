package com.s95ammar.weeklyschedule.viewModels

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.s95ammar.weeklyschedule.R
import com.s95ammar.weeklyschedule.models.Repository
import com.s95ammar.weeklyschedule.models.data.Event
import com.s95ammar.weeklyschedule.util.*
import com.s95ammar.weeklyschedule.viewModels.viewModelHelpers.SingleLiveEvent
import kotlinx.coroutines.launch
import org.joda.time.LocalTime
import javax.inject.Inject

class ScheduleViewerViewModel @Inject constructor(private var repo: Repository) : ViewModel() {
	private val _scheduleMode = MutableLiveData<ScheduleMode>()
	private val _onEventOperationAllowed = SingleLiveEvent<Result>()

	val scheduleMode: LiveData<ScheduleMode> = _scheduleMode
	val onEventOperationAttempt: LiveData<Result> = _onEventOperationAllowed

	fun getScheduleById(id: Int) = repo.getScheduleById(id)

	fun insert(vararg events: Event) = viewModelScope.launch { repo.insert(*events) }
	fun update(event: Event) = viewModelScope.launch { repo.update(event) }
	fun delete(event: Event) = viewModelScope.launch { repo.delete(event) }
	fun getEventById(id: Int) = repo.getEventById(id)
	fun getEventsBy(scheduleId: Int) = repo.getEventsBy(scheduleId)
	fun getEventsBy(scheduleId: Int, day: String) = repo.getEventsBy(scheduleId, day)

	fun getCategoryById(id: Int) = repo.getCategoryById(id)
	fun getAllCategories() = repo.getAllCategories()

	fun setScheduleViewerMode(scheduleMode: ScheduleMode) {
		_scheduleMode.value = scheduleMode
	}

	fun getDaysAbbreviationsString(days: List<String>): String {
		val str = getDaysAbbreviations(days).toString()
		return str.substring(1, str.lastIndex)
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
