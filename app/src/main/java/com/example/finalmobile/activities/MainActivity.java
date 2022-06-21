package com.example.finalmobile.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.example.finalmobile.R;
import com.example.finalmobile.listeners.OnUpdateViewRadioPlayListener;
import com.example.finalmobile.utils.PlayerRadio;

public class MainActivity extends AppCompatActivity {

    private NavController navController;
    private static BottomNavigationView bottomNavigationView;
    public static int SET_PORTRAIT_REQUEST_CODE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);

        navController = navHostFragment.getNavController();
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        PlayerRadio.setContext(MainActivity.this);
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController navController, @NonNull NavDestination navDestination, @Nullable Bundle bundle) {
                if(navDestination.getId() != R.id.baseRadioFragment){
                    PlayerRadio playerRadio = PlayerRadio.getInstance(new OnUpdateViewRadioPlayListener() {
                        @Override
                        public void onBuffering() {

                        }

                        @Override
                        public void onReady() {

                        }

                        @Override
                        public void onEnd() {

                        }
                    });
                    if(playerRadio.checkPlay()){
                        playerRadio.pauseRadio();
                    }
                }
            }
        });
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        if(getIntent() != null){
            Intent intent = getIntent();
            if (intent.getExtras() != null &&
                    intent.getExtras().getInt("choice", -1) == 0) {
                bottomNavigationView.setSelectedItemId(R.id.baseMoreFragment);
            }
        }

    }


    public static void hide_Navi(){
        bottomNavigationView.setVisibility(View.GONE);
    }

    public static void show_Navi(){
        bottomNavigationView.setVisibility(View.VISIBLE);
    }

    public static void choice_Navi(int id_case){
        bottomNavigationView.setSelectedItemId(id_case);
    }
}