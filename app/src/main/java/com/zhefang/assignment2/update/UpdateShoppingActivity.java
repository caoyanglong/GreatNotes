package com.zhefang.assignment2.update;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.zhefang.assignment2.databinding.ActivityInsertLayoutBinding;
import com.zhefang.assignment2.utils.AppUtils;
import com.zhefang.assignment2.utils.PhotoUtil;
import com.zhefang.assignment2.utils.UriPathUtils;

import java.io.File;
import java.util.Calendar;


public class UpdateShoppingActivity extends BaseActivity implements DatePickerDialog.OnDateSetListener{
    private ShoppingBean goodsBean;
    private int type;

    private DatePickerDialog datePickerDialog;

    private ActivityInsertLayoutBinding mDataBinding;
    @Override
    protected void onCreate() {
        mDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_insert_layout);
        setTitle("UPDATE");
        findViewById(R.id.title).setVisibility(View.INVISIBLE);
        type = getIntent().getIntExtra("type",0);
        goodsBean = (ShoppingBean) getIntent().getSerializableExtra("data");


        save = findViewById(R.id.submit);
        save.setVisibility(View.VISIBLE);
        save.setText("UPDATE");

        mDataBinding.name.setText(goodsBean.getName());
        mDataBinding.price.setText(goodsBean.getPrice()+"");
        mDataBinding.count.setText(goodsBean.getCount()+"");
        mDataBinding.desc.setText(goodsBean.getDesc());
        mDataBinding.tag.setText(goodsBean.getTag());
        mDataBinding.wantBuy.setChecked(goodsBean.isWantBuy());
        mDataBinding.date.setText(goodsBean.getDate());

        findViewById(R.id.delete).setVisibility(type == 0?View.VISIBLE:View.GONE);
        try {
            mDataBinding.image.setImageBitmap(BitmapFactory.decodeFile(goodsBean.getIcon()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        mDataBinding.select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PhotoUtil.selectPictureFromAlbum(UpdateShoppingActivity.this);
            }
        });
        mDataBinding.selectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view ){
                Calendar ca = Calendar.getInstance();
                int mYear = ca.get(Calendar.YEAR);
                int mMonth = ca.get(Calendar.MONTH);
                int mDay = ca.get(Calendar.DAY_OF_MONTH);
                if(null == datePickerDialog){
                    datePickerDialog = new DatePickerDialog(UpdateShoppingActivity.this,UpdateShoppingActivity.this,mYear,mMonth,mDay);
                }
                datePickerDialog.show();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update();
            }
        });
    }



    public void add(View view){
        update();
    }

    private void update() {
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
        goodsBean.setName(mDataBinding.name.getText().toString());
        goodsBean.setTag(mDataBinding.tag.getText().toString());
        goodsBean.setDesc(mDataBinding.desc.getText().toString());
        goodsBean.setPrice(AppUtils.parseInt(mDataBinding.price.getText().toString()));
        goodsBean.setDate(AppUtils.parseToDate(mDataBinding.date.getText().toString()));
        goodsBean.setWantBuy(mDataBinding.wantBuy.isChecked());
        goodsBean.setCount(AppUtils.parseInt(mDataBinding.count.getText().toString()));
        if(type == 0){
            MyApp.getDb().update(goodsBean);
        }else{
            MyApp.getDb().save(goodsBean);
        }
        Toast.makeText(this,"success",Toast.LENGTH_LONG).show();
        setResult(1);
        finish();
    }

    public void delete(View view){
        MyApp.getDb().delete(goodsBean);
        finish();
    }

    public static void launch(Activity context, ShoppingBean goodsBean, int type){
        Intent intent = new Intent(context,UpdateShoppingActivity.class);
        intent.putExtra("data",goodsBean);
        intent.putExtra("type",type);
        context.startActivityForResult(intent,0);
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
                    picture = new File(UpdateShoppingActivity.this.getFilesDir() + PhotoUtil.imageName);
                }
            }

            path = PhotoUtil.getPath(this);
            if (TextUtils.isEmpty(path)) {

                return;
            }
            Uri imageUri = UriPathUtils.getUri(this, path);
            PhotoUtil.startPhotoZoom(UpdateShoppingActivity.this, Uri.fromFile(picture), PhotoUtil.PICTURE_HEIGHT, PhotoUtil.PICTURE_WIDTH, imageUri);
        }

        if (data == null)
            return;

        if (requestCode == PhotoUtil.PHOTOZOOM) {

            path = PhotoUtil.getPath(this);
            if (TextUtils.isEmpty(path)) {
                return;
            }
            Uri imageUri = UriPathUtils.getUri(this, path);
            PhotoUtil.startPhotoZoom(UpdateShoppingActivity.this, data.getData(), PhotoUtil.PICTURE_HEIGHT, PhotoUtil.PICTURE_WIDTH, imageUri);
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
}
