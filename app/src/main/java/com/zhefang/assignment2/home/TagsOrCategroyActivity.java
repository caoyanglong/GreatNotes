package com.zhefang.assignment2.home;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhefang.assignment2.MyApp;
import com.zhefang.assignment2.R;
import com.zhefang.assignment2.base.BaseActivity;
import com.zhefang.assignment2.bean.ShoppingBean;
import com.zhefang.assignment2.databinding.ActivityTagsorcategroyListLayoutBinding;

import java.util.ArrayList;
import java.util.List;


public class TagsOrCategroyActivity extends BaseActivity {
    private List<ShoppingBean> goodsBeans = new ArrayList<>();
    private MyAdapter adapter;
    private int type;
    private List<String> tags = new ArrayList<>();
    private int listId;


    private ActivityTagsorcategroyListLayoutBinding mDataBinding;
    @Override
    protected void onCreate() {
        mDataBinding = DataBindingUtil.setContentView(this,R.layout.activity_tagsorcategroy_list_layout);
        adapter = new MyAdapter();
        mDataBinding.list.setLayoutManager(new LinearLayoutManager(this));
        mDataBinding.list.setAdapter(adapter);
        type = getIntent().getIntExtra("data",0);
        listId = getIntent().getIntExtra("listId",0);
        if(type == 0){
            setTitle("TAG");
        }else {
            setTitle("CATEGORY");
        }
        goodsBeans.addAll(MyApp.getDb().findAll(ShoppingBean.class));
        if(goodsBeans != null && goodsBeans.size() > 0){
            for (ShoppingBean goodsBean:goodsBeans){
                if(type == 0){
                    if(!tags.contains(goodsBean.getTag())){
                        tags.add(goodsBean.getTag());
                    }
                }else{
                    if(!tags.contains(goodsBean.getDesc())){
                        tags.add(goodsBean.getDesc());
                    }
                }
            }
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tag;

        public MyViewHolder(View itemView) {
            super(itemView);
            tag = itemView.findViewById(R.id.tag);
        }
    }

    class MyAdapter extends RecyclerView.Adapter<TagsOrCategroyActivity.MyViewHolder> {
        @Override
        public TagsOrCategroyActivity.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new TagsOrCategroyActivity.MyViewHolder(getLayoutInflater().inflate(R.layout.tags_shopping_list_item, parent, false));
        }
        @Override
        public void onBindViewHolder(TagsOrCategroyActivity.MyViewHolder holder, final int position) {
            holder.tag.setText(tags.get(position));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SelectedShoppingListActivity.launch(TagsOrCategroyActivity.this,type,tags.get(position),listId);
                }
            });
        }

        @Override
        public int getItemCount() {
            return tags.size();
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

    public static void launch(Activity context, int type, int listId){
        Intent intent = new Intent(context,TagsOrCategroyActivity.class);
        intent.putExtra("data",type);
        intent.putExtra("listId",listId);
        context.startActivityForResult(intent,0);
    }
}
