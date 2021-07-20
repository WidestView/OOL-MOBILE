package com.example.ool_mobile.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.ool_mobile.R;

public class HomeFragment extends Fragment {

    private TextView textView;

    @NonNull
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        textView = root.findViewById(R.id.text_home);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        HomeViewModel homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
    }
}