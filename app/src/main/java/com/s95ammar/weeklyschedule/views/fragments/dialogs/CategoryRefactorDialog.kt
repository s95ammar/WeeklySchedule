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
import javax.inject.Inject


class CategoryRefactorDialog : DaggerDialogFragment() {
	private var mode = Mode.ADD
	private lateinit var editedCategory: Category
	@StringRes private val addTitle = R.string.category_add_title
	private var categoryName = ""
		set(value) {
			field = value
			editText_add_category_name.setText(categoryName)
		}
	@ColorInt private var fillColor = COLOR_GREEN
		set(value) {
			field = value
			assignFillColor()
		}
	@ColorInt private var textColor = COLOR_BLACK
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
		fillColor = editedCategory.fillColor
		textColor = editedCategory.textColor
		categoryName = editedCategory.name
		dialog?.setTitle(R.string.category_edit_title)
	}

	private fun setViews() {
		editText_add_category_name.setText(categoryName)
		assignFillColor()
		assignTextColor()
	}

	private fun setListeners() {
		view_category_fill_color.setOnClickListener { onColorClicked(ColorType.FILL, fillColor) }
		view_category_text_color.setOnClickListener { onColorClicked(ColorType.TEXT, textColor) }
	}

	private fun onColorClicked(colorType: ColorType, @ColorInt color: Int) {
		viewModel.showColorPickerDialog(colorType, color)
		observeColorSelection()
	}

	private fun observeColorSelection() {
		viewModel.onCategoryColorSelected.observe(viewLifecycleOwner, Observer { pair ->
			pair?.let { (type, color) ->
				when (type) {
					ColorType.FILL -> fillColor = color
					ColorType.TEXT -> textColor = color
				}
			}
		})
	}

	private fun onOkListener() = DialogInterface.OnClickListener { _,_ ->
		if (editText_add_category_name.input.isNotBlank()) {
			val category = Category(editText_add_category_name.input, fillColor, textColor)
			when (mode) {
				Mode.ADD -> viewModel.insert(category)
				Mode.EDIT -> viewModel.update(category.apply { id = editedCategory.id })
			}
		} else {
			toast(activity, R.string.category_empty_name)
		}
	}

	private fun assignFillColor() {
		view_category_fill_color.setBackgroundColor(fillColor)
		text_category_preview_value.setBackgroundColor(fillColor)
	}

	private fun assignTextColor() {
		view_category_text_color.setBackgroundColor(textColor)
		text_category_preview_value.setTextColor(textColor)
	}

	override fun onDetach() {
		viewModel.clearRefactorDialogValues()
		super.onDetach()
	}
}
