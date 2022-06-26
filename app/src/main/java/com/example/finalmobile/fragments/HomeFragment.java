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
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            int currentPosition = binding.vpgSlideHomeFrag.getCurrentItem();
            if(currentPosition == list_cat_Video.size() - 1){
                binding.vpgSlideHomeFrag.setCurrentItem(0);
            } else {
                binding.vpgSlideHomeFrag.setCurrentItem(currentPosition + 1);
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();

        navController = NavHostFragment.findNavController(this);

        if(first_time || list_cat_Video.isEmpty() || list_video_trending.isEmpty() || list_radio_trending.isEmpty() || list_TV_trending.isEmpty()) {
            list_cat_Video = new ArrayList<>();
            list_cat_Video.add(
                    new Category_M(1, "Ten", "https://marvelphim.com/wp-content/uploads/2022/01/poster-da%CC%82%CC%80u-cho-series-moon-knight.jpg", 1)
            );
            list_cat_Video.add(
                    new Category_M(1, "Ten", "https://image.lag.vn/upload/news/22/04/07/278027632_3227456860903478_2335780498416729987_n_OPMF.jpg", 1)
            );
            list_cat_Video.add(
                    new Category_M(1, "Ten", "https://upload.wikimedia.org/wikipedia/vi/4/46/Deadpool_poster.jpg", 1)
            );
            list_cat_Video.add(
                    new Category_M(1, "Ten", "https://cdn.vietnambiz.vn/171464876016439296/2020/6/4/eventtechnology-15912456396331111417122.jpg", 1)
            );
            list_video_trending = new ArrayList<>();
            list_TV_trending = new ArrayList<>();
            list_radio_trending = new ArrayList<>();
            Load_Video_Trending();
            first_time = false;
        }

        CountDownTimer countDownTimer = new CountDownTimer(200,1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                if(getContext() != null){
                    if(check_internet !=
                            Methods.getInstance().isNetworkConnected(getContext()) &&
                            !check_internet ){
                        Load_Video_Trending();
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

        setUp();

        return  rootView;
    }

    public void setUp(){
        Handler handler = new Handler(Looper.getMainLooper());
        if(runnable != null){
            runnable = null;
        }

        binding.swipeViewHomeFrag.setColorSchemeColors(getResources().getColor(R.color.neonGreen));
        binding.swipeViewHomeFrag.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Load_Video_Trending();
                binding.swipeViewHomeFrag.setRefreshing(false);
            }
        });

        runnable = new Runnable() {
            @Override
            public void run() {
                int currentPosition = binding.vpgSlideHomeFrag.getCurrentItem();
                if(currentPosition == list_cat_Video.size() - 1){
                    binding.vpgSlideHomeFrag.setCurrentItem(0);
                } else {
                    binding.vpgSlideHomeFrag.setCurrentItem(currentPosition + 1);
                }
            }
        };

        binding.vpgSlideHomeFrag.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                SlideShowHomeFragAdapter.setSelected_index(position);
                handler.removeCallbacks(runnable);
                handler.postDelayed(runnable, 2500);
                slideShowHomeFragAdapter.notifyDataSetChanged();
            }
        });

        LinearLayout.LayoutParams layoutParams_slideShow = new LinearLayout.LayoutParams(getActivity().getResources().
                getDisplayMetrics().widthPixels, (int)3.2*(getActivity().getResources().getDisplayMetrics().widthPixels)/5);
        binding.vpg2HomeFrag.setLayoutParams(layoutParams_slideShow);

        LinearLayout.LayoutParams layoutParams_TextView = new LinearLayout.LayoutParams(getActivity().getResources().
                getDisplayMetrics().widthPixels, 5*(getActivity().getResources().getDisplayMetrics().heightPixels)/100);
        binding.txtTrendingVidHomeFrag.setLayoutParams(layoutParams_TextView);
        binding.txtTrendingVidHomeFrag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.choice_Navi(R.id.baseVideoFragment);
            }
        });
        binding.txtTrendingTVHomeFrag.setLayoutParams(layoutParams_TextView);
        binding.txtTrendingTVHomeFrag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.choice_Navi(R.id.baseTvFragment2);
            }
        });
        binding.txtTrendingRadHomeFrag.setLayoutParams(layoutParams_TextView);
        binding.txtTrendingRadHomeFrag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.choice_Navi(R.id.baseRadioFragment);
            }
        });
        slideShowHomeFragAdapter = new SlideShowHomeFragAdapter(list_cat_Video, new OnHomeItemClickListeners() {
            @Override
            public void onClick_homeItem(int position) {

            }
        });
        binding.vpgSlideHomeFrag.setAdapter(slideShowHomeFragAdapter);
        binding.indicatorHomeFrag.setViewPager(binding.vpgSlideHomeFrag);

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams((int) (getActivity().getResources().
                        getDisplayMetrics().widthPixels*(0.42)), (int) (Math.round(getActivity().getResources().getDisplayMetrics().widthPixels)/2*0.9));
        layoutParams.setMargins(30,20,30,20);

        featuredVideoAdapter = new FeaturedVideoAdapter(Methods.getInstance(), layoutParams, list_video_trending, new OnVideoFeatureClickListener() {
            @Override
            public void onClick(int position) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("video", list_video_trending.get(position));
                bundle.putBoolean("is_home", true);

                try{
                    navController.navigate(R.id.Home_to_Video_Detail, bundle);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        binding.rcvTrendVidHomeFrag.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        binding.rcvTrendVidHomeFrag.setAdapter(featuredVideoAdapter);

        ConstraintLayout.LayoutParams layoutParams_TV_item = new ConstraintLayout.LayoutParams(getActivity().getResources().
                getDisplayMetrics().widthPixels*1/3, (getActivity().getResources().getDisplayMetrics().widthPixels)*1/3*3/4);
        layoutParams_TV_item.setMargins(30, 10,30,10);

        tvFragmentAdapter = new TVFragmentAdapter(list_TV_trending, layoutParams_TV_item, new OnHomeItemClickListeners() {
            @Override
            public void onClick_homeItem(int position) {
                Bundle bundle = new Bundle();
                bundle.putInt("id", list_TV_trending.get(position).getVid_id());
                bundle.putString("url", list_TV_trending.get(position).getVid_url());
                bundle.putString("url_img", list_TV_trending.get(position).getVid_thumbnail());
                bundle.putString("des", list_TV_trending.get(position).getVid_description());
                bundle.putBoolean("isHome", true);
                try{
                    navController.navigate(R.id.homeFrag_to_TVDetailFrag, bundle);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        binding.rcvTrendTVHomeFrag.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        binding.rcvTrendTVHomeFrag.setAdapter(tvFragmentAdapter);

        ConstraintLayout.LayoutParams layoutParams_radio = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                getActivity().getResources().getDisplayMetrics().heightPixels*1/10);
        layoutParams_radio.setMargins(5, 10, 5, 10);

        radioItemAdapter = new RadioItemAdapter(layoutParams_radio, list_radio_trending, new OnRadioClickListeners() {
            @Override
            public void onClick(int position) {
                Bundle bundle = new Bundle();
                index_selected_radio = position;
                Constant.Radio_Listening = list_radio_trending.get(index_selected_radio);
                RadioDetailsFragment.setControlRadioListener(new ControlRadioListener() {
                    @Override
                    public void onNext() {
                        if (index_selected_radio == list_radio_trending.size() - 1) {
                            index_selected_radio = 0;
                        } else {
                            index_selected_radio = index_selected_radio + 1;
                        }
                        Constant.Radio_Listening = list_radio_trending.get(index_selected_radio);
                    }

                    @Override
                    public void onPrevious() {
                        if (index_selected_radio == 0 || index_selected_radio == -1) {
                            index_selected_radio = list_radio_trending.size() - 1;
                        } else {
                            index_selected_radio = index_selected_radio - 1;
                        }
                        Constant.Radio_Listening = list_radio_trending.get(index_selected_radio);
                    }
                });
                bundle.putString("from", "from_home_screen");
                try{
                    navController.navigate(R.id.home_to_radioDetails, bundle);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        binding.rcvTrendRadHomeFrag.setAdapter(radioItemAdapter);
        binding.rcvTrendRadHomeFrag.setLayoutManager(new LinearLayoutManager(getContext()));

    }

    private void Load_Video_Trending(){
        RequestBody requestBody = Methods.getInstance().getVideoRequestBody("LOAD_VIDEO_SCREEN", null);
        LoadVideoAsyncListener listener = new LoadVideoAsyncListener() {
            @Override
            public void onStart() {
                binding.prgHomeFrag.setVisibility(View.VISIBLE);
                binding.imgTempHomeFrag.setVisibility(View.VISIBLE);
                list_video_trending.clear();
            }

            @Override
            public void onEnd(boolean status, ArrayList<Videos_M> arrayList_trending, ArrayList<Videos_M> arrayList_mostview, ArrayList<Videos_M> arrayList_latest, ArrayList<Videos_M> arrayList_toprate, ArrayList<Category_M> arrayList_category) {
                if(getContext() != null){
                    if(Methods.getInstance().isNetworkConnected(getContext())){

                        if(status){
                            list_video_trending.clear();
                            list_video_trending.addAll(arrayList_trending);

                            if(arrayList_trending.isEmpty()){
                                list_video_trending.addAll(SharedPref.getInstance(getContext()).getTempVideoList(Constant.VIDEO));
                            }else{
                                list_radio_trending.addAll(arrayList_trending);
                                SharedPref.getInstance(getContext()).setTempVideoList(Constant.VIDEO, list_video_trending);
                            }

                        }else{
                            Toast.makeText(getContext(), "Something wrong happened, try again!", Toast.LENGTH_SHORT).show();
                            list_video_trending.addAll(SharedPref.getInstance(getContext()).getTempVideoList(Constant.VIDEO));
                        }
                    }else{
                        Toast.makeText(getContext(), "Please connect to the internet!", Toast.LENGTH_SHORT).show();
                        list_video_trending.addAll(SharedPref.getInstance(getContext()).getTempVideoList(Constant.VIDEO));
                    }

                    Load_TV_Trending();
                }
            }
        };
        LoadVideoAsync async = new LoadVideoAsync(requestBody, listener, Methods.getInstance());
        async.execute();
    }

    private void Load_TV_Trending(){
        RequestBody requestBody = Methods.getInstance().GetTVRequestBody("LOAD_TV_SCREEN", null);
        LoadTVAsyncListener listener = new LoadTVAsyncListener() {
            @Override
            public void onPre() {
                list_TV_trending.clear();
            }

            @Override
            public void onEnd(Boolean ablBoolean, ArrayList<Videos_M> list_tv_all,
                              ArrayList<Videos_M> list_tv_trending, ArrayList<Category_M> list_categList_tv) {
                if(getContext() != null){
                    if(Methods.getInstance().isNetworkConnected(getContext())){

                        if(ablBoolean){
                            HomeFragment.list_TV_trending.clear();
                            HomeFragment.list_TV_trending.addAll(list_tv_trending);

                            if(list_TV_trending.isEmpty()){
                                list_TV_trending.addAll(SharedPref.getInstance(getContext()).getTempVideoList(Constant.TV));
                            }else{
                                list_TV_trending.addAll(list_tv_trending);
                                SharedPref.getInstance(getContext()).setTempVideoList(Constant.TV, list_TV_trending);
                            }
                        }else{
                            Toast.makeText(getContext(), "Something wrong happened, try again!", Toast.LENGTH_SHORT).show();
                            list_TV_trending.addAll(SharedPref.getInstance(getContext()).getTempVideoList(Constant.TV));
                        }
                    }else{
                        Toast.makeText(getContext(), "Please connect to the internet!", Toast.LENGTH_SHORT).show();
                        list_TV_trending.addAll(SharedPref.getInstance(getContext()).getTempVideoList(Constant.TV));
                    }
                    Load_Radio_Trending();
                }
            }
        };
        LoadTVAsync async = new LoadTVAsync(requestBody, listener);
        async.execute();
    }

    private void Load_Radio_Trending(){
        RequestBody requestBody = Methods.getInstance().GetRadioRequestBody("LOAD_RADIO_SCREEN", null);
        LoadRadioAsyncListener listener = new LoadRadioAsyncListener() {
            @Override
            public void onStart() {
                list_radio_trending.clear();
            }

            @Override
            public void onEnd(boolean status, ArrayList<Videos_M> arrayList_trending, ArrayList<Category_M> arrayList_category) {
                binding.prgHomeFrag.setVisibility(View.GONE);

                binding.imgTempHomeFrag.setVisibility(View.GONE);

                featuredVideoAdapter.notifyDataSetChanged();

                tvFragmentAdapter.notifyDataSetChanged();

                if(getContext() != null){
                    if(Methods.getInstance().isNetworkConnected(getContext())){
                        if(status){
                            list_radio_trending.clear();

                            if(arrayList_trending.isEmpty()){
                                arrayList_trending.addAll(SharedPref.getInstance(getContext()).getTempVideoList(Constant.RADIO));

                                for(int i = 0; i < 5; ++i){
                                    list_radio_trending.add(arrayList_trending.get(i));
                                    if(i > arrayList_trending.size()){
                                        break;
                                    }
                                }

                            }else{
                                    list_radio_trending.addAll(arrayList_trending);
                                }

                                SharedPref.getInstance(getContext()).setTempVideoList(Constant.RADIO, list_radio_trending);
                            }

                        }else{
                            Toast.makeText(getContext(), "Something wrong happened, try again!", Toast.LENGTH_SHORT).show();
                            arrayList_trending.addAll(SharedPref.getInstance(getContext()).getTempVideoList(Constant.RADIO));

                            for(int i = 0; i < 5; ++i){
                                list_radio_trending.add(arrayList_trending.get(i));
                                if(i > arrayList_trending.size()){
                                    break;
                                }
                            }
                        }
                    }else{
                        Toast.makeText(getContext(), "Please connect to the internet!", Toast.LENGTH_SHORT).show();
                        arrayList_trending.addAll(SharedPref.getInstance(getContext()).getTempVideoList(Constant.RADIO));

                        for(int i = 0; i < 5; ++i){
                            list_radio_trending.add(arrayList_trending.get(i));
                            if(i > arrayList_trending.size()){
                                break;
                            }
                        }
                    }
                    radioItemAdapter.notifyDataSetChanged();
                }
            };


        LoadRadioAsync async = new LoadRadioAsync(requestBody, listener, Methods.getInstance());
        async.execute();
    }
}