package com.example.finalmobile.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.finalmobile.databinding.ActivityDailymotionPlayerBinding;

import java.util.HashMap;
import java.util.Map;

public class DailymotionPlayerActivity extends AppCompatActivity {

    private ActivityDailymotionPlayerBinding binding;
    private String mVideoID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDailymotionPlayerBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
    }



}