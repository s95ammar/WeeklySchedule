package com.s95ammar.weeklyschedule.views.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.s95ammar.weeklyschedule.R
import com.s95ammar.weeklyschedule.di.TimePattern
import com.s95ammar.weeklyschedule.models.data.Event
import com.s95ammar.weeklyschedule.models.data.Schedule
import com.s95ammar.weeklyschedule.util.Mode
import com.s95ammar.weeklyschedule.util.fetchAndIfExists
import com.s95ammar.weeklyschedule.viewModels.ScheduleViewerViewModel
import com.s95ammar.weeklyschedule.views.adapters.CategorySpinnerAdapter
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_event_editor.*
import javax.inject.Inject


class EventEditorFragment : DaggerFragment() {
	private val t = "log_${javaClass.simpleName}"

	@Inject
	lateinit var factory: ViewModelProvider.Factory

	@field: [Inject TimePattern]
	lateinit var timePattern: String

	private lateinit var viewModel: ScheduleViewerViewModel
	private lateinit var schedule: Schedule
	private lateinit var event: Event
	private val mode
		get() = viewModel.eventEditorMode.value
	private val argEventId
		get() = arguments?.getInt(resources.getString(R.string.key_event_id)) ?: 0
	private val argScheduleId
		get() = arguments?.getInt(resources.getString(R.string.key_schedule_id)) ?: 0

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
	                          savedInstanceState: Bundle?): View? {
		return inflater.inflate(R.layout.fragment_event_editor, container, false)
	}

	override fun onActivityCreated(savedInstanceState: Bundle?) {
		super.onActivityCreated(savedInstanceState)
		viewModel = ViewModelProviders.of(requireActivity(), factory).get(ScheduleViewerViewModel::class.java)
		setMode()
		setValues()
		setUpLayout()
	}

	private fun setMode() {
		viewModel.setEventEditorMode(when (argEventId) {
			0 -> Mode.ADD
			else -> Mode.EDIT
		})
	}

	private fun setValues() {
		when (mode) {
			Mode.ADD -> setSchedule(argScheduleId)
			Mode.EDIT -> viewModel.getEventById(argEventId).fetchAndIfExists { event ->
				this.event = event
				setSchedule(event.scheduleId)
			}
		}
	}

	private fun setSchedule(scheduleId: Int) =
			viewModel.getScheduleById(scheduleId).fetchAndIfExists { schedule -> this.schedule = schedule }

	private fun setUpLayout() {
		setUpCategorySpinner()
		setUpDaysCardView()

		if (mode == Mode.EDIT) {
			setCategorySpinnerSelection()
			textView_event_days.setText(R.string.day)
			textView_event_name.text = event.name
			spinner_event_days.setSelection(schedule.days.indexOf(event.day))
			textView_event_start_value.text = event.startTime.toString(timePattern)
			textView_event_end_value.text = event.endTime.toString(timePattern)
		}
	}

	private fun setUpCategorySpinner() {
		viewModel.getAllCategories().fetchAndIfExists { allCategories ->
			spinner_event_categories.adapter = CategorySpinnerAdapter(requireContext(), allCategories)
			cardView_event_category.setOnClickListener { spinner_event_categories.performClick() }
		}
	}

	private fun setCategorySpinnerSelection() {
		viewModel.getCategoryById(event.categoryId).fetchAndIfExists { category ->
			viewModel.getAllCategories().fetchAndIfExists { allCategories ->
				spinner_event_categories.setSelection(allCategories.indexOf(category))
			}
		}
	}

	private fun setUpDaysCardView() {
		when (mode) {
			Mode.EDIT -> {
				spinner_event_days.visibility = View.VISIBLE
				cardView_event_day.setOnClickListener { spinner_event_days.performClick() }
			}
			Mode.ADD -> {
				spinner_event_days.visibility = View.GONE
				cardView_event_day.setOnClickListener {
					viewModel.showDaysMultiChoiceDialog(schedule.days)
					observeDaysSelection()
				}
			}

		}
	}

	private fun observeDaysSelection() {
		viewModel.onDaysSelected.observe(viewLifecycleOwner, Observer {
			it?.let { values ->
				textView_event_days_value.text = values
			}
		})
	}


}
