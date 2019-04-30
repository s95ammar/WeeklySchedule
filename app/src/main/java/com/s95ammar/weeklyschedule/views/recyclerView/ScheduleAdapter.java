package com.s95ammar.weeklyschedule.views.recyclerView;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.Switch;
import android.widget.TextView;

import com.s95ammar.weeklyschedule.R;
import com.s95ammar.weeklyschedule.models.ScheduleItem;

import java.util.ArrayList;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder> {
    private static final String TAG = "ScheduleAdapter";
    private ArrayList<ScheduleItem> mScheduleItems;
    private OnItemClickListener mListener;

    public ScheduleAdapter(ArrayList<ScheduleItem> ScheduleItems) {
        mScheduleItems = ScheduleItems;
    }

    @NonNull
    @Override
    // Called when in need for ScheduleViewHolder to represent an item
    public ScheduleViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_schedule, viewGroup, false);
        ScheduleViewHolder scheduleViewHolder = new ScheduleViewHolder(v, mListener);
        return scheduleViewHolder;
    }


    @Override
    // Called by RecyclerView to display the data at the specified position
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
        private TextView tvScheduleName;
        private Switch switchIsActive;
        private Button buttonMore;

        public ScheduleViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            tvScheduleName = itemView.findViewById(R.id.textView_schedule_name);
            switchIsActive = itemView.findViewById(R.id.switch_is_active);
            buttonMore = itemView.findViewById(R.id.button_more);
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
