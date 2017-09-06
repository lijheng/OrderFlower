package com.example.summer.orderflower.base;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.summer.orderflower.R;
import com.example.summer.orderflower.databinding.ContentBaseBinding;
import com.example.summer.orderflower.util.Utils;

public class BaseActivity<SV extends ViewDataBinding> extends AppCompatActivity {

//    布局View
    protected SV bindindView;
    protected ContentBaseBinding mBaseBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
//        竖屏，显示时高度大于宽度
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mBaseBinding = DataBindingUtil.inflate(LayoutInflater.from(this),
                R.layout.content_base,null,false);
        bindindView = DataBindingUtil.inflate(getLayoutInflater(),layoutResID,null,false);
//        content
        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
        bindindView.getRoot().setLayoutParams(params);
        LinearLayout mContainer = (LinearLayout) mBaseBinding.getRoot().findViewById(R.id.container);
        mContainer.addView(bindindView.getRoot());
        getWindow().setContentView(mBaseBinding.getRoot());

        Utils.setStatusBar(this,false,false);

        setTitleBar();
    }

    /**
     * 设置titleBar
     */
    protected void setTitleBar(){
        setSupportActionBar(mBaseBinding.toolBar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar!=null){
//            去除默认的Title显示
            actionBar.setDisplayShowTitleEnabled(false);
//             使左上角图标是否显示，如果设成false，则没有程序图标，仅仅就个标题，否则，显示应用程序图标
            actionBar.setDisplayHomeAsUpEnabled(false);
        }
        mBaseBinding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);

                    if (inputMethodManager.isActive()){//如果为true，则键盘正在显示
//                    软键盘处于显示状态
//                    隐藏键盘
                        ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).
                                hideSoftInputFromWindow(BaseActivity.this.getCurrentFocus().getWindowToken(),
                                        InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                onBackPressed();
            }
        });
    }

    /**
     * 设置标题
     * @param text
     */
    public void setTitle(CharSequence text){
        mBaseBinding.toolBar.setTitle("");
        mBaseBinding.titleTv.setText(text);
    }

    /**
     * 设置标题右侧文字
     * @param text
     */
    public void setRightTitle(CharSequence text){
        mBaseBinding.titlebarRightTv.setText(text);
    }

    /**
     * 设置标题左侧文字
     * @param text
     */
    public void setLeftText(CharSequence text){
        mBaseBinding.leftTitleTv.setText(text);
    }

    public void setToolBarTitle(CharSequence text){
//        取消状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mBaseBinding.toolBar.setTitle(text);
        mBaseBinding.titleTv.setText("");
    }

    /**
     * 隐藏返回按钮
     */
    public void hideBackBtn(){
        mBaseBinding.backBtn.setVisibility(View.GONE);
    }

    /**
     * 隐藏标题栏
     * @param variable
     */
    public void hideToolBar(int variable){
        mBaseBinding.toolBar.setVisibility(variable);
    }

    protected void hideBack(SV bindindView){
        this.bindindView = bindindView;
    }

    /**
     * 标题栏右上角图标设置
     */
    public void setRightBtn(int iconRes){
        mBaseBinding.titlebarRightTv.setVisibility(View.VISIBLE);
        mBaseBinding.titlebarRightTv.setBackground(getResources().getDrawable(iconRes));
    }

    /**
     * 点击右侧按钮
     */
    public void titleRightClick(View view){
        rightClick();
    }

    protected  void rightClick(){}

    /**
     * 点击左侧按钮
     * @param view
     */
    public void titleLeftClick(View view){
        leftClick();
    }

    protected void leftClick(){}

    /**
     * 显示搜索框
     */
    public void showSearchView(){
        mBaseBinding.searchView.setVisibility(View.VISIBLE);
    }
    /**
     * 设置搜索框搜索提示
     */
    public void setSearchViewQueryHint(CharSequence text){
        mBaseBinding.searchView.setQueryHint(text);
    }
}
