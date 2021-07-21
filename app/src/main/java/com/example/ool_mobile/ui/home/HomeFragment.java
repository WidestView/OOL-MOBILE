package com.example.ool_mobile.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ool_mobile.R;
import com.example.ool_mobile.databinding.FragmentHomeBinding;

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

        HomeViewModel homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        binding.homeFragmentMonthTextView.setText(homeViewModel.getDayOfMonth());
        binding.homeFragmentWeekTextView.setText(homeViewModel.getDayOfWeek());


        binding.homeFragmentPendingSessionsRecyclerview.setAdapter(new RecyclerView.Adapter<RecyclerView.ViewHolder>() {
            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                @LayoutRes
                int resource = Math.random() < 0.8
                        ? R.layout.pending_photoshoot_row 
                        : R.layout.pending_photoshoot_row_due;

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
                return 3;
            }

        });

        binding.homeFragmentPendingSessionsRecyclerview.setHasFixedSize(true);

        binding.homeFragmentPendingSessionsRecyclerview.setLayoutManager(
                new LinearLayoutManager(requireContext())
        );

    }
}