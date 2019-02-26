package com.dacer.androidcharts;

import android.util.Log;

public class TempLog {
    public static void show(String s){
        Log.e("Log--->",s);
    }
    public static void show(int i){
        Log.e("Log--->",String.valueOf(i));
    }public static void show(float i){
        Log.e("Log--->",String.valueOf(i));
    }
}
