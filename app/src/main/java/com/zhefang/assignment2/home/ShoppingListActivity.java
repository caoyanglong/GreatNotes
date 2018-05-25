package com.zhefang.assignment2.home;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhefang.assignment2.MyApp;
import com.zhefang.assignment2.R;
import com.zhefang.assignment2.base.BaseActivity;
import com.zhefang.assignment2.bean.ShoppingBean;
import com.zhefang.assignment2.bean.ShoppingTipsBean;
import com.zhefang.assignment2.databinding.ActivityShoppingListLayoutBinding;
import com.zhefang.assignment2.update.InsertShoppingActivity;
import com.zhefang.assignment2.update.UpdateShoppingActivity;
import com.zhefang.assignment2.utils.AppUtils;
import com.zhefang.assignment2.widget.CustomDatePicker;

import java.util.ArrayList;
import java.util.List;

public class ShoppingListActivity extends BaseActivity implements DatePickerDialog.OnDateSetListener, CustomDatePicker.DateChanged {
    private List<ShoppingBean> goodsBeans = new ArrayList<>();
    private MyAdapter adapter;
    private ShoppingTipsBean tipsBean;
    private DatePickerDialog datePickerDialog;


    public final static int day = 0;
    public final static int month = 1;
    public final static int year = 2;

    private int type = day;
    private String selectDate;

    private ActivityShoppingListLayoutBinding mDataBinding;

    @Override
    protected void onCreate() {
        mDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_shopping_list_layout);
        tipsBean = (ShoppingTipsBean) getIntent().getSerializableExtra("data");
        setTitle(tipsBean.getName());


        adapter = new MyAdapter();
        mDataBinding.list.setLayoutManager(new LinearLayoutManager(this));
        mDataBinding.list.setAdapter(adapter);

        mDataBinding.create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InsertShoppingActivity.lanuch(ShoppingListActivity.this, tipsBean);
            }
        });


        mDataBinding.tag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TagsOrCategroyActivity.launch(ShoppingListActivity.this, 0, tipsBean.getId());
            }
        });
        mDataBinding.category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TagsOrCategroyActivity.launch(ShoppingListActivity.this, 1, tipsBean.getId());
            }
        });

        mDataBinding.specificDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                type = day;
//                CustomDatePicker.showDatePicker(ShoppingListActivity.this, ShoppingListActivity.this, type);
                showDatePicker(ShoppingListActivity.this);
            }
        });

        mDataBinding.year.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                type = year;
                showDatePicker(ShoppingListActivity.this);
            }
        });
        mDataBinding.month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                type = month;
                showDatePicker(ShoppingListActivity.this);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateList();
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

    class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MyViewHolder(getLayoutInflater().inflate(R.layout.shopping_list_item, parent, false));
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            final ShoppingBean bean = goodsBeans.get(position);
            holder.title.setText(bean.getName());
            holder.desc.setText(bean.getDesc());
            holder.wantBuy.setChecked(bean.isWantBuy());

            try {
                holder.icon.setImageBitmap(BitmapFactory.decodeFile(bean.getIcon()));
            } catch (Exception e) {
                e.printStackTrace();
            }

            holder.date.setText(bean.getDate());
            holder.edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    UpdateShoppingActivity.launch(ShoppingListActivity.this, bean, 0);
                }
            });
            if (bean.isComplete()) {
                holder.delete.setText("COMPLETEd");
                holder.delete.setBackgroundColor(Color.GRAY);
                holder.delete.setEnabled(false);
            } else {
                holder.delete.setText("COMPLETE");
                holder.delete.setBackgroundColor(Color.parseColor("#cd0000"));
                holder.delete.setEnabled(true);
            }
            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    bean.setComplete(true);
                    MyApp.getDb().update(bean);
                    updateList();
                }
            });
        }

        @Override
        public int getItemCount() {
            return goodsBeans.size();
        }
    }


    public static void lanuch(Activity activity, ShoppingTipsBean id) {
        Intent intent = new Intent(activity, ShoppingListActivity.class);
        intent.putExtra("data", id);
        activity.startActivity(intent);
    }


    private void updateList() {
        goodsBeans.clear();

        List<ShoppingBean> temp = MyApp.getDb().findAllByWhere(ShoppingBean.class, "listId = " + tipsBean.getId());
        if (!TextUtils.isEmpty(selectDate)) {
            for (ShoppingBean bean : temp) {
                if (type == day) {
                    if (TextUtils.equals(AppUtils.parseToDate(bean.getDate()), AppUtils.parseToDate(selectDate))) {
                        goodsBeans.add(bean);
                    }
                } else if (type == month) {
                    if (TextUtils.equals(AppUtils.getMonth(bean.getDate()), AppUtils.getMonth(selectDate))) {
                        goodsBeans.add(bean);
                    }
                } else if (type == year) {
                    if (TextUtils.equals(AppUtils.getYear(bean.getDate()), AppUtils.getYear(selectDate))) {
                        goodsBeans.add(bean);
                    }
                }
            }
        } else {
            goodsBeans.addAll(temp);
        }
        adapter.notifyDataSetChanged();
        mDataBinding.tips.setVisibility(goodsBeans.size() == 0 ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        updateList();
    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != datePickerDialog) {
            datePickerDialog.cancel();
        }
        if(null != alertDialog){
            alertDialog.cancel();
        }
    }

    @Override
    public void dateChange(String date) {
        selectDate = date;
        updateList();
    }

    private AlertDialog alertDialog;
    private View view = null;
    public AlertDialog showDatePicker(Activity activity) {

        if(null == alertDialog){
            view = LayoutInflater.from(activity).inflate(R.layout.custom_date_layout, null);
            @SuppressLint("WrongViewCast") final org.chenglei.widget.datepicker.DatePicker datePicker = view.findViewById(R.id.specificDate);
            datePicker.setTextColor(Color.WHITE).setFlagTextColor(Color.WHITE).setBackground(Color.BLACK).setTextSize(25)
                    .setFlagTextSize(15);
            datePicker.setDescendantFocusability(org.chenglei.widget.datepicker.DatePicker.FOCUS_BLOCK_DESCENDANTS);
            Button yes = view.findViewById(R.id.yes);
            alertDialog = new AlertDialog.Builder(activity).setTitle("tips").setView(view).create();
            yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectDate = datePicker.getYear() + "-" + datePicker.getMonth() + "-" + datePicker.getDayOfMonth();
                    alertDialog.hide();
                    updateList();
                }
            });
        }
        view.findViewById(org.chenglei.widget.datepicker.R.id.month_picker).setVisibility(type == ShoppingListActivity.year ? View.INVISIBLE : View.VISIBLE);
        view.findViewById(org.chenglei.widget.datepicker.R.id.day_picker).setVisibility(type == ShoppingListActivity.day ? View.VISIBLE : View.INVISIBLE);
        alertDialog.show();
        return alertDialog;
    }

}
