package com.s95ammar.weeklyschedule.views.fragments


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.view.menu.MenuBuilder
import androidx.appcompat.view.menu.MenuPopupHelper
import androidx.appcompat.widget.PopupMenu
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.s95ammar.weeklyschedule.R
import com.s95ammar.weeklyschedule.models.data.Schedule
import com.s95ammar.weeklyschedule.util.toast
import com.s95ammar.weeklyschedule.viewModels.SchedulesListViewModel
import com.s95ammar.weeklyschedule.views.recViewAdapters.SchedulesListAdapter
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_schedules_list.*
import javax.inject.Inject

class SchedulesListFragment : DaggerFragment(), SchedulesListAdapter.OnItemClickListener {
	private val t = "log_${javaClass.simpleName}"

	@Inject
	lateinit var factory: ViewModelProvider.Factory
	private lateinit var viewModel: SchedulesListViewModel
	@Inject
	lateinit var listAdapter: SchedulesListAdapter

	init {
		Log.d(t, "init: $this")
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
	                          savedInstanceState: Bundle?): View? {
		return inflater.inflate(R.layout.fragment_schedules_list, container, false)
	}

	override fun onActivityCreated(savedInstanceState: Bundle?) {
		super.onActivityCreated(savedInstanceState)
		activity?.let { viewModel = ViewModelProviders.of(it, factory).get(SchedulesListViewModel::class.java) }
		buildRecyclerView()
		startObservers()
		button_add_schedule.setOnClickListener { viewModel.showScheduleRefactorDialog() }
	}

	private fun startObservers() {
		viewModel.getAllSchedules().observe(viewLifecycleOwner, Observer {
			Log.d(t, "startObservers: allSchedules onChanged $it")
			onSchedulesChanged(it)
		})
	}

	private fun buildRecyclerView() {
		recyclerView_schedules.setHasFixedSize(true)
		recyclerView_schedules.layoutManager = LinearLayoutManager(activity)
		recyclerView_schedules.adapter = listAdapter
		listAdapter.onItemClickListener = this
	}

	private fun onSchedulesChanged(list: List<Schedule>) {
		listAdapter.submitList(list)
		text_no_schedules.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
	}

	override fun onItemClicked(i: Int) {
		listAdapter.getScheduleAt(i).let {
			// TODO: open schedule
		}
	}

	// TODO: break down
	override fun onMoreClicked(i: Int, buttonMore: Button) {
		activity?.let { activity ->
			val popupMenu = PopupMenu(activity, buttonMore).apply {
				inflate(R.menu.schedules_more_menu)
				setOnMenuItemClickListener { menuItem ->
					when (menuItem.itemId) {
						R.id.schedules_more_rename -> listAdapter.getScheduleAt(i).also {
							viewModel.setEditedSchedule(it)
							viewModel.showScheduleRefactorDialog(it)
						}
						R.id.schedules_more_delete -> listAdapter.getScheduleAt(i).also {
							if (!it.isActive) viewModel.delete(it) else toast(activity, R.string.active_schedule_delete_error, Toast.LENGTH_LONG)
						}
					}
					true
				}
			}
			MenuPopupHelper(activity, popupMenu.menu as MenuBuilder, buttonMore).apply {
				setForceShowIcon(true)
			}.show()
		}
	}

	override fun onSwitchChecked(i: Int, isChecked: Boolean) {
		val modifiedSchedule = listAdapter.getScheduleAt(i)

		when {
			!isChecked -> viewModel.deactivateSchedule(modifiedSchedule)
			isChecked -> when {
				Schedule.activeExists() -> viewModel.replaceActiveSchedule(modifiedSchedule)
				Schedule.activeDoesntExist() -> viewModel.activateSchedule(modifiedSchedule)
			}
		}
	}


}
