package com.example.finalmobile.listeners;

public interface CheckRatingAsyncListener {
    void onStart();
    void onEnd(boolean status, double rate);
}