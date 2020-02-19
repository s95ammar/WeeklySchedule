package com.s95ammar.weeklyschedule.views.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.s95ammar.weeklyschedule.R
import com.s95ammar.weeklyschedule.models.data.Category
import kotlinx.android.synthetic.main.item_category.view.*
import javax.inject.Inject

class CategoriesListAdapter @Inject constructor() : ListAdapter<Category, CategoriesListAdapter.CategoryViewHolder>(DIFF_CALLBACK) {
	var onItemClickListener: OnListItemClickListener<Category>? = null

	companion object {
		val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Category>() {
			override fun areItemsTheSame(oldItem: Category, newItem: Category) = oldItem.id == newItem.id
			override fun areContentsTheSame(oldItem: Category, newItem: Category) = oldItem == newItem
		}
	}

	override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int) = CategoryViewHolder(
			LayoutInflater.from(viewGroup.context).inflate(R.layout.item_category, viewGroup, false)
	)

	override fun onBindViewHolder(holder: CategoryViewHolder, i: Int) {
		val currentItem = getItem(i)
		holder.apply {
			tvCategoryName.text = currentItem.name
			cardView.setCardBackgroundColor(currentItem.fillColor)
			tvCategoryName.setTextColor(currentItem.textColor)
			buttonMore.background?.mutate()?.setTint(currentItem.textColor)
		}
	}

	inner class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
		var tvCategoryName: TextView = itemView.textView_category_name
		var buttonMore: Button = itemView.button_more_categories
		var cardView: CardView = itemView.cardView_categories

		init {
			itemView.setOnClickListener { onItemClicked() }
			buttonMore.setOnClickListener { onMoreClicked() }
		}

		private fun onItemClicked() = onItemClickListener?.let {
			if (adapterPosition != RecyclerView.NO_POSITION) it.onItemClicked(getItem(adapterPosition))
		}


		private fun onMoreClicked() = onItemClickListener?.let {
			if (adapterPosition != RecyclerView.NO_POSITION) it.onMoreClicked(getItem(adapterPosition), buttonMore)
		}

	}

}
