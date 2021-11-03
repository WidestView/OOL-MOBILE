package com.example.ool_mobile.ui.list.photoshoot;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.example.ool_mobile.R;
import com.example.ool_mobile.databinding.FragmentListPhotoshootBinding;
import com.example.ool_mobile.model.Photoshoot;
import com.example.ool_mobile.service.Dependencies;
import com.example.ool_mobile.ui.util.DisposedFromLifecycle;
import com.example.ool_mobile.ui.util.adapter.AdapterParameters;
import com.example.ool_mobile.ui.util.form.FormMode;
import com.example.ool_mobile.ui.util.form.FormModeValue;

import java.util.UUID;

import static com.example.ool_mobile.ui.util.SnackMessage.snack;

public class PhotoshootListFragment extends Fragment {

    private FragmentListPhotoshootBinding binding;

    private PhotoshootRowAdapter adapter;

    private PhotoshootListViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        binding = FragmentListPhotoshootBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.setFragment(this);

        setupViewModel();
    }

    private void setupViewModel() {

        viewModel = new ViewModelProvider(
                this,
                PhotoshootListViewModel.create(
                        Dependencies.from(this).getPhotoshootApi()
                )
        ).get(PhotoshootListViewModel.class);
    }

    public void onEdit(@NonNull Photoshoot photoshoot) {
        startFormActivity(FormMode.Update, photoshoot.resourceId());
    }

    public void onAddButtonClick() {
        startFormActivity(FormMode.Add, null);
    }

    private void startFormActivity(FormMode mode, @Nullable UUID id) {

        NavDirections action = PhotoshootListFragmentDirections.actionPhotoshootListToPhotoshootForm(
                FormModeValue.of(mode),
                id == null ? null : id.toString()
        );

        Navigation.findNavController(requireView()).navigate(action);
    }

    @Override
    public void onStart() {
        super.onStart();

        viewModel.getCurrentPhotoshootList().observe(getViewLifecycleOwner(), photoshoots -> {

                    adapter = new PhotoshootRowAdapter(
                            new AdapterParameters.Builder<Photoshoot>()
                                    .onEdit(this::onEdit)
                                    .items(photoshoots)
                                    .build()
                    );

                    binding.fragmentListRecyclerView.setAdapter(adapter);
                }
        );

        viewModel.getEvents()
                .to(DisposedFromLifecycle.of(this))
                .subscribe(error -> {
                    snack(this, R.string.error_operation_failed);
                });
    }
}
