package com.s95ammar.weeklyschedule.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.s95ammar.weeklyschedule.models.Repository
import com.s95ammar.weeklyschedule.models.data.Category
import com.s95ammar.weeklyschedule.models.data.Event
import com.s95ammar.weeklyschedule.models.data.Schedule
import com.s95ammar.weeklyschedule.util.Mode
import com.s95ammar.weeklyschedule.util.safeFetch
import javax.inject.Inject

class SharedDataViewModel @Inject constructor(private var repo: Repository) : ViewModel() {

	private val _allCategories = MutableLiveData<List<Category>>()
	private val _eventEditorFragmentMode = MutableLiveData<Mode>()
	private val _editedSchedule = MutableLiveData<Schedule>()
	private val _editedEvent = MutableLiveData<Event>()
	private val _actionBarTitle = MutableLiveData<String>()

	val allCategories: LiveData<List<Category>> = _allCategories
	val eventEditorFragmentMode: LiveData<Mode> = _eventEditorFragmentMode
	val editedSchedule: LiveData<Schedule> = _editedSchedule
	val editedEvent: LiveData<Event> = _editedEvent
	val actionBarTitle: LiveData<String> = _actionBarTitle

	fun setAllCategories() {
		repo.getAllCategories().safeFetch {
			_allCategories.value = it
		}
	}

	fun setEventEditorFragmentMode (mode: Mode) {
		_eventEditorFragmentMode .value = mode
	}

	fun setEditedSchedule(schedule: Schedule) {
		_editedSchedule.value = schedule
	}

	fun setEditedEvent(event: Event) {
		_editedEvent.value = event
	}

	fun setActionBarTitle(title: String) {
		_actionBarTitle.value = title
	}
}
