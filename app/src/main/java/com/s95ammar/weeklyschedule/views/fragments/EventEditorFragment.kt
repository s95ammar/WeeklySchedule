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
import com.s95ammar.weeklyschedule.util.*
import com.s95ammar.weeklyschedule.viewModels.ScheduleViewerViewModel
import com.s95ammar.weeklyschedule.views.adapters.CategorySpinnerAdapter
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_event_editor.*
import org.joda.time.LocalTime
import org.joda.time.format.DateTimeFormat
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
	private lateinit var selectedDaysIndices: IntArray
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
			viewModel.getScheduleById(scheduleId).fetchAndIfExists { schedule ->
				this.schedule = schedule
				selectedDaysIndices = emptyArray<Int>().toIntArray()
			}

	private fun setUpLayout() {
		setUpCategorySpinner()
		setUpDaysCardView()
		setUpTimeCardViews()

		if (mode == Mode.EDIT) {
			setCategorySpinnerSelection()
			textView_event_name.text = event.name
			textView_event_days.setText(R.string.day)
			spinner_event_days.setSelection(schedule.days.array.indexOf(event.day))
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
		viewModel.getCategoryById(event.categoryId).fetchAndIfExists { eventCategory ->
			viewModel.getAllCategories().fetchAndIfExists { allCategories ->
				spinner_event_categories.setSelection(allCategories.indexOf(eventCategory))
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
					viewModel.showDaysMultiChoiceDialog(schedule.days, selectedDaysIndices)
					observeDaysSelection()
				}
			}
		}
	}

	private fun setUpTimeCardViews() {
		textView_event_start_value.text = DEFAULT_TIME.toString(timePattern)
		textView_event_end_value.text = DEFAULT_TIME.toString(timePattern)
		cardView_event_start.setOnClickListener { onTimeClicked(
				TimeDetails(LocalTime.parse(textView_event_start_value.text.toString(), DateTimeFormat.forPattern(timePattern)), TimeTarget.START_TIME)
		)}
		cardView_event_end.setOnClickListener { onTimeClicked(
				TimeDetails(LocalTime.parse(textView_event_end_value.text.toString(), DateTimeFormat.forPattern(timePattern)), TimeTarget.END_TIME)
		)}
	}

	private fun observeDaysSelection() {
		viewModel.onDaysSelected.observe(viewLifecycleOwner, Observer {
			it?.let { selectedDaysIndices ->
				this.selectedDaysIndices = selectedDaysIndices
				val selectedDays: List<String> = schedule.days.array.filterIndexed { i, _ ->
					selectedDaysIndices.contains(i)
				}
				textView_event_days_value.text = viewModel.getDaysAbbreviationsString(selectedDays)
				viewModel.onDaysSelected.removeObservers(viewLifecycleOwner)
			}
		})
	}

	private fun onTimeClicked(timeDetails: TimeDetails) {
		viewModel.showEventTimePicker(timeDetails)
		observeTimeSet()
	}


	private fun observeTimeSet() {
		viewModel.onEventTimeSet.observe(viewLifecycleOwner, Observer { timeDetails ->
			timeDetails?.let {
				when (it.target) {
					TimeTarget.START_TIME -> textView_event_start_value.text = it.time.toString(timePattern)
					TimeTarget.END_TIME -> textView_event_end_value.text = it.time.toString(timePattern)
				}
			}
		})
	}


}
