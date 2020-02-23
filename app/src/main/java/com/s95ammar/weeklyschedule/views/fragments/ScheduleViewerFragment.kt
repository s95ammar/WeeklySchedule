package com.s95ammar.weeklyschedule.views.fragments

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.*
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.widget.TextViewCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.s95ammar.weeklyschedule.R
import com.s95ammar.weeklyschedule.di.TimePattern
import com.s95ammar.weeklyschedule.models.data.Event
import com.s95ammar.weeklyschedule.models.data.Schedule
import com.s95ammar.weeklyschedule.util.*
import com.s95ammar.weeklyschedule.viewModels.ScheduleViewerViewModel
import com.s95ammar.weeklyschedule.viewModels.SharedDataViewModel
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_schedule_viewer.*
import javax.inject.Inject

class ScheduleViewerFragment : DaggerFragment() {

	@Inject lateinit var factory: ViewModelProvider.Factory
	private lateinit var viewModel: ScheduleViewerViewModel
	private lateinit var sharedDataViewModel: SharedDataViewModel
	private lateinit var scheduleToolbarMenu: Menu

	private var schedule: Schedule
		get() = viewModel.schedule
		set(value) { viewModel.schedule = value }
	private var events: List<Event>
		get() = viewModel.events
		set(value) { viewModel.events = value }

	private val argScheduleId
		get() = arguments?.getInt(resources.getString(R.string.key_schedule_id)) ?: 0

	companion object {
		private const val TEXT_VIEW_HEADER_HEIGHT = 100
		private const val MIN_TEXT_SIZE = 4
		private const val TEXT_SIZE = 20
		private const val TEXT_VIEWS_WIDTH = 300
	}

	@field: [Inject TimePattern] lateinit var timePattern: String
	private val textViewsDays = ArrayList<TextView>()
	private val textViewsHours = ArrayList<TextView>()
	private var mapEventsTextViews = HashMap<Event, TextView>()

	private enum class Direction { Horizontal, Vertical }

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
	                          savedInstanceState: Bundle?): View? {
		setHasOptionsMenu(true)
		return inflater.inflate(R.layout.fragment_schedule_viewer, container, false)
	}

	override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
		super.onCreateOptionsMenu(menu, inflater)
		inflater.inflate(R.menu.toolbar_menu, menu)
		scheduleToolbarMenu = menu
		viewModel.scheduleMode.observe(viewLifecycleOwner, getModeObserver())
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		when (item.itemId) {
			R.id.button_edit -> viewModel.setMode(ScheduleMode.EDIT)
			R.id.button_done -> viewModel.setMode(ScheduleMode.VIEW)
		}
		return false
	}

	override fun onActivityCreated(savedInstanceState: Bundle?) {
		super.onActivityCreated(savedInstanceState)
		viewModel = ViewModelProvider(this, factory).get(ScheduleViewerViewModel::class.java)
		sharedDataViewModel = ViewModelProvider(requireActivity()).get(SharedDataViewModel::class.java)
		viewModel.getAllCategories().safeFetch { sharedDataViewModel.allCategories = it }
		setMode()
		if (viewModel.scheduleMode.value != ScheduleMode.MISSING) {
			displaySchedule()
			sharedDataViewModel.actionBarTitle.observe(viewLifecycleOwner, Observer { setActionBarTitle(it) })
		}
	}

	private fun setMode() {
		viewModel.scheduleMode.value ?: viewModel.setMode(when (argScheduleId) {
			0 -> ScheduleMode.MISSING
			else -> ScheduleMode.VIEW
		})
	}

	private fun getModeObserver(): Observer<ScheduleMode> = Observer { mode ->
		setScheduleToolbarMenuMode(mode)
		when (mode) {
			ScheduleMode.MISSING -> textView_no_active_schedule.visibility = VISIBLE
			ScheduleMode.VIEW -> button_add_event.visibility = GONE
			ScheduleMode.EDIT -> {
				button_add_event.visibility = VISIBLE
				button_add_event.setOnClickListener {
					if (sharedDataViewModel.allCategories.isNotEmpty()) navigateToEventEditorFragment()
					else toast(R.string.category_list_empty_error, Toast.LENGTH_LONG)
				}
			}
		}
	}

	private fun navigateToEventEditorFragment(event: Event? = null) {
		sharedDataViewModel.editedSchedule = schedule
		event?.let { sharedDataViewModel.editedEvent = event }
		sharedDataViewModel.eventEditorFragmentMode = event?.let { Mode.EDIT } ?: Mode.ADD
		findNavController().navigate(R.id.action_nav_schedule_viewer_to_eventEditorFragment)
	}

	private fun setScheduleToolbarMenuMode(mode: ScheduleMode) {
		scheduleToolbarMenu.findItem(R.id.button_edit).isVisible = when (mode) {
			ScheduleMode.VIEW -> true
			else -> false
		}
		scheduleToolbarMenu.findItem(R.id.button_done).isVisible = when (mode) {
			ScheduleMode.EDIT -> true
			else -> false
		}
	}

	private fun displaySchedule() {
		viewModel.getScheduleById(argScheduleId).safeFetch {
			Log.d(LOG_TAG, "showSchedule: $it")
			schedule = it
			sharedDataViewModel.setActionBarTitle(schedule.name)
			prepareHeaderTextViews(textViewsHours, HOURS_IN_DAY, getHoursStringArray(timePattern))
			prepareHeaderTextViews(textViewsDays, schedule.days.amount, schedule.days.array)
			viewModel.getEventsBy(schedule.id).observe(viewLifecycleOwner, Observer { scheduleEvents ->
				Log.d(LOG_TAG, "showSchedule: $scheduleEvents")
				events = scheduleEvents
				prepareEventTextViews()
				connectTextViews()
			})
		}
	}

	private fun getEventsOfDay(day: String) = events.filter { day == it.day }

	private fun prepareHeaderTextViews(textViews: ArrayList<TextView>, length: Int, values: Array<String>) {
		textViews.clear()
		for (i in 0 until length) {
			val tv = getTableTextView()
			formatHeaderTextView(tv, values[i])
			textViews.add(tv)
			layout_schedule_viewer.addView(tv)
		}
	}

	private fun prepareEventTextViews() {
		mapEventsTextViews.clear()
		for (i in 0 until schedule.days.amount) {
			val day = schedule.getDayOfSchedule(i)
			val dayEvents = events.filter { day == it.day }
			for (event in dayEvents) {
				val tv = getTableTextView()
				formatEventTextView(tv, event)
				mapEventsTextViews[event] = tv
				layout_schedule_viewer.addView(tv)
			}
		}
		setEventsTextViewsOnClickListeners()
	}

	private fun setEventsTextViewsOnClickListeners() {
		schedule.days.array.forEach { day ->
			viewModel.getEventsBy(schedule.id, day).safeFetch {
				it.forEach { event ->
					mapEventsTextViews[event]?.let { eventTextView ->
						eventTextView.setOnClickListener {
							when (viewModel.scheduleMode.value) {
								ScheduleMode.EDIT -> { navigateToEventEditorFragment(event) }
								else -> {}
							}
						}
					}
				}
			}
		}
	}

	private fun getTableTextView() = TextView(activity).apply {
		id = View.generateViewId()
		minWidth = TEXT_VIEWS_WIDTH
		TextViewCompat.setAutoSizeTextTypeUniformWithConfiguration(this, MIN_TEXT_SIZE, TEXT_SIZE, 1, TypedValue.COMPLEX_UNIT_SP)
		background = requireActivity().getDrawable(R.drawable.shape_rounded_rectangle)?.mutate()
		gravity = Gravity.CENTER
	}

	private fun formatHeaderTextView(tv: TextView, text: String) {
		tv.apply {
			minHeight = TEXT_VIEW_HEADER_HEIGHT
			this.text = text
			setTextColor(COLOR_BLACK)
			background.mutate().setTint(COLOR_GRAY)
		}
	}

	private fun formatEventTextView(tv: TextView, event: Event) {
		tv.apply {
			text = event.name
			viewModel.getCategoryById(event.categoryId).safeFetch { category ->
				setTextColor(category.textColor)
				background.mutate().setTint(category.fillColor)
			}
			layoutParams = ConstraintLayout.LayoutParams(TEXT_VIEWS_WIDTH, 0)
		}
	}

	private fun connectTextViews() {
		val constraintSet = ConstraintSet()
		constraintSet.clone(layout_schedule_viewer)
		connectHeaderTextViews(textViewsHours, constraintSet, Direction.Vertical)
		connectHeaderTextViews(textViewsDays, constraintSet, Direction.Horizontal)
		connectEventTextViews(constraintSet)
		constraintSet.applyTo(layout_schedule_viewer)
	}

	private fun connectHeaderTextViews(headerTextViews: ArrayList<TextView>, constraintSet: ConstraintSet, direction: Direction) {
		val headerStartSide: Int
		val headerEndSide: Int
		val layoutEndSide: Int
		val marginToLayout: Int
		val marginInBetween = 2

		when (direction) {
			Direction.Horizontal -> {
				headerStartSide = ConstraintSet.LEFT
				headerEndSide = ConstraintSet.RIGHT
				layoutEndSide = ConstraintSet.LEFT
				marginToLayout = 300
			}
			Direction.Vertical -> {
				headerStartSide = ConstraintSet.TOP
				headerEndSide = ConstraintSet.BOTTOM
				layoutEndSide = ConstraintSet.TOP
				marginToLayout = 100
			}
		}

		for (i in headerTextViews.indices)
			when (i) {
				0 -> constraintSet.connect(headerTextViews[i].id, headerStartSide, layout_schedule_viewer.id, layoutEndSide, marginToLayout)
				else -> constraintSet.connect(headerTextViews[i].id, headerStartSide, headerTextViews[i - 1].id, headerEndSide, marginInBetween)
			}
	}

	private fun connectEventTextViews(constraintSet: ConstraintSet) {
		for (day in schedule.days.array)
			for (event in getEventsOfDay(day))
				mapEventsTextViews[event]?.id?.let { eventTextViewId ->
					val ec = EventConstraints(event)
					constraintSet.apply {
						connect(eventTextViewId, ConstraintSet.TOP, ec.targetStartHourId, ConstraintSet.TOP, ec.marginTop)
						connect(eventTextViewId, ConstraintSet.BOTTOM, ec.targetEndHourId, ConstraintSet.BOTTOM, ec.marginBottom)
						connect(eventTextViewId, ConstraintSet.LEFT, ec.targetDayId, ConstraintSet.LEFT)
						connect(eventTextViewId, ConstraintSet.RIGHT, ec.targetDayId, ConstraintSet.RIGHT)
						constrainDefaultHeight(eventTextViewId, ConstraintSet.MATCH_CONSTRAINT_SPREAD)
					}
				}
	}

	private inner class EventConstraints(event: Event) {
		private val heightPerMinute = TEXT_VIEW_HEADER_HEIGHT.toDouble() / MINUTES_IN_HOUR

		val targetDayId: Int = textViewsDays[schedule.days.array.indexOf(event.day)].id
		val targetStartHourId: Int = textViewsHours[event.startHour].id
		val targetEndHourId: Int = textViewsHours[event.endHour].id

		val marginTop: Int = (heightPerMinute * event.startMinute).toInt()
		val marginBottom: Int = (heightPerMinute * (MINUTES_IN_HOUR - event.endMinute)).toInt()
	}

}
