package com.s95ammar.weeklyschedule.views.fragments.dialogs

import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.annotation.ColorInt
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.s95ammar.weeklyschedule.R
import com.s95ammar.weeklyschedule.models.data.Category
import com.s95ammar.weeklyschedule.util.COLOR_PRIMARY
import com.s95ammar.weeklyschedule.util.ColorType
import com.s95ammar.weeklyschedule.util.Mode
import com.s95ammar.weeklyschedule.util.input
import com.s95ammar.weeklyschedule.viewModels.CategoriesListViewModel
import dagger.android.support.DaggerDialogFragment
import kotlinx.android.synthetic.main.dialog_add_category.*
import javax.inject.Inject


class CategoryRefactorDialog : DaggerDialogFragment() {
	private val t = "log_${javaClass.simpleName}"
	lateinit var categoryName: String
	lateinit var mode: Mode
	@ColorInt var categoryFillColor = COLOR_PRIMARY
	@ColorInt var categoryTextColor = Color.BLACK

	@Inject
	lateinit var factory: ViewModelProvider.Factory
	private lateinit var viewModel: CategoriesListViewModel
	private var dialogView: View? = null

	// Not using Kotlin synthetic here because editText_add_category_name is being accessed in onCreateDialog() (before onViewCreated())
	@BindView(R.id.editText_add_category_name)
	lateinit var editTextAddCategoryName: EditText

	override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
		val builder = AlertDialog.Builder(activity!!)
		val inflater = activity!!.layoutInflater
		dialogView = inflater.inflate(R.layout.dialog_add_category, null)
		dialogView?.let { ButterKnife.bind(this, it) }

//		val obj = arguments!!.getSerializable(KEY_CATEGORY)
		val title: String = ADD_TITLE // TODO: temp
		mode = Mode.ADD // TODO: temp
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
		return builder.setView(dialogView)
				.setTitle(title)
				.setNegativeButton("Cancel", null)
				.setPositiveButton("Ok", onOkListener(/*i*/))
				.create()
	}

	// Workaround for "Can't access the Fragment View's LifecycleOwner when getView() is null"
	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) = dialogView
	override fun onDestroy() { dialogView = null; super.onDestroy()	}

	override fun onActivityCreated(savedInstanceState: Bundle?) {
		super.onActivityCreated(savedInstanceState)
		//		activity?.let { viewModel = ViewModelProviders.of(it, factory).get(CategoriesListViewModel::class.java) }
		assignFillColor(categoryFillColor)
		assignTextColor(categoryTextColor)

		viewModel = ViewModelProviders.of(activity!!, factory).get(CategoriesListViewModel::class.java)
		Log.d(t, "onActivityCreated: $viewModel")
		viewModel.onColorReceived.observe(viewLifecycleOwnerLiveData.value!!, Observer {
			receiveColor(it.first, it.second)
		})
	}

	@OnClick(R.id.view_add_category_fill_color, R.id.view_add_category_text_color)
	fun openColorPicker(view: View) {
		Log.d(t, "openColorPicker: ")
		when (view.id) {
			R.id.view_add_category_fill_color -> viewModel.showColorPickerDialog(ColorType.FILL)
			R.id.view_add_category_text_color -> viewModel.showColorPickerDialog(ColorType.TEXT)
		}
	}

	private fun onOkListener(/*i: Int*/): DialogInterface.OnClickListener {

		return DialogInterface.OnClickListener { _, _ -> // TODO
			val category = Category(editTextAddCategoryName.input, categoryFillColor, categoryTextColor)
			Log.d(t, "onOkListener: $category")
		}
	}

	private fun assignFillColor(@ColorInt color: Int) {
		categoryFillColor = color
		view_add_category_fill_color.setBackgroundColor(color)
		text_add_category_preview_value.setBackgroundColor(color)
	}

	private fun assignTextColor(@ColorInt color: Int) {
		categoryTextColor = color
		view_add_category_text_color.setBackgroundColor(color)
		text_add_category_preview_value.setTextColor(color)
	}


	private fun receiveColor(colorType: ColorType, @ColorInt color: Int) = when (colorType) {
		ColorType.FILL -> assignFillColor(color)
		ColorType.TEXT -> assignTextColor(color)
	}

	companion object {
		private val TAG = "CategoryRefactorDialog"
		private val ADD_TITLE = "New Category"
		private val EDIT_TITLE = "Edit Category"
	}
}
