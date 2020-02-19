package com.s95ammar.weeklyschedule.views.fragments


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.s95ammar.weeklyschedule.R
import com.s95ammar.weeklyschedule.models.data.Schedule
import com.s95ammar.weeklyschedule.util.ACTIVE_SCHEDULE_ID_KEY
import com.s95ammar.weeklyschedule.util.LOG_TAG
import com.s95ammar.weeklyschedule.util.SHARED_PREFERENCES
import com.s95ammar.weeklyschedule.util.toast
import com.s95ammar.weeklyschedule.viewModels.SchedulesListViewModel
import com.s95ammar.weeklyschedule.views.adapters.SchedulesListAdapter
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.fragment_schedules_list.*
import javax.inject.Inject

class SchedulesListFragment : AbstractDaggerListFragment<Schedule, SchedulesListViewModel, SchedulesListAdapter>(),
		SchedulesListAdapter.OnScheduleClickListener {

	@Inject lateinit var factory: ViewModelProvider.Factory
	@Inject lateinit var listAdapter: SchedulesListAdapter

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		return inflater.inflate(R.layout.fragment_schedules_list, container, false)
	}

	override fun setListeners() = button_add_schedule.setOnClickListener {
		getActivityNavController().navigate(R.id.action_nav_schedules_to_scheduleEditorDialog)
	}

	override fun initViewModel() = ViewModelProvider(this, factory).get(SchedulesListViewModel::class.java)

	override fun assignItemsList() = viewModel.getAllSchedules()

	override fun assignRecyclerView(): RecyclerView = recyclerView_schedules

	override fun assignListAdapter() = listAdapter.apply { onScheduleClickListener = this@SchedulesListFragment }

	override fun startObservers() {
		super.startObservers()
		viewModel.onActiveScheduleIdChanged.observe(viewLifecycleOwner, Observer {
			Log.d(LOG_TAG, "saveActiveScheduleId: ${Schedule.activeScheduleId}")
			requireActivity()
					.getSharedPreferences(SHARED_PREFERENCES, DaggerAppCompatActivity.MODE_PRIVATE)
					.edit()
					.putInt(ACTIVE_SCHEDULE_ID_KEY, Schedule.activeScheduleId)
					.apply()
		})
	}

	override fun onListChanged(itemsList: List<Schedule>) {
		listAdapter.submitList(itemsList)
		textView_no_schedules.visibility = if (itemsList.isEmpty()) View.VISIBLE else View.GONE
	}

	override fun onItemClicked(item: Schedule) {
		getActivityNavController().navigate(R.id.action_nav_schedules_to_nav_schedule_viewer,
				bundleOf(resources.getString(R.string.key_schedule_id) to item.id))
	}

	override fun onMoreClicked(item: Schedule, buttonMore: Button) = showPopupMenu(R.menu.schedules_more_menu, buttonMore,
			PopupMenu.OnMenuItemClickListener { menuItem -> onMenuItemClick(item, menuItem) })

	override fun onSwitchChecked(schedule: Schedule, isChecked: Boolean) = viewModel.handleSwitchChange(schedule, isChecked)

	private fun onMenuItemClick(schedule: Schedule, menuItem: MenuItem): Boolean {
		when (menuItem.itemId) {
			R.id.schedules_more_rename -> getActivityNavController().navigate(R.id.action_nav_schedules_to_scheduleEditorDialog,
					bundleOf(resources.getString(R.string.key_schedule_id) to schedule.id))
			R.id.schedules_more_delete ->
				if (!schedule.isActive) viewModel.delete(schedule)
				else toast(R.string.active_schedule_delete_error, Toast.LENGTH_LONG)
		}
		return true
	}


}
