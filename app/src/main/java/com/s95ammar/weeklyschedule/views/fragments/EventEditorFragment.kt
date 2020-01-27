package com.s95ammar.weeklyschedule.views.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.s95ammar.weeklyschedule.R
import com.s95ammar.weeklyschedule.viewModels.ScheduleViewerViewModel
import dagger.android.support.DaggerFragment
import javax.inject.Inject


class EventEditorFragment : DaggerFragment() {

	@Inject lateinit var factory: ViewModelProvider.Factory
	private lateinit var viewModel: ScheduleViewerViewModel

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
	                          savedInstanceState: Bundle?): View? {
		return inflater.inflate(R.layout.fragment_event_editor, container, false)
	}

	override fun onActivityCreated(savedInstanceState: Bundle?) {
		super.onActivityCreated(savedInstanceState)
		viewModel = ViewModelProviders.of(requireActivity(), factory).get(ScheduleViewerViewModel::class.java)
		startListeners()
	}

	private fun startListeners() {
		viewModel
	}


}
