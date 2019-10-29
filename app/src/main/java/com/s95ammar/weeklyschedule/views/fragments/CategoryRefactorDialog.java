package com.s95ammar.weeklyschedule.views.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.s95ammar.weeklyschedule.R;
import com.s95ammar.weeklyschedule.models.Category;

import java.io.Serializable;
import java.util.HashSet;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.s95ammar.weeklyschedule.views.fragments.CategoriesListFragment.CategoriesListManager.KEY_CATEGORY;
import static com.s95ammar.weeklyschedule.views.fragments.CategoriesListFragment.CategoriesListManager.KEY_INDEX;

public class CategoryRefactorDialog extends AppCompatDialogFragment {
    private static final String TAG = "CategoryRefactorDialog";
    protected @BindView(R.id.text_add_category_preview_value) TextView mTextViewPreview;
    protected @BindView(R.id.eText_add_category_name) EditText mEditTextName;
    protected @BindView(R.id.view_add_category_fill_color) View mViewFillColor;
    protected @BindView(R.id.view_add_category_text_color) View mViewTextColor;
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
        ButterKnife.bind(this, view);
        String title;
        Category category = null;
        if (obj instanceof Category) {
            category = (Category) obj;
            title = EDIT_TITLE;
        } else {
            title = ADD_TITLE;
        }
        setViews(category);
        final int i = getArguments().getInt(KEY_INDEX);
        return builder.setView(view)
                .setTitle(title)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Ok", onOkListener(i))
                .create();
    }

    private void setViews(@Nullable Category category) {
        mTextViewPreview.setText(R.string.category_preview_value);
        if (category == null) {
            mTextViewPreview.setBackgroundColor(((ColorDrawable) mViewFillColor.getBackground()).getColor());
        } else {
            mTextViewPreview.setTextColor(category.getTextColor());
            mTextViewPreview.setBackgroundColor(category.getFillColor());
            mEditTextName.setText(category.getName());
            mEditTextName.setSelection(mEditTextName.getText().length());
            mViewFillColor.setBackgroundColor(category.getFillColor());
            mViewTextColor.setBackgroundColor(category.getTextColor());
        }
    }

    private DialogInterface.OnClickListener onOkListener(final int i) {
        return (dialog, which) -> {
            String name = mEditTextName.getText().toString();
            if (!name.isEmpty()) {
                Category category = new Category(name,
                        ((ColorDrawable) mViewFillColor.getBackground()).getColor(),
                        ((ColorDrawable) mViewTextColor.getBackground()).getColor(),
                        new HashSet<>());
                mListener.applyCategory(category, i);
            } else {
                Toast.makeText(getActivity(), R.string.category_empty_name, Toast.LENGTH_SHORT).show();
            }
        };
    }

    @OnClick({R.id.view_add_category_fill_color, R.id.view_add_category_text_color})
    public void openColorPicker(View v) {
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
