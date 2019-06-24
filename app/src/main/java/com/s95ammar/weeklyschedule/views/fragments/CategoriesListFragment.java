package com.s95ammar.weeklyschedule.views.fragments;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.s95ammar.weeklyschedule.R;
import com.s95ammar.weeklyschedule.models.CategoriesList;
import com.s95ammar.weeklyschedule.models.Category;
import com.s95ammar.weeklyschedule.adapters.CategoryRecViewAdapter;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class CategoriesListFragment extends Fragment implements CategoryRecViewAdapter.OnItemClickListener {
    private static final String TAG = "SchedulesListFragment";
    private CategoriesListManager mListener;
    private RecyclerView mRecyclerView;
    private CategoryRecViewAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public CategoriesListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_categories_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        buildRecyclerView();
        refreshLayout();
        getActivity().setTitle(R.string.title_categories);
    }

    private void refreshLayout() {
        getView().findViewById(R.id.textView_no_categories).setVisibility(CategoriesList.getInstance().isEmpty() ? View.VISIBLE : View.GONE);
    }

    private void buildRecyclerView() {
        mRecyclerView = getView().findViewById(R.id.recyclerView_categories);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new CategoryRecViewAdapter(CategoriesList.getInstance());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickedListener(this);
    }

    @Override
    public void onItemClicked(int i) {
        mListener.showCategoryRefactorDialog(CategoriesList.getInstance().get(i), i);
    }

    @Override
    public void onMoreClicked(final int i, Button buttonMore) {
        PopupMenu popupMenu = new PopupMenu(getActivity(), buttonMore);
        popupMenu.inflate(R.menu.categories_more_menu);
        popupMenu.setOnMenuItemClickListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.categories_more_edit:
                    mListener.showCategoryRefactorDialog(CategoriesList.getInstance().get(i), i); // onOk -> applyCategory(Category category)
                    break;
                case R.id.categories_more_delete:
                    deleteCategory(i);
                    break;
            }
            return true;
        });
        MenuPopupHelper menuHelper = new MenuPopupHelper(getActivity(), (MenuBuilder) popupMenu.getMenu(), buttonMore);
        menuHelper.setForceShowIcon(true);
//        TODO: add icons
        menuHelper.show();
    }

    public void addCategory(Category category) {
        CategoriesList.getInstance().add(0, category);
        mAdapter.notifyItemInserted(0);
        mLayoutManager.scrollToPosition(0);
        refreshLayout();
    }

    public void editCategory(Category category, int i) {
        CategoriesList.getInstance().set(i, category);
        mAdapter.notifyItemChanged(i);
    }

    public void deleteCategory(int i) {
        CategoriesList.getInstance().remove(i);
        mAdapter.notifyItemRemoved(i);
        refreshLayout();
    }

    @OnClick(R.id.button_add_category)
    protected void showCategoryRefactorDialog(){
        mListener.showCategoryRefactorDialog(null, CategoryRefactorDialog.Action.ADD);  // onOk -> addCategory(name)
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof CategoriesListManager) {
            mListener = (CategoriesListManager) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement CategoriesListManager");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface CategoriesListManager {
        void showCategoryRefactorDialog(Category category, int i);
        String KEY_CATEGORY = "category";
        String KEY_INDEX = "index";
    }

}
