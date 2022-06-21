package com.example.finalmobile.listeners;

import com.example.finalmobile.models.Category_M;
import com.example.finalmobile.models.Videos_M;

import java.util.ArrayList;

public interface LoadRadioAsyncListener {
    void onStart();
    void onEnd(boolean status,
               ArrayList<Videos_M> arrayList_trending,
               ArrayList<Category_M> arrayList_category
               );
}