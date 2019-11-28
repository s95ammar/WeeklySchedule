package com.s95ammar.weeklyschedule.views.fragments.dialogs

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.s95ammar.weeklyschedule.R
import com.s95ammar.weeklyschedule.models.data.Schedule
import com.s95ammar.weeklyschedule.util.ListMode
import com.s95ammar.weeklyschedule.util.input
import com.s95ammar.weeklyschedule.util.toast
import com.s95ammar.weeklyschedule.viewModels.SchedulesListViewModel
import com.s95ammar.weeklyschedule.views.BlankFieldRequiredException
import com.s95ammar.weeklyschedule.views.requireNonBlankFields
import dagger.android.support.DaggerDialogFragment
import kotlinx.android.synthetic.main.dialog_edit_schedule.*
import javax.inject.Inject


class ScheduleNamerDialog : DaggerDialogFragment() {
	private var mode = ListMode.ADD
	private lateinit var editedSchedule: Schedule
	private var scheduleName = ""
		set(value) {
			field = value
			editText_edit_schedule_name.setText(scheduleName)
		}

	@Inject lateinit var factory: ViewModelProvider.Factory
	private lateinit var viewModel: SchedulesListViewModel
	private var dialogView: View? = null

	override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
		dialogView = requireActivity().layoutInflater.inflate(R.layout.dialog_edit_schedule, null)
		return AlertDialog.Builder(requireActivity())
				.setView(dialogView)
				.setTitle(R.string.schedule_add_title)
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
		viewModel = ViewModelProviders.of(requireActivity(), factory).get(SchedulesListViewModel::class.java)
		initObservers()
		editText_edit_schedule_name.setText(scheduleName)
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
		mode = ListMode.EDIT
		scheduleName = editedSchedule.name
		dialog?.setTitle(R.string.schedule_rename_title)
	}

	private fun onOkListener() = DialogInterface.OnClickListener { _, _ ->
		try {
			requireNonBlankFields(editText_edit_schedule_name to "schedule name")
			when (mode) {
				ListMode.ADD -> viewModel.insert(Schedule(editText_edit_schedule_name.input))
				ListMode.EDIT -> viewModel.update(getUpdatedSchedule())
			}
		} catch (e: BlankFieldRequiredException) {
			toast(e.message, Toast.LENGTH_LONG)
		}
	}

	private fun getUpdatedSchedule() = Schedule(
			editText_edit_schedule_name.input,
			editedSchedule.isActive
	).apply { id = editedSchedule.id }

	override fun onDetach() {
		viewModel.clearNamerDialogValues()
		super.onDetach()
	}
}
