package com.example.summer.orderflower.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * 商家信息表的JavaBean
 * Created by summer on 2017/4/24.
 */

public class Business extends BmobObject {
    private String businessNum;//商家编号
    private String businessName;//商家店名
    private String businessBrief;//商家简介
    private BmobFile businessLogo;//商家logo
    private String address;//商家地址
    public void setBusinessNum(String businessNum){
        this.businessNum=businessNum;
    }
    public void setBusinessName(String businessName){
        this.businessName=businessName;
    }
    public void setBusinessBrief(String businessBrief){
        this.businessBrief = businessBrief;
    }
    public void setBusinessLogo(BmobFile businessLogo){
        this.businessLogo=businessLogo;
    }

    public void setAdress(String address) {
        this.address = address;
    }

    public String getBusinessNum(){
        return this.businessNum;
    }
    public String getBusinessName(){
        return this.businessName;
    }
    public String getBusinessBrief(){
        return this.businessBrief;
    }
    public BmobFile getBusinessLogo(){
        return this.businessLogo;
    }

    public String getAdress() {
        return address;
    }
}
