package com.example.divyanshu.smyt.Interfaces;

/**
 * Created by deii on 12/29/2015.
 */
public interface PlayerInterface {
    void onPlayed();

    void onPaused();

    void error();

    void onNextPlayed();

    void onPrevPlayed();

    void onStopped();

    void onBuffering();

    void onDestroy();
}