package com.s95ammar.weeklyschedule.adapters;

import android.graphics.Typeface;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import com.s95ammar.weeklyschedule.R;
import com.s95ammar.weeklyschedule.models.Schedule;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

public class ScheduleRecViewAdapter extends RecyclerView.Adapter<ScheduleRecViewAdapter.ScheduleViewHolder> {
    private static final String TAG = "ScheduleRecViewAdapter";
    private ArrayList<Schedule> mSchedules;
    private OnItemClickListener mListener;

    public ScheduleRecViewAdapter(ArrayList<Schedule> schedules) {
        mSchedules = schedules;
    }

    @NonNull
    @Override
    // Called when in need for CategoryViewHolder to represent an item
    public ScheduleViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_schedule, viewGroup, false);
        ScheduleViewHolder scheduleViewHolder = new ScheduleViewHolder(v);
        return scheduleViewHolder;
    }


    @Override
    // Called by RecyclerView to display the data at the specified position
    public void onBindViewHolder(@NonNull ScheduleViewHolder holder, int i) {
        Schedule currentItem = mSchedules.get(i);
        holder.tvScheduleName.setText(currentItem.getName());
        holder.switchIsActive.setChecked(currentItem.isActive());
    }

    @Override
    public int getItemCount() {
        return mSchedules == null ? 0 : mSchedules.size();
    }

    public class ScheduleViewHolder extends RecyclerView.ViewHolder {
        protected @BindView(R.id.textView_schedule_name) TextView tvScheduleName;
        protected @BindView(R.id.switch_is_active) Switch switchIsActive;
        protected @BindView(R.id.schedules_button_more) Button buttonMore;
        protected @BindView(R.id.textView_is_active) TextView tvIsActive;

        public ScheduleViewHolder(@NonNull final View itemView) {
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

        @OnClick(R.id.schedules_button_more)
        protected void onClick() {
            if (mListener != null) {
                int i = getAdapterPosition();
                if (i != RecyclerView.NO_POSITION) {
                    mListener.onMoreClicked(i, buttonMore);
                }
            }
        }

        @OnCheckedChanged(R.id.switch_is_active)
        protected void onCheckedChanged(boolean isChecked) {
            if (mListener != null) {
                int i = getAdapterPosition();
                if (i != RecyclerView.NO_POSITION) {
                    if (isChecked) {
                        tvIsActive.setText(R.string.status_active);
                        tvIsActive.setTextColor(itemView.getResources().getColor(R.color.colorPrimary));
                        tvIsActive.setTypeface(null, Typeface.BOLD);
                    } else {
                        tvIsActive.setText(R.string.status_inactive);
                        tvIsActive.setTextColor(itemView.getResources().getColor(R.color.colorDarkerGray));
                        tvIsActive.setTypeface(null, Typeface.NORMAL);
                    }
                    mListener.onSwitchChecked(i, isChecked);
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
        void onSwitchChecked(int i, boolean isChecked);
    }
}
