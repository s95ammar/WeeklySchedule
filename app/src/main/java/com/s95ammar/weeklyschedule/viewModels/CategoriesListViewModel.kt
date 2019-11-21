package com.s95ammar.weeklyschedule.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.s95ammar.weeklyschedule.models.Repository
import com.s95ammar.weeklyschedule.models.data.Category
import com.s95ammar.weeklyschedule.util.ColorDetails
import com.s95ammar.weeklyschedule.viewModels.viewModelHelpers.SingleLiveEvent
import javax.inject.Inject

class CategoriesListViewModel @Inject constructor(private var repo: Repository) : ViewModel() {
	private val t = "log_${javaClass.simpleName}"

	private val _showCategoryColorPicker = SingleLiveEvent<ColorDetails>()
	private val _showCategoryRefactorDialog = SingleLiveEvent<Category?>()
	private val _editedCategory = MutableLiveData<Category>()
	private val _onCategoryColorSelected = MutableLiveData<ColorDetails>()

	val showCategoryColorPicker: LiveData<ColorDetails> = _showCategoryColorPicker
	val showCategoryRefactorDialog: LiveData<Category?> = _showCategoryRefactorDialog
	val editedCategory: LiveData<Category> = _editedCategory
	val onCategoryColorSelected: LiveData<ColorDetails> = _onCategoryColorSelected

	init {
		Log.d(t, "init: ")
	}

	fun insert(category: Category) = repo.insert(category)
	fun update(category: Category) = repo.update(category)
	fun delete(category: Category) = repo.delete(category)
	fun deleteAllCategories() = repo.deleteAllCategories()
	fun getCategoryById(id: Int) = repo.getCategoryById(id)
	fun getAllCategories() = repo.getAllCategories()

	fun showColorPickerDialog(colorDetails: ColorDetails) {
		_showCategoryColorPicker.value = (colorDetails)
	}

	fun showCategoryRefactorDialog(category: Category? = null) {
		_showCategoryRefactorDialog.value = category
	}

	fun setEditedCategory(category: Category) {
		_editedCategory.value = category
	}

	fun setCategoryColor(colorDetails: ColorDetails) {
		_onCategoryColorSelected.value = colorDetails
	}

	fun clearRefactorDialogValues() {
		_onCategoryColorSelected.value = null
		_editedCategory.value = null
	}


}