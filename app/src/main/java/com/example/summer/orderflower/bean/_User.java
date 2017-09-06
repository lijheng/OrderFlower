package com.example.summer.orderflower.bean;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by summer on 2017/4/19.
 * 功能：用户信息JavaBean
 */

public class _User extends BmobUser {
    private String userPetName;
    private BmobFile file;
    public void setFile(BmobFile file){
        this.file = file;
    }
    public void setUserPetName(String userPetName){
        this.userPetName = userPetName;
    }
    public String getUserPetName(){
        return this.userPetName;
    }
    public BmobFile getFile(){
        return this.file;
    }
}
