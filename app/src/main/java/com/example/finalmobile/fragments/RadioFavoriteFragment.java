package com.example.finalmobile.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.finalmobile.databinding.FragmentRadioFavoriteBinding;

import java.util.ArrayList;

import okhttp3.RequestBody;

public class RadioFavoriteFragment extends Fragment {
    private View rootView;
    private FragmentRadioFavoriteBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRadioFavoriteBinding.inflate(inflater, container, false);
        rootView = binding.getRoot();
        return rootView;
    }
}