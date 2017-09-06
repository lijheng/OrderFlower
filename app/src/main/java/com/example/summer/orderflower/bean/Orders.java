package com.example.summer.orderflower.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by summer on 2017/5/23.
 */

public class Orders extends BmobObject {
    private String bpName;//商店名字
    private String ordersPrice;//价格
    private String number;//数量
    private String userName;//用户名
    private BmobFile img;

    public BmobFile getImg() {
        return img;
    }

    public String getUserName() {
        return userName;
    }

    public String getBpName() {
        return bpName;
    }

    public String getOrdersPrice() {
        return ordersPrice;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setOrdersPrice(String ordersPrice) {
        this.ordersPrice = ordersPrice;
    }

    public void setBpName(String bpName) {
        this.bpName = bpName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setImg(BmobFile img) {
        this.img = img;
    }
}
