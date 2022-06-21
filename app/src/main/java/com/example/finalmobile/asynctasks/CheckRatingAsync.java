package com.example.finalmobile.asynctasks;

import android.os.AsyncTask;
import android.util.Log;

import com.example.finalmobile.listeners.CheckFavAsyncListener;
import com.example.finalmobile.listeners.CheckRatingAsyncListener;
import com.example.finalmobile.utils.Constant;
import com.example.finalmobile.utils.JsonUtils;
import com.example.finalmobile.utils.Methods;

import org.json.JSONObject;

import okhttp3.RequestBody;

public class CheckRatingAsync extends AsyncTask<Void, String, Boolean> {

    private RequestBody requestBody;
    private CheckRatingAsyncListener listener;
    private Methods methods;
    private double rate = 0;

    public CheckRatingAsync(RequestBody requestBody, CheckRatingAsyncListener listener, Methods methods) {
        this.requestBody = requestBody;
        this.listener = listener;
        this.methods = methods;
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
            JSONObject returnObj = new JSONObject(result);
            boolean status = returnObj.getString("status").equals("success");

            if(status){

                rate = returnObj.getDouble("rate");

                return true;
            }else {
                Log.e(Constant.ERR_TAG, returnObj.getString("message"));
                return false;
            }

        }catch (Exception e){
            Log.e(Constant.ERR_TAG, e.getMessage());
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean status) {
        listener.onEnd(status, rate);
        super.onPostExecute(status);
    }
}
