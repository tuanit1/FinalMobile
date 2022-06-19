package com.example.finalmobile.activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;

import com.example.finalmobile.utils.Constant;
import com.github.ybq.android.spinkit.style.WanderingCubes;
import com.google.firebase.auth.FirebaseAuth;
import com.example.finalmobile.R;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import okhttp3.RequestBody;

public class SplashActivity extends AppCompatActivity {
    ProgressBar progressBar;
    FirebaseAuth mAuth;
    private String gg_email;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        progressBar = findViewById(R.id.progress_bar_splash);
        progressBar.setIndeterminateDrawableTiled(new WanderingCubes());
        mAuth = FirebaseAuth.getInstance();
        sharedPreferences = getSharedPreferences("DataLogin", Context.MODE_PRIVATE);
        gg_email = sharedPreferences.getString("gg_email", "");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(Methods.getInstance().isNetworkConnected(SplashActivity.this)){

                    if (mAuth.getCurrentUser()!=null){
                        if (gg_email!=null){
                            CheckUser(mAuth.getCurrentUser());
                        }
                        else{
                            startActivity(new Intent(SplashActivity.this, LoginActivity.class));

                        }
                    }
                    else{
                        startActivity(new Intent(SplashActivity.this, LoginActivity.class));

                    }
                }else{
                    Constant.ADS_KEY_BANNER = "";
                    Constant.ADS_KEY_INTERSTIAL = "";
                    Constant.ADS_DISPLAY_COUNT = 0;
                    Constant.ADS_KEY_OPENADS = "";
                    Constant.ARR_VID_TREND = "";
                    Constant.ARR_TV_TREND = "";
                    Constant.ARR_RADIO_TREND = "";
                }
            }
        }, 3000);

        Load_Async();
    }

    private void CheckUser(FirebaseUser user){
        DatabaseReference databaseReference;
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Users").child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.P)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                } else {
                    Intent intent = new Intent(SplashActivity.this, UpdateProfileGoogleActivity.class);
                    startActivity(intent);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void Load_Async(){
        ExecuteQueryAsyncListener listener = new ExecuteQueryAsyncListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onEnd(boolean status) {
                if(status){
                    for(String w: Constant.ARR_VID_TREND.split(":")){
                        if(w.contains("RESULT"))
                            continue;
                        else{
                            Constant.LIST_TRENDING_VID.add(Integer.parseInt(w));
                        }
                    }
                    for(String w:Constant.ARR_TV_TREND.split(":")){
                        if(w.contains("RESULT"))
                            continue;
                        else{
                            Constant.LIST_TRENDING_TV.add(Integer.parseInt(w));
                        }
                    }
                    for(String w:Constant.ARR_RADIO_TREND.split(":")){
                        if(w.contains("RESULT"))
                            continue;
                        else{
                            Constant.LIST_TRENDING_RADIO.add(Integer.parseInt(w));
                        }
                    }
//

                }else{
                    Constant.ADS_KEY_BANNER = "";
                    Constant.ADS_KEY_INTERSTIAL = "";
                    Constant.ADS_DISPLAY_COUNT = 0;
                    Constant.ADS_KEY_OPENADS = "";
                    Constant.ARR_VID_TREND = "";
                    Constant.ARR_TV_TREND = "";
                    Constant.ARR_RADIO_TREND = "";
                }
            }

        };

        RequestBody requestBody = Methods.getInstance().getSettingRequestBody("GET_SETTING",null);
        GetSettingAsync async = new GetSettingAsync(requestBody, listener);
        async.execute();
    }
}
