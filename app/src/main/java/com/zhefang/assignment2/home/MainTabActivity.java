package com.zhefang.assignment2.home;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zhefang.assignment2.MyApp;
import com.zhefang.assignment2.R;
import com.zhefang.assignment2.base.BaseActivity;
import com.zhefang.assignment2.bean.ShoppingBean;
import com.zhefang.assignment2.bean.ShoppingTipsBean;
import com.zhefang.assignment2.databinding.ActivityMainTabLayoutBinding;
import com.zhefang.assignment2.login.LoginActivity;
import com.zhefang.assignment2.utils.AppUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhefang on 2018/5/19.
 */

public class MainTabActivity extends BaseActivity {

    private MyAdapter adapter;
    private Dialog createDialog;
    private EditText editText;
    private List<ShoppingTipsBean> data = new ArrayList<>();
    private ActivityMainTabLayoutBinding mDataBinding;

    @Override
    protected void onCreate() {
        mDataBinding = DataBindingUtil.setContentView(this,R.layout.activity_main_tab_layout);

        adapter = new MyAdapter();
        mDataBinding.list.setLayoutManager(new LinearLayoutManager(this));
        mDataBinding.list.setAdapter(adapter);
        updateList();
        mDataBinding.create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (createDialog == null) {
                    editText = new EditText(MainTabActivity.this);
                    editText.setHint("Name Your List");
                    createDialog = new AlertDialog.Builder(MainTabActivity.this)
                            .setView(editText)
                            .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    String listName = editText.getText().toString();
                                    if (TextUtils.isEmpty(listName)) {
                                        Toast.makeText(MainTabActivity.this, "name is not empty", Toast.LENGTH_LONG).show();
                                        return;
                                    }
                                    else{
                                        ShoppingTipsBean tipsBean = new ShoppingTipsBean(listName, AppUtils.parseDateToStr());
                                        MyApp.getDb().save(tipsBean);
                                        updateList();
                                    }
                                }
                            })
                            .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            })
                            .create();
                }
                editText.setText("");
                editText.setHint("Name Your List");
                createDialog.show();
            }
        });
        setTitle("My Shopping List");
        hideBack();
    }

    private class MyAdapter extends RecyclerView.Adapter<MyViewHolder>{
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MyViewHolder(getLayoutInflater().inflate(R.layout.shoping_tips_item,parent,false));
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            final ShoppingTipsBean tipsBean = data.get(position);
            if(null != tipsBean){
                holder.name.setText(tipsBean.getName());
                holder.date.setText(tipsBean.getDate());
                holder.totalPrice.setText("$"+tipsBean.getTotalPrice());
            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ShoppingListActivity.lanuch(MainTabActivity.this,tipsBean);
                }
            });
            holder.share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AppUtils.shareText(MainTabActivity.this,"",tipsBean.getName(),getShareText(tipsBean.getId()));
                }
            });

            holder.complete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MyApp.getDb().delete(tipsBean);
                    MyApp.getDb().deleteByWhere(ShoppingBean.class,"listId = "+tipsBean.getId());
                    updateList();
                }
            });
        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }
    private class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView name,date,totalPrice,share,complete;
        public MyViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            date = itemView.findViewById(R.id.date);
            totalPrice = itemView.findViewById(R.id.totalPrice);
            share = itemView.findViewById(R.id.share);
            complete = itemView.findViewById(R.id.complete);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != createDialog) {
            createDialog.cancel();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateList();
    }

    private void updateList(){
        data.clear();
        data.addAll(MyApp.getDb().findAll(ShoppingTipsBean.class));
        for (ShoppingTipsBean tipsBean:data){
            int totalPrice = 0;
            List<ShoppingBean> goodsBeans = MyApp.getDb().findAllByWhere(ShoppingBean.class,"listId = "+tipsBean.getId());
            if(null != goodsBeans && goodsBeans.size() > 0){
                for (ShoppingBean g:goodsBeans){
                    if(g.isWantBuy()){
                        totalPrice += g.getCount()*g.getPrice();
                    }
                }
            }
            tipsBean.setTotalPrice(totalPrice+"");
        }
        adapter.notifyDataSetChanged();
        if(data.size() > 0){
            mDataBinding.tips.setVisibility(View.GONE);
        }
    }


    private String getShareText(int id){
        List<ShoppingBean> goodsBeans = MyApp.getDb().findAllByWhere(ShoppingBean.class,"listId = "+id);
        StringBuffer stringBuffer = new StringBuffer();
//        stringBuffer.append(title+"\n");
        for (ShoppingBean bean:goodsBeans){
            stringBuffer.append(bean.getCount() + bean.getName()+"\n");
        }

        return stringBuffer.toString();
    }

    public void login(View view){
        LoginActivity.launchActivity(MainTabActivity.this,LoginActivity.class);
    }
}
