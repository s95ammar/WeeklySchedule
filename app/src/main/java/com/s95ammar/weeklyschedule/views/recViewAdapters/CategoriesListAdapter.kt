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

class CategoriesListAdapter : ListAdapter<Category, CategoriesListAdapter.CategoryViewHolder>(DIFF_CALLBACK) {
	private var mListener: OnItemClickListener? = null

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

	fun getCategoryAt(position: Int): Category = getItem(position)

	inner class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
		var tvCategoryName: TextView = itemView.text_category_name
		var buttonMore: Button = itemView.button_more_categories
		var cardView: CardView = itemView.cardView_categories

		init {
			itemView.setOnClickListener { onItemClicked() }
			buttonMore.setOnClickListener { onMoreClicked() }
		}

		private fun onItemClicked() = mListener?.let {
			if (adapterPosition != RecyclerView.NO_POSITION) it.onItemClicked(adapterPosition)
		}


		private fun onMoreClicked() = mListener?.let {
			if (adapterPosition != RecyclerView.NO_POSITION) it.onMoreClicked(adapterPosition, buttonMore)
		}

	}

	fun setOnItemClickedListener(listener: OnItemClickListener) {
		mListener = listener
	}

	interface OnItemClickListener {
		fun onItemClicked(i: Int)
		fun onMoreClicked(i: Int, buttonMore: Button)
	}

}
