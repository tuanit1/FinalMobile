package com.example.finalmobile.fragments;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.finalmobile.R;
import com.example.finalmobile.activities.DailymotionPlayerActivity;
import com.example.finalmobile.activities.YoutubePlayerActivity;
import com.example.finalmobile.adapters.FeaturedVideoAdapter;
import com.example.finalmobile.asynctasks.LoadDailymotionVideoAsync;
import com.example.finalmobile.asynctasks.LoadYoutubeVideoAsync;
import com.example.finalmobile.databinding.FragmentYoutubeSearchBinding;
import com.example.finalmobile.listeners.LoadSearchVideoAsyncListener;
import com.example.finalmobile.listeners.LoadYoutubeSearchAsyncListener;
import com.example.finalmobile.listeners.OnVideoFeatureClickListener;
import com.example.finalmobile.models.Videos_M;
import com.example.finalmobile.utils.Methods;

import java.util.ArrayList;

public class YoutubeSearchFragment extends Fragment {

    private View rootView;
    private NavController navController;
    private FragmentYoutubeSearchBinding binding;
    private ArrayList<Videos_M> mVideos;
    private String search_text = "";
    private String type = "";
    private int cat_id = 0;
    private String page = "";
    private int step = 20;
    private boolean loading = true;
    private int width = 0;
    private int height = 0;
    private FeaturedVideoAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentYoutubeSearchBinding.inflate(inflater, container, false);
        rootView = binding.getRoot();
        navController = NavHostFragment.findNavController(this);

        search_text = getArguments().getString("search_text");
        binding.tvResult.setText("Results match with \""+ search_text +"\"");

        mVideos = new ArrayList<>();

        width = getContext().getResources().getDisplayMetrics().widthPixels;
        height = getContext().getResources().getDisplayMetrics().heightPixels;

        SetupView();

        LoadSearchVideo(false);

        return rootView;
    }

    private void LoadSearchVideo(boolean isLazy) {

        Bundle bundle = new Bundle();

        bundle.putString("search_text", search_text);
        bundle.putString("page", page);
        bundle.putInt("step", step);

        LoadYoutubeVideoAsync async = new LoadYoutubeVideoAsync(bundle, new LoadYoutubeSearchAsyncListener() {
            @Override
            public void onStart() {
                if(isLazy){
                    binding.progressBarLazy.setVisibility(View.VISIBLE);
                }else{
                    binding.rv.setVisibility(View.GONE);
                    binding.progressBar.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onEnd(boolean status, String nextPageToken, ArrayList<Videos_M> arrayList) {
                if(getContext() != null){
                    if(Methods.getInstance().isNetworkConnected(getContext())){
                        if(status){

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if(isLazy){

                                        binding.progressBarLazy.setVisibility(View.GONE);

                                        if(arrayList.isEmpty()){
                                            Toast.makeText(getContext(), "No more videos to get!", Toast.LENGTH_SHORT).show();
                                        }

                                    }else{
                                        binding.rv.setVisibility(View.VISIBLE);
                                        binding.progressBar.setVisibility(View.GONE);

                                        if(arrayList.isEmpty()){
                                            binding.tvNotFound.setVisibility(View.VISIBLE);
                                            binding.rv.setVisibility(View.GONE);
                                        }
                                    }

                                    page = nextPageToken;
                                    mVideos.addAll(arrayList);
                                    adapter.notifyItemRangeInserted(mVideos.size() + 1, arrayList.size());
                                    loading = true;
                                }
                            }, 300);

                        }else{
                            Toast.makeText(getContext(), "Something wrong happened, try again!", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(getContext(), "Please connect to the internet!", Toast.LENGTH_SHORT).show();
                    }
                }
            }

        });

        async.execute();
    }

    private void SetupView() {
        int height = getContext().getResources().getDisplayMetrics().heightPixels;
        LinearLayout.LayoutParams layoutParams00 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) Math.round(height*0.15));
        binding.llTop.setLayoutParams(layoutParams00);

        binding.btnBack.setOnClickListener(v->{
            navController.navigate(R.id.YoutubeSearchBackToVideo);
        });

        binding.btnBackFloat.setOnClickListener(v->{
            navController.navigate(R.id.YoutubeSearchBackToVideo);
        });

        binding.btnBackFloat.hide();

        GridLayoutManager llm = new GridLayoutManager(getContext(), 2);

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) Math.round(width/2*0.8));
        layoutParams.setMargins(10,20,10,20);

        adapter = new FeaturedVideoAdapter(Methods.getInstance(), layoutParams, mVideos, new OnVideoFeatureClickListener() {
            @Override
            public void onClick(int position) {
                String vid_id = mVideos.get(position).getVid_url();

                Intent intent = new Intent(getContext(), YoutubePlayerActivity.class);
                intent.putExtra("vid_id", vid_id);

                startActivityForResult(intent, 225);
            }
        });

        binding.rv.setItemAnimator(new DefaultItemAnimator());
        binding.rv.setLayoutManager(llm);
        binding.rv.setAdapter(adapter);

        binding.scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                if(0 <= scrollY && scrollY < v.getMeasuredHeight()/3){
                    binding.btnBackFloat.hide();
                }else{
                    binding.btnBackFloat.show();
                }

                if(v.getChildAt(v.getChildCount() - 1) != null) {
                    if(loading){
                        if ((scrollY >= (v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight())) &&
                                scrollY > oldScrollY) {
                            loading = false;
                            LoadSearchVideo(true);
                        }
                    }

                }
            }
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 225 && resultCode == -1){
            int orientation = this.getResources().getConfiguration().orientation;
            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
        }

    }
}