package com.root.mssm.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.root.mssm.R;
import com.root.mssm.databinding.ActivityMainBinding;
import com.root.mssm.service.SessionManage;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    Fragment loginFragment;
    NavController navController;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    SessionManage sessionManage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//        setSupportActionBar(binding.content.toolbar);
        sessionManage = new SessionManage(this);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, binding.container, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        binding.container.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ActivityAction();

        SetAcivityData();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        NavHostFragment navHostFragment = (NavHostFragment)getSupportFragmentManager().getPrimaryNavigationFragment();
        FragmentManager fragmentManager = navHostFragment.getChildFragmentManager();
        loginFragment = fragmentManager.getPrimaryNavigationFragment();
        loginFragment.onActivityResult(requestCode, resultCode, data);
    }

    void ActivityAction(){
        binding.content.menu.setOnClickListener(v -> openDrawer());
        if(binding!=null){
            binding.content.login.setOnClickListener(v -> {navController.navigate(R.id.cartFragment);});
            binding.content.search.setOnClickListener(v -> navController.navigate(R.id.searchFragment));
        }
    }

    private void openDrawer() {
        SetAcivityData();
        if (binding.container.isDrawerVisible(GravityCompat.START)) {
            binding.container.closeDrawer(GravityCompat.START);
        } else binding.container.openDrawer(GravityCompat.START);
    }

    void SetAcivityData(){
        View view = binding.navView.getHeaderView(0);
        TextView name= view.findViewById(R.id.name);
        TextView mobile= view.findViewById(R.id.mobile);

        if(sessionManage.isLoggedIn()){
            name.setText(sessionManage.getUserDetails().get(SessionManage.NAME));
            mobile.setText(sessionManage.getUserDetails().get(SessionManage.MOBILE));
        }else {
            name.setText("Welcome");
            mobile.setVisibility(View.GONE);
        }
    }

}