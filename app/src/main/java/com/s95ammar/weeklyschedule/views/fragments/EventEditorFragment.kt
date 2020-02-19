package com.s95ammar.weeklyschedule.views.fragments


import android.os.Bundle
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.datetime.timePicker
import com.afollestad.materialdialogs.list.checkAllItems
import com.afollestad.materialdialogs.list.listItemsMultiChoice
import com.afollestad.materialdialogs.list.uncheckAllItems
import com.s95ammar.weeklyschedule.R
import com.s95ammar.weeklyschedule.di.TimePattern
import com.s95ammar.weeklyschedule.models.data.Category
import com.s95ammar.weeklyschedule.models.data.Event
import com.s95ammar.weeklyschedule.models.data.Schedule
import com.s95ammar.weeklyschedule.util.*
import com.s95ammar.weeklyschedule.viewModels.ScheduleViewerViewModel
import com.s95ammar.weeklyschedule.views.adapters.CategorySpinnerAdapter
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.dialog_days_multi_choice_buttons.*
import kotlinx.android.synthetic.main.fragment_event_editor.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.joda.time.LocalTime
import org.joda.time.format.DateTimeFormat
import java.util.*
import javax.inject.Inject


class EventEditorFragment : DaggerFragment() {

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
		viewModel = ViewModelProvider(this, factory).get(ScheduleViewerViewModel::class.java)
		setMode()
		setValues {
			setUpLayout()
			setListeners()
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

	private fun setValues(onComplete: () -> Unit) {
		CoroutineScope(IO).launch {
			when (mode) {
				Mode.ADD -> this@EventEditorFragment.schedule = viewModel.getScheduleById(argScheduleId).suspendFetch()
				Mode.EDIT -> {
					this@EventEditorFragment.event = viewModel.getEventById(argEventId).suspendFetch()
					this@EventEditorFragment.schedule = viewModel.getScheduleById(event.scheduleId).suspendFetch()
				}
			}
			selectedDaysIndices = emptyArray<Int>().toIntArray()
			withContext(Main) { onComplete() }
		}
	}


	private fun setUpLayout() {
		viewModel.getAllCategories().safeFetch { allCategories ->
			setUpCategorySpinner(allCategories)
			setUpDaysCardView()
			setUpTimeCardViews()
			if (mode == Mode.EDIT) {
				setCategorySpinnerSelection(allCategories)
				editText_event_name.setText(event.name)
				textView_event_days.setText(R.string.day)
				spinner_event_days.setSelection(schedule.days.array.indexOf(event.day))
				textView_event_start_value.text = event.startTime.toString(timePattern)
				textView_event_end_value.text = event.endTime.toString(timePattern)
				button_event_delete.visibility = View.VISIBLE
			}
		}
	}

	private fun setUpCategorySpinner(allCategories: List<Category>) {
		spinner_event_categories.adapter = CategorySpinnerAdapter(requireContext(), allCategories)
		cardView_event_category.setOnClickListener { spinner_event_categories.performClick() }

	}

	private fun setCategorySpinnerSelection(allCategories: List<Category>) {
		allCategories.find { it.id == event.categoryId }?.let { eventCategory ->
			spinner_event_categories.setSelection(allCategories.indexOf(eventCategory))
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
				cardView_event_day.setOnClickListener { showDaysMultiChoiceDialog(schedule.days, selectedDaysIndices) }
			}
		}
	}

	private fun showDaysMultiChoiceDialog(days: Days, selectionIndices: IntArray) {
		MaterialDialog(requireContext()).show {
			title(R.string.days)
			customView(R.layout.dialog_days_multi_choice_buttons).apply {
				button_days_select_all.setOnClickListener { checkAllItems() }
				button_days_clear.setOnClickListener { uncheckAllItems() }
			}
			listItemsMultiChoice(
					items = days.array.asList(),
					initialSelection = selectionIndices,
					allowEmptySelection = true
			) { _, _, selection ->
				val newSelectionIndices = IntArray(selection.size) { i -> days.array.indexOf(selection[i]) }
				this@EventEditorFragment.selectedDaysIndices = newSelectionIndices
				displaySelectedDays()
			}
			positiveButton(R.string.select)
			negativeButton(R.string.cancel)
		}
	}

	private fun displaySelectedDays() {
		val selectedDays: List<String> = schedule.days.array.filterIndexed { i, _ ->
			selectedDaysIndices.contains(i)
		}
		textView_event_days_value.text = viewModel.getDaysAbbreviationsString(selectedDays)
	}

	private fun setUpTimeCardViews() {
		textView_event_start_value.text = DEFAULT_TIME.toString(timePattern)
		textView_event_end_value.text = DEFAULT_TIME.toString(timePattern)
		cardView_event_start.setOnClickListener {
			showTimePicker(getEnteredStartTime(), TimeTarget.START_TIME)
		}
		cardView_event_end.setOnClickListener {
			showTimePicker(getEnteredEndTime(), TimeTarget.END_TIME)
		}
	}

	private fun showTimePicker(time: LocalTime, target: TimeTarget) {
		MaterialDialog(requireContext()).show {
			title(when (target) {
				TimeTarget.START_TIME -> R.string.start_time
				TimeTarget.END_TIME -> R.string.end_time
			})
			timePicker(
					currentTime = time.toCalendarInstance(),
					show24HoursView = DateFormat.is24HourFormat(requireContext())
			) {
				_, time: Calendar ->
				displaySelectedTime(LocalTime.fromCalendarFields(time), target)
			}
			positiveButton(R.string.ok)
			negativeButton(R.string.cancel)
		}
	}

	private fun displaySelectedTime(selectedTime: LocalTime, target: TimeTarget) {
		when (target) {
			TimeTarget.START_TIME -> textView_event_start_value.text = selectedTime.toString(timePattern)
			TimeTarget.END_TIME -> textView_event_end_value.text = selectedTime.toString(timePattern)
		}
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
							is Result.Error -> { toast(it.message) }
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
