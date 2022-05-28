package com.example.finalmobile.fragments;

import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager2.widget.ViewPager2;

import com.example.finalmobile.R;
import com.example.finalmobile.activities.MainActivity;
import com.example.finalmobile.adapters.FeaturedVideoAdapter;
import com.example.finalmobile.adapters.RadioItemAdapter;
import com.example.finalmobile.adapters.SlideShowHomeFragAdapter;
import com.example.finalmobile.adapters.TVFragmentAdapter;
import com.example.finalmobile.asynctasks.LoadRadioAsync;
import com.example.finalmobile.asynctasks.LoadTVAsync;
import com.example.finalmobile.asynctasks.LoadVideoAsync;
import com.example.finalmobile.databinding.FragmentHomeBinding;
import com.example.finalmobile.listeners.ControlRadioListener;
import com.example.finalmobile.listeners.LoadRadioAsyncListener;
import com.example.finalmobile.listeners.LoadTVAsyncListener;
import com.example.finalmobile.listeners.LoadVideoAsyncListener;
import com.example.finalmobile.listeners.OnHomeItemClickListeners;
import com.example.finalmobile.listeners.OnRadioClickListeners;
import com.example.finalmobile.listeners.OnVideoFeatureClickListener;
import com.example.finalmobile.models.Category_M;
import com.example.finalmobile.models.Videos_M;
import com.example.finalmobile.utils.Constant;
import com.example.finalmobile.utils.Methods;
import com.example.finalmobile.utils.SharedPref;

import java.io.Serializable;
import java.util.ArrayList;

import okhttp3.RequestBody;


public class HomeFragment extends Fragment {
    private NavController navController;
    private static ArrayList<Category_M> list_cat_Video;
    private static ArrayList<Videos_M> list_video_trending, list_TV_trending;
    private static ArrayList<Videos_M> list_radio_trending;
    private static int index_selected_radio = -1;
    private FragmentHomeBinding binding;
    private static boolean first_time = true;
    private SlideShowHomeFragAdapter slideShowHomeFragAdapter;
    private FeaturedVideoAdapter featuredVideoAdapter;
    private TVFragmentAdapter tvFragmentAdapter;
    private RadioItemAdapter radioItemAdapter;
    private ConnectivityManager connectivityManager;
    private static boolean check_internet = false;
    private boolean isDisconnect = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();

        return  rootView;
    }

    
}