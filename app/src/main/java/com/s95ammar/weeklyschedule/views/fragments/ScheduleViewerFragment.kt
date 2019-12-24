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
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.s95ammar.weeklyschedule.R
import com.s95ammar.weeklyschedule.models.data.Event
import com.s95ammar.weeklyschedule.models.data.Schedule
import com.s95ammar.weeklyschedule.util.*
import com.s95ammar.weeklyschedule.viewModels.ScheduleViewerViewModel
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_schedule_viewer.*
import javax.inject.Inject

/* GET READY TO SEE SOME FREAKY SHIT XD */

class ScheduleViewerFragment : DaggerFragment() {
	private val t = "log_${javaClass.simpleName}"

	@Inject lateinit var factory: ViewModelProvider.Factory
	private lateinit var viewModel: ScheduleViewerViewModel
	private lateinit var schedule: Schedule
	private lateinit var events: List<Event>

	private val scheduleDays
		get() = when (schedule.daysAmount) {
			DaysAmount.OneWeek -> DAYS_OF_ONE_WEEK
			DaysAmount.TwoWeeks -> DAYS_OF_TWO_WEEKS
		}

	private val daysAmount
		get() = schedule.daysAmount.value

	companion object {
		private const val TEXT_VIEW_HEADER_HEIGHT = 100
		private const val TEXT_SIZE = 20f
		private const val TEXT_VIEWS_WIDTH = 300
		private const val PADDING = 8
	}

	private val timePattern by lazy { requireActivity().application.SYSTEM_TIME_PATTERN }
	private val textViewsDays = ArrayList<TextView>()
	private val textViewsHours = ArrayList<TextView>()
	private lateinit var mapEventsTextViews: HashMap<Event, TextView>

	private enum class Direction { Horizontal, Vertical }

	init {
		Log.d(t, "init: $this")
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
	                          savedInstanceState: Bundle?): View? {
		return inflater.inflate(R.layout.fragment_schedule_viewer, container, false)
	}

	override fun onActivityCreated(savedInstanceState: Bundle?) {
		super.onActivityCreated(savedInstanceState)
		viewModel = ViewModelProviders.of(requireActivity(), factory).get(ScheduleViewerViewModel::class.java)
		button_add_event.setOnClickListener { viewModel.showEventEditorFragment() }
		arguments?.getInt(resources.getString(R.string.key_schedule_id))?.let { id ->
			viewModel.setMode(when (id) {
				0 -> ScheduleMode.NOT_DISPLAYED
				else -> ScheduleMode.VIEW
			})
			viewModel.mode.observe(viewLifecycleOwner, Observer {
				it?.let { mode ->
					when (mode) {
						ScheduleMode.NOT_DISPLAYED -> text_no_active_schedule.visibility = VISIBLE
						ScheduleMode.VIEW -> {
							button_add_event.visibility = GONE
						}
						ScheduleMode.EDIT -> {
							button_add_event.visibility = VISIBLE
						}
					}
				}
			})
			viewModel.mode.observeOnce(Observer { if (it != ScheduleMode.NOT_DISPLAYED) showSchedule(id) })
		}

	}

	private fun showSchedule(scheduleId: Int) {
		viewModel.getScheduleById(scheduleId).observeOnce(Observer {
			it?.let {
				Log.d(t, "showSchedule: $it")
				schedule = it
				viewModel.setActionBarTitle(it.name)
				viewModel.getEventsOfSchedule(schedule.id).observe(viewLifecycleOwner, Observer { scheduleEvents ->
					events = scheduleEvents

					prepareHeaderTextViews(textViewsHours, HOURS_IN_DAY, getHoursStringArray(timePattern))
					prepareHeaderTextViews(textViewsDays, daysAmount, scheduleDays)
					prepareEventTextViews()
					connectTextViews()
				})
			}
		})
	}

	private fun setEventsTextViewsOnClickListeners() {
		for (dayNum in 0 until daysAmount)
			for (event in getEventsOfDay(dayNum))
				mapEventsTextViews[event]?.let { eventTextView ->
					when (viewModel.mode.value) {
						ScheduleMode.EDIT -> eventTextView.setOnClickListener {
							viewModel.setEditedEvent(event)
							viewModel.showEventEditorFragment()
						}
						else -> eventTextView.setOnClickListener {}
					}
				}
	}

	private fun getEventsOfDay(dayNum: Int) = events.filter { schedule.getDayOfSchedule(dayNum) == it.day }

	private fun prepareHeaderTextViews(textViews: ArrayList<TextView>, length: Int, stringArray: Array<String>) {
		for (i in 0 until length) {
			val tv = getTableTextView()
			formatHeaderTextView(tv, stringArray[i])
			textViews.add(tv)
			layout_schedule_viewer.addView(tv)
		}
	}

	private fun prepareEventTextViews() {
		mapEventsTextViews = HashMap()
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
		viewModel.getCategoryById(event.categoryId).observeOnce(Observer {
			tv.apply {
				text = event.name
				setTextColor(it.textColor)
				background.mutate().setTint(it.fillColor)
				layoutParams = ConstraintLayout.LayoutParams(TEXT_VIEWS_WIDTH, 0)
			}
		})
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
				0 -> constraintSet.connect(headerTextViews[i].id, headerStartSide, R.id.layout_schedule_viewer, layoutEndSide, marginToLayout)
				else -> constraintSet.connect(headerTextViews[i].id, headerStartSide, headerTextViews[i - 1].id, headerEndSide, marginInBetween)
			}
	}

	private fun connectEventTextViews(constraintSet: ConstraintSet) {
		for (dayNum in 0 until daysAmount)
			for (event in getEventsOfDay(dayNum))
				mapEventsTextViews[event]?.id?.let { eventTextViewId ->
					val ec = EventConstraints(event)
					constraintSet.connect(eventTextViewId, ConstraintSet.TOP, ec.targetStartHourId, ConstraintSet.TOP, ec.marginTop)
					constraintSet.connect(eventTextViewId, ConstraintSet.BOTTOM, ec.targetEndHourId, ConstraintSet.BOTTOM, ec.marginBottom)
					constraintSet.connect(eventTextViewId, ConstraintSet.LEFT, ec.targetDayId, ConstraintSet.LEFT)
					constraintSet.connect(eventTextViewId, ConstraintSet.RIGHT, ec.targetDayId, ConstraintSet.RIGHT)
					constraintSet.constrainDefaultHeight(eventTextViewId, ConstraintSet.MATCH_CONSTRAINT_SPREAD)
				}
	}

	private inner class EventConstraints(event: Event) {
		private val minuteHeight = TEXT_VIEW_HEADER_HEIGHT.toDouble() / MINUTES_IN_HOUR

		val targetDayId: Int = textViewsDays[scheduleDays.indexOf(event.day)].id
		val targetStartHourId: Int = textViewsHours[event.startHour].id
		val targetEndHourId: Int = textViewsHours[event.endHour].id

		val marginTop: Int = (minuteHeight * event.startMinute).toInt()
		val marginBottom: Int = (minuteHeight * (MINUTES_IN_HOUR - event.endMinute)).toInt()
	}

}
