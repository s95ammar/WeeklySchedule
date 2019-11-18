package com.s95ammar.weeklyschedule.views.fragments.dialogs

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.s95ammar.weeklyschedule.R
import com.s95ammar.weeklyschedule.models.data.Category
import com.s95ammar.weeklyschedule.util.*
import com.s95ammar.weeklyschedule.viewModels.CategoriesListViewModel
import dagger.android.support.DaggerDialogFragment
import kotlinx.android.synthetic.main.dialog_add_category.*
import kotlinx.android.synthetic.main.dialog_add_category.view.*
import javax.inject.Inject


class CategoryRefactorDialog : DaggerDialogFragment() {
	private lateinit var mode: Mode
	@StringRes private var title: Int = R.string.category_add_title
	private var editedCategory: Category? = null
	private lateinit var categoryName: String
	private val fillColor: Int
		get() = viewModel.fillColor.value ?: COLOR_GREEN
	private val textColor: Int
		get() = viewModel.textColor.value ?: COLOR_BLACK


	@Inject
	lateinit var factory: ViewModelProvider.Factory
	private lateinit var viewModel: CategoriesListViewModel
	private var dialogView: View? = null

	override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
		val builder = AlertDialog.Builder(activity!!)
		val inflater = activity!!.layoutInflater
		dialogView = inflater.inflate(R.layout.dialog_add_category, null)
		setMode()
		return builder.setView(dialogView)
				.setTitle(title)
				.setNegativeButton(R.string.cancel, null)
				.setPositiveButton(R.string.ok, onOkListener())
				.create()
	}

	private fun setMode() {
		when (val obj = arguments?.get(KEY_CATEGORY)) {
			is Category -> {
				mode = Mode.EDIT
				editedCategory = obj
				title = R.string.category_edit_title
			}
			else -> {
				mode = Mode.ADD
				title = R.string.category_add_title
			}
		}
	}

	// Workaround for "Can't access the Fragment View's LifecycleOwner when getView() is null"
	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) = dialogView

	override fun onDestroy() {
		dialogView = null
		super.onDestroy()
	}

	override fun onActivityCreated(savedInstanceState: Bundle?) {
		super.onActivityCreated(savedInstanceState)
		activity?.let { viewModel = ViewModelProviders.of(it, factory).get(CategoriesListViewModel::class.java) }
		initObservers()
		setValues()
		setViews()
	}

	private fun initObservers() {
		viewModel.fillColor.observe(viewLifecycleOwner, Observer { assignFillColor(it) })
		viewModel.textColor.observe(viewLifecycleOwner, Observer { assignTextColor(it) })
	}

	private fun setValues() {
		when (mode) {
			Mode.ADD -> {
				categoryName = ""
				viewModel.setFillColor(COLOR_GREEN)
				viewModel.setTextColor(COLOR_BLACK)
			}
			Mode.EDIT -> editedCategory?.let {
				categoryName = it.name
				viewModel.setFillColor(it.fillColor)
				viewModel.setTextColor(it.textColor)
			}
		}
	}

	private fun setViews() {
		editText_add_category_name.setText(categoryName)
		view_category_fill_color.setOnClickListener { viewModel.showColorPickerDialog(ColorType.FILL, fillColor) }
		view_category_text_color.setOnClickListener { viewModel.showColorPickerDialog(ColorType.TEXT, textColor) }
	}

	private fun onOkListener(): DialogInterface.OnClickListener {
		return DialogInterface.OnClickListener { _, _ ->
			dialogView?.let { view ->
				val category = Category(view.rootView.editText_add_category_name.input, fillColor, textColor)
				when (mode) {
					Mode.ADD -> viewModel.insert(category)
					Mode.EDIT -> {
						editedCategory?.let { category.id = it.id }
						viewModel.update(category)
					}
				}
			}
		}
	}

	private fun assignFillColor(@ColorInt color: Int) {
		view_category_fill_color.setBackgroundColor(color)
		text_add_category_preview_value.setBackgroundColor(color)
	}

	private fun assignTextColor(@ColorInt color: Int) {
		view_category_text_color.setBackgroundColor(color)
		text_add_category_preview_value.setTextColor(color)
	}

}
