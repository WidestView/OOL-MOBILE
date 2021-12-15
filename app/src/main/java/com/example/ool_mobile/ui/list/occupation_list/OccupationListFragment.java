package com.example.ool_mobile.ui.list.occupation_list;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.ool_mobile.databinding.FragmentListOccupationBinding;
import com.example.ool_mobile.model.Occupation;
import com.example.ool_mobile.service.Dependencies;
import com.example.ool_mobile.ui.util.DisposedFromLifecycle;
import com.example.ool_mobile.ui.util.SnackMessage;
import com.example.ool_mobile.ui.util.adapter.AdapterParameters;

public class OccupationListFragment extends Fragment {

    FragmentListOccupationBinding binding;

    private OccupationViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        binding = FragmentListOccupationBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this, OccupationViewModel.create(Dependencies.from(this))
        ).get(OccupationViewModel.class);
    }

    @Override
    public void onStart() {
        super.onStart();

        viewModel.getEvents().to(DisposedFromLifecycle.of(this))
                .subscribe(error -> {
                    SnackMessage.swalError(this);
                });

        viewModel.getOccupations().observe(this, occupations -> {
            binding.setAdapter(new OccupationRowAdapter(
                    new AdapterParameters.Builder<Occupation>()
                            .items(occupations)
                            .build()
            ));
        });
    }
}
