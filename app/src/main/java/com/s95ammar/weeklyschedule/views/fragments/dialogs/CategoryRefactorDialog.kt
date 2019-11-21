package com.s95ammar.weeklyschedule.views.fragments.dialogs

import android.app.Dialog
import android.content.DialogInterface
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.core.graphics.drawable.DrawableCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.s95ammar.weeklyschedule.R
import com.s95ammar.weeklyschedule.models.data.Category
import com.s95ammar.weeklyschedule.util.*
import com.s95ammar.weeklyschedule.viewModels.CategoriesListViewModel
import dagger.android.support.DaggerDialogFragment
import kotlinx.android.synthetic.main.dialog_add_category.*
import javax.inject.Inject


class CategoryRefactorDialog : DaggerDialogFragment() {
	private var mode = Mode.ADD
	private lateinit var editedCategory: Category
	@StringRes private val addTitle = R.string.category_add_title
	private var categoryName = ""
		set(value) {
			field = value
			editText_refactor_category_name.setText(categoryName)
		}
	@ColorInt private var selectedFillColor = COLOR_GREEN
		set(value) {
			field = value
			assignFillColor()
		}
	@ColorInt private var selectedTextColor = COLOR_BLACK
		set(value) {
			field = value
			assignTextColor()
		}


	@Inject
	lateinit var factory: ViewModelProvider.Factory
	private lateinit var viewModel: CategoriesListViewModel
	private var dialogView: View? = null

	override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
		dialogView = activity!!.layoutInflater.inflate(R.layout.dialog_add_category, null)
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
		activity?.let { viewModel = ViewModelProviders.of(it, factory).get(CategoriesListViewModel::class.java) }
		initObservers()
		setViews()
		setListeners()
	}

	private fun initObservers() {
		viewModel.editedCategory.observe(viewLifecycleOwner, Observer {
			it?.let { category ->
				editedCategory = category
				setModeEdit()
			}
		})
	}

	private fun setModeEdit() {
		mode = Mode.EDIT
		selectedFillColor = editedCategory.fillColor
		selectedTextColor = editedCategory.textColor
		categoryName = editedCategory.name
		dialog?.setTitle(R.string.category_edit_title)
	}

	private fun setViews() {
		editText_refactor_category_name.setText(categoryName)
		assignFillColor()
		assignTextColor()
	}

	private fun setListeners() {
		button_refactor_category_reverse_colors.setOnClickListener { reverseColors() }
		view_refactor_category_fill_color.setOnClickListener { onColorClicked(ColorDetails(selectedFillColor, ColorTarget.FILL)) }
		view_refactor_category_text_color.setOnClickListener { onColorClicked(ColorDetails(selectedTextColor, ColorTarget.TEXT)) }
	}

	private fun reverseColors() {
		val fillColor = selectedFillColor
		selectedFillColor = selectedTextColor
		selectedTextColor = fillColor
	}

	private fun onColorClicked(colorDetails: ColorDetails) {
		viewModel.showColorPickerDialog(colorDetails)
		observeColorSelection()
	}

	private fun observeColorSelection() {
		viewModel.onCategoryColorSelected.observe(viewLifecycleOwner, Observer { colorDetails ->
			colorDetails?.let {
				when (it.target) {
					ColorTarget.FILL -> selectedFillColor = it.color
					ColorTarget.TEXT -> selectedTextColor = it.color
				}
			}
		})
	}

	private fun onOkListener() = DialogInterface.OnClickListener { _,_ ->
		if (editText_refactor_category_name.input.isNotBlank()) {
			val category = Category(editText_refactor_category_name.input, selectedFillColor, selectedTextColor)
			when (mode) {
				Mode.ADD -> viewModel.insert(category)
				Mode.EDIT -> viewModel.update(category.apply { id = editedCategory.id })
			}
		} else {
			toast(activity, R.string.category_empty_name)
		}
	}

	private fun assignFillColor() {
		view_refactor_category_fill_color.background.setColorFilter(selectedFillColor, PorterDuff.Mode.SRC) // changes color but not shape
		text_refactor_category_preview_value.setBackgroundColor(selectedFillColor)
	}

	private fun assignTextColor() {
		view_refactor_category_text_color.background.setColorFilter(selectedTextColor, PorterDuff.Mode.SRC)
		text_refactor_category_preview_value.setTextColor(selectedTextColor)
	}

	override fun onDetach() {
		viewModel.clearRefactorDialogValues()
		super.onDetach()
	}
}
