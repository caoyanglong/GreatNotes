package com.zhefang.assignment2.bean;

import net.tsz.afinal.annotation.sqlite.Id;
import net.tsz.afinal.annotation.sqlite.Table;

import java.io.Serializable;


@Table(name = "shopping")
public class ShoppingBean implements Serializable{
    @Id
    private int id;
    private String name;
    private String tag;
    private String type;
    private String desc;
    private int price;
    private String date;
    private boolean wantBuy;
    private int count;
    private int listId;
    private String icon;
    private boolean complete;

    public ShoppingBean() {
    }


    public ShoppingBean(String name, String tag, String type, String desc, int price, String date, boolean wantBuy, int count) {
        this.name = name;
        this.tag = tag;
        this.type = type;
        this.desc = desc;
        this.price = price;
        this.date = date;
        this.wantBuy = wantBuy;
        this.count = count;
    }

    public boolean isComplete() {
        return complete;
    }


    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getListId() {
        return listId;
    }

    public void setListId(int listId) {
        this.listId = listId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isWantBuy() {
        return wantBuy;
    }

    public void setWantBuy(boolean wantBuy) {
        this.wantBuy = wantBuy;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
