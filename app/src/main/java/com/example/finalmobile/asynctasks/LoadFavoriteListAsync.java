package com.example.finalmobile.asynctasks;

import android.os.AsyncTask;
import android.provider.MediaStore;

import com.example.finalmobile.listeners.LoadRadioAsyncListener;
import com.example.finalmobile.listeners.LoadSearchVideoAsyncListener;
import com.example.finalmobile.models.Category_M;
import com.example.finalmobile.models.Videos_M;
import com.example.finalmobile.utils.Constant;
import com.example.finalmobile.utils.JsonUtils;
import com.example.finalmobile.utils.Methods;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.RequestBody;

public class LoadFavoriteListAsync  extends AsyncTask<Void, String, Boolean> {
    private RequestBody requestBody;
    private LoadSearchVideoAsyncListener listener;
    private Methods methods;
    private ArrayList<Videos_M> arrayList_fav;


    public LoadFavoriteListAsync(RequestBody requestBody, LoadSearchVideoAsyncListener listener, Methods methods) {
        this.requestBody = requestBody;
        this.listener = listener;
        this.methods = methods;
        arrayList_fav = new ArrayList<Videos_M>();
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        try {
            String api_url = Constant.SERVER_URL;
            String result = JsonUtils.okhttpPost(api_url, requestBody);
            JSONObject jsonObject = new JSONObject(result);
            boolean status = jsonObject.getString("status").equals("success");

            if (status) {
                JSONArray jsonArray_fav= jsonObject.getJSONArray("list");

                for (int i = 0; i < jsonArray_fav.length(); i++){
                    JSONObject obj = jsonArray_fav.getJSONObject(i);
                    arrayList_fav.add(methods.getRowVideo(obj));
                }

                return true;
            }
            else{
                return false;
            }
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    protected void onPreExecute() {
        listener.onStart();
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        listener.onEnd(aBoolean, arrayList_fav);
        super.onPostExecute(aBoolean);
    }
}
