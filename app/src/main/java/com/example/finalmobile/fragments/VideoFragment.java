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
        navController = NavHostFragment.findNavController(this);

        int height = getContext().getResources().getDisplayMetrics().heightPixels;
        LinearLayout.LayoutParams layoutParams00 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) Math.round(height*0.3));
        binding.rlPager.setLayoutParams(layoutParams00);

        if(first_time || mTrendings.isEmpty() || mMostViews.isEmpty() || mLatests.isEmpty() || mTopRates.isEmpty()) {
            mTrendings = new ArrayList<>();
            mMostViews = new ArrayList<>();
            mLatests = new ArrayList<>();
            mTopRates = new ArrayList<>();
            mCategories = new ArrayList<>();
            mCategories.add(new Category_M(-1, "All", "", 1));
            LoadVideoData();
            first_time = false;
        } else {
            updateUI();
        }

        SetupView();

        CountDownTimer countDownTimer = new CountDownTimer(200,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if(getContext() != null){
                    if(check_internet !=
                            Methods.getInstance().isNetworkConnected(getContext()) &&
                            !check_internet ){
                        LoadVideoData();
                    }
                    check_internet = Methods.getInstance().isNetworkConnected(getContext());
                }
            }

            @Override
            public void onFinish() {
                this.start();
            }
        };
        countDownTimer.start();

        return rootView;
    }

    private void SetupView() {

        binding.swiperVideoFrag.setColorSchemeColors(getResources().getColor(R.color.neonGreen));
        binding.swiperVideoFrag.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                LoadVideoData();
                binding.swiperVideoFrag.setRefreshing(false);
            }
        });

        binding.ivSearch.setOnClickListener(v->{
            if(!binding.edtSearch.getText().toString().isEmpty()){
                Bundle bundle = new Bundle();
                bundle.putString("type", "search");
                bundle.putString("search_text", binding.edtSearch.getText().toString());
                navController.navigate(R.id.VideoToSearchVideo, bundle);
            }else{
                Toast.makeText(getContext(), "Please fill the text input!", Toast.LENGTH_SHORT).show();
            }
        });

        binding.btnSearchDmotion.setOnClickListener(v->{
            if(!binding.edtSearch.getText().toString().isEmpty()){
                Bundle bundle = new Bundle();
                bundle.putString("search_text", binding.edtSearch.getText().toString());
                navController.navigate(R.id.VideoToDailymotionSearch, bundle);
            }else{
                Toast.makeText(getContext(), "Please fill the text input!", Toast.LENGTH_SHORT).show();
            }
        });

        binding.btnSearchYoutube.setOnClickListener(v->{
            if(!binding.edtSearch.getText().toString().isEmpty()){
                Bundle bundle = new Bundle();
                bundle.putString("search_text", binding.edtSearch.getText().toString());
                navController.navigate(R.id.VideoToYoutubeSearch, bundle);
            }else{
                Toast.makeText(getContext(), "Please fill the text input!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void LoadVideoData(){

        RequestBody requestBody = Methods.getInstance().getVideoRequestBody("LOAD_VIDEO_SCREEN", null);
        LoadVideoAsyncListener listener = new LoadVideoAsyncListener() {
            @Override
            public void onStart() {
                binding.llList.setVisibility(View.GONE);
                binding.viewPager.setVisibility(View.GONE);
                binding.circleIndicator.setVisibility(View.GONE);
                binding.progressBar.setVisibility(View.VISIBLE);
                binding.progressBarPager.setVisibility(View.VISIBLE);

                mTrendings.clear();
                mMostViews.clear();
                mLatests.clear();
                mTopRates.clear();
                mCategories.clear();
            }

            @Override
            public void onEnd(boolean status, ArrayList<Videos_M> arrayList_trending, ArrayList<Videos_M> arrayList_mostview, ArrayList<Videos_M> arrayList_latest, ArrayList<Videos_M> arrayList_toprate, ArrayList<Category_M> arrayList_category) {
                if(getContext() != null){
                    binding.llList.setVisibility(View.VISIBLE);
                    binding.viewPager.setVisibility(View.VISIBLE);
                    binding.circleIndicator.setVisibility(View.VISIBLE);
                    binding.progressBar.setVisibility(View.GONE);
                    binding.progressBarPager.setVisibility(View.GONE);
                    if(Methods.getInstance().isNetworkConnected(getContext())){
                        if(status){

                            mTrendings.clear();
                            mMostViews.clear();
                            mLatests.clear();
                            mTopRates.clear();
                            mCategories.clear();

                            if(arrayList_trending.isEmpty()){
                                mTrendings.addAll(SharedPref.getInstance(getContext()).getTempVideoList(Constant.VIDEO));
                            }else{
                                mTrendings.addAll(arrayList_trending);
                            }

                            if(arrayList_mostview.isEmpty()){
                                mMostViews.addAll(SharedPref.getInstance(getContext()).getTempVideoList(Constant.VIDEO));
                            }else{
                                mMostViews.addAll(arrayList_mostview);
                            }

                            if(arrayList_latest.isEmpty()){
                                mLatests.addAll(SharedPref.getInstance(getContext()).getTempVideoList(Constant.VIDEO));
                            }else{
                                mLatests.addAll(arrayList_latest);
                            }

                            if(arrayList_toprate.isEmpty()){
                                mTopRates.addAll(SharedPref.getInstance(getContext()).getTempVideoList(Constant.VIDEO));
                            }else{
                                mTopRates.addAll(arrayList_toprate);
                            }

                            if(arrayList_category.isEmpty()){
                                mCategories.addAll(SharedPref.getInstance(getContext()).getTempCategoryList(Constant.VIDEO));
                            }else{
                                mCategories.addAll(arrayList_category);
                            }

                            SharedPref.getInstance(getContext()).setTempVideoList(Constant.VIDEO, mMostViews);

                        }else{
                            Toast.makeText(getContext(), "Something wrong happened, try again!", Toast.LENGTH_SHORT).show();
                            mTrendings.addAll(SharedPref.getInstance(getContext()).getTempVideoList(Constant.VIDEO));
                            mLatests.addAll(SharedPref.getInstance(getContext()).getTempVideoList(Constant.VIDEO));
                            mTopRates.addAll(SharedPref.getInstance(getContext()).getTempVideoList(Constant.VIDEO));
                            mMostViews.addAll(SharedPref.getInstance(getContext()).getTempVideoList(Constant.VIDEO));
                            mCategories.addAll(SharedPref.getInstance(getContext()).getTempCategoryList(Constant.VIDEO));
                        }
                    }else{
                        Toast.makeText(getContext(), "Please connect to the internet!", Toast.LENGTH_SHORT).show();
                        mTrendings.addAll(SharedPref.getInstance(getContext()).getTempVideoList(Constant.VIDEO));
                        mLatests.addAll(SharedPref.getInstance(getContext()).getTempVideoList(Constant.VIDEO));
                        mTopRates.addAll(SharedPref.getInstance(getContext()).getTempVideoList(Constant.VIDEO));
                        mMostViews.addAll(SharedPref.getInstance(getContext()).getTempVideoList(Constant.VIDEO));
                        mCategories.addAll(SharedPref.getInstance(getContext()).getTempCategoryList(Constant.VIDEO));
                    }

                    updateUI();

                    binding.viewPager.setVisibility(View.VISIBLE);
                    binding.circleIndicator.setVisibility(View.VISIBLE);
                    binding.llList.setVisibility(View.VISIBLE);
                    binding.progressBar.setVisibility(View.GONE);
                    binding.progressBarPager.setVisibility(View.GONE);
                }
            }
        };
        LoadVideoAsync async = new LoadVideoAsync(requestBody, listener, Methods.getInstance());
        async.execute();
    }

    private void updateUI() {

        binding.rvLatestVideo.setItemAnimator(new DefaultItemAnimator());
        binding.rvMostView.setItemAnimator(new DefaultItemAnimator());
        binding.rvTopRating.setItemAnimator(new DefaultItemAnimator());
        binding.rvCategory.setItemAnimator(new DefaultItemAnimator());

        binding.rvLatestVideo.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        binding.rvMostView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        binding.rvTopRating.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        binding.rvCategory.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));

        int width = getContext().getResources().getDisplayMetrics().widthPixels;
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams((int)Math.round(width*0.5), (int)Math.round(width*0.45));
        layoutParams.setMargins(35,0,0,0);

        binding.rvLatestVideo.setAdapter(new FeaturedVideoAdapter(Methods.getInstance(), layoutParams, mLatests, new OnVideoFeatureClickListener() {
            @Override
            public void onClick(int position) {
                Videos_M video = mLatests.get(position);
                openVideoDetail(video);
            }
        }));

        binding.rvTopRating.setAdapter(new FeaturedVideoAdapter(Methods.getInstance(), layoutParams, mTopRates, new OnVideoFeatureClickListener() {
            @Override
            public void onClick(int position) {
                Videos_M video = mTopRates.get(position);
                openVideoDetail(video);
            }
        }));

        binding.rvMostView.setAdapter(new FeaturedVideoAdapter(Methods.getInstance(), layoutParams, mMostViews, new OnVideoFeatureClickListener() {
            @Override
            public void onClick(int position) {
                Videos_M video = mMostViews.get(position);
                openVideoDetail(video);
            }
        }));

        RelativeLayout.LayoutParams layoutParam_category = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, (int) Math.round(width*0.1));
        layoutParam_category.setMargins(30, 0, 0 ,0);

        binding.rvCategory.setAdapter(new HorizontalCategoryAdapter(layoutParam_category, mCategories, new OnCategoryHorizontalListener() {
            @Override
            public void onClick(int position) {
                Category_M c = mCategories.get(position);
                Bundle bundle = new Bundle();
                bundle.putString("type", "category");
                bundle.putInt("cat_id", c.getCat_id());
                bundle.putString("cat_name", c.getCat_name());
                navController.navigate(R.id.VideoToSearchVideo, bundle);
            }
        }));

        TrendingMoviePagerAdapter pagerAdapter = new TrendingMoviePagerAdapter(getContext(), mTrendings, new OnVideoFeatureClickListener() {
            @Override
            public void onClick(int position) {
                Videos_M video = mTrendings.get(position);
                openVideoDetail(video);
            }
        });

        binding.viewPager.setAdapter(pagerAdapter);
        binding.circleIndicator.setViewPager(binding.viewPager);
        pagerAdapter.registerDataSetObserver(binding.circleIndicator.getDataSetObserver());

    }

    private void openVideoDetail(Videos_M videos){
        Bundle bundle = new Bundle();
        bundle.putSerializable("video", videos);
        bundle.putBoolean("is_home", false);

        navController.navigate(R.id.VideoToDetail, bundle);
    }


}