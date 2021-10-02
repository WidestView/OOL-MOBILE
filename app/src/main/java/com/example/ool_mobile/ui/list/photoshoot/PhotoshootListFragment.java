package com.example.ool_mobile.ui.list.photoshoot;

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
import com.example.ool_mobile.service.Dependencies;
import com.example.ool_mobile.ui.calendar.PhotoshootListViewModel;
import com.example.ool_mobile.ui.util.adapter.PhotoshootRowAdapter;

public class PhotoshootListFragment extends Fragment {

    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_list_photoshoot, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupViews();

        setupViewModel();
    }

    private void setupViews() {

        recyclerView = getView().findViewById(R.id.fragmentList_recyclerView);

        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
    }

    private void setupViewModel() {

        PhotoshootListViewModel viewModel = new ViewModelProvider(
                this,
                PhotoshootListViewModel.create(
                        Dependencies.from(this).getPhotoshootApi()
                )
        ).get(PhotoshootListViewModel.class);

        viewModel.getPhotoshootList().observe(getViewLifecycleOwner(), photoshoots ->
                recyclerView.setAdapter(new PhotoshootRowAdapter(photoshoots))
        );

    }
}
