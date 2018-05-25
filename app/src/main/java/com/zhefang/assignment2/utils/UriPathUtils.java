package com.zhefang.assignment2.utils;

import android.content.Context;
import android.net.Uri;

import java.io.File;



public class UriPathUtils {
    public static Uri getUri(Context context, String path){
        return Uri.fromFile(new File(path));
    }
}
