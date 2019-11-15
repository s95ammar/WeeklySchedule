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
	private val _onCategoryColorButtonClick = SingleLiveEvent<Pair<ColorType, Int>>()
	private val _onCategoryRefactorDialogRequest = SingleLiveEvent<Category>()
	private val _onColorReceived = SingleLiveEvent<Pair<ColorType, Int>>()

//	val onActionButtonClicked: LiveData<Unit> = _onActionButtonClicked
	val onCategoryColorButtonClick: LiveData<Pair<ColorType, Int>> = _onCategoryColorButtonClick
	val onCategoryRefactorDialogRequest: LiveData<Category> = _onCategoryRefactorDialogRequest
	val onColorReceived: LiveData<Pair<ColorType, Int>> = _onColorReceived

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
		_onCategoryColorButtonClick.value = Pair(colorType, initialSelection)
	}

	fun receiveColor(colorType: ColorType, @ColorInt color: Int) {
		_onColorReceived.value = Pair(colorType, color)
	}

	fun onCategoryRefactorDialogRequest(category: Category? = null) {
		_onCategoryRefactorDialogRequest.value = category
	}

}