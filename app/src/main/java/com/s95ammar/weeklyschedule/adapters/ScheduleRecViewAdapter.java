package com.s95ammar.weeklyschedule.adapters;

import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.s95ammar.weeklyschedule.R;
import com.s95ammar.weeklyschedule.models.Schedule;

import java.util.ArrayList;

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
        ScheduleViewHolder scheduleViewHolder = new ScheduleViewHolder(v, mListener);
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

    public static class ScheduleViewHolder extends RecyclerView.ViewHolder {
        private TextView tvScheduleName;
        private Switch switchIsActive;
        private Button buttonMore;
        private TextView tvIsActive;

        public ScheduleViewHolder(@NonNull final View itemView, final OnItemClickListener listener) {
            super(itemView);
            tvScheduleName = itemView.findViewById(R.id.textView_schedule_name);
            switchIsActive = itemView.findViewById(R.id.switch_is_active);
            tvIsActive = itemView.findViewById(R.id.textView_is_active);
            buttonMore = itemView.findViewById(R.id.schedules_button_more);
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
            switchIsActive.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (listener != null) {
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
                            listener.onSwitchChecked(i, isChecked);
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
        void onSwitchChecked(int i, boolean isChecked);
    }
}
