package com.s95ammar.weeklyschedule.adapters;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.s95ammar.weeklyschedule.R;
import com.s95ammar.weeklyschedule.models.Category;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_category, viewGroup, false);
        return new CategoryViewHolder(view);
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

    public class CategoryViewHolder extends RecyclerView.ViewHolder {
        protected @BindView(R.id.textView_category_name) TextView tvCategoryName;
        protected @BindView(R.id.categories_button_more) Button buttonMore;
        protected @BindView(R.id.cardView_categories) CardView cardView;

        public CategoryViewHolder(@NonNull final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick
        protected void onItemClicked() {
            if (mListener != null) {
                int i = getAdapterPosition();
                if (i != RecyclerView.NO_POSITION) {
                    mListener.onItemClicked(i);
                }
            }
        }

        @OnClick(R.id.categories_button_more)
        protected void onMoreClicked() {
            if (mListener != null) {
                int i = getAdapterPosition();
                if (i != RecyclerView.NO_POSITION) {
                    mListener.onMoreClicked(i, buttonMore);
                }
            }
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
