package com.s95ammar.weeklyschedule.views.fragments.dialogs

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.s95ammar.weeklyschedule.R
import com.s95ammar.weeklyschedule.models.data.Schedule
import com.s95ammar.weeklyschedule.util.*
import com.s95ammar.weeklyschedule.viewModels.SchedulesListViewModel
import com.s95ammar.weeklyschedule.views.BlankFieldRequiredException
import com.s95ammar.weeklyschedule.views.requireNonBlankFields
import dagger.android.support.DaggerDialogFragment
import kotlinx.android.synthetic.main.dialog_edit_schedule.*
import javax.inject.Inject


class ScheduleEditorDialog : DaggerDialogFragment() {
	private lateinit var mode: Mode
	private val argScheduleId
		get() = arguments?.getInt(resources.getString(R.string.key_schedule_id)) ?: 0
	private val daysSelection
		get() = spinner_edit_schedule.selectedItem.toString().toInt()

	@Inject
	lateinit var factory: ViewModelProvider.Factory
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
		viewModel = ViewModelProvider(this, factory).get(SchedulesListViewModel::class.java)
		setMode()
		setViews()
	}


	private fun setMode() {
		mode = when (argScheduleId) {
			0 -> Mode.ADD
			else -> Mode.EDIT
		}
	}

	private fun setViews() {
		if (mode == Mode.EDIT) viewModel.getScheduleById(argScheduleId).safeFetch { editedSchedule ->
			dialog?.setTitle(R.string.schedule_rename_title)
			editText_edit_schedule_name.setText(editedSchedule.name)
			textView_edit_schedule_days.visibility = GONE
			spinner_edit_schedule.visibility = GONE
		}
	}

	private fun onOkListener() = DialogInterface.OnClickListener { _, _ ->
		try {
			requireNonBlankFields(editText_edit_schedule_name to "schedule name")
			when (mode) {
				Mode.ADD -> {
					Schedule(editText_edit_schedule_name.input, Days.fromInt(daysSelection))
							.let { viewModel.insertSchedule(it) }
				}
				Mode.EDIT -> viewModel.renameSchedule(argScheduleId, editText_edit_schedule_name.input)
			}
		} catch (e: BlankFieldRequiredException) {
			toast(e.message, Toast.LENGTH_LONG)
		}
	}

}
