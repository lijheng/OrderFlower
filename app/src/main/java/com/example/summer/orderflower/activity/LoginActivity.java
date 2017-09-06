/**
 * 功能描述：登录，若登录成功则跳转至主界面
 */
package com.example.summer.orderflower.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.summer.orderflower.R;
import com.example.summer.orderflower.bean._User;

import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

public class LoginActivity extends AppCompatActivity  {

    private EditText editLoginTel;
    private EditText editLoginPassword;
    private _User user;//用户信息
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Bmob.initialize(this,"a21eece14997c9434b98c86d64d14931");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ini();
    }

    /**
     * 功能：登录按钮监听，当登录用户名和密码匹配时，从数据库中取出用户昵称，并跳转到应用首页
     * @param view
     */
    public void actionSignIn(View view){
        user.setUsername(editLoginTel.getText().toString());
        user.setPassword(editLoginPassword.getText().toString());
        user.login(new SaveListener<_User>() {
            @Override
            public void done(_User user, BmobException e) {
                if (e==null){
                    storageMessageFile(editLoginTel.getText().toString());
                    Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(LoginActivity.this, "用户名或密码错误，请重新输入",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    /**
     * 功能：注册按钮监听，当该Button被按下时，跳转至注册界面
     * @param view
     */
    public void actionRegisterIn(View view){
        Intent intent = new Intent(this,RegisterActivity.class);
        startActivity(intent);
    }
    private void ini(){
        editLoginTel = (EditText)findViewById(R.id.edit_login_tel);
        editLoginPassword = (EditText)findViewById(R.id.edit_login_password);
        user = new _User();
    }

    /**
     * 功能：存储信息到本地文件
     */
    private void storageMessageFile(String userTel){
        //从数据库中取出用户信息
        String userPetName;
        BmobQuery<_User> bmobQuery = new BmobQuery<_User>();
        bmobQuery.addWhereEqualTo("username",userTel);
        bmobQuery.findObjects(new FindListener<_User>() {
            @Override
            public void done(List<_User> list, BmobException e) {
                if (e==null){
                    Context context = LoginActivity.this;
                    SharedPreferences sharedPreferences =context.getSharedPreferences("user",Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("userPetName",list.get(0).getUserPetName());
                    editor.putString("userTel",list.get(0).getUsername());
                    editor.apply();
//                    sharedPreferences.edit().putString("userPetName",list.get(0).getUserPetName()).apply();
//                    sharedPreferences.edit().putString("userTel",list.get(0).getUsername()).apply();
                }else{
                    Toast.makeText(LoginActivity.this, "该号码暂未注册", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
