package com.s95ammar.weeklyschedule.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.s95ammar.weeklyschedule.R;
import com.s95ammar.weeklyschedule.models.Category;

import java.util.ArrayList;

public class CategoryRecViewAdapter extends RecyclerView.Adapter<CategoryRecViewAdapter.CategoryViewHolder> {
    private static final String TAG = "ScheduleRecViewAdapter";
    private ArrayList<Category> mCategoryItems;
    private OnItemClickListener mListener;

    public CategoryRecViewAdapter(ArrayList<Category> CategoryItems) {
        mCategoryItems = CategoryItems;
    }

    @NonNull
    @Override
    // Called when in need for CategoryViewHolder to represent an item
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_category, viewGroup, false);
        CategoryViewHolder categoryViewHolder = new CategoryViewHolder(v, mListener);
        return categoryViewHolder;
    }


    @Override
    // Called by RecyclerView to display the data at the specified position
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int i) {
        Category currentItem = mCategoryItems.get(i);
        holder.tvCategoryName.setText(currentItem.getName());
        holder.cardView.setCardBackgroundColor(currentItem.getFillColor());
        holder.tvCategoryName.setTextColor(currentItem.getTextColor());
        holder.buttonMore.getBackground().mutate().setTint(currentItem.getTextColor());
    }

    @Override
    public int getItemCount() {
        return mCategoryItems == null ? 0 : mCategoryItems.size();
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        private TextView tvCategoryName;
        private Button buttonMore;
        private CardView cardView;

        public CategoryViewHolder(@NonNull final View itemView, final OnItemClickListener listener) {
            super(itemView);
            tvCategoryName = itemView.findViewById(R.id.textView_category_name);
            buttonMore = itemView.findViewById(R.id.categories_button_more);
            cardView = itemView.findViewById(R.id.cardView_categories);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int i = getAdapterPosition();
                        if (i != RecyclerView.NO_POSITION) {
                            listener.onItemClicked(i);
                        }
                    }
                }
            });

            buttonMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int i = getAdapterPosition();
                        if (i != RecyclerView.NO_POSITION) {
                            listener.onMoreClicked(i, buttonMore);
                        }
                    }
                }
            });
        }
    }

    public void setOnItemClickedListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClicked(int i);
        void onMoreClicked(int i, Button buttonMore);
    }
}
