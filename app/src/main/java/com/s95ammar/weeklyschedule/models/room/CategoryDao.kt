package com.s95ammar.weeklyschedule.models.room

import androidx.lifecycle.LiveData
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.s95ammar.weeklyschedule.models.Category

interface CategoryDao {
	@Insert
	suspend fun insert(category: Category)

	@Update
	suspend fun update(category: Category)

	@Delete
	suspend fun delete(category: Category)

	@Query("DELETE FROM category")
	suspend fun deleteAllCategorys()

	@Query("SELECT * FROM category WHERE id=:id")
	fun getCategoryById(id: Int): LiveData<Category>

	@Query("SELECT * FROM category")
	fun getAllCategorys(): LiveData<List<Category>>
}