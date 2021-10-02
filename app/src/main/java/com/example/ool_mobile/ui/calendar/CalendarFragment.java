package com.example.ool_mobile.ui.calendar;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ool_mobile.R;
import com.example.ool_mobile.model.Photoshoot;
import com.example.ool_mobile.service.Dependencies;
import com.example.ool_mobile.ui.util.adapter.PhotoshootAdapter;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;
import java.util.Objects;

public class CalendarFragment extends Fragment {

    private CompactCalendarView calendarView;

    private RecyclerView recyclerView;

    private PhotoshootListViewModel viewModel;

    @NonNull
    public View onCreateView(
            @NonNull
                    LayoutInflater inflater,
            @Nullable
                    ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.fragment_calendar, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupViews(view);

        setupViewModel();
    }


    private void setupViews(@NonNull View view) {
        calendarView = view.findViewById(R.id.calendarFragment_calendarView);

        calendarView.setEventIndicatorStyle(CompactCalendarView.FILL_LARGE_INDICATOR);
        recyclerView = view.findViewById(R.id.calendarFragment_recyclerView);

        recyclerView.setFocusable(false);

        recyclerView.setHasFixedSize(false);

        recyclerView.setLayoutManager(
                new LinearLayoutManager(requireContext())
        );
    }

    private void setupViewModel() {

        viewModel = new ViewModelProvider(
                this,
                PhotoshootListViewModel.create(
                        Dependencies.from(this).getPhotoshootApi()
                )
        ).get(PhotoshootListViewModel.class);

        viewModel.getPhotoshootList()
                .observe(getViewLifecycleOwner(), this::displayPhotoshoots);
    }

    private void displayPhotoshoots(List<Photoshoot> photoshoots) {

        // todo: select good color lmao
        int color = Color.parseColor("#056162");

        photoshoots.stream()
                .map(photoshoot -> new Event(color, photoshoot.startTime().getTime(), photoshoot))
                .forEach(event -> calendarView.addEvent(event));

        recyclerView.setAdapter(new PhotoshootAdapter(photoshoots, this::startPhotoshootActivity));

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