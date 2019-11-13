package com.s95ammar.weeklyschedule.models.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.s95ammar.weeklyschedule.models.data.Category

@Dao
interface CategoryDao {
	@Insert
	suspend fun insert(category: Category)

	@Update
	suspend fun update(category: Category)

	@Delete
	suspend fun delete(category: Category)

	@Query("DELETE FROM category")
	suspend fun deleteAllCategories()

	@Query("SELECT * FROM category WHERE id=:id")
	fun getCategoryById(id: Int): LiveData<Category>

	@Query("SELECT * FROM category")
	fun getAllCategories(): LiveData<List<Category>>
}