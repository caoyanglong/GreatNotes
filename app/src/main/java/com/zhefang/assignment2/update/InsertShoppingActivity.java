package com.zhefang.assignment2.update;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import com.zhefang.assignment2.MyApp;
import com.zhefang.assignment2.R;
import com.zhefang.assignment2.base.BaseActivity;
import com.zhefang.assignment2.bean.ShoppingBean;
import com.zhefang.assignment2.bean.ShoppingTipsBean;
import com.zhefang.assignment2.databinding.ActivityInsertLayoutBinding;
import com.zhefang.assignment2.utils.AppUtils;
import com.zhefang.assignment2.utils.PhotoUtil;
import com.zhefang.assignment2.utils.UriPathUtils;

import java.io.File;
import java.util.Calendar;


public class InsertShoppingActivity extends BaseActivity implements DatePickerDialog.OnDateSetListener{
    private ShoppingTipsBean tipsBean;

    private DatePickerDialog datePickerDialog;

    private ActivityInsertLayoutBinding mDataBinding;
    @Override
    protected void onCreate() {
        mDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_insert_layout);
        setTitle("edit");
        tipsBean = (ShoppingTipsBean) getIntent().getSerializableExtra("data");

        findViewById(R.id.title).setVisibility(View.INVISIBLE);

        save = findViewById(R.id.submit);
        save.setVisibility(View.VISIBLE);

        mDataBinding.select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PhotoUtil.selectPictureFromAlbum(InsertShoppingActivity.this);
            }
        });
        mDataBinding.selectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view ){
                selectDate();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add();
            }
        });
    }

    private void selectDate() {
        Calendar ca = Calendar.getInstance();
        int mYear = ca.get(Calendar.YEAR);
        int mMonth = ca.get(Calendar.MONTH);
        int mDay = ca.get(Calendar.DAY_OF_MONTH);
        if(null == datePickerDialog){
            datePickerDialog = new DatePickerDialog(InsertShoppingActivity.this,InsertShoppingActivity.this,mYear,mMonth,mDay);
        }
        datePickerDialog.show();
    }

    public void add(View view){
        add();
    }

    private void add() {
        if(TextUtils.isEmpty(mDataBinding.name.getText())){
            Toast.makeText(this,"name is not null",Toast.LENGTH_LONG).show();
            return;
        }else if(TextUtils.isEmpty(mDataBinding.price.getText())){
            Toast.makeText(this,"price is not null",Toast.LENGTH_LONG).show();
            return;
        }else if(TextUtils.isEmpty(mDataBinding.count.getText())){
            Toast.makeText(this,"count is not null",Toast.LENGTH_LONG).show();
            return;
        }else if(TextUtils.isEmpty(mDataBinding.date.getText())){
            Toast.makeText(this,"date is not null",Toast.LENGTH_LONG).show();
            return;
        }
        ShoppingBean goodsBean = new ShoppingBean(mDataBinding.name.getText().toString(),
                mDataBinding.tag.getText().toString(),"",mDataBinding.desc.getText().toString(),
                AppUtils.parseInt(mDataBinding.price.getText().toString()),
                AppUtils.parseToDate(mDataBinding.date.getText().toString()),mDataBinding.wantBuy.isChecked(),
                AppUtils.parseInt(mDataBinding.count.getText().toString()));
        goodsBean.setListId(tipsBean.getId());
        goodsBean.setIcon(path);
        MyApp.getDb().save(goodsBean);
        Toast.makeText(this,"success",Toast.LENGTH_LONG).show();
        finish();
    }

    public static void lanuch(Activity activity, ShoppingTipsBean tipsBean){
        Intent intent = new Intent(activity,InsertShoppingActivity.class);
        intent.putExtra("data",tipsBean);
        activity.startActivity(intent);
    }

    private String path;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == PhotoUtil.NONE)
            return;
        if (requestCode == PhotoUtil.PHOTOGRAPH) {

            File picture = null;
            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
                picture = new File(Environment.getExternalStorageDirectory() + PhotoUtil.imageName);
                if (!picture.exists()) {
                    picture = new File(Environment.getExternalStorageDirectory() + PhotoUtil.imageName);
                }
            } else {
                picture = new File(this.getFilesDir() + PhotoUtil.imageName);
                if (!picture.exists()) {
                    picture = new File(InsertShoppingActivity.this.getFilesDir() + PhotoUtil.imageName);
                }
            }

            path = PhotoUtil.getPath(this);
            if (TextUtils.isEmpty(path)) {

                return;
            }
            Uri imageUri = UriPathUtils.getUri(this, path);
            PhotoUtil.startPhotoZoom(InsertShoppingActivity.this, Uri.fromFile(picture), PhotoUtil.PICTURE_HEIGHT, PhotoUtil.PICTURE_WIDTH, imageUri);
        }

        if (data == null)
            return;
        if (requestCode == PhotoUtil.PHOTOZOOM) {

            path = PhotoUtil.getPath(this);
            if (TextUtils.isEmpty(path)) {
                return;
            }
            Uri imageUri = UriPathUtils.getUri(this, path);
            PhotoUtil.startPhotoZoom(InsertShoppingActivity.this, data.getData(), PhotoUtil.PICTURE_HEIGHT, PhotoUtil.PICTURE_WIDTH, imageUri);
        }

        if (requestCode == PhotoUtil.PHOTORESOULT) {

            Bitmap bitmap = PhotoUtil.convertToBitmap(path,PhotoUtil.PICTURE_HEIGHT, PhotoUtil.PICTURE_WIDTH);
            if(bitmap != null){
                mDataBinding.image.setImageBitmap(bitmap);
            }

            Bitmap bitmap2 = PhotoUtil.convertToBitmap(path,120, 120);
            if(bitmap2 != null){
                mDataBinding.image.setImageBitmap(bitmap2);
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        mDataBinding.date.setText(i+"-"+(i1+1)+"-"+i2);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(null != datePickerDialog){
            datePickerDialog.cancel();
        }
    }
}
