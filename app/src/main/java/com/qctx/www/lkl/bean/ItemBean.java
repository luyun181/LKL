package com.qctx.www.lkl.bean;

import java.io.Serializable;

/**
 * Created by admin on 2017/8/22.
 */

public class ItemBean implements Serializable {

    private String itemName;
    private double price;
    private int count;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }


    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }


}
