package com.example.ool_mobile.ui.content;//package com.example.ool_mobile.ui.content;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.ool_mobile.R;
import com.example.ool_mobile.service.Dependencies;
import com.example.ool_mobile.ui.form.employee.EmployeeFormActivity;
import com.example.ool_mobile.ui.log_export.ExportLogActivity;
import com.example.ool_mobile.ui.util.WithDrawer;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import static java.util.Objects.requireNonNull;

public class ContentActivity extends AppCompatActivity implements WithDrawer {

    private AppBarConfiguration mAppBarConfiguration;

    private Toolbar toolbar;

    private DrawerLayout drawer;

    private NavigationView navigationView;

    private CurrentEmployeeViewModel viewModel;

    private ImageView employeeImageView;

    private TextView employeeRoleTextView;

    private TextView employeeNameTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);

        setupToolbar();

        setupAppbarConfiguration();

        setupNavigation();

        setupViews();

        setupViewModel();
    }

    private void setupViews() {

        View view = navigationView.getHeaderView(0);

        employeeImageView = view.findViewById(R.id.navHeader_employeeImageView);

        employeeRoleTextView = view.findViewById(R.id.navHeader_employeeRole);

        employeeNameTextView = view.findViewById(R.id.navHeader_employeeName);

        requireNonNull(employeeImageView);
        requireNonNull(employeeRoleTextView);
        requireNonNull(employeeNameTextView);

        employeeNameTextView.setOnClickListener(v -> {
            startActivity(new Intent(this, EmployeeFormActivity.class));
        });

        view.findViewById(R.id.navHeader_exportLogsTextView).setOnClickListener(v -> {
            startActivity(new Intent(this, ExportLogActivity.class));
        });

    }

    private void setupViewModel() {

        viewModel = new ViewModelProvider(
                this,
                CurrentEmployeeViewModel.create(
                        Dependencies.from(this).getEmployeeRepository()
                )
        ).get(CurrentEmployeeViewModel.class);

        viewModel.getCurrentEmployee().observe(this, employee -> {

            employeeNameTextView.setText(employee.name());

            String occupation = employee.occupation() == null ?
                    "unknown occupation" : employee.requireOccupation().name();

            employeeRoleTextView.setText(occupation);

            Picasso.get()
                    .load(viewModel.getEmployeeImageURL().toString())
                    .into(employeeImageView);

        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        setupToolbar();
    }

    private void setupToolbar() {
        toolbar = requireNonNull(findViewById(R.id.toolbar));

        setSupportActionBar(toolbar);
    }

    private void setupAppbarConfiguration() {
        drawer = findViewById(R.id.drawer_layout);

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home,
                R.id.navigation_calendar,
                R.id.navigation_packages,
                R.id.navigation_photoshoots,
                R.id.navigation_equipments,
                R.id.navigation_equipmentDetails,
                R.id.navigation_equipmentWithdraws,
                R.id.navigation_employees,
                R.id.navigation_orders
        ).setOpenableLayout(drawer).build();
    }

    private void setupNavigation() {

        navigationView = findViewById(R.id.nav_view);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        navController.addOnDestinationChangedListener((controller, destination, arguments) ->
                onDestinationChanged(destination)
        );


        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    private void onDestinationChanged(NavDestination destination) {
        if (destination.getId() == R.id.navigation_home) {
            toolbar.setVisibility(View.GONE);
        } else {
            toolbar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @SuppressLint("RtlHardcoded")
    @Override
    public void openDrawer() {
        drawer.openDrawer(Gravity.LEFT);
    }
}