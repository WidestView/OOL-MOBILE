package com.example.ool_mobile.ui.home;

import android.app.Activity;
import android.content.Intent;
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
import com.example.ool_mobile.ui.login.LoginActivity;
import com.example.ool_mobile.ui.login.LoginActivityArgs;
import com.example.ool_mobile.ui.util.DisposedFromLifecycle;
import com.example.ool_mobile.ui.util.WithDrawer;
import com.example.ool_mobile.ui.util.adapter.PendingPhotoshootAdapter;
import com.example.ool_mobile.ui.util.form.FormMode;
import com.example.ool_mobile.ui.util.form.FormModeValue;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import static com.example.ool_mobile.ui.util.SnackMessage.swalError;

public class HomeFragment extends Fragment implements HomeViewModel.Event.Visitor {

    private FragmentHomeBinding binding;

    private HomeViewModel viewModel;

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

        setupViewModel();

        setupOptions();

        binding.setLifecycleOwner(getViewLifecycleOwner());

        binding.setFragment(this);
    }


    private void setupViewModel() {

        viewModel = new ViewModelProvider(
                this,
                HomeViewModel.create(
                        Dependencies.from(this).getEmployeeRepository(),
                        Dependencies.from(this).getPhotoshootApi()
                )
        ).get(HomeViewModel.class);

        viewModel.getPendingPhotoshoots().observe(
                getViewLifecycleOwner(),
                this::displayPhotoshootList
        );

        binding.setViewModel(viewModel);
    }

    @Override
    public void onStart() {
        super.onStart();


        viewModel.getEvents()
                .to(DisposedFromLifecycle.of(this))
                .subscribe(event -> {
                    event.accept(this);
                });
    }

    private void setupOptions() {
        RecyclerView elementRecyclerView = binding.homeFragmentRecyclerView;

        List<OptionItem> items = getOptionItems();

        elementRecyclerView.setAdapter(new OptionAdapter(items));

        elementRecyclerView.setHasFixedSize(true);

        elementRecyclerView.setLayoutManager(new LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL,
                false)
        );
    }

    @NotNull
    private List<OptionItem> getOptionItems() {
        return Arrays.asList(
                makeItem(
                        R.string.label_equipments, R.string.description_equipment_icon, R.drawable.ic_camera_roll,
                        HomeFragmentDirections.actionHomeToEquipments()
                ),

                makeItem(
                        R.string.label_packages, R.string.description_package_icon, R.drawable.ic_package,
                        HomeFragmentDirections.actionHomeToPackages()
                ),

                makeItem(
                        R.string.label_employee, R.string.description_employee_icon, R.drawable.ic_person,
                        HomeFragmentDirections.actionHomeToEmployees()
                ),

                makeItem(
                        R.string.label_calendar, R.string.description_calendar_icon, R.drawable.ic_calendar,
                        HomeFragmentDirections.actionHomeToCalendar()
                ),

                makeItem(
                        R.string.label_photoshoot, R.string.description_photoshoot_icon, R.drawable.ic_camera,
                        HomeFragmentDirections.actionHomeToPhotoshoots()
                ),

                makeItem(
                        R.string.label_order, R.string.description_order_icon, R.drawable.ic_order,
                        HomeFragmentDirections.actionHomeToOrders()
                )
        );
    }

    private OptionItem makeItem(
            @StringRes int title,
            @StringRes int contentDescription,
            @DrawableRes int drawable,
            NavDirections navigationTarget) {

        return new OptionItemBuilder()
                .itemName(getString(title))
                .contentDescription(getString(contentDescription))
                .imageIcon(getDrawable(drawable))
                .clickListener(v -> {
                    NavHostFragment.findNavController(this)
                            .navigate(navigationTarget);
                })
                .build();
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

    @Override
    public void visitLogout() {
        LoginActivityArgs args = new LoginActivityArgs.Builder()
                .setIsError(false)
                .build();

        startActivity(
                new Intent(requireActivity(), LoginActivity.class)
                        .putExtras(args.toBundle())
        );

        requireActivity().finish();
    }

    @Override
    public void visitError() {
        swalError(this);
    }
}