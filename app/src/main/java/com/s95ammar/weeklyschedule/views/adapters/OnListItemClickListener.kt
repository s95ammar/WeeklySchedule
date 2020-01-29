package com.s95ammar.weeklyschedule.views.adapters

import android.widget.Button

interface OnListItemClickListener<T> {
	fun onItemClicked(item: T)
	fun onMoreClicked(item: T, buttonMore: Button)
}
