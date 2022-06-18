package com.example.finalmobile.asynctasks;

import android.os.AsyncTask;
import android.util.Log;

import com.example.finalmobile.listeners.LoadSearchVideoAsyncListener;
import com.example.finalmobile.models.Videos_M;
import com.example.finalmobile.utils.Constant;
import com.example.finalmobile.utils.JsonUtils;
import com.example.finalmobile.utils.Methods;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.RequestBody;

public class LoadSearchVideoAsync extends AsyncTask<Void, String, Boolean> {

    private RequestBody requestBody;
    private LoadSearchVideoAsyncListener listener;
    private Methods methods;
    private ArrayList<Videos_M> mVideos;

    public LoadSearchVideoAsync(Methods methods, RequestBody requestBody, LoadSearchVideoAsyncListener listener) {
        this.requestBody = requestBody;
        this.listener = listener;
        this.methods = methods;
        mVideos = new ArrayList<>();
    }

    @Override
    protected void onPreExecute() {
        listener.onStart();
        super.onPreExecute();
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        try{
            String api_url = Constant.SERVER_URL;
            String result = JsonUtils.okhttpPost(api_url, requestBody);
            JSONObject jsonObject = new JSONObject(result);
            boolean status = jsonObject.getString("status").equals("success");

            if(status){
                JSONArray jsonArray = jsonObject.getJSONArray("search");

                for (int i = 0; i < jsonArray.length(); i++){
                    JSONObject obj = jsonArray.getJSONObject(i);
                    mVideos.add(methods.getRowVideo(obj));
                }

                return true;
            }else {
                Log.e(Constant.ERR_TAG, jsonObject.getString("message"));
                return false;
            }

        }catch (Exception e){
            Log.e(Constant.ERR_TAG, e.getMessage());
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        listener.onEnd(aBoolean, mVideos);
        super.onPostExecute(aBoolean);
    }
}