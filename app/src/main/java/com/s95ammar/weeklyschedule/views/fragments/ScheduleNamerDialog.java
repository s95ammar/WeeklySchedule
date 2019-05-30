package com.s95ammar.weeklyschedule.views.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.s95ammar.weeklyschedule.R;

public class ScheduleNamerDialog extends AppCompatDialogFragment {
    private EditText mEditTextName;
    private DialogListener mListener;
    private String action;

    public interface Action {
        int ADD = -1;
        String ADD_TITLE = "New Schedule";
        String RENAME_TITLE = "Rename Schedule";
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof DialogListener) {
            mListener = (DialogListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement SchedulesListManager");
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
        String name = getArguments().getString("name");
        final int i = getArguments().getInt("index");
        action = (i == Action.ADD ? Action.ADD_TITLE : Action.RENAME_TITLE);
        builder.setView(view)
                .setTitle(action)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name = mEditTextName.getText().toString();
                        if (!name.isEmpty()) {
                            mListener.applyName(name, i);
                        }
                    }
                });

        mEditTextName = view.findViewById(R.id.eText_add_schedule_name);
        if (name != null) {
            mEditTextName.setText(name);
            mEditTextName.selectAll();
        }
        return builder.create();
    }

    public interface DialogListener {
        void applyName(String name, int i);
    }
}
