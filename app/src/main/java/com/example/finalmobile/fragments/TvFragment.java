package com.example.finalmobile.fragments;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.finalmobile.R;
import com.example.finalmobile.activities.MainActivity;
import com.example.finalmobile.adapters.CateTvFragmentAdapter;
import com.example.finalmobile.adapters.TVFragmentAdapter;
import com.example.finalmobile.asynctasks.LoadTVAsync;
import com.example.finalmobile.asynctasks.LoadVideoAsync;
import com.example.finalmobile.databinding.FragmentTvBinding;
import com.example.finalmobile.listeners.LoadTVAsyncListener;
import com.example.finalmobile.listeners.LoadVideoAsyncListener;
import com.example.finalmobile.listeners.OnHomeItemClickListeners;
import com.example.finalmobile.models.Category_M;
import com.example.finalmobile.models.Videos_M;
import com.example.finalmobile.utils.Constant;
import com.example.finalmobile.utils.Methods;
import com.example.finalmobile.utils.SharedPref;

import org.checkerframework.checker.units.qual.A;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import okhttp3.RequestBody;

public class TvFragment extends Fragment {
    private FragmentTvBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTvBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();
        
        return rootView;
    }
}