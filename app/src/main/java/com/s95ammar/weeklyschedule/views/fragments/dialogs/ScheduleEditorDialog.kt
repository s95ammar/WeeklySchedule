package com.s95ammar.weeklyschedule.views.fragments.dialogs

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.AdapterView
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
import kotlinx.android.synthetic.main.dialog_edit_schedule.view.*
import javax.inject.Inject


class ScheduleEditorDialog : DaggerDialogFragment() {
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
		setUpDaysSpinner()
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
		textView_edit_schedule_days.visibility = GONE
		spinner_edit_schedule.visibility = GONE
		scheduleName = editedSchedule.name
		dialog?.setTitle(R.string.schedule_rename_title)
	}

	private val t = "log_${javaClass.simpleName}"
	private fun onOkListener() = DialogInterface.OnClickListener { _, _ ->
		try {
			requireNonBlankFields(editText_edit_schedule_name to "schedule name")
			when (mode) {
				ListMode.ADD -> {
					val newSchedule = Schedule(editText_edit_schedule_name.input, spinner_edit_schedule.selectedItem.toString().toInt())
					viewModel.insertScheduleWithDays(newSchedule)
				}
				ListMode.EDIT -> viewModel.update(getUpdatedSchedule())
			}
		} catch (e: BlankFieldRequiredException) {
			toast(e.message, Toast.LENGTH_LONG)
		}
	}

	private fun setUpDaysSpinner() {
		dialogView?.rootView?.spinner_edit_schedule?.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
			override fun onNothingSelected(parent: AdapterView<*>?) {

			}

			// TODO: implement
			override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
				if (position != 0) {
					parent.setSelection(0)
					toast("Feature not yet implemented")
				}
			}
		}
	}


	private fun getUpdatedSchedule() = Schedule(
			editText_edit_schedule_name.input,
			editedSchedule.daysAmount,
			editedSchedule.isActive
	).apply { id = editedSchedule.id }

	override fun onDetach() {
		viewModel.clearEditorDialogValues()
		super.onDetach()
	}
}
