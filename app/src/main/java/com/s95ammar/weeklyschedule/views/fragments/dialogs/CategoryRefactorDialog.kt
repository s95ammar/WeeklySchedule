package com.s95ammar.weeklyschedule.views.fragments.dialogs

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorInt
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
	private lateinit var title: String
	private var editedCategory: Category? = null
	private lateinit var categoryName: String
	@ColorInt
	var categoryFillColor = COLOR_GREEN
	@ColorInt
	var categoryTextColor = COLOR_BLACK

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
				title = EDIT_TITLE
			}
			else -> {
				mode = Mode.ADD
				title = ADD_TITLE
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
		setValues()
		setViews()
		activity?.let { viewModel = ViewModelProviders.of(it, factory).get(CategoriesListViewModel::class.java) }
		viewModel.onColorSelected.observe(viewLifecycleOwner, Observer { receiveColor(it.first, it.second) })
	}

	private fun setValues() {
		when (mode) {
			Mode.ADD -> categoryName = ""
			Mode.EDIT -> editedCategory?.let {
				categoryName = it.name
				categoryFillColor = it.fillColor
				categoryTextColor = it.textColor
			}
		}
	}

	private fun setViews() {
		editText_add_category_name.setText(categoryName)
		assignFillColor()
		assignTextColor()
		view_add_category_fill_color.setOnClickListener { viewModel.showColorPickerDialog(ColorType.FILL, categoryFillColor) }
		view_add_category_text_color.setOnClickListener { viewModel.showColorPickerDialog(ColorType.TEXT, categoryTextColor) }
	}

	private fun onOkListener(): DialogInterface.OnClickListener {
		return DialogInterface.OnClickListener { _, _ ->
			dialogView?.let { view ->
				val category = Category(view.rootView.editText_add_category_name.input, categoryFillColor, categoryTextColor)
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

	private fun assignFillColor() {
		view_add_category_fill_color.setBackgroundColor(categoryFillColor)
		text_add_category_preview_value.setBackgroundColor(categoryFillColor)
	}

	private fun assignTextColor() {
		view_add_category_text_color.setBackgroundColor(categoryTextColor)
		text_add_category_preview_value.setTextColor(categoryTextColor)
	}


	private fun receiveColor(colorType: ColorType, @ColorInt color: Int) {
		when (colorType) {
			ColorType.FILL -> {
				categoryFillColor = color
				assignFillColor()
			}
			ColorType.TEXT -> {
				categoryTextColor = color
				assignTextColor()
			}
		}
	}

	companion object {
		private val ADD_TITLE = "New Category"
		private val EDIT_TITLE = "Edit Category"
	}
}
