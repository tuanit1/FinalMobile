package com.example.finalmobile.fragments;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.finalmobile.databinding.FragmentTvDetailBinding;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.ui.PlayerControlView;
import com.google.firebase.auth.FirebaseAuth;
import com.example.finalmobile.R;
import com.example.finalmobile.activities.MainActivity;
import com.example.finalmobile.asynctasks.ExecuteQueryAsync;
import com.example.finalmobile.listeners.CheckFavListener;
import com.example.finalmobile.listeners.ExecuteQueryAsyncListener;
import com.example.finalmobile.listeners.SetFavListener;
import com.example.finalmobile.utils.Methods;
import com.squareup.picasso.Picasso;

import okhttp3.RequestBody;

public class TvDetailFragment extends Fragment {

    private FragmentTvDetailBinding binding;
    private NavController navController;
    private ExoPlayer player;
    private Boolean mIsFav = false;
    private int id;
    Bundle bundle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentTvDetailBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();
        bundle = getArguments();
        id = bundle.getInt("id");
        setUp();
        Methods.getInstance().checkVideoFav(getContext(), id, new CheckFavListener() {
            @Override
            public void onComplete(boolean isSuccess, boolean isFav) {
                if(isSuccess){
                    mIsFav = isFav;
                }
                setUpFav();
            }
        });

        return rootView;
    }

    private void setUp(){
        MainActivity.hide_Navi();
        (getActivity()).setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        navController = NavHostFragment.findNavController(this);

        binding.layoutMainDetailFrag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int state = -1;
                if(binding.layoutInfTvDetailFrag.getVisibility() == View.VISIBLE){
                    state = View.GONE;
                    binding.controlViewTVDetailFrag.hide();
                } else {
                    state = View.VISIBLE;
                    binding.controlViewTVDetailFrag.show();
                }
                binding.layoutInfTvDetailFrag.setVisibility(state);
                binding.btnOutTvDetailFrag.setVisibility(state);
            }
        });


        Picasso.get().load(bundle.getString("url_img")).into(binding.imgTvDetailFrag);
        binding.txtTvDetailFrag.setText(bundle.getString("des"));

        binding.txtTvDetailFrag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.controlViewTVDetailFrag.show();
            }
        });

        binding.btnOutTvDetailFrag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bundle.getBoolean("isHome")){
                    navController.navigate(R.id.TVDetail_to_HomeFrag);
                } else if(bundle.getBoolean("isFavorite")) {
                    navController.navigate(R.id.tv_detail_to_favorite);
                }
                else{
                    navController.navigate(R.id.fromDetailtoTVFrag);
                }
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            binding.scrollViewTvDetailFrag.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    binding.controlViewTVDetailFrag.show();
                }
            });
        }

        binding.layoutInfTvDetailFrag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.controlViewTVDetailFrag.show();
            }
        });

        player = new ExoPlayer.Builder(getContext()).build();
        player.addListener(new Player.Listener() {
            @Override
            public void onPlaybackStateChanged(int playbackState) {
                Player.Listener.super.onPlaybackStateChanged(playbackState);
                switch (playbackState) {
                    case Player.STATE_BUFFERING:
                        binding.prgTvDetailFrag.setVisibility(View.VISIBLE);
                        binding.imgTempTVDetailFrag.setVisibility(View.VISIBLE);
                        break;
                    case Player.STATE_READY:
                        binding.prgTvDetailFrag.setVisibility(View.GONE);
                        binding.imgTempTVDetailFrag.setVisibility(View.GONE);
                        break;
                    case Player.STATE_ENDED:
                        break;
                }
            }
        });
        binding.videoTvDetailFrag.setUseController(false);
        binding.videoTvDetailFrag.setPlayer(player);

        binding.controlViewTVDetailFrag.addVisibilityListener(new PlayerControlView.VisibilityListener() {
            @Override
            public void onVisibilityChange(int visibility) {
                if(visibility == View.VISIBLE){
                    binding.layoutInfTvDetailFrag.setVisibility(View.VISIBLE);
                    binding.btnOutTvDetailFrag.setVisibility(View.VISIBLE);
                } else {
                    binding.layoutInfTvDetailFrag.setVisibility(View.GONE);
                    binding.btnOutTvDetailFrag.setVisibility(View.GONE);
                }
            }
        });

        binding.controlViewTVDetailFrag.setPlayer(player);
        binding.controlViewTVDetailFrag.setShowNextButton(false);
        binding.controlViewTVDetailFrag.setShowPreviousButton(false);

        MediaItem mediaItem = MediaItem.fromUri(bundle.getString("url"));
        player.setMediaItem(mediaItem);
        player.prepare();
        player.play();

        binding.imgLikeTvdetailFrag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Methods.getInstance().setFavState(getContext(), id, !mIsFav, new SetFavListener() {
                    @Override
                    public void onComplete(boolean isSuccess) {
                        if(isSuccess){
                            mIsFav = !mIsFav;
                            if(mIsFav){
                                Picasso.get()
                                        .load(R.drawable.ic_heart4_check)
                                        .into(binding.imgLikeTvdetailFrag);
                            }else{
                                Picasso.get()
                                        .load(R.drawable.ic_heart4_uncheckpng)
                                        .into(binding.imgLikeTvdetailFrag);
                            }
                        }
                    }
                });
                binding.controlViewTVDetailFrag.show();
            }
        });

        binding.imgreport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FirebaseAuth.getInstance().getCurrentUser()!=null){
                    showReportDialog();
                }
                else{
                    Toast.makeText(getContext(), "Please login first!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void showReportDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view1 = LayoutInflater.from(getContext()).inflate(R.layout.dialog_report_landscape_layout, null,false);
        builder.setView(view1);
        builder.setCancelable(false);

        final AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ConstraintLayout cs_main = view1.findViewById(R.id.cs_main);
        Button btn_submit = view1.findViewById(R.id.btn_submit);
        Button btn_cancel = view1.findViewById(R.id.btn_cancel);
        EditText edt_report_content = view1.findViewById(R.id.edt_report_content);

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        try{
            btn_submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String content = edt_report_content.getText().toString();
                    ExecuteQueryAsyncListener listener = new ExecuteQueryAsyncListener() {
                        @Override
                        public void onStart() {}

                        @Override
                        public void onEnd(boolean status) {
                            if(status){
                            }else{
                                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                            }
                        }

                    };

                    Bundle bundle = new Bundle();
                    bundle.putString("uid", FirebaseAuth.getInstance().getCurrentUser().getUid());
                    bundle.putString("report_content",content);
                    bundle.putInt("vid_id", id);

                    RequestBody requestBody = Methods.getInstance().getReportRequestBody("INSERT_REPORT",bundle);
                    ExecuteQueryAsync async = new ExecuteQueryAsync(requestBody, listener);
                    async.execute();
                    alertDialog.dismiss();
                    Toast.makeText(getContext(), "Thank you for your report!", Toast.LENGTH_SHORT).show();
                }
            });

        }
        catch (Exception e){
            Toast.makeText(getContext(), "Please input your report!", Toast.LENGTH_SHORT).show();
        }

        alertDialog.show();

    }

    private void setUpFav(){
        if(mIsFav){
            Picasso.get()
                    .load(R.drawable.ic_heart4_check)
                    .into(binding.imgLikeTvdetailFrag);
        }else{
            Picasso.get()
                    .load(R.drawable.ic_heart4_uncheckpng)
                    .into(binding.imgLikeTvdetailFrag);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(player!=null){
            player.stop();
        }
        MainActivity.show_Navi();
        getActivity().setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
}