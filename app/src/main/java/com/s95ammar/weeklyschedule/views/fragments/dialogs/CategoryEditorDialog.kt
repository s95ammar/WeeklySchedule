package com.s95ammar.weeklyschedule.views.fragments.dialogs

import android.app.Dialog
import android.content.DialogInterface
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.s95ammar.weeklyschedule.R
import com.s95ammar.weeklyschedule.models.data.Category
import com.s95ammar.weeklyschedule.util.*
import com.s95ammar.weeklyschedule.viewModels.CategoriesListViewModel
import com.s95ammar.weeklyschedule.views.BlankFieldRequiredException
import com.s95ammar.weeklyschedule.views.requireNonBlankFields
import dagger.android.support.DaggerDialogFragment
import kotlinx.android.synthetic.main.dialog_edit_category.*
import javax.inject.Inject


class CategoryEditorDialog : DaggerDialogFragment() {
	private var mode = Mode.ADD
	private lateinit var editedCategory: Category
	private var categoryName = ""
		set(value) {
			field = value
			editText_edit_category_name.setText(categoryName)
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
		dialogView = requireActivity().layoutInflater.inflate(R.layout.dialog_edit_category, null)
		return AlertDialog.Builder(requireActivity())
				.setView(dialogView)
				.setTitle(R.string.category_add_title)
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
		viewModel = ViewModelProviders.of(requireActivity(), factory).get(CategoriesListViewModel::class.java)
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
		editText_edit_category_name.setText(categoryName)
		assignFillColor()
		assignTextColor()
	}

	private fun setListeners() {
		button_edit_category_reverse_colors.setOnClickListener { reverseColors() }
		view_edit_category_fill_color.setOnClickListener { onColorClicked(ColorDetails(selectedFillColor, ColorTarget.FILL)) }
		view_edit_category_text_color.setOnClickListener { onColorClicked(ColorDetails(selectedTextColor, ColorTarget.TEXT)) }
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
		try {
			requireNonBlankFields(editText_edit_category_name to "category name")
			val category = Category(editText_edit_category_name.input, selectedFillColor, selectedTextColor)
			when (mode) {
				Mode.ADD -> viewModel.insert(category)
				Mode.EDIT -> viewModel.update(category.apply { id = editedCategory.id })
			}
		} catch (e: BlankFieldRequiredException) {
			toast(e.message, Toast.LENGTH_SHORT)
		}
	}

	private fun assignFillColor() {
		view_edit_category_fill_color.background.setColorFilter(selectedFillColor, PorterDuff.Mode.SRC) // changes color but not shape
		text_edit_category_preview_value.setBackgroundColor(selectedFillColor)
	}

	private fun assignTextColor() {
		view_edit_category_text_color.background.setColorFilter(selectedTextColor, PorterDuff.Mode.SRC)
		text_edit_category_preview_value.setTextColor(selectedTextColor)
	}

	override fun onDetach() {
		viewModel.clearEditorDialogValues()
		super.onDetach()
	}
}
