package com.s95ammar.weeklyschedule.viewModels

import android.util.Log
import androidx.annotation.ColorInt
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.s95ammar.weeklyschedule.models.Repository
import com.s95ammar.weeklyschedule.models.data.Category
import com.s95ammar.weeklyschedule.util.ColorType
import com.s95ammar.weeklyschedule.viewModels.viewModelHelpers.SingleLiveEvent
import javax.inject.Inject

class CategoriesListViewModel @Inject constructor(private var repo: Repository) : ViewModel() {
	private val t = "log_${javaClass.simpleName}"

	private val _showCategoryColorPicker = SingleLiveEvent<Pair<ColorType, Int>>()
	private val _showCategoryRefactorDialog = SingleLiveEvent<Category?>()
	private val _editedCategory = MutableLiveData<Category>()
	private val _onCategoryColorSelected = MutableLiveData<Pair<ColorType,Int>>()

	val showCategoryColorPicker: LiveData<Pair<ColorType, Int>> = _showCategoryColorPicker
	val showCategoryRefactorDialog: LiveData<Category?> = _showCategoryRefactorDialog
	val editedCategory: LiveData<Category> = _editedCategory
	val onCategoryColorSelected: LiveData<Pair<ColorType,Int>> = _onCategoryColorSelected

	init {
		Log.d(t, "init: ")
	}

	fun insert(category: Category) = repo.insert(category)
	fun update(category: Category) = repo.update(category)
	fun delete(category: Category) = repo.delete(category)
	fun deleteAllCategories() = repo.deleteAllCategories()
	fun getCategoryById(id: Int) = repo.getCategoryById(id)
	fun getAllCategories() = repo.getAllCategories()

	fun showColorPickerDialog(colorType: ColorType, initialSelection: Int) {
		_showCategoryColorPicker.value = (colorType to initialSelection)
	}

	fun showCategoryRefactorDialog(category: Category? = null) {
		_showCategoryRefactorDialog.value = category
	}

	fun setEditedCategory(category: Category) {
		_editedCategory.value = category
	}

	fun setCategoryColor(colorType: ColorType, @ColorInt color: Int) {
		_onCategoryColorSelected.value = (colorType to color)
	}

	fun clearRefactorDialogValues() {
		_onCategoryColorSelected.value = null
		_editedCategory.value = null
	}


}