package com.example.finalmobile.listeners;

import com.example.finalmobile.models.Videos_M;

import java.util.ArrayList;

public interface LoadSearchVideoAsyncListener {
    void onStart();
    void onEnd(boolean status, ArrayList<Videos_M> arrayList);
}
