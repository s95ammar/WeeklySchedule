package com.s95ammar.weeklyschedule.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.s95ammar.weeklyschedule.models.Repository
import com.s95ammar.weeklyschedule.models.data.Category
import kotlinx.coroutines.launch
import javax.inject.Inject

class CategoriesListViewModel @Inject constructor(private var repo: Repository) : ViewModel() {

	fun insert(category: Category) = viewModelScope.launch { repo.insert(category) }
	fun update(category: Category) = viewModelScope.launch { repo.update(category) }
	fun delete(category: Category) = viewModelScope.launch { repo.delete(category) }
	fun getCategoryById(id: Int) = repo.getCategoryById(id)
	fun getAllCategories() = repo.getAllCategories()

}
