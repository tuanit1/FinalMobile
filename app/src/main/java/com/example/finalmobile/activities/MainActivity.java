package com.example.finalmobile.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.example.finalmobile.databinding.ActivityMainBinding;
import com.example.finalmobile.utils.PlayerRadio;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.example.finalmobile.R;

public class MainActivity extends AppCompatActivity {

    private NavController navController;
    private NavHostFragment navHostFragment;
    private static BottomNavigationView bottomNavigationView;
    public static int SET_PORTRAIT_REQUEST_CODE;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);

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

    @Override
    public void onBackPressed() {

        if(navHostFragment.getChildFragmentManager().getBackStackEntryCount() < 1){
            showQuitDialog();
        }else{
            MainActivity.super.onBackPressed();
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

    private void showQuitDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view1 = LayoutInflater.from(this).inflate(R.layout.layout_dialog_quit, null,false);
        builder.setView(view1);

        final AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button btn_submit = view1.findViewById(R.id.btn_submit);
        Button btn_cancel = view1.findViewById(R.id.btn_cancel);

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishAffinity();
            }
        });

        alertDialog.show();

    }
}