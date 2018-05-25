package com.zhefang.assignment2.detail;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.BitmapFactory;
import android.view.View;

import com.zhefang.assignment2.MyApp;
import com.zhefang.assignment2.R;
import com.zhefang.assignment2.base.BaseActivity;
import com.zhefang.assignment2.bean.ShoppingBean;
import com.zhefang.assignment2.databinding.ActivityShoppingDetailsBinding;
import com.zhefang.assignment2.update.UpdateShoppingActivity;



public class ShopsDetailsActivity extends BaseActivity {

    private ShoppingBean goodsBean;


    private ActivityShoppingDetailsBinding mDataBinding;
    public void onCreate(){
        mDataBinding = DataBindingUtil.setContentView(this,R.layout.activity_shopping_details);

        goodsBean = (ShoppingBean) getIntent().getSerializableExtra("data");
        updateView();
    }


    private void updateView() {
        setTitle(goodsBean.getName());
        try {
            mDataBinding.icon.setImageBitmap(BitmapFactory.decodeFile(goodsBean.getIcon()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        mDataBinding.text.setText("name:"+goodsBean.getName()+"\n"+
                "price:"+goodsBean.getPrice()+"\n"+
                "count:"+goodsBean.getCount()+"\n"+
                "categroy:"+goodsBean.getDesc()+"\n"+
                "wantBuy:"+goodsBean.isWantBuy()+"\n"+
                "date:"+goodsBean.getDate()+"\n");
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            if(MyApp.getDb().findAllByWhere(ShoppingBean.class, "listId = " + goodsBean.getId()).get(0) != null){
             goodsBean = MyApp.getDb().findAllByWhere(ShoppingBean.class, "id = " + goodsBean.getId()).get(0);
            }
            updateView();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void launch(Activity context, ShoppingBean goodsBean){
        Intent intent = new Intent(context,ShopsDetailsActivity.class);
        intent.putExtra("data",goodsBean);
        context.startActivity(intent);
    }

    public void edit(View view){
        UpdateShoppingActivity.launch(ShopsDetailsActivity.this,goodsBean,0);
    }
}
