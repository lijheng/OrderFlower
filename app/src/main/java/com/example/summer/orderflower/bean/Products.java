package com.example.summer.orderflower.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * 商品信息表的JavaBean
 * Created by summer on 2017/4/25.
 */

public class Products extends BmobObject {
    private String businessNum;//商家编号
    private String productsNum;//商品编号
    private String productsName;//商品名称
    private BmobFile productsImg;//商品图片
    private String productsPrice;//商品价格

    public void setProductsPrice(String productsPrice) {
        this.productsPrice = productsPrice;
    }

    public void setBusinessNum(String businessNum) {
        this.businessNum = businessNum;
    }

    public void setProductsNum(String productsNum) {
        this.productsNum = productsNum;
    }

    public void setProductsName(String productsName) {
        this.productsName = productsName;
    }

    public void setProductsImg(BmobFile productsImg) {
        this.productsImg = productsImg;
    }

    public String getBusinessNum() {
        return businessNum;
    }

    public String getProductsNum() {
        return productsNum;
    }

    public String getProductsName() {
        return productsName;
    }

    public BmobFile getProductsImg() {
        return productsImg;
    }

    public String getProductsPrice() {
        return productsPrice;
    }
}
