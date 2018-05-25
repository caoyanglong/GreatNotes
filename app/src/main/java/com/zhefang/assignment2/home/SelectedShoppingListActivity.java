package com.zhefang.assignment2.home;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.BitmapFactory;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhefang.assignment2.MyApp;
import com.zhefang.assignment2.R;
import com.zhefang.assignment2.base.BaseActivity;
import com.zhefang.assignment2.bean.ShoppingBean;
import com.zhefang.assignment2.databinding.ActivitySelectedListLayoutBinding;
import com.zhefang.assignment2.update.UpdateShoppingActivity;
import com.zhefang.assignment2.utils.AppUtils;

import java.util.ArrayList;
import java.util.List;


public class SelectedShoppingListActivity extends BaseActivity {
    private List<ShoppingBean> goodsBeans = new ArrayList<>();
    private MyAdapter adapter;
    private int type;
    private String valce;
    private int listId;
    private ActivitySelectedListLayoutBinding mDataBing;
    @Override
    protected void onCreate() {
        mDataBing = DataBindingUtil.setContentView(this,R.layout.activity_selected_list_layout);

        adapter = new MyAdapter();
        mDataBing.list.setLayoutManager(new LinearLayoutManager(this));
        mDataBing.list.setAdapter(adapter);
        type = getIntent().getIntExtra("data",0);
        valce = getIntent().getStringExtra("valce");
        listId = getIntent().getIntExtra("listId",0);
        setTitle(valce);
        goodsBeans.addAll(MyApp.getDb().findAll(ShoppingBean.class));
        List<ShoppingBean> temp = new ArrayList<>();
        for (ShoppingBean goodsBean:goodsBeans){
            if(type == 0){
                if(TextUtils.equals(goodsBean.getTag(),valce)){
                    temp.add(goodsBean);
                }
            }else {
                if(TextUtils.equals(goodsBean.getDesc(),valce)){
                    temp.add(goodsBean);
                }
            }
        }
        goodsBeans.clear();
        goodsBeans.addAll(temp);

        adapter.notifyDataSetChanged();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title, desc, date, edit, delete;
        CheckBox wantBuy;
        ImageView icon;

        public MyViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            desc = itemView.findViewById(R.id.desc);
            date = itemView.findViewById(R.id.date);
            wantBuy = itemView.findViewById(R.id.wantBuy);
            edit = itemView.findViewById(R.id.edit);
            delete = itemView.findViewById(R.id.delete);
            icon = itemView.findViewById(R.id.icon);
        }
    }

    class MyAdapter extends RecyclerView.Adapter<SelectedShoppingListActivity.MyViewHolder> {
        @Override
        public SelectedShoppingListActivity.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new SelectedShoppingListActivity.MyViewHolder(getLayoutInflater().inflate(R.layout.shopping_list_item, parent, false));
        }

        @Override
        public void onBindViewHolder(SelectedShoppingListActivity.MyViewHolder holder, int position) {
            final ShoppingBean bean = goodsBeans.get(position);
            holder.title.setText(bean.getName());
            holder.desc.setText(bean.getDesc());
            holder.wantBuy.setChecked(bean.isWantBuy());

            try {
                holder.icon.setImageBitmap(BitmapFactory.decodeFile(bean.getIcon()));
            } catch (Exception e) {
                e.printStackTrace();
            }

            holder.edit.setText("add");
            holder.delete.setVisibility(View.GONE);
            holder.date.setText(bean.getDate());
            holder.edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ShoppingBean goodsBean = new ShoppingBean(bean.getName(),bean.getTag(),bean.getType(),bean.getDesc(),bean.getPrice(), AppUtils.parseDateToStr(),bean.isWantBuy(),bean.getCount());
                    goodsBean.setIcon(bean.getIcon());
                    goodsBean.setListId(listId);
                    UpdateShoppingActivity.launch(SelectedShoppingListActivity.this,goodsBean,1);
                }
            });
//            holder.delete.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    AlertDialog alertDialog = new AlertDialog.Builder(SelectedShoppingListActivity.this).setTitle("tips")
//                            .setNegativeButton("yes", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialogInterface, int i) {
//                                    MyApp.getDb().delete(bean);
//                                }
//                            })
//                            .setPositiveButton("cancel", null)
//                            .setMessage("do you want delete item?")
//                            .create();
//                    alertDialog.show();
//                }
//            });
        }

        @Override
        public int getItemCount() {
            return goodsBeans.size();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 1){
            setResult(1);
            finish();
        }
    }

    public static void launch(Activity context, int type, String valce, int listId){
        Intent intent = new Intent(context,SelectedShoppingListActivity.class);
        intent.putExtra("data",type);
        intent.putExtra("valce",valce);
        intent.putExtra("listId",listId);
        context.startActivityForResult(intent,0);
    }
}
