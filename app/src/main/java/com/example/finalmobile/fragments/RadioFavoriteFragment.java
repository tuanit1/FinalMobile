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

import com.google.firebase.auth.FirebaseAuth;
import com.example.finalmobile.R;
import com.example.finalmobile.adapters.RadioItemAdapter;
import com.example.finalmobile.asynctasks.LoadFavoriteListAsync;
import com.example.finalmobile.asynctasks.LoadRadioAsync;
import com.example.finalmobile.asynctasks.LoadSearchVideoAsync;
import com.example.finalmobile.asynctasks.LoadVideoAsync;
import com.example.finalmobile.databinding.FragmentRadioFavoriteBinding;
import com.example.finalmobile.databinding.FragmentVideoFavoriteBinding;
import com.example.finalmobile.listeners.FavoriteToDetailListener;
import com.example.finalmobile.listeners.LoadRadioAsyncListener;
import com.example.finalmobile.listeners.LoadSearchVideoAsyncListener;
import com.example.finalmobile.listeners.LoadVideoAsyncListener;
import com.example.finalmobile.listeners.OnRadioClickListeners;
import com.example.finalmobile.models.Category_M;
import com.example.finalmobile.models.Videos_M;
import com.example.finalmobile.utils.Constant;
import com.example.finalmobile.utils.Methods;
import com.squareup.picasso.Picasso;

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