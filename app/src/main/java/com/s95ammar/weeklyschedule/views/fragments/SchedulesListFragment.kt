package com.s95ammar.weeklyschedule.views.fragments


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.*
import com.s95ammar.weeklyschedule.R
import com.s95ammar.weeklyschedule.models.data.Schedule
import com.s95ammar.weeklyschedule.viewModels.SchedulesListViewModel
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class SchedulesListFragment : DaggerFragment() {
	private val t = "log_${javaClass.simpleName}"

	@Inject
	lateinit var factory: ViewModelProvider.Factory
	private lateinit var viewModel: SchedulesListViewModel

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
		startObservers()
	}

	private fun startObservers() {
		viewModel.getAllSchedules().observe(viewLifecycleOwner, Observer {
			Log.d(t, "startObservers: allSchedules onChanged $it") // TODO: remove
		})
	}

	fun insert(schedule: Schedule) = viewModel.insert(schedule)
	fun update(schedule: Schedule) = viewModel.update(schedule)
	fun delete(schedule: Schedule) = viewModel.delete(schedule)
}
