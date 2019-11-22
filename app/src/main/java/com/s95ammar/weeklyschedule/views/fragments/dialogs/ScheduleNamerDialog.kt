package com.s95ammar.weeklyschedule.views.fragments.dialogs

import android.app.Dialog
import android.content.DialogInterface
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.s95ammar.weeklyschedule.R
import com.s95ammar.weeklyschedule.models.data.Schedule
import com.s95ammar.weeklyschedule.util.*
import com.s95ammar.weeklyschedule.viewModels.SchedulesListViewModel
import dagger.android.support.DaggerDialogFragment
import kotlinx.android.synthetic.main.dialog_refactor_schedule.*
import javax.inject.Inject


class ScheduleNamerDialog : DaggerDialogFragment() {
	private var mode = Mode.ADD
	private lateinit var editedSchedule: Schedule
	@StringRes private val addTitle = R.string.schedule_add_title
	private var scheduleName = ""
		set(value) {
			field = value
			editText_refactor_schedule_name.setText(scheduleName)
		}

	@Inject
	lateinit var factory: ViewModelProvider.Factory
	private lateinit var viewModel: SchedulesListViewModel
	private var dialogView: View? = null

	override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
		dialogView = activity!!.layoutInflater.inflate(R.layout.dialog_refactor_schedule, null)
		return AlertDialog.Builder(activity!!)
				.setView(dialogView)
				.setTitle(addTitle)
				.setNegativeButton(R.string.cancel, null)
				.setPositiveButton(R.string.ok, onOkListener())
				.create()
	}

	// Workaround for "Can't access the Fragment View's LifecycleOwner before onCreateView()"
	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) = dialogView

	override fun onDestroy() {
		dialogView = null
		super.onDestroy()
	}

	override fun onActivityCreated(savedInstanceState: Bundle?) {
		super.onActivityCreated(savedInstanceState)
		activity?.let { viewModel = ViewModelProviders.of(it, factory).get(SchedulesListViewModel::class.java) }
		initObservers()
		editText_refactor_schedule_name.setText(scheduleName)
	}

	private fun initObservers() {
		viewModel.editedSchedule.observe(viewLifecycleOwner, Observer {
			it?.let { schedule ->
				editedSchedule = schedule
				setModeEdit()
			}
		})
	}

	private fun setModeEdit() {
		mode = Mode.EDIT
		scheduleName = editedSchedule.name
		dialog?.setTitle(R.string.schedule_rename_title)
	}


	private fun onOkListener() = DialogInterface.OnClickListener { _,_ ->
		if (editText_refactor_schedule_name.input.isNotBlank()) {
			val schedule = Schedule(editText_refactor_schedule_name.input)
			when (mode) {
				Mode.ADD -> viewModel.insert(schedule)
				Mode.EDIT -> viewModel.update(schedule.apply { id = editedSchedule.id })
			}
		} else {
			toast(activity, R.string.schedule_empty_name)
		}
	}

	override fun onDetach() {
		viewModel.clearRefactorDialogValues()
		super.onDetach()
	}
}
