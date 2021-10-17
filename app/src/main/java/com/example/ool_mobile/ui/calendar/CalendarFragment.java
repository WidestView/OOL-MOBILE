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
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;

import com.example.ool_mobile.databinding.FragmentCalendarBinding;
import com.example.ool_mobile.model.Photoshoot;
import com.example.ool_mobile.service.Dependencies;
import com.example.ool_mobile.ui.util.adapter.PendingPhotoshootAdapter;
import com.example.ool_mobile.ui.util.form.FormMode;
import com.example.ool_mobile.ui.util.form.FormModeValue;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import java.util.List;
import java.util.Objects;

public class CalendarFragment extends Fragment {


    private FragmentCalendarBinding binding;

    @NonNull
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        binding = FragmentCalendarBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupViewModel();
    }



    private void setupViewModel() {

        CalendarViewModel viewModel = new ViewModelProvider(
                this,
                CalendarViewModel.create(
                        Dependencies.from(this).getPhotoshootApi()
                )
        ).get(CalendarViewModel.class);

        viewModel.getPhotoshootList()
                .observe(getViewLifecycleOwner(), this::displayPhotoshoots);
    }

    private void displayPhotoshoots(List<Photoshoot> photoshoots) {

        CompactCalendarView calendarView = binding.calendarFragmentCalendarView;

        // todo: select good color lmao
        int color = Color.parseColor("#056162");

        photoshoots.stream()
                .map(photoshoot -> new Event(color, photoshoot.startTime().getTime(), photoshoot))
                .forEach(calendarView::addEvent);

        binding.calendarFragmentRecyclerView
                .setAdapter(new PendingPhotoshootAdapter(photoshoots, this::startPhotoshootActivity));
    }

    private void startPhotoshootActivity(@NonNull Photoshoot photoshoot) {
        Objects.requireNonNull(photoshoot, "photoshoot is null");

        NavDirections action = CalendarFragmentDirections.actionCalendarToFormActivity(
                FormModeValue.of(FormMode.Update),
                photoshoot.resourceId().toString()
        );

        NavHostFragment.findNavController(this).navigate(action);
    }
}