package com.example.ool_mobile.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ool_mobile.R;

public class HomeFragment extends Fragment {


    private RecyclerView recyclerView;
    private TextView weekTextView;
    private TextView monthTextView;

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

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        HomeViewModel homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        monthTextView.setText(homeViewModel.getDayOfMonth());
        weekTextView.setText(homeViewModel.getDayOfWeek());


        recyclerView.setAdapter(new RecyclerView.Adapter<RecyclerView.ViewHolder>() {
            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                @LayoutRes
                int resource = Math.random() > 0.5
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

        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(
                new LinearLayoutManager(requireContext())
        );

    }
}