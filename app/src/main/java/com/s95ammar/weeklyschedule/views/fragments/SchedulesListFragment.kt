package com.s95ammar.weeklyschedule.views.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.s95ammar.weeklyschedule.R
import com.s95ammar.weeklyschedule.models.data.Schedule
import com.s95ammar.weeklyschedule.util.toast
import com.s95ammar.weeklyschedule.viewModels.SchedulesListViewModel
import com.s95ammar.weeklyschedule.views.adapters.SchedulesListAdapter
import kotlinx.android.synthetic.main.fragment_schedules_list.*
import javax.inject.Inject

class SchedulesListFragment : AbstractDaggerListFragment<Schedule, SchedulesListViewModel, SchedulesListAdapter>(),
		SchedulesListAdapter.OnScheduleClickListener {

	@Inject lateinit var factory: ViewModelProvider.Factory
	@Inject lateinit var listAdapter: SchedulesListAdapter

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		return inflater.inflate(R.layout.fragment_schedules_list, container, false)
	}

	override fun setListeners() = button_add_schedule.setOnClickListener { viewModel.showScheduleEditorDialog() }

	override fun initViewModel() = ViewModelProviders.of(requireActivity(), factory).get(SchedulesListViewModel::class.java)

	override fun assignItemsList() = viewModel.getAllSchedules()

	override fun assignRecyclerView(): RecyclerView = recyclerView_schedules

	override fun assignListAdapter() = listAdapter.apply { onScheduleClickListener = this@SchedulesListFragment }

	override fun onListChanged(itemsList: List<Schedule>) {
		listAdapter.submitList(itemsList)
		textView_no_schedules.visibility = if (itemsList.isEmpty()) View.VISIBLE else View.GONE
	}

	override fun onItemClicked(item: Schedule) {
		item.let { viewModel.navigateToScheduleViewer(it.id) }
	}

	override fun onMoreClicked(item: Schedule, buttonMore: Button) = showPopupMenu(R.menu.schedules_more_menu, buttonMore,
			PopupMenu.OnMenuItemClickListener { menuItem -> onMenuItemClick(item, menuItem) })

	override fun onSwitchChecked(schedule: Schedule, isChecked: Boolean) = viewModel.handleSwitchChange(schedule, isChecked)

	private fun onMenuItemClick(schedule: Schedule, menuItem: MenuItem): Boolean {
		when (menuItem.itemId) {
			R.id.schedules_more_rename -> schedule.let {
				viewModel.showScheduleEditorDialog(it.id)
			}
			R.id.schedules_more_delete -> schedule.let {
				if (!it.isActive) viewModel.delete(it) else toast(R.string.active_schedule_delete_error, Toast.LENGTH_LONG)
			}
		}
		return true
	}


}
