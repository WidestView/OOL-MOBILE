package com.example.ool_mobile.ui.calendar;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ool_mobile.R;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import java.util.Date;

public class CalendarFragment extends Fragment {

    private CompactCalendarView calendarView;

    private RecyclerView recyclerView;

    @NonNull
    public View onCreateView(
            @NonNull
            LayoutInflater inflater,
            @Nullable
            ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        calendarView = view.findViewById(R.id.calendarFragment_calendarView);
        recyclerView = view.findViewById(R.id.calendarFragment_recyclerView);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Event event = new Event(Color.GREEN, new Date().getTime(), "Some extra data");

        calendarView.addEvent(event);

        recyclerView.setFocusable(false);

        setupSampleRecyclerView();
    }

    private void setupSampleRecyclerView() {

        final double chanceOfPending = 0.5;

        recyclerView.setAdapter(new RecyclerView.Adapter<RecyclerView.ViewHolder>() {
            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                @LayoutRes
                int resource = Math.random() > chanceOfPending
                        ? R.layout.row_pending_photoshoot
                        : R.layout.row_due_pending_photoshoot;

                View view = LayoutInflater
                        .from(parent.getContext())
                        .inflate(resource, parent, false);

                return new RecyclerView.ViewHolder(view) {
                };
            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) { }

            @Override
            public int getItemCount() {
                return 10;
            }

        });

//        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(
                new LinearLayoutManager(requireContext())
        );
    }
}