package com.s95ammar.weeklyschedule.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.s95ammar.weeklyschedule.models.Repository
import com.s95ammar.weeklyschedule.models.data.Category
import com.s95ammar.weeklyschedule.util.ColorDetails
import com.s95ammar.weeklyschedule.util.LOG_TAG
import com.s95ammar.weeklyschedule.util.launchIO
import com.s95ammar.weeklyschedule.viewModels.viewModelHelpers.SingleLiveEvent
import javax.inject.Inject

class CategoriesListViewModel @Inject constructor(private var repo: Repository) : ViewModel() {

	private val _showCategoryEditorDialog = SingleLiveEvent<Int>()
	private val _showCategoryColorPicker = SingleLiveEvent<ColorDetails>()
	private val _onCategoryColorSelected = MutableLiveData<ColorDetails>()

	val showCategoryEditorDialog: LiveData<Int> = _showCategoryEditorDialog
	val showCategoryColorPicker: LiveData<ColorDetails> = _showCategoryColorPicker
	val onCategoryColorSelected: LiveData<ColorDetails> = _onCategoryColorSelected

	init {
		Log.d(LOG_TAG, "init: ")
	}

	fun insert(category: Category) = launchIO { repo.insert(category) }
	fun update(category: Category) = launchIO { repo.update(category) }
	fun delete(category: Category) = launchIO { repo.delete(category) }
	fun deleteAllCategories() = launchIO { repo.deleteAllCategories() }
	fun getCategoryById(id: Int) = repo.getCategoryById(id)
	fun getAllCategories() = repo.getAllCategories()

	fun showCategoryEditorDialog(categoryId: Int = 0) {
		_showCategoryEditorDialog.value = categoryId
	}

	fun showColorPickerDialog(colorDetails: ColorDetails) {
		_showCategoryColorPicker.value = (colorDetails)
	}

	fun setCategoryColor(colorDetails: ColorDetails) {
		_onCategoryColorSelected.value = colorDetails
	}

	fun clearEditorDialogValues() {
		_onCategoryColorSelected.value = null
	}


}