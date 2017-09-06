package com.example.summer.orderflower.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.summer.orderflower.R;
import com.example.summer.orderflower.bean._User;

import cn.bmob.sms.BmobSMS;
import cn.bmob.sms.exception.BmobException;
import cn.bmob.sms.listener.RequestSMSCodeListener;
import cn.bmob.sms.listener.VerifySMSCodeListener;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.listener.SaveListener;


public class RegisterActivity extends AppCompatActivity {

    private int recLen = 60;//倒计时的时间
    private boolean run = true;
    private boolean verification = false;//记录验证码是否正确
    private boolean passwordSame = false;//记录两次密码输入是否相同
    private boolean isSuccessRegister = false;//记录是否注册成功
    private Button btnGetVerification;
    private EditText editTelNumber;//电话号码输入控件
    private EditText editVerification;//验证码输入控件
    private EditText editUserPetName;
    private EditText editUserPassword;
    private EditText editConfirmUserPassword;
    private _User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        BmobSMS.initialize(this,"a21eece14997c9434b98c86d64d14931");
        Bmob.initialize(this,"a21eece14997c9434b98c86d64d14931");
        ini();
    }

    /**
     * 功能：响应注册并登陆Button
     * 当此Button被触发时，检测验证码是否正确、两次密码输入是否匹配、用户名是否合法
     * 若都满足，则进入HomeActivity ，并将用户信息注册到数据库，并将用户名和密码存储到本地；
     * 否则 ，提示用户重新输入信息
     * @param view
     */
    public void register(View view){
        //短信验证码 验证
        BmobSMS.verifySmsCode(RegisterActivity.this, editTelNumber.getText().toString(),
                editVerification.getText().toString(), new VerifySMSCodeListener() {
            @Override
            public void done(BmobException e) {
                //验证码正确
                if (e ==null){
                    verification = true;
                }else{
                    Toast.makeText(RegisterActivity.this, "验证码输入错误，请重新输入",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        //密码验证
        if (editUserPassword.getText().toString().equals(
                editConfirmUserPassword.getText().toString())){
            passwordSame = true;
        }else{
            Toast.makeText(this, "前后两次输入密码不一致，请重新输入", Toast.LENGTH_SHORT).show();
        }
        //验证码及密码输入满足要求
        if (passwordSame&&verification){
            /**
             * 用户信息注册
             */
            user.setUsername(editTelNumber.getText().toString());
            user.setPassword(editUserPassword.getText().toString());
            user.setUserPetName(editUserPetName.getText().toString());
            user.signUp(new SaveListener<_User>() {
                @Override
                public void done(_User user, cn.bmob.v3.exception.BmobException e) {
                    if (e==null){
                        Toast.makeText(RegisterActivity.this, "注册成功",
                                Toast.LENGTH_SHORT).show();
                        isSuccessRegister = true;
                    }else{
                        Toast.makeText(RegisterActivity.this, "注册失败，该号码已被注册",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
            if (isSuccessRegister){
                //将用户部分信息存入本地
                storageMessageFile(editTelNumber.getText().toString(),
                        editUserPetName.getText().toString(),
                        editUserPassword.getText().toString());
                Intent intent = new Intent(RegisterActivity.this,HomeActivity.class);
                startActivity(intent);
            }
        }
    }

    /**
     * 功能：点击按钮发送短信验证码
     * @param view
     */
    public void actionGetVerification(View view){
        //判断电话号码是否合法
        if(editTelNumber.getText().toString().length()!=11){
            Toast.makeText(this, "请输入正确的电话号码", Toast.LENGTH_SHORT).show();
        }else{
            final String editTelephone = editTelNumber.getText().toString();
            btnGetVerification.setEnabled(false);
            new Thread(new MyThread()).start();

            //发送短信验证码
            BmobSMS.requestSMSCode(this, editTelephone, "Flower", new RequestSMSCodeListener() {
                @Override
                public void done(Integer integer, BmobException e) {
                    //验证码发送成功
                    if (e ==null) {
                        Toast.makeText(RegisterActivity.this, "验证码发送成功，请尽快查收",
                                Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(RegisterActivity.this, "验证码发送失败，请重试",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
    final Handler handler = new Handler(){     // handle
        public void handleMessage(Message msg){
            switch (msg.what) {
                case 1:
                    recLen--;
                    btnGetVerification.setText(recLen+"s后重新获取");
                    if (recLen-1 ==0){
                        run = false;
                    }else if(recLen ==0){
                        btnGetVerification.setEnabled(true);
                        btnGetVerification.setText("获取验证码");
                    }
                    break;
            }
            super.handleMessage(msg);
        }
    };
    public class MyThread implements Runnable{

        @Override
        public void run() {
            while (run){
                try {
                    Thread.sleep(1000);
                    Message message = new Message();
                    message.what = 1;
                    handler.sendMessage(message);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 功能：初始化所有控件
     */
    private void ini(){
        btnGetVerification = (Button)findViewById(R.id.btn_get_verification);
        editTelNumber = (EditText)findViewById(R.id.edit_telNumber);
        editVerification = (EditText)findViewById(R.id.edit_verification);
        editUserPetName = (EditText)findViewById(R.id.edit_user_petName);
        editUserPassword = (EditText)findViewById(R.id.edit_user_password);
        editConfirmUserPassword = (EditText)findViewById(R.id.edit_confirm_user_password);
        user = new _User();
    }

    /**
     * 功能：存储信息到本地文件
     */
    private void storageMessageFile(String userName,String userTel,String userPassword){
        SharedPreferences sharedPreferences;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.edit().putString("userPetName",userName).apply();
        sharedPreferences.edit().putString("userPassword",userPassword).apply();
        sharedPreferences.edit().putString("userTel",userTel).apply();
    }
}
