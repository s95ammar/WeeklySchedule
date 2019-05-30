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
import android.widget.Spinner;
import android.widget.TextView;

import com.s95ammar.weeklyschedule.R;
import com.s95ammar.weeklyschedule.models.Event;

public class EventRefactorDialog extends AppCompatDialogFragment {
    private static final String TITLE = "Add event";
    private EditText mEditTextName;
    private TextView mTextViewStart;
    private TextView mTextViewEnd;
    private Spinner mSpinnerDay;
    private EventCreatorListener mListener;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof EventCreatorListener) {
            mListener = (EventCreatorListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement EventCreatorListener");
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
        View view = inflater.inflate(R.layout.dialog_add_event, null);
        Event event = null; // TODO: create constant and pass arguments
        builder.setView(view)
                .setTitle(TITLE)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name = mEditTextName.getText().toString();
                        if (!name.isEmpty()) {
                            mListener.createEvent(mSpinnerDay.getSelectedItem().toString(), mTextViewStart.getText().toString(), mTextViewEnd.getText().toString());
                        }
                    }
                });

        mEditTextName = view.findViewById(R.id.eText_add_schedule_name);
        if (event != null) {
            mEditTextName.setText(event.getName());
            mTextViewStart.setText(event.getStartTime().toString());
            mTextViewEnd.setText(event.getStartTime().toString());
//            mSpinnerDay. TODO: select day
        }
        return builder.create();
    }

    public interface EventCreatorListener {
        void createEvent(String day, String startTime, String endTime);
    }
}
