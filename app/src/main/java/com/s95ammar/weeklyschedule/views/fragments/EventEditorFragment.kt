package com.s95ammar.weeklyschedule.views.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.s95ammar.weeklyschedule.R
import com.s95ammar.weeklyschedule.di.TimePattern
import com.s95ammar.weeklyschedule.models.data.Event
import com.s95ammar.weeklyschedule.models.data.Schedule
import com.s95ammar.weeklyschedule.util.Mode
import com.s95ammar.weeklyschedule.util.fetchValue
import com.s95ammar.weeklyschedule.viewModels.ScheduleViewerViewModel
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

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
	                          savedInstanceState: Bundle?): View? {
		return inflater.inflate(R.layout.fragment_event_editor, container, false)
	}

	override fun onActivityCreated(savedInstanceState: Bundle?) {
		super.onActivityCreated(savedInstanceState)
		viewModel = ViewModelProviders.of(requireActivity(), factory).get(ScheduleViewerViewModel::class.java)
		startListeners()
		arguments?.getInt(resources.getString(R.string.key_event_id))?.let { eventId ->
			when (eventId) {
				0 -> arguments?.getInt(resources.getString(R.string.key_schedule_id))?.let { scheduleId -> setValuesFromSchedule(scheduleId) }
				else -> setValuesFromEvent(eventId)
			}
			setUpCardViewDays()
		}
	}

	private fun setValuesFromSchedule(scheduleId: Int) {
		viewModel.setEventEditorMode(Mode.ADD)
		viewModel.getScheduleById(scheduleId).fetchValue {
			it?.let {
				schedule = it
//				setUpCardViewDays()
			}
		}
	}

	private fun setValuesFromEvent(eventId: Int) {
		viewModel.setEventEditorMode(Mode.EDIT)
		viewModel.getEventById(eventId).fetchValue {
			it?.let { event -> setUiValues(event) }
//			setUpCardViewDays()
		}
	}

	private fun setUiValues(event: Event) {
		viewModel.getCategoryById(event.categoryId).fetchValue {
			//		TODO: selectCategory
		}
		textView_event_name.text = event.name
		viewModel.getScheduleById(event.scheduleId).fetchValue {
			it?.let { schedule ->
				this.schedule = schedule
				spinner_event_days.setSelection(schedule.days.indexOf(event.day))
			}
		}
		textView_event_start_value.text = event.startTime.toString(timePattern)
		textView_event_end_value.text = event.endTime.toString(timePattern)

	}

	private fun setUpCardViewDays() {
		when (viewModel.eventEditorMode.value) {
			Mode.EDIT -> {
				spinner_event_days.visibility = View.VISIBLE
				cardView_event_day.setOnClickListener { spinner_event_days.performClick() }
			}
			Mode.ADD -> {
				spinner_event_days.visibility = View.GONE
				cardView_event_day.setOnClickListener { viewModel.showDaysMultiChoiceDialog(schedule.days) }
			}

		}
	}

	private fun startListeners() {
//		cardView_event_day.setOnClickListener { viewModel.showDaysMultiChoiceDialog() }
	}


}
