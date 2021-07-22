package com.example.ool_mobile.ui.package_list;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ool_mobile.R;
import com.example.ool_mobile.ui.util.Collapse;


public class PackageListFragment extends Fragment {

    ViewGroup expandedView;
    ImageView arrowImageView;

    private boolean expanded = true;


    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        View view = inflater.inflate(R.layout.package_list_fragment, container, false);

        expandedView = view.findViewById(R.id.packageList_expandedLayout);
        arrowImageView = view.findViewById(R.id.package_expandImageView);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.search_bar, menu);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Collapse.on(expandedView)
                .startCollapsed()
                .withToggle(arrowImageView);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

}
