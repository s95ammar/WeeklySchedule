package com.s95ammar.weeklyschedule.views.fragments

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.s95ammar.weeklyschedule.R
import com.s95ammar.weeklyschedule.di.TimePattern
import com.s95ammar.weeklyschedule.models.data.Event
import com.s95ammar.weeklyschedule.models.data.Schedule
import com.s95ammar.weeklyschedule.util.*
import com.s95ammar.weeklyschedule.viewModels.ScheduleViewerViewModel
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_schedule_viewer.*
import javax.inject.Inject

/* GET READY TO SEE SOME FREAKY SHIT XD */

class ScheduleViewerFragment : DaggerFragment() {

	@Inject
	lateinit var factory: ViewModelProvider.Factory
	private lateinit var viewModel: ScheduleViewerViewModel
	private lateinit var schedule: Schedule
	private lateinit var events: List<Event>

	private val daysAmount
		get() = schedule.days.amount

	private val argScheduleId
		get() = arguments?.getInt(resources.getString(R.string.key_schedule_id)) ?: 0

	companion object {
		private const val TEXT_VIEW_HEADER_HEIGHT = 100
		private const val TEXT_SIZE = 20f
		private const val TEXT_VIEWS_WIDTH = 300
		private const val PADDING = 8
	}

	@field: [Inject TimePattern]
	lateinit var timePattern: String
	private val textViewsDays = ArrayList<TextView>()
	private val textViewsHours = ArrayList<TextView>()
	private var mapEventsTextViews = HashMap<Event, TextView>()

	private enum class Direction { Horizontal, Vertical }

	init {
		Log.d(LOG_TAG, "init: $this")
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
	                          savedInstanceState: Bundle?): View? {
		return inflater.inflate(R.layout.fragment_schedule_viewer, container, false)
	}

	override fun onActivityCreated(savedInstanceState: Bundle?) {
		super.onActivityCreated(savedInstanceState)
		viewModel = ViewModelProvider(requireActivity(), factory).get(ScheduleViewerViewModel::class.java)
		setMode()
		startObservers()
		if (viewModel.scheduleMode.value != ScheduleMode.NOT_DISPLAYED) showSchedule()
	}

	private fun setMode() {
		viewModel.setScheduleViewerMode(when (argScheduleId) {
			0 -> ScheduleMode.NOT_DISPLAYED
			else -> ScheduleMode.VIEW
		})
	}

	private fun startObservers() {
		viewModel.scheduleMode.observe(viewLifecycleOwner, getModeObserver())
	}

	private fun getModeObserver(): Observer<ScheduleMode> = Observer { mode ->
		when (mode) {
			ScheduleMode.NOT_DISPLAYED -> textView_no_active_schedule.visibility = VISIBLE
			ScheduleMode.VIEW -> button_add_event.visibility = GONE
			ScheduleMode.EDIT -> {
				button_add_event.visibility = VISIBLE
				button_add_event.setOnClickListener {
					viewModel.getAllCategories().safeFetch {
						if (it.isNotEmpty()) viewModel.showEventEditorFragment(resources.getString(R.string.key_schedule_id) to argScheduleId)
						else Toast.makeText(requireContext(), R.string.category_list_empty_error, Toast.LENGTH_LONG).show()
					}
				}
			}
		}
	}

	private fun showSchedule() {
		viewModel.getScheduleById(argScheduleId).safeFetch {
			Log.d(LOG_TAG, "showSchedule: $it")
			schedule = it
			viewModel.setActionBarTitle(it.name)
			prepareHeaderTextViews(textViewsHours, HOURS_IN_DAY, getHoursStringArray(timePattern))
			prepareHeaderTextViews(textViewsDays, daysAmount, schedule.days.array)
			viewModel.getEventsBy(schedule.id).observe(viewLifecycleOwner, Observer { scheduleEvents ->
				Log.d(LOG_TAG, "showSchedule: $scheduleEvents")
				events = scheduleEvents
				prepareEventTextViews()
				connectTextViews()
			})
		}
	}

	private fun setEventsTextViewsOnClickListeners() {
		schedule.days.array.forEach { day ->
			viewModel.getEventsBy(schedule.id, day).safeFetch {
				it.forEach { event ->
					mapEventsTextViews[event]?.let { eventTextView ->
						eventTextView.setOnClickListener {
							when (viewModel.scheduleMode.value) {
								ScheduleMode.EDIT -> {
									viewModel.showEventEditorFragment(resources.getString(R.string.key_event_id) to event.id)
								}
								else -> {
								}
							}
						}
					}
				}
			}
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
		for (i in 0 until daysAmount) {
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

	private fun getTableTextView() = TextView(activity).apply {
		id = View.generateViewId()
		minWidth = TEXT_VIEWS_WIDTH
		textSize = TEXT_SIZE
		background = requireActivity().getDrawable(R.drawable.shape_rounded_rectangle)?.mutate()
		setPaddingRelative(PADDING, PADDING, PADDING, PADDING)
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
