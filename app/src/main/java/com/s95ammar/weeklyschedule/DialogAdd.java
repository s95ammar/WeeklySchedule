package com.s95ammar.weeklyschedule;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

public class DialogAdd extends AppCompatDialogFragment {
    private EditText mEditTextName;
    private DialogListener mListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof DialogListener) {
            mListener = (DialogListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ScheduleCreator");
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
        builder.setView(view)
                .setTitle("New Schedule")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name = mEditTextName.getText().toString();
                        if (!name.isEmpty()) {
                            mListener.applyName(name);
                        }
                    }
                });

        mEditTextName = view.findViewById(R.id.eText_add_schedule_name);
        return builder.create();
    }

    public interface DialogListener {
        void applyName(String name);
    }
}
