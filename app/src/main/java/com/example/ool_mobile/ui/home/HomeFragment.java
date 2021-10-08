package com.example.ool_mobile.ui.home;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.ool_mobile.databinding.FragmentHomeBinding;
import com.example.ool_mobile.model.Photoshoot;
import com.example.ool_mobile.service.Dependencies;
import com.example.ool_mobile.ui.meta.WithDrawer;
import com.example.ool_mobile.ui.util.adapter.PendingPhotoshootAdapter;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    @NonNull
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.setViewModel(setupViewModel());

        binding.setLifecycleOwner(getViewLifecycleOwner());

        binding.setFragment(this);
    }

    private HomeViewModel setupViewModel() {

        HomeViewModel homeViewModel = new ViewModelProvider(
                this,
                HomeViewModel.create(
                        Dependencies.from(this).getEmployeeRepository(),
                        Dependencies.from(this).getPhotoshootApi()
                )
        ).get(HomeViewModel.class);

        homeViewModel.getPendingPhotoshoots().observe(
                getViewLifecycleOwner(),
                this::displayPhotoshootList
        );

        return homeViewModel;
    }

    private void displayPhotoshootList(List<Photoshoot> photoshoots) {

        Consumer<Photoshoot> onClick = this::startPhotoshootActivity;

        binding.homePendingSessions.setAdapter(new PendingPhotoshootAdapter(photoshoots, onClick));
    }

    public void openNavigation() {

        Activity activity = getActivity();

        if (activity instanceof WithDrawer) {
            ((WithDrawer) activity).openDrawer();
        }
    }

    private void startPhotoshootActivity(@NonNull Photoshoot photoshoot) {
        Objects.requireNonNull(photoshoot, "photoshoot is null");

        Snackbar.make(
                Objects.requireNonNull(requireView()),
                "Not implemented",
                Snackbar.LENGTH_LONG
        ).show();
    }
}