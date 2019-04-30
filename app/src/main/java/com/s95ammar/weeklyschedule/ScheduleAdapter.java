package com.s95ammar.weeklyschedule;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder> {
    private ArrayList<ScheduleItem> mScheduleItems;

    public ScheduleAdapter(ArrayList<ScheduleItem> ScheduleItems) {
        mScheduleItems = ScheduleItems;
    }

    @NonNull
    @Override
    public ScheduleViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_schedule, viewGroup, false);
        ScheduleViewHolder scheduleViewHolder = new ScheduleViewHolder(v);
        return scheduleViewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull ScheduleViewHolder holder, int i) {
        ScheduleItem currentItem = mScheduleItems.get(i);
        holder.tvScheduleName.setText(currentItem.getName());
        holder.switchIsActive.setChecked(currentItem.isActive());
    }

    @Override
    public int getItemCount() {
        return mScheduleItems.size();
    }

    public static class ScheduleViewHolder extends RecyclerView.ViewHolder {
        public TextView tvScheduleName;
        public Switch switchIsActive;

        public ScheduleViewHolder(@NonNull View itemView) {
            super(itemView);
            tvScheduleName = itemView.findViewById(R.id.textView_schedule_name);
            switchIsActive = itemView.findViewById(R.id.switch_is_active);
        }
    }

    ;
}
