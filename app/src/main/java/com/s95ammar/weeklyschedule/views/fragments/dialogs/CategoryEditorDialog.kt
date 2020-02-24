package com.s95ammar.weeklyschedule.views.fragments.dialogs

import android.app.Dialog
import android.content.DialogInterface
import android.graphics.PorterDuff
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.color.colorChooser
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
	@Inject lateinit var factory: ViewModelProvider.Factory
	private lateinit var viewModel: CategoriesListViewModel
	private var dialogView: View? = null

	private var mode: Mode
		get() = viewModel.categoryEditorMode
		set(value) { viewModel.categoryEditorMode = value }
	@ColorInt private var selectedFillColor: Int = COLOR_GREEN
	@ColorInt private var selectedTextColor: Int = COLOR_BLACK

	private val argCategoryId
		get() = arguments?.getInt(resources.getString(R.string.key_category_id)) ?: 0

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
		viewModel = ViewModelProvider(this, factory).get(CategoriesListViewModel::class.java)
		setMode()
		setViews()
		setListeners()
	}

	private fun setMode() {
		mode = when (argCategoryId) {
			0 -> Mode.ADD
			else -> Mode.EDIT
		}
	}

	private fun setViews() {
		when (mode) {
			Mode.EDIT -> {
				viewModel.getCategoryById(argCategoryId).safeFetch { editedCategory ->
					assignFillColor(editedCategory.fillColor)
					assignTextColor(editedCategory.textColor)
					dialog?.setTitle(R.string.category_edit_title)
					editText_edit_category_name.setText(editedCategory.name)
				}
			}
			Mode.ADD -> {
				assignFillColor(COLOR_GREEN)
				assignTextColor(COLOR_BLACK)
			}
		}
	}

	private fun setListeners() {
		button_edit_category_reverse_colors.setOnClickListener { reverseColors() }
		view_edit_category_fill_color.setOnClickListener { showColorPicker(selectedFillColor, ColorTarget.FILL) }
		view_edit_category_text_color.setOnClickListener { showColorPicker(selectedTextColor, ColorTarget.TEXT) }
	}

	private fun reverseColors() {
		val fillColor = selectedFillColor
		assignFillColor(selectedTextColor)
		assignTextColor(fillColor)
	}

	private fun showColorPicker(@ColorInt initialColor: Int, target: ColorTarget) {
		MaterialDialog(requireActivity()).show {
			title(when (target) {
				ColorTarget.FILL -> R.string.fill_color
				ColorTarget.TEXT -> R.string.text_color
			})
			colorChooser(MAIN_COLORS_ARRAY,
					subColors = SHADES_OF_MAIN_COLORS,
					allowCustomArgb = true,
					showAlphaSelector = true,
					initialSelection = initialColor) { _, selectedColor ->
				when (target) {
					ColorTarget.FILL -> assignFillColor(selectedColor)
					ColorTarget.TEXT -> assignTextColor(selectedColor)
				}
				positiveButton(R.string.select)
				negativeButton(R.string.cancel)
			}
		}
	}


	private fun onOkListener() = DialogInterface.OnClickListener { _, _ ->
		try {
			requireNonBlankFields(editText_edit_category_name to "category name")
			val category = Category(editText_edit_category_name.input, selectedFillColor, selectedTextColor)
			when (mode) {
				Mode.ADD -> viewModel.insert(category)
				Mode.EDIT -> viewModel.update(category.apply { id = argCategoryId })
			}
		} catch (e: BlankFieldRequiredException) {
			toast(e.message)
		}
	}

	private fun assignFillColor(@ColorInt colorInt: Int) {
		view_edit_category_fill_color.background.setColorFilter(colorInt, PorterDuff.Mode.SRC) // changes color but not shape
		selectedFillColor = colorInt
		setPreview()
	}

	private fun assignTextColor(@ColorInt colorInt: Int) {
		view_edit_category_text_color.background.setColorFilter(colorInt, PorterDuff.Mode.SRC)
		selectedTextColor = colorInt
		setPreview()
	}

	private fun setPreview() {
		textView_edit_category_preview_value.setTextColor(selectedTextColor)
		textView_edit_category_preview_value.background = GradientDrawable().apply {
			setColor(selectedFillColor)
			cornerRadius = CORNER_RADIUS
			setStroke(STROKE_WIDTH, selectedTextColor)
		}

	}

}
