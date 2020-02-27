package com.s95ammar.weeklyschedule.views.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.annotation.Nullable
import com.s95ammar.weeklyschedule.R
import com.s95ammar.weeklyschedule.models.data.Category
import com.s95ammar.weeklyschedule.util.getStrokedBackground
import kotlinx.android.synthetic.main.spinner_row_category.view.*


class CategorySpinnerAdapter(context: Context, categoriesList: List<Category>) : ArrayAdapter<Category>(context, 0, categoriesList) {

	override fun getView(position: Int, @Nullable convertView: View?, parent: ViewGroup): View {
		return initView(position, convertView, parent)
	}

	override fun getDropDownView(position: Int, @Nullable convertView: View?, parent: ViewGroup): View {
		return initView(position, convertView, parent)
	}

	private fun initView(position: Int, convertView: View?, parent: ViewGroup): View {
		val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.spinner_row_category, parent, false)
		getItem(position)?.let { category ->
			view.textView_category_spinner.apply {
				text = category.name
				setTextColor(category.textColor)
				background = getStrokedBackground(category.fillColor, category.textColor)
			}
		}
		return view
	}
}
