package com.s95ammar.weeklyschedule.views.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.s95ammar.weeklyschedule.R;

import java.util.HashSet;

import static com.s95ammar.weeklyschedule.views.fragments.SchedulesListFragment.SchedulesListManager.KEY_INDEX;
import static com.s95ammar.weeklyschedule.views.fragments.SchedulesListFragment.SchedulesListManager.KEY_NAME;

public class ScheduleNamerDialog extends AppCompatDialogFragment {
    private EditText EditTextName;
    private ScheduleNamerListener mListener;
    private static final String ADD_TITLE = "New Schedule";
    private static final String RENAME_TITLE = "Rename Schedule";

    public interface Action {
        int ADD = -1;
        // REMAKE = any value other than -1 (value will be used as index)
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ScheduleNamerListener) {
            mListener = (ScheduleNamerListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ScheduleNamerListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_schedule, null);
        EditTextName = view.findViewById(R.id.eText_add_schedule_name);
        String name = getArguments().getString(KEY_NAME);
        final int i = getArguments().getInt(KEY_INDEX);
        builder.setView(view)
                .setTitle(i == Action.ADD ? ADD_TITLE : RENAME_TITLE)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Ok", onOkListener(i));

        if (name != null) {
            EditTextName.setText(name);
            EditTextName.selectAll();
        }
        return builder.create();
    }

    private DialogInterface.OnClickListener onOkListener(final int i) {
        return (dialog, which) -> {
            String name = EditTextName.getText().toString();
            if (!name.isEmpty()) {
                mListener.applyName(name, i);
            }
        };
    }


    public interface ScheduleNamerListener {
        void applyName(String name, int i);
    }
}
