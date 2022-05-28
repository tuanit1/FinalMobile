package com.example.finalmobile.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.navigation.NavController;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.PlaybackException;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.analytics.AnalyticsListener;
import com.example.finalmobile.R;
import com.example.finalmobile.databinding.ActivityVideoPlayerBinding;
import com.example.finalmobile.models.Videos_M;
import com.example.finalmobile.utils.Constant;

public class VideoPlayerActivity extends AppCompatActivity {

    private ActivityVideoPlayerBinding binding;
    private ExoPlayer player;
    private NavController navController;
    private ImageView btn_ffwd, btn_rew, btn_back, btn_speed, btn_rotate;
    private TextView tv_title;
    private Videos_M mVideo;
    private ConstraintLayout main_cs, cs_toolbar, cs_shadow_up, cs_shadow_down, cs_control, cs_progress;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFullScreen();
        binding = ActivityVideoPlayerBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

    }

}