package com.example.finalmobile.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalmobile.R;
import com.example.finalmobile.adapters.FeaturedVideoAdapter;
import com.example.finalmobile.adapters.HorizontalCategoryAdapter;
import com.example.finalmobile.adapters.TrendingMoviePagerAdapter;
import com.example.finalmobile.asynctasks.LoadVideoAsync;
import com.example.finalmobile.databinding.FragmentVideoBinding;
import com.example.finalmobile.listeners.LoadVideoAsyncListener;
import com.example.finalmobile.listeners.OnCategoryHorizontalListener;
import com.example.finalmobile.listeners.OnVideoFeatureClickListener;
import com.example.finalmobile.models.Category_M;
import com.example.finalmobile.models.Videos_M;
import com.example.finalmobile.utils.Constant;
import com.example.finalmobile.utils.Methods;
import com.example.finalmobile.utils.SharedPref;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;
import okhttp3.RequestBody;


public class VideoFragment extends Fragment {

    private View rootView;
    private NavController navController;
    private FragmentVideoBinding binding;
    private static ArrayList<Videos_M> mTrendings;
    private static ArrayList<Videos_M> mMostViews;
    private static ArrayList<Videos_M> mLatests;
    private static ArrayList<Videos_M> mTopRates;
    private static ArrayList<Category_M> mCategories;
    private static boolean first_time = true;
    private static boolean check_internet = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentVideoBinding.inflate(inflater, container, false);
        rootView = binding.getRoot();
    
        return rootView;
    }

    
}