package com.example.finalmobile.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.example.finalmobile.R;
import com.example.finalmobile.adapters.FeaturedVideoAdapter;
import com.example.finalmobile.asynctasks.LoadFavoriteListAsync;
import com.example.finalmobile.databinding.FragmentVideoFavoriteBinding;
import com.example.finalmobile.listeners.LoadSearchVideoAsyncListener;
import com.example.finalmobile.listeners.OnVideoFeatureClickListener;
import com.example.finalmobile.listeners.FavoriteToDetailListener;
import com.example.finalmobile.models.Videos_M;
import com.example.finalmobile.utils.Methods;

import java.util.ArrayList;

import okhttp3.RequestBody;

public class VideoFavoriteFragment extends Fragment {
    private View rootView;
    private FragmentVideoFavoriteBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentVideoFavoriteBinding.inflate(inflater, container, false);
        rootView = binding.getRoot();

        return rootView;
    }

}