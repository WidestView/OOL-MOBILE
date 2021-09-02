package com.example.ool_mobile.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.ool_mobile.R;
import com.example.ool_mobile.ui.meta.WithDrawer;
import com.google.android.material.navigation.NavigationView;

import static java.util.Objects.requireNonNull;

public class ContentActivity extends AppCompatActivity implements WithDrawer {

    private AppBarConfiguration mAppBarConfiguration;

    private Toolbar toolbar;

    DrawerLayout drawer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_content);

        setupToolbar();

        setupAppbarConfiguration();

        setupNavigation();
    }

    private void setupToolbar() {
        toolbar = requireNonNull(findViewById(R.id.toolbar));

        setSupportActionBar(toolbar);
    }

    private void setupAppbarConfiguration() {
        drawer = findViewById(R.id.drawer_layout);

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                // todo: fill this with other root navigation ids
                R.id.navigation_home,
                R.id.navigation_calendar,
                R.id.navigation_packages,
                R.id.navigation_reports,
                R.id.navigation_photoshoots,
                R.id.navigation_equipments,
                R.id.navigation_equipmentDetails,
                R.id.navigation_equipmentBorrowings,
                R.id.navigation_employees,
                R.id.navigation_customers,
                R.id.navigation_orders
        ).setOpenableLayout(drawer).build();
    }

    private void setupNavigation() {
        NavigationView navigationView = findViewById(R.id.nav_view);

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