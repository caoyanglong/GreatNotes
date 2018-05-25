package com.zhefang.assignment2.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.zhefang.assignment2.R;


public abstract class BaseActivity extends AppCompatActivity {
    protected TextView title;
    protected TextView back;
    protected TextView save;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onCreate();
        save = findViewById(R.id.submit);
    }

    public void setTitle(String text){
        try {
            title = findViewById(R.id.title);
            back = findViewById(R.id.back);
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(title != null){
            title.setText(text);
        }
    }

    public void hideBack(){
        if(back != null){
            back.setVisibility(View.INVISIBLE);
        }
    }

    protected abstract void onCreate();

    public static void launchActivity(Activity activity, Class<?> activityClass){
        Intent intent = new Intent(activity,activityClass);
        activity.startActivity(intent);
    }

}
