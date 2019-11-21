package com.s95ammar.weeklyschedule.views.recViewAdapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.s95ammar.weeklyschedule.R
import com.s95ammar.weeklyschedule.models.data.Schedule
import kotlinx.android.synthetic.main.item_schedule.view.*
import javax.inject.Inject

class SchedulesListAdapter @Inject constructor() : ListAdapter<Schedule, SchedulesListAdapter.ScheduleViewHolder>(DIFF_CALLBACK) {
	var onItemClickListener: OnItemClickListener? = null

	companion object {
		val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Schedule>() {
			override fun areItemsTheSame(oldItem: Schedule, newItem: Schedule) = oldItem.id == newItem.id
			override fun areContentsTheSame(oldItem: Schedule, newItem: Schedule) = oldItem == newItem
		}
	}

	// Called when in need for ScheduleViewHolder to represent an item
	override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int) = ScheduleViewHolder(
			LayoutInflater.from(viewGroup.context).inflate(R.layout.item_schedule, viewGroup, false)
	)


	// Called by RecyclerView to display the data at the specified position
	override fun onBindViewHolder(holder: ScheduleViewHolder, i: Int) {
		val currentItem = getItem(i)
		holder.apply {
			tvScheduleName.text = currentItem.name
		}
	}

	fun getScheduleAt(position: Int): Schedule = getItem(position)

	inner class ScheduleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
		var tvScheduleName: TextView = itemView.text_schedule_name
		var buttonMore: Button = itemView.button_more_schedules

		init {
			itemView.setOnClickListener { onItemClicked() }
			buttonMore.setOnClickListener { onMoreClicked() }
		}

		private fun onItemClicked() = onItemClickListener?.let {
			if (adapterPosition != RecyclerView.NO_POSITION) it.onItemClicked(adapterPosition)
		}


		private fun onMoreClicked() = onItemClickListener?.let {
			if (adapterPosition != RecyclerView.NO_POSITION) it.onMoreClicked(adapterPosition, buttonMore)
		}

	}

	interface OnItemClickListener {
		fun onItemClicked(i: Int)
		fun onMoreClicked(i: Int, buttonMore: Button)
	}

}
