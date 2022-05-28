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


import com.example.finalmobile.databinding.FragmentVideoFavoriteBinding;

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