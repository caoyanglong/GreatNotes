package com.zhefang.assignment2;

import android.app.Application;

import net.tsz.afinal.FinalDb;

public class MyApp extends Application {
    private final static String DB_NAME = "greatNotes";
    private static FinalDb finalDb;
    @Override
    public void onCreate() {
        super.onCreate();
        finalDb = FinalDb.create(this,DB_NAME);
    }


    public static FinalDb getDb(){
        return finalDb;
    }



}
