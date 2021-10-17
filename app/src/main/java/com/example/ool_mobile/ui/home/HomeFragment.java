package com.example.ool_mobile.ui.home;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ool_mobile.R;
import com.example.ool_mobile.databinding.FragmentHomeBinding;
import com.example.ool_mobile.model.Photoshoot;
import com.example.ool_mobile.service.Dependencies;
import com.example.ool_mobile.ui.meta.WithDrawer;
import com.example.ool_mobile.ui.util.adapter.PendingPhotoshootAdapter;
import com.example.ool_mobile.ui.util.form.FormMode;
import com.example.ool_mobile.ui.util.form.FormModeValue;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

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

        binding.setViewModel(setupViewModel());

        binding.setLifecycleOwner(getViewLifecycleOwner());

        binding.setFragment(this);

        setupOptions();
    }

    private HomeViewModel setupViewModel() {


        HomeViewModel homeViewModel = new ViewModelProvider(
                this,
                HomeViewModel.create(
                        Dependencies.from(this).getEmployeeRepository(),
                        Dependencies.from(this).getPhotoshootApi()
                )
        ).get(HomeViewModel.class);

        homeViewModel.getPendingPhotoshoots().observe(
                getViewLifecycleOwner(),
                this::displayPhotoshootList
        );

        return homeViewModel;
    }

    private void setupOptions() {
        RecyclerView elementRecyclerView = binding.homeFragmentRecyclerView;

        List<OptionItem> items = Arrays.asList(
                getItem(R.string.label_equipments, R.drawable.ic_camera_roll),
                getItem(R.string.label_packages, R.drawable.ic_package)
        );

        elementRecyclerView.setAdapter(new OptionAdapter(items));

        elementRecyclerView.setHasFixedSize(true);

        elementRecyclerView.setLayoutManager(new LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL,
                false)
        );
    }

    private OptionItem getItem(@StringRes int string, @DrawableRes int drawable) {
        return new OptionItem(getString(string), getDrawable(drawable));
    }

    private Drawable getDrawable(int id) {
        return ContextCompat.getDrawable(requireActivity(), id);
    }

    private void displayPhotoshootList(List<Photoshoot> photoshoots) {

        Consumer<Photoshoot> onClick = this::startPhotoshootActivity;

        binding.homePendingSessions.setAdapter(new PendingPhotoshootAdapter(photoshoots, onClick));
    }

    public void openNavigation() {

        Activity activity = getActivity();

        if (activity instanceof WithDrawer) {
            ((WithDrawer) activity).openDrawer();
        }
    }

    private void startPhotoshootActivity(@NonNull Photoshoot photoshoot) {

        Objects.requireNonNull(photoshoot, "photoshoot is null");

        NavDirections action = HomeFragmentDirections.actionHomeToPhotoshootFormActivity(
                FormModeValue.of(FormMode.Update),
                photoshoot.resourceId().toString()
        );

        NavHostFragment.findNavController(this).navigate(action);
    }
}