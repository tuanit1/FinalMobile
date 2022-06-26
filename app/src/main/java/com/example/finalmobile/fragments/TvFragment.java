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
    private NavController navController;
    private static ArrayList<Category_M> list_category;
    private static ArrayList<Videos_M> list_video;
    private static boolean first_time = true;
    private ArrayList<Videos_M> list_cate_video;
    private CateTvFragmentAdapter catetvFragmentAdapter;
    private TVFragmentAdapter tvFragmentAdapter;
    private static boolean check_internet = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTvBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();
        navController = NavHostFragment.findNavController(this);

        list_cate_video = new ArrayList<>();
        if(first_time || list_video.isEmpty() || list_category.isEmpty()) {
            list_cate_video = new ArrayList<>();
            list_video = new ArrayList<>();
            list_category = new ArrayList<>();
            list_category.add(
                    new Category_M(-1, "All", "https://d1j8r0kxyu9tj8.cloudfront.net/images/1566809284X4pyEDCj7CFMsGu.jpg", 1)
            );
            Load_TV_Screen();
            first_time = false;
        } else {
            list_cate_video.addAll(list_video);
        }

        CountDownTimer countDownTimer = new CountDownTimer(200,1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                if(getContext() != null){
                    if(check_internet !=
                            Methods.getInstance().isNetworkConnected(getContext()) &&
                            !check_internet ){
                        Load_TV_Screen();
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
        return rootView;
    }

    private void setUp(){
        navController = NavHostFragment.findNavController(this);

        binding.swiperTvFrag.setColorSchemeColors(getResources().getColor(R.color.neonGreen));
        binding.swiperTvFrag.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Load_TV_Screen();
                binding.swiperTvFrag.setRefreshing(false);
            }
        });
        catetvFragmentAdapter = new CateTvFragmentAdapter(list_category, (MainActivity) getActivity(),
                new OnHomeItemClickListeners() {
            @Override
            public void onClick_homeItem(int position) {
                catetvFragmentAdapter.setSelected_index(position);
                catetvFragmentAdapter.notifyDataSetChanged();
                list_cate_video.clear();
                if(list_category.get(position).getCat_id() == -1){
                    list_cate_video.addAll(list_video);
                } else {
                    for(Videos_M videos_m : list_video){
                        if(videos_m.getCat_id() == list_category.get(position).getCat_id()){
                            list_cate_video.add(videos_m);
                        }
                    }
                }
                tvFragmentAdapter.setList_TV(list_cate_video);
                tvFragmentAdapter.notifyDataSetChanged();
            }
        });
        binding.rclCategoryTvFrag.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        binding.rclCategoryTvFrag.setAdapter(catetvFragmentAdapter);

        ConstraintLayout.LayoutParams layoutParams_TV_item = new ConstraintLayout.LayoutParams(getActivity().getResources().
                getDisplayMetrics().widthPixels*1/3 - 40, (getActivity().getResources().getDisplayMetrics().widthPixels)*1/3*3/4 - 40);
        layoutParams_TV_item.setMargins(20, 10,20,10);
        tvFragmentAdapter = new TVFragmentAdapter(list_cate_video, layoutParams_TV_item, new OnHomeItemClickListeners() {
            @Override
            public void onClick_homeItem(int position) {
                Bundle bundle = new Bundle();
                bundle.putString("url", list_cate_video.get(position).getVid_url());
                bundle.putString("url_img", list_cate_video.get(position).getVid_thumbnail());
                bundle.putString("des", list_cate_video.get(position).getVid_description());
                bundle.putInt("id", list_cate_video.get(position).getVid_id());
                bundle.putBoolean("isHome", false);
                navController.navigate(R.id.fromTVToDetail, bundle);
            }
        });
        binding.rclItemTvFrag.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        binding.rclItemTvFrag.setAdapter(tvFragmentAdapter);

        binding.sVTvFragment.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                onSearch(newText);
                if(newText.length() == 0){
                    tvFragmentAdapter.setList_TV(list_cate_video);
                    tvFragmentAdapter.notifyDataSetChanged();
                }
                return false;
            }
        });
    }

    private void onSearch(String text){
        ArrayList<Videos_M> list_search = new ArrayList<>();
        for(Videos_M i : list_cate_video){
            if(i.getVid_title().toLowerCase().contains(text.toLowerCase()))
                list_search.add(i);
        }
        if(list_search.isEmpty()) {
            if (text.length() > 0)
                Toast.makeText(getActivity(), "No TV Founds", Toast.LENGTH_SHORT).show();
        } else {
            tvFragmentAdapter.setList_TV(list_search);
            tvFragmentAdapter.notifyDataSetChanged();
        }
    }

    private void Load_TV_Screen(){
        RequestBody requestBody = Methods.getInstance().GetTVRequestBody("LOAD_TV_SCREEN", null);
        LoadTVAsyncListener listener = new LoadTVAsyncListener() {
            @Override
            public void onPre() {
                binding.prgTVFrag.setVisibility(View.VISIBLE);
                binding.imgTempTVFragment.setVisibility(View.VISIBLE);
                list_category.clear();
                list_cate_video.clear();
                list_video.clear();
            }

            @Override
            public void onEnd(Boolean ablBoolean, ArrayList<Videos_M> list_tv_all,
                              ArrayList<Videos_M> list_tv_trending, ArrayList<Category_M> list_categList_tv) {
                binding.prgTVFrag.setVisibility(View.GONE);
                binding.imgTempTVFragment.setVisibility(View.GONE);

                if(getContext() != null){
                    if(Methods.getInstance().isNetworkConnected(getContext())){
                        if(ablBoolean){
                            TvFragment.list_video.clear();
                            TvFragment.list_category.clear();

                            if(list_tv_all.isEmpty()){
                                list_video.addAll(SharedPref.getInstance(getContext()).getTempVideoList(Constant.TV));
                                list_cate_video.addAll(SharedPref.getInstance(getContext()).getTempVideoList(Constant.TV));
                            }else{
                                list_video.addAll(list_tv_all);
                                list_cate_video.addAll(list_tv_all);
                                SharedPref.getInstance(getContext()).setTempVideoList(Constant.TV, list_video);
                            }

                            if(list_categList_tv.isEmpty()){
                                list_category.addAll(SharedPref.getInstance(getContext()).getTempCategoryList(Constant.TV));
                            }else{
                                list_category.addAll(list_categList_tv);
                            }


//
//                            TvFragment.this.list_cate_video.clear();
//                            TvFragment.list_video.addAll(list_tv_all);
//                            TvFragment.this.list_cate_video.addAll(list_tv_all);
//                            TvFragment.list_category.addAll(list_categList_tv);

                        }else{
                            Toast.makeText(getContext(), "Something wrong happened, try again!", Toast.LENGTH_SHORT).show();
                            list_video.addAll(SharedPref.getInstance(getContext()).getTempVideoList(Constant.TV));
                            list_cate_video.addAll(SharedPref.getInstance(getContext()).getTempVideoList(Constant.TV));
                            list_category.addAll(SharedPref.getInstance(getContext()).getTempCategoryList(Constant.TV));
                        }
                    }else{
                        Toast.makeText(getContext(), "Please connect to the internet!", Toast.LENGTH_SHORT).show();
                        list_video.addAll(SharedPref.getInstance(getContext()).getTempVideoList(Constant.TV));
                        list_cate_video.addAll(SharedPref.getInstance(getContext()).getTempVideoList(Constant.TV));
                        list_category.addAll(SharedPref.getInstance(getContext()).getTempCategoryList(Constant.TV));
                    }

                    list_category.add(0,
                            new Category_M(-1, "All", "https://d1j8r0kxyu9tj8.cloudfront.net/images/1566809284X4pyEDCj7CFMsGu.jpg", 1)
                    );

                    tvFragmentAdapter.notifyDataSetChanged();
                    catetvFragmentAdapter.notifyDataSetChanged();
                }
            }
        };
        LoadTVAsync async = new LoadTVAsync(requestBody, listener);
        async.execute();
    }
}