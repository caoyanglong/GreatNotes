package com.zhefang.assignment2.widget;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.zhefang.assignment2.R;
import com.zhefang.assignment2.home.ShoppingListActivity;

import org.chenglei.widget.datepicker.DatePicker;

/**
 * Created by caoyanglong on 2018/5/25.
 */

public class CustomDatePicker {
    public static AlertDialog showDatePicker(Activity activity, final DateChanged dateChanged,int type) {

        View view = LayoutInflater.from(activity).inflate(R.layout.custom_date_layout,null);
        @SuppressLint("WrongViewCast") final DatePicker datePicker = view.findViewById(R.id.specificDate);
        datePicker.setTextColor(Color.WHITE).setFlagTextColor(Color.WHITE).setBackground(Color.BLACK).setTextSize(25)
                .setFlagTextSize(15);
        datePicker.setDescendantFocusability(DatePicker.FOCUS_BLOCK_DESCENDANTS);
        Button yes = view.findViewById(R.id.yes);
        view.findViewById(org.chenglei.widget.datepicker.R.id.month_picker).setVisibility(type == ShoppingListActivity.year?View.INVISIBLE:View.VISIBLE);
        view.findViewById(org.chenglei.widget.datepicker.R.id.day_picker).setVisibility(type == ShoppingListActivity.day?View.VISIBLE:View.INVISIBLE);
        final AlertDialog alertDialog = new AlertDialog.Builder(activity).setTitle("tips").setView(view).create();
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if(null != dateChanged){
                   dateChanged.dateChange(datePicker.getYear()+"-"+datePicker.getMonth()+"-"+datePicker.getDayOfMonth());
                   alertDialog.cancel();
               }
            }
        });

        alertDialog.show();
        return alertDialog;
    }

    public interface DateChanged{
        void dateChange(String date);
    }
}
