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
                R.id.nav_home,
                R.id.nav_calendar,
                R.id.nav_package,
                R.id.nav_report,
                R.id.nav_photoshoots,
                R.id.nav_equipment,
                R.id.nav_equipment_details,
                R.id.nav_equipment_borrowing,
                R.id.nav_employee,
                R.id.nav_customer,
                R.id.nav_order
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
        if (destination.getId() == R.id.nav_home) {
            toolbar.setVisibility(View.GONE);
        }
        else {
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