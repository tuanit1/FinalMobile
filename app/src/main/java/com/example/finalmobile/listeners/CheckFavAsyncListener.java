package com.example.finalmobile.listeners;

public interface CheckFavAsyncListener {
    void onStart();
    void onEnd(boolean status, boolean isFav);
}