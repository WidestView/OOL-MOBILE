package com.example.ool_mobile.ui.home;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ool_mobile.R;
import com.example.ool_mobile.horizontal_package.ElementAdapter;
import com.example.ool_mobile.horizontal_package.ElementItem;
import com.example.ool_mobile.ui.meta.WithDrawer;

import java.util.Arrays;
import java.util.List;

public class HomeFragment extends Fragment {


    private RecyclerView recyclerView;
    private TextView weekTextView;
    private TextView monthTextView;
    private TextView welcomeTextView;

    private RecyclerView elementRecyclerView;

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

        elementRecyclerView = view.findViewById(R.id.homeFragment_recyclerView);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupDates();

        setupWelcomeMessage();

        setupSampleRecyclerView();

        setupElements();
    }

    private void setupElements()
    {

        List<ElementItem> items = Arrays.asList(
                new ElementItem("Equipamento", getDrawable(R.drawable.ic_camera_roll)),
                new ElementItem("Pacote", getDrawable(R.drawable.ic_package))
        );

        elementRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));

        elementRecyclerView.setAdapter(new ElementAdapter(items));

        elementRecyclerView.setHasFixedSize(true);

    }
    private Drawable getDrawable(int id)
    {
        return ContextCompat.getDrawable(requireActivity(), id);
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

        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(
                new LinearLayoutManager(requireContext())
        );
    }

    private void setupWelcomeMessage() {
        welcomeTextView.setOnClickListener(v -> {
            Activity activity = getActivity();

            if (activity instanceof WithDrawer) {
                ((WithDrawer) activity).openDrawer();
            }
        });
    }

    private void setupDates() {
        HomeViewModel homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        monthTextView.setText(homeViewModel.getDayOfMonth());
        weekTextView.setText(homeViewModel.getDayOfWeek());
    }
}