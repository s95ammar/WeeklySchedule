package com.s95ammar.weeklyschedule.views.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.s95ammar.weeklyschedule.R;
import com.s95ammar.weeklyschedule.models.Event;

import java.io.Serializable;

import static com.s95ammar.weeklyschedule.views.fragments.ScheduleViewerFragment.ScheduleEditor.KEY_EVENT;

public class EventRefactorDialog extends AppCompatDialogFragment {
    private EditText mEditTextName;
    private TextView mTextViewStart;
    private TextView mTextViewEnd;
    private Spinner mSpinnerDay;
    private EventCreatorListener mListener;
    private static final String ADD_TITLE = "New Event";
    private static final String EDIT_TITLE = "Edit Event";

    public interface Action {
        int ADD = 0;
        int EDIT = 1;
    }

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
        Serializable obj = getArguments().getSerializable(KEY_EVENT); // TODO: create constant and pass arguments
        builder.setView(view)
                .setTitle(obj instanceof Event /*&& (!= null)*/ ? EDIT_TITLE : ADD_TITLE)
                .setNegativeButton("Cancel", null)
                //TODO: add delete option if obj!= null
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name = mEditTextName.getText().toString();
                        if (!name.isEmpty()) {
                            mListener.createEvent(/*mSpinnerDay.getSelectedItem().toString()*/null, mEditTextName.getText().toString(), mTextViewStart.getText().toString(), mTextViewEnd.getText().toString());
                        }
                    }
                });

        mEditTextName = view.findViewById(R.id.eText_event_name);
        mTextViewStart = view.findViewById(R.id.tView_event_start_value);
        mTextViewEnd = view.findViewById(R.id.tView_event_end_value);
//        mSpinnerDay = view.findViewById(R.id.eText_event_name);
        if (obj instanceof Event) {
            Event event = (Event) obj;
            mEditTextName.setText(event.getName());
            mTextViewStart.setText(event.getStartTime().toString());
            mTextViewEnd.setText(event.getStartTime().toString());
//            mSpinnerDay. TODO: select day
        }
        return builder.create();
    }

    public interface EventCreatorListener {
        void createEvent(String day, String name, String startTime, String endTime);
//        void deleteEvent(Event event);
    }
}
