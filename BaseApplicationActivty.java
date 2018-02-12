package com.ghat.socket;


import android.support.multidex.MultiDexApplication;



public class BaseApplicationActivty extends MultiDexApplication {
    public static NetworkManager networkManager;

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
