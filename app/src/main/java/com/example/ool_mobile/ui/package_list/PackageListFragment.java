package com.example.ool_mobile.ui.package_list;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.ool_mobile.R;
import com.example.ool_mobile.ui.util.Collapse;


public class PackageListFragment extends Fragment {

    ViewGroup filterButtonView;

    ImageView arrowImageView;

    ViewGroup collapsedView;


    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        View view = inflater.inflate(R.layout.fragment_package_list, container, false);

        collapsedView = view.findViewById(R.id.packageRow_collapseLayout);
        arrowImageView = view.findViewById(R.id.packageRow_expandImageView);
        filterButtonView = view.findViewById(R.id.packageList_filterView);

        setHasOptionsMenu(true);

        view.findViewById(R.id.packageList_addFab).setOnClickListener(v -> {

            NavController controller = Navigation.findNavController(getView());

            controller.navigate(R.id.action_nav_package_to_addPhotoShootActivity);

        });

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

        Collapse.on(collapsedView)
                .startCollapsed()
                .withToggle(arrowImageView);

        filterButtonView.setOnClickListener(this::showMenu);
    }

    private void showMenu(View view) {

        PopupMenu popup = new PopupMenu(requireContext(), view);

        Menu menu = popup.getMenu();
        menu.add(R.string.type);
        menu.add(R.string.price);
        menu.add(R.string.quantity);

        popup.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

}
