package com.example.finalmobile.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.finalmobile.databinding.FragmentRadioDetailsBinding;

public class RadioDetailsFragment extends Fragment {

    private View rootView;
    private NavController navController;
    private FragmentRadioDetailsBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRadioDetailsBinding.inflate(inflater, container, false);
        rootView = binding.getRoot();
        navController = NavHostFragment.findNavController(this);
        return rootView;
    }
}