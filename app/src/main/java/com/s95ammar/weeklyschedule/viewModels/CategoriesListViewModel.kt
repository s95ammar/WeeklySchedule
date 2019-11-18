package com.s95ammar.weeklyschedule.viewModels

import android.util.Log
import androidx.annotation.ColorInt
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.s95ammar.weeklyschedule.models.Repository
import com.s95ammar.weeklyschedule.models.data.Category
import com.s95ammar.weeklyschedule.util.ColorType
import com.s95ammar.weeklyschedule.viewModels.viewModelHelpers.SingleLiveEvent
import javax.inject.Inject

class CategoriesListViewModel @Inject constructor(private var repo: Repository) : ViewModel() {
	private val t = "log_${javaClass.simpleName}"

//	private val _onActionButtonClicked = SingleLiveEvent<Unit>()
	private val _showCategoryColorPicker = SingleLiveEvent<Pair<ColorType, Int>>()
	private val _showCategoryRefactorDialog = SingleLiveEvent<Category>()
	private val _onColorSelected = SingleLiveEvent<Pair<ColorType, Int>>()

//	val onActionButtonClicked: LiveData<Unit> = _onActionButtonClicked
	val showCategoryColorPicker: LiveData<Pair<ColorType, Int>> = _showCategoryColorPicker
	val showCategoryRefactorDialog: LiveData<Category> = _showCategoryRefactorDialog
	val onColorSelected: LiveData<Pair<ColorType, Int>> = _onColorSelected

	init {
		Log.d(t, "init: ")
	}

	fun insert(category: Category) = repo.insert(category)
	fun update(category: Category) = repo.update(category)
	fun delete(category: Category) = repo.delete(category)
	fun deleteAllCategories() = repo.deleteAllCategories()
	fun getCategoryById(id: Int) = repo.getCategoryById(id)
	fun getAllCategories() = repo.getAllCategories()

//	fun callOnActionButtonClicked() = _onActionButtonClicked.call()

	fun showColorPickerDialog(colorType: ColorType, initialSelection: Int) {
		_showCategoryColorPicker.value = Pair(colorType, initialSelection)
	}

	fun setOnColorSelected(colorType: ColorType, @ColorInt color: Int) {
		_onColorSelected.value = Pair(colorType, color)
	}

	fun showCategoryRefactorDialog(category: Category? = null) {
		_showCategoryRefactorDialog.value = category
	}

}