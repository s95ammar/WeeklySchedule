package com.s95ammar.weeklyschedule.views.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.s95ammar.weeklyschedule.R
import com.s95ammar.weeklyschedule.di.TimePattern
import com.s95ammar.weeklyschedule.models.data.Category
import com.s95ammar.weeklyschedule.models.data.Event
import com.s95ammar.weeklyschedule.models.data.Schedule
import com.s95ammar.weeklyschedule.util.*
import com.s95ammar.weeklyschedule.viewModels.ScheduleViewerViewModel
import com.s95ammar.weeklyschedule.views.adapters.CategorySpinnerAdapter
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_event_editor.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.joda.time.LocalTime
import org.joda.time.format.DateTimeFormat
import java.lang.NullPointerException
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
		get() = viewModel.eventEditorMode.value ?: throw NullPointerException()
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
		viewModel = ViewModelProvider(requireActivity(), factory).get(ScheduleViewerViewModel::class.java)
		setMode()
		setValues()
		// TODO: FIX
		launchIO {
			while (!::event.isInitialized && !::schedule.isInitialized) Thread.sleep(50)
			withContext(Dispatchers.Main) {
				setUpLayout()
				setListeners()
			}
		}
	}

	private fun setListeners() {
		button_event_ok.setOnClickListener(onOkListener())
		button_event_cancel.setOnClickListener { requireActivity().onBackPressed() }
		button_event_delete.setOnClickListener { viewModel.delete(event); requireActivity().onBackPressed() }
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
			Mode.EDIT -> viewModel.getEventById(argEventId).safeFetch { event ->
				this.event = event
				setSchedule(event.scheduleId)
			}
		}
	}

	private fun setSchedule(scheduleId: Int) = viewModel.getScheduleById(scheduleId).safeFetch { schedule ->
		this.schedule = schedule
		selectedDaysIndices = emptyArray<Int>().toIntArray()
	}

	private fun setUpLayout() {
		setUpCategorySpinner()
		setUpDaysCardView()
		setUpTimeCardViews()
		if (mode == Mode.EDIT) {
			setCategorySpinnerSelection()
			editText_event_name.setText(event.name)
			textView_event_days.setText(R.string.day)
			spinner_event_days.setSelection(schedule.days.array.indexOf(event.day))
			textView_event_start_value.text = event.startTime.toString(timePattern)
			textView_event_end_value.text = event.endTime.toString(timePattern)
			button_event_delete.visibility = View.VISIBLE
			button_event_delete.setOnClickListener { viewModel.deleteEventById(argEventId) }
		}
	}

	private fun setUpCategorySpinner() {
		viewModel.getAllCategories().safeFetch { allCategories ->
			spinner_event_categories.adapter = CategorySpinnerAdapter(requireContext(), allCategories)
			cardView_event_category.setOnClickListener { spinner_event_categories.performClick() }
		}
	}

	private fun setCategorySpinnerSelection() {
		viewModel.getCategoryById(event.categoryId).safeFetch { eventCategory ->
			viewModel.getAllCategories().safeFetch { allCategories ->
				spinner_event_categories.setSelection(allCategories.indexOf(eventCategory))
			}
		}
	}

	private fun setUpDaysCardView() {
		when (mode) {
			Mode.EDIT -> {
				spinner_event_days.visibility = View.VISIBLE
				spinner_event_days.adapter = ArrayAdapter(requireContext(), R.layout.spinner_row_day, schedule.days.array)
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

	private fun setUpTimeCardViews() {
		textView_event_start_value.text = DEFAULT_TIME.toString(timePattern)
		textView_event_end_value.text = DEFAULT_TIME.toString(timePattern)
		cardView_event_start.setOnClickListener {
			onTimeClicked(TimeDetails(getEnteredStartTime(), TimeTarget.START_TIME))
		}
		cardView_event_end.setOnClickListener {
			onTimeClicked(TimeDetails(getEnteredEndTime(), TimeTarget.END_TIME))
		}
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

	private fun onOkListener() = View.OnClickListener {
		when (val validationResult = viewModel.validateInput(toValidateBundle())) {
			is Result.Error -> toast(validationResult.message)
			is Result.Success -> {
				when (mode) {
					Mode.ADD -> tryInsert()
					Mode.EDIT -> tryUpdate()
				}
				viewModel.onEventOperationAttempt.observe(viewLifecycleOwner, object : Observer<Result> {
					override fun onChanged(it: Result) {
						when (it) {
							is Result.Success -> requireActivity().onBackPressed()
							is Result.Error -> {toast(it.message)}
						}
						viewModel.onEventOperationAttempt.removeObserver(this)
					}
				})
			}
		}
	}

	private fun toValidateBundle() = bundleOf(
			KEY_MODE to mode,
			KEY_DAYS_INDICES to selectedDaysIndices,
			KEY_START_TIME to getEnteredStartTime(),
			KEY_END_TIME to getEnteredEndTime()
	)

	private fun getEnteredStartTime() = LocalTime.parse(textView_event_start_value.text.toString(), DateTimeFormat.forPattern(timePattern))
	private fun getEnteredEndTime() = LocalTime.parse(textView_event_end_value.text.toString(), DateTimeFormat.forPattern(timePattern))

	private fun tryInsert() {
		val eventsToInsert = Array(selectedDaysIndices.size) { i -> getEventFromInput(schedule.days.array[selectedDaysIndices[i]]) }
		viewModel.tryInsert(eventsToInsert, schedule.id)
	}

	private fun tryUpdate() {
		val eventToUpdate = getEventFromInput(schedule.days.array[spinner_event_days.selectedItemPosition])
		viewModel.tryUpdate(eventToUpdate)
	}

	private fun getEventFromInput(day: String) = Event(
			editText_event_name.input,
			day,
			getEnteredStartTime(),
			getEnteredEndTime(),
			(spinner_event_categories.selectedItem as Category).id,
			schedule.id
	).apply { if (mode == Mode.EDIT) id = event.id }


}
