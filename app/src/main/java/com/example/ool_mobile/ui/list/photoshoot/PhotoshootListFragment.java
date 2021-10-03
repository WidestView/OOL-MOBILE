package com.example.ool_mobile.ui.list.photoshoot;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ool_mobile.R;
import com.example.ool_mobile.model.Photoshoot;
import com.example.ool_mobile.service.Dependencies;
import com.example.ool_mobile.ui.util.adapter.AdapterParameters;

public class PhotoshootListFragment extends Fragment {

    private RecyclerView recyclerView;

    private PhotoshootRowAdapter adapter;

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

        recyclerView = requireView().findViewById(R.id.fragmentList_recyclerView);

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

        viewModel.getPhotoshootList().observe(getViewLifecycleOwner(), photoshoots -> {

                    adapter = new PhotoshootRowAdapter(
                            new AdapterParameters.Builder<Photoshoot>()
                                    .onEdit(this::onEdit)
                                    .items(photoshoots)
                                    .build()
                    );

                    recyclerView.setAdapter(adapter);
                }
        );

    }

    private void onEdit(@NonNull Photoshoot photoshoot) {

        Navigation.findNavController(requireView())
                .navigate(R.id.action_navigation_photoshoots_to_addPhotoShootActivity);

    }
}
