package com.example.finalmobile.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Handler;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.common.collect.Range;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.example.finalmobile.R;
import com.example.finalmobile.databinding.FragmentUpdateProfileBinding;
import com.example.finalmobile.models.Users_M;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class UpdateProfileFragment extends Fragment {

    private static final int RESULT_OK = -1;
    private FragmentUpdateProfileBinding binding;
    private NavController navController;
    private View rootView;
    private DatabaseReference databaseReference;
    private AwesomeValidation awesomeValidation;
    private static GoogleSignInAccount gg_account;
    private static String gg_email;
    private SharedPreferences sharedPreferences;
    private FirebaseAuth mAuth;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private static Uri url, url_gg;
    private static Users_M user;
    private Boolean isFirstPhoto;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentUpdateProfileBinding.inflate(inflater, container, false);
        rootView = binding.getRoot();

        return rootView;
    }

    
}
