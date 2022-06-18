package com.example.finalmobile.listeners;

public interface SetRatingAsyncListener {
    void onStart();
    void onEnd(boolean status, float returnRate);
}
