package com.example.summer.orderflower.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;

import com.example.summer.orderflower.R;

import cn.bmob.v3.Bmob;


public class MainActivity extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGHT = 3000;//延迟3s
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;
    private boolean isFirst;
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();//        去掉标题栏
        Bmob.initialize(this,"a21eece14997c9434b98c86d64d14931");
        context = MainActivity.this;
        sharedPreferences = context.getSharedPreferences("booleanFirst",MODE_PRIVATE);
        isFirst = sharedPreferences.getBoolean("booleanFirst",true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isFirst){
                    //引导页面
                    editor = getSharedPreferences("booleanFirst",MODE_PRIVATE).edit();
                    editor.putBoolean("booleanFirst",false);
                    editor.apply();
                    intent = new Intent(MainActivity.this,GuideActivity.class);
                    startActivity(intent);
                    MainActivity.this.finish();
                }else{
                    //首页
                   intent = new Intent(MainActivity.this,HomeActivity.class);
                    startActivity(intent);
                    MainActivity.this.finish();
                }
            }
        },SPLASH_DISPLAY_LENGHT);
    }
}
