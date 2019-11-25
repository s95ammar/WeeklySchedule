package com.s95ammar.weeklyschedule.views.recViewAdapters

import androidx.recyclerview.widget.RecyclerView
import butterknife.OnClick
import androidx.cardview.widget.CardView
import android.widget.TextView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.s95ammar.weeklyschedule.R
import com.s95ammar.weeklyschedule.models.data.Category
import kotlinx.android.synthetic.main.item_category.view.*
import javax.inject.Inject

class CategoriesListAdapter @Inject constructor() : ListAdapter<Category, CategoriesListAdapter.CategoryViewHolder>(DIFF_CALLBACK) {
	var onItemClickListener: OnItemClickListener? = null

	companion object {
		val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Category>() {
			override fun areItemsTheSame(oldItem: Category, newItem: Category) = oldItem.id == newItem.id
			override fun areContentsTheSame(oldItem: Category, newItem: Category) = oldItem == newItem
		}
	}

	// Called when in need for CategoryViewHolder to represent an item
	override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int) = CategoryViewHolder(
			LayoutInflater.from(viewGroup.context).inflate(R.layout.item_category, viewGroup, false)
	)


	// Called by RecyclerView to display the data at the specified position
	override fun onBindViewHolder(holder: CategoryViewHolder, i: Int) {
		val currentItem = getItem(i)
		holder.apply {
			tvCategoryName.text = currentItem.name
			cardView.setCardBackgroundColor(currentItem.fillColor)
			tvCategoryName.setTextColor(currentItem.textColor)
			buttonMore.background?.mutate()?.setTint(currentItem.textColor)
		}
	}

	private fun getCategoryAt(position: Int): Category = getItem(position)

	inner class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
		var tvCategoryName: TextView = itemView.text_category_name
		var buttonMore: Button = itemView.button_more_categories
		var cardView: CardView = itemView.cardView_categories

		init {
			itemView.setOnClickListener { onItemClicked() }
			buttonMore.setOnClickListener { onMoreClicked() }
		}

		private fun onItemClicked() = onItemClickListener?.let {
			if (adapterPosition != RecyclerView.NO_POSITION) it.onItemClicked(getCategoryAt(adapterPosition))
		}


		private fun onMoreClicked() = onItemClickListener?.let {
			if (adapterPosition != RecyclerView.NO_POSITION) it.onMoreClicked(getCategoryAt(adapterPosition), buttonMore)
		}

	}

	interface OnItemClickListener {
		fun onItemClicked(category: Category)
		fun onMoreClicked(category: Category, buttonMore: Button)
	}

}
