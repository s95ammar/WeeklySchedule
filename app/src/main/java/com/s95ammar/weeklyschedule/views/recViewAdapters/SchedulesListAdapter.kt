package com.s95ammar.weeklyschedule.views.recViewAdapters

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Switch
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.s95ammar.weeklyschedule.R
import com.s95ammar.weeklyschedule.models.data.Schedule
import com.s95ammar.weeklyschedule.util.COLOR_GRAY
import com.s95ammar.weeklyschedule.util.COLOR_GREEN
import com.s95ammar.weeklyschedule.util.SHADES_BLACK
import com.s95ammar.weeklyschedule.util.SHADES_WHITE
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
			switchIsActive.isChecked = currentItem.isActive

		}
	}

	fun getScheduleAt(position: Int): Schedule = getItem(position)

	inner class ScheduleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
		var tvScheduleName: TextView = itemView.textView_schedule_name
		var buttonMore: Button = itemView.button_more_schedules
		var switchIsActive: Switch = itemView.switch_is_active
		var tvIsActive: TextView = itemView.textView_is_active

		init {
			itemView.setOnClickListener { onItemClicked() }
			buttonMore.setOnClickListener { onMoreClicked() }
			switchIsActive.setOnCheckedChangeListener { _, isChecked -> onCheckedChanged(isChecked) }
		}

		private fun onItemClicked() = onItemClickListener?.let {
			if (adapterPosition != RecyclerView.NO_POSITION) it.onItemClicked(adapterPosition)
		}

		private fun onMoreClicked() = onItemClickListener?.let {
			if (adapterPosition != RecyclerView.NO_POSITION) it.onMoreClicked(adapterPosition, buttonMore)
		}


		private fun onCheckedChanged(isChecked: Boolean) {
			if (isChecked) {
				tvIsActive.setText(R.string.status_active)
				tvIsActive.setTextColor(COLOR_GREEN)
				tvIsActive.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
			} else {
				tvIsActive.setText(R.string.status_inactive)
				tvIsActive.setTextColor(COLOR_GRAY)
				tvIsActive.typeface = Typeface.defaultFromStyle(Typeface.NORMAL)
			}

			if (adapterPosition != RecyclerView.NO_POSITION) onItemClickListener?.onSwitchChecked(adapterPosition, isChecked)

		}
	}

	interface OnItemClickListener {
		fun onItemClicked(i: Int)
		fun onMoreClicked(i: Int, buttonMore: Button)
		fun onSwitchChecked(i: Int, isChecked: Boolean)

	}

}
