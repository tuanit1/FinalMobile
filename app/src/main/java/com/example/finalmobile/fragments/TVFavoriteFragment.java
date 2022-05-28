package com.naosteam.watchvideoapp.fragments;

import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.naosteam.watchvideoapp.R;
import com.naosteam.watchvideoapp.adapters.RadioItemAdapter;
import com.naosteam.watchvideoapp.adapters.TVFragmentAdapter;
import com.naosteam.watchvideoapp.asynctasks.LoadFavoriteListAsync;
import com.naosteam.watchvideoapp.databinding.FragmentRadioFavoriteBinding;
import com.naosteam.watchvideoapp.databinding.FragmentTVFavoriteBinding;
import com.naosteam.watchvideoapp.listeners.FavoriteToDetailListener;
import com.naosteam.watchvideoapp.listeners.LoadSearchVideoAsyncListener;
import com.naosteam.watchvideoapp.listeners.OnHomeItemClickListeners;
import com.naosteam.watchvideoapp.listeners.OnRadioClickListeners;
import com.naosteam.watchvideoapp.models.Videos_M;
import com.naosteam.watchvideoapp.utils.Constant;
import com.naosteam.watchvideoapp.utils.Methods;

import java.util.ArrayList;

import okhttp3.RequestBody;

public class TVFavoriteFragment extends Fragment {
    private View rootView;
    private FragmentTVFavoriteBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTVFavoriteBinding.inflate(inflater, container, false);
        rootView = binding.getRoot();

        return rootView;
    }
}