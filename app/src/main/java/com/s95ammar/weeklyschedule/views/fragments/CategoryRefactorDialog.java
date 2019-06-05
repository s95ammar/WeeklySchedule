package com.s95ammar.weeklyschedule.views.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.s95ammar.weeklyschedule.R;
import com.s95ammar.weeklyschedule.models.Category;
import com.s95ammar.weeklyschedule.models.Event;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.TreeSet;

import static com.s95ammar.weeklyschedule.views.fragments.CategoriesListFragment.CategoriesListManager.KEY_CATEGORY;
import static com.s95ammar.weeklyschedule.views.fragments.CategoriesListFragment.CategoriesListManager.KEY_INDEX;

public class CategoryRefactorDialog extends AppCompatDialogFragment implements View.OnClickListener {
    private static final String TAG = "CategoryRefactorDialog";
    private TextView mTextViewPreview;
    private EditText mEditTextName;
    private View mViewFillColor;
    private View mViewTextColor;
    private CategoryRefactor mListener;
    private static final String ADD_TITLE = "New Category";
    private static final String EDIT_TITLE = "Edit Category";

    public interface Action {
        int ADD = -1;
        // REMAKE = any value other than -1 (value will be used as index)
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof CategoryRefactor) {
            mListener = (CategoryRefactor) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement CategoryRefactor");
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
        View view = inflater.inflate(R.layout.dialog_add_category, null);
        Serializable obj = getArguments().getSerializable(KEY_CATEGORY);
        initializeViews(view);
        String title;
        if (obj instanceof Category) {
            Category category = (Category) obj;
            setViews(category);
            title = EDIT_TITLE;
        } else {
            title = ADD_TITLE;
        }
        final int i = getArguments().getInt(KEY_INDEX);
        builder.setView(view)
                .setTitle(title)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Ok", onOkListener(i));

        return builder.create();
    }

    private void initializeViews(View view) {
        mTextViewPreview = view.findViewById(R.id.tView_add_category_preview_value);
        mViewFillColor = view.findViewById(R.id.view_add_category_fill_color);
        mViewTextColor = view.findViewById(R.id.view_add_category_text_color);
        mEditTextName = view.findViewById(R.id.eText_add_category_name);
        mTextViewPreview.setText(R.string.category_preview_value);
        mTextViewPreview.setBackgroundColor(((ColorDrawable) mViewFillColor.getBackground()).getColor());
        mViewFillColor.setOnClickListener(this);
        mViewTextColor.setOnClickListener(this);
    }

    private void setViews(Category category) {
//        mTextViewPreview.setText(category.getName());
        mTextViewPreview.setTextColor(category.getTextColor());
        mTextViewPreview.setBackgroundColor(category.getFillColor());
        mEditTextName.setText(category.getName());
        mEditTextName.setSelection(mEditTextName.getText().length());
        mViewFillColor.setBackgroundColor(category.getFillColor());
        mViewTextColor.setBackgroundColor(category.getTextColor());
    }

    private DialogInterface.OnClickListener onOkListener(final int i) {
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = mEditTextName.getText().toString();
                if (!name.isEmpty()) {
                    Category category = new Category(name,
                            ((ColorDrawable) mViewFillColor.getBackground()).getColor(),
                            ((ColorDrawable) mViewTextColor.getBackground()).getColor(),
                            new TreeSet<>(new Event.EventNameComparator()));
                    mListener.applyCategory(category, i);
                } else {
                    Toast.makeText(getActivity(), R.string.category_empty_name, Toast.LENGTH_SHORT).show();
                }
            }
        };
        return listener;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.view_add_category_fill_color:
                mListener.openColorPicker(this, R.id.view_add_category_fill_color, ((ColorDrawable) v.getBackground()).getColor());
                break;
            case R.id.view_add_category_text_color:
                mListener.openColorPicker(this, R.id.view_add_category_text_color, ((ColorDrawable) v.getBackground()).getColor());
                break;
        }
    }

    public void receiveColor() {
        Bundle bundle = getArguments();
        int viewId = bundle.getInt(CategoryRefactor.KEY_ID);
        int color = bundle.getInt(CategoryRefactor.KEY_COLOR);
        switch (viewId) {
            case R.id.view_add_category_fill_color:
                mViewFillColor.setBackgroundColor(color);
                mTextViewPreview.setBackgroundColor(color);
                break;
            case R.id.view_add_category_text_color:
                mViewTextColor.setBackgroundColor(color);
                mTextViewPreview.setTextColor(color);
                break;
        }
    }

    public interface CategoryRefactor {
        void applyCategory(Category category, int i);
        void openColorPicker(CategoryRefactorDialog dialog, int viewId, int defaultColor);
        String KEY_ID = "id";
        String KEY_COLOR = "color";
    }
}
