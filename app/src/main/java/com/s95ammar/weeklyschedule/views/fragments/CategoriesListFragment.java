package com.s95ammar.weeklyschedule.views.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuPopupHelper;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.s95ammar.weeklyschedule.R;
import com.s95ammar.weeklyschedule.models.CategoriesList;
import com.s95ammar.weeklyschedule.models.Category;
import com.s95ammar.weeklyschedule.models.ScheduleItem;
import com.s95ammar.weeklyschedule.models.SchedulesList;
import com.s95ammar.weeklyschedule.views.recyclerView.CategoryAdapter;

public class CategoriesListFragment extends Fragment implements CategoryAdapter.OnItemClickListener{
    private static final String TAG = "SchedulesListFragment";
    private CategoriesListManager mListener;
    private RecyclerView mRecyclerView;
    private CategoryAdapter mAdapter;
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
        setUpActionButtonListener();
        buildRecyclerView();
        refreshLayout();
        getActivity().setTitle(R.string.title_categories);
    }



    private void refreshLayout() {
        getView().findViewById(R.id.textView_no_categories).setVisibility(CategoriesList.getInstance().isEmpty() ? View.VISIBLE : View.GONE);
        getView().findViewById(R.id.recyclerView_categories).setBackgroundColor(CategoriesList.getInstance().isEmpty() ? Color.WHITE : getResources().getColor(R.color.colorLightGray));
    }

    private void buildRecyclerView() {
        mRecyclerView = getView().findViewById(R.id.recyclerView_categories);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new CategoryAdapter(CategoriesList.getInstance());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickedListener(this);

    }


    @Override
    public void onItemClicked(int i) {
        mListener.showCategoryRefactorDialog(CategoriesList.getInstance().get(i), i);
        Log.d(TAG, "onItemClicked: " + CategoriesList.getInstance().get(i).getName());
    }


    @Override
    public void onMoreClicked(final int i, Button buttonMore) {
        PopupMenu popupMenu = new PopupMenu(getActivity(), buttonMore);
        popupMenu.inflate(R.menu.categories_more_menu);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.categories_more_edit:
                        mListener.showCategoryRefactorDialog(CategoriesList.getInstance().get(i), i); // TODO: onOk -> applyCategory(name, tColor, fColor)
                        break;
                    case R.id.categories_more_delete:
                        deleteCategory(i);
                        break;
                }
                return true;
            }
        });
        MenuPopupHelper menuHelper = new MenuPopupHelper(getActivity(), (MenuBuilder) popupMenu.getMenu(), buttonMore);
        menuHelper.setForceShowIcon(true);
        menuHelper.show();
    }

    public void addCategory(Category category) {
        CategoriesList.getInstance().add(0, category);
        mAdapter.notifyItemInserted(0);
        mLayoutManager.scrollToPosition(0);
        refreshLayout();
        Log.d(TAG, "addCategory: " + CategoriesList.getInstance());
    }

    public void editCategory(Category category, int i) {
        CategoriesList.getInstance().set(i, category);
        mAdapter.notifyItemChanged(i);
        Log.d(TAG, "editCategory: " + CategoriesList.getInstance());
    }

    public void deleteCategory(int i) {
        CategoriesList.getInstance().remove(i);
        mAdapter.notifyItemRemoved(i);
        refreshLayout();
        Log.d(TAG, "deleteCategory: " + CategoriesList.getInstance());
    }


    private void setUpActionButtonListener() {
        FloatingActionButton fab = getView().findViewById(R.id.button_add_category);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.showCategoryRefactorDialog(null, ScheduleNamerDialog.Action.ADD);  // onOk -> addCategory(name)
            }
        });

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
