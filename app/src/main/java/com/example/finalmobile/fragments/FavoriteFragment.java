package com.example.finalmobile.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.example.finalmobile.R;
import com.example.finalmobile.adapters.ViewPagerAdapter;
import com.example.finalmobile.databinding.FragmentFavoriteBinding;
import com.example.finalmobile.listeners.FavoriteToDetailListener;

public class FavoriteFragment extends Fragment {

    private ViewPagerAdapter viewPagerAdapter;
    private View rootView;
    private NavController navController;
    private FragmentFavoriteBinding binding;
    private FavoriteToDetailListener listener;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFavoriteBinding.inflate(inflater, container, false);
        rootView = binding.getRoot();
        navController = NavHostFragment.findNavController(this);

        listener = new FavoriteToDetailListener() {
            @Override
            public void onDirect(int type, Bundle bundle) {
                switch(type){
                    case 1:
                        navController.navigate(R.id.favorite_to_video_detail, bundle);
                        break;
                    case 2:
                        navController.navigate(R.id.favorite_to_tv_detail, bundle);
                        break;
                    case 3:
                        navController.navigate(R.id.favorite_to_radio_detail, bundle);
                        break;
                }

            }
        };


        viewPagerAdapter = new ViewPagerAdapter(listener, getChildFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        binding.viewpager.setAdapter(viewPagerAdapter);

        binding.tablayout.setupWithViewPager(binding.viewpager);

        viewPagerAdapter.notifyDataSetChanged();

        binding.imvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.favorite_to_more);
            }
        });

        binding.searchView2.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText!=null){
                    switch( binding.viewpager.getCurrentItem()){
                        case 0:
                            viewPagerAdapter.onVideoPage(newText);
                            break;
                        case 1:
                            viewPagerAdapter.onTVPage(newText);
                            break;
                        case 2:
                            viewPagerAdapter.onRadioPage(newText);
                            break;
                    }
                }
                return false;
            }
        });




        return rootView;
    }
}