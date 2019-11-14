package com.s95ammar.weeklyschedule.views.fragments.dialogs

import android.app.Dialog
import android.os.Bundle
import android.content.DialogInterface
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import butterknife.BindView
import butterknife.ButterKnife
import com.s95ammar.weeklyschedule.R
import com.s95ammar.weeklyschedule.viewModels.MainViewModel
import dagger.android.support.DaggerDialogFragment
import javax.inject.Inject
import butterknife.OnClick




class CategoryRefactorDialog : DaggerDialogFragment() {
	private val t = "log_${javaClass.simpleName}"

	@Inject
	lateinit var factory: ViewModelProvider.Factory
	private lateinit var viewModel: MainViewModel

	// Not using Kotlin synthetic here because this view is being accessed in onCreateDialog() (before onViewCreated())
	@BindView(R.id.editText_add_category_name)
	lateinit var editTextAddCategoryName: EditText

	override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
		val builder = AlertDialog.Builder(activity!!)
		val inflater = activity!!.layoutInflater
		val view = inflater.inflate(R.layout.dialog_add_category, null)
		ButterKnife.bind(this, view)

//		val obj = arguments!!.getSerializable(KEY_CATEGORY)
		val title: String = ADD_TITLE // TODO: temp
/*
		var category: Category
		if (obj is Category) {
			category = obj
			title = EDIT_TITLE
		} else {
			title = ADD_TITLE
		}
*/
//		setViews(/*category*/)
//		val i = arguments!!.getInt(KEY_INDEX)
		return builder.setView(view)
				.setTitle(title)
				.setNegativeButton("Cancel", null)
				.setPositiveButton("Ok", onOkListener(/*i*/))
				.create()
	}

	override fun onActivityCreated(savedInstanceState: Bundle?) {
		super.onActivityCreated(savedInstanceState)
		activity?.let { viewModel = ViewModelProviders.of(it, factory).get(MainViewModel::class.java) }
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

	}

	@OnClick(R.id.view_add_category_fill_color, R.id.view_add_category_text_color)
	fun openColorPicker(view: View) {
		Log.d(t, "openColorPicker: ")
		when (view.id) {
			R.id.view_add_category_fill_color -> viewModel.showColorPickerDialog(R.id.view_add_category_fill_color)
			R.id.view_add_category_text_color -> viewModel.showColorPickerDialog(R.id.view_add_category_text_color)
		}
	}

	private fun onOkListener(/*i: Int*/): DialogInterface.OnClickListener {
		return DialogInterface.OnClickListener { _, _ ->
		}
	}

	companion object {
		private val TAG = "CategoryRefactorDialog"
		private val ADD_TITLE = "New Category"
		private val EDIT_TITLE = "Edit Category"
	}
}
