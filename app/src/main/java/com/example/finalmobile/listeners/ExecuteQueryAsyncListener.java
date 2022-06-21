package com.example.finalmobile.listeners;

import com.example.finalmobile.models.Videos_M;

import java.util.ArrayList;

public interface ExecuteQueryAsyncListener {
    void onStart();
    void onEnd(boolean status);
}