package com.example.ool_mobile.ui.home;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ool_mobile.R;
import com.example.ool_mobile.model.Photoshoot;
import com.example.ool_mobile.service.Dependencies;
import com.example.ool_mobile.ui.meta.WithDrawer;
import com.example.ool_mobile.ui.util.adapter.PendingPhotoshootAdapter;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private TextView weekTextView;
    private TextView monthTextView;
    private TextView welcomeTextView;

    @NonNull
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.homeFragment_pendingSessionsRecyclerview);
        weekTextView = view.findViewById(R.id.homeFragment_weekTextView);
        monthTextView = view.findViewById(R.id.homeFragment_monthTextView);
        welcomeTextView = view.findViewById(R.id.homeFragment_welcomeTextView);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupRecyclerView();

        setupNavigationWelcomeMessage();

        setupViewModel();
    }

    private void setupNavigationWelcomeMessage() {
        welcomeTextView.setOnClickListener(v -> {
            Activity activity = getActivity();

            if (activity instanceof WithDrawer) {
                ((WithDrawer) activity).openDrawer();
            }
        });
    }

    private void setupViewModel() {

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

        homeViewModel.getEmployeeName().observe(getViewLifecycleOwner(), employeeName ->
                welcomeTextView.setText(
                        String.format(getString(R.string.format_welcome),
                                employeeName
                        )));

        homeViewModel.getDayOfMonth().observe(getViewLifecycleOwner(), dayOfMonth ->
                monthTextView.setText(dayOfMonth)
        );

        homeViewModel.getDayOfWeek().observe(getViewLifecycleOwner(), dayOfWeek ->
                weekTextView.setText(dayOfWeek)
        );

    }

    private void displayPhotoshootList(List<Photoshoot> photoshoots) {

        Consumer<Photoshoot> onClick = this::startPhotoshootActivity;

        recyclerView.setAdapter(new PendingPhotoshootAdapter(photoshoots, onClick));

    }

    private void setupRecyclerView() {
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(
                new LinearLayoutManager(requireContext())
        );

    }


    private void startPhotoshootActivity(@NonNull Photoshoot photoshoot) {
        Objects.requireNonNull(photoshoot, "photoshoot is null");
        // todo: start photoshoot activity with given photoshoot

        Snackbar.make(
                Objects.requireNonNull(requireView()),
                "Not implemented",
                Snackbar.LENGTH_LONG
        ).show();
    }


}