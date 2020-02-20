package com.s95ammar.weeklyschedule.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.s95ammar.weeklyschedule.models.Repository
import com.s95ammar.weeklyschedule.models.data.Category
import com.s95ammar.weeklyschedule.models.data.Event
import com.s95ammar.weeklyschedule.models.data.Schedule
import com.s95ammar.weeklyschedule.util.Mode
import javax.inject.Inject

class SharedDataViewModel @Inject constructor(private var repo: Repository) : ViewModel() {

	lateinit var allCategories: List<Category>
	lateinit var eventEditorFragmentMode: Mode
	lateinit var editedSchedule: Schedule
	lateinit var editedEvent: Event

	private val _actionBarTitle = MutableLiveData<String>()
	val actionBarTitle: LiveData<String> = _actionBarTitle

	init {
		repo.getAllCategories().observeForever {
			allCategories = it
		}
	}

	fun setActionBarTitle(title: String) {
		_actionBarTitle.value = title
	}

}
