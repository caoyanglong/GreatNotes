package com.zhefang.assignment2.utils;

import android.app.Activity;
import android.content.Intent;

import java.text.SimpleDateFormat;
import java.util.Date;


public class AppUtils {
    public static int parseInt(String val){
        try {
            return Integer.parseInt(val);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String parseToDate(String date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return format.format(format.parse(date));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "2018-05-24";
    }

    public static Long dateToLong(String date){
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            return format.parse(date).getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0l;
    }

    public static String getMonth(String date){
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
            return format.format(format.parse(date));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "2018-05";
    }

    public static String getYear(String date){
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy");
            return format.format(format.parse(date));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "2018";
    }

    public static String parseDateToStr(){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date=new Date();
        return sdf.format(date);
    }


    public static void shareText(Activity activity,String dlgTitle, String subject, String content) {
        if (content == null || "".equals(content)) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        if (subject != null && !"".equals(subject)) {
            intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        }

        intent.putExtra(Intent.EXTRA_TEXT, content);

        if (dlgTitle != null && !"".equals(dlgTitle)) {
            activity.startActivity(Intent.createChooser(intent, dlgTitle));
        } else {
            activity.startActivity(intent);
        }
    }
}
