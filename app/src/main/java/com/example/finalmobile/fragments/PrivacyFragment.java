package com.example.finalmobile.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.finalmobile.R;
import com.example.finalmobile.databinding.FragmentPrivacyBinding;

public class PrivacyFragment extends Fragment {
    private View rootView;
    private NavController navController;
    private FragmentPrivacyBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPrivacyBinding.inflate(inflater, container, false);
        rootView = binding.getRoot();
        
        return rootView;
    }


}
