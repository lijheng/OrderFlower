package com.example.summer.orderflower.activity;

import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.summer.orderflower.fragment.HomeFragment;
import com.example.summer.orderflower.fragment.OrderFragment;
import com.example.summer.orderflower.fragment.PersonalFragment;
import com.example.summer.orderflower.R;
import com.example.summer.orderflower.databinding.ActivityHomeBinding;
import com.example.summer.orderflower.util.Utils;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener,
        HomeFragment.OnFragmentInteractionListener,
        OrderFragment.OnFragmentInteractionListener,PersonalFragment.OnFragmentInteractionListener {

    private ViewPager myViewPager;
    private FragmentPagerAdapter fragmentPagerAdapter;
    private List<Fragment> fragmentList = new ArrayList<Fragment>();

    //三个tab页每一个Tab都有一个按钮
    private LinearLayout mTabHome;
    private LinearLayout mTabOrder;
    private LinearLayout mTabPersonal;
    //三个按钮
    private ImageButton imgBtnHome;
    private ImageButton imgBtnOrder;
    private ImageButton imgBtnPersonal;

    private ActivityHomeBinding homeBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Utils.getStatusBarHeight(this);
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_home);
        homeBinding = DataBindingUtil.setContentView(this, R.layout.activity_home);
//        Utils.setStatusBar(this,false,true);
        initView();
        initViewPager();
        setEvent();
    }

    /**
     * 功能：初始化控件
     */
    private void initView(){
        myViewPager =(ViewPager)findViewById(R.id.viewpager_home);
        mTabHome = (LinearLayout)findViewById(R.id.id_tab_home);
        mTabOrder = (LinearLayout)findViewById(R.id.id_tab_order);
        mTabPersonal = (LinearLayout)findViewById(R.id.id_tab_personal);
        imgBtnHome = (ImageButton)findViewById(R.id.id_tab_homeImg);
        imgBtnOrder = (ImageButton)findViewById(R.id.id_tab_order_img);
        imgBtnPersonal  = (ImageButton)findViewById(R.id.id_tab_personal_img);
    }

    /**
     * 功能：初始化ViewPager
     */
    private void initViewPager(){
        HomeFragment tabHome = new HomeFragment();
        OrderFragment tabOrder = new OrderFragment();
        PersonalFragment tabPersonal = new PersonalFragment();

        fragmentList.add(tabHome);
        fragmentList.add(tabOrder);
        fragmentList.add(tabPersonal);
        //初始化Adapter，这里使用的是FragmentPagerAdapter
        fragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragmentList.get(position);
            }

            @Override
            public int getCount() {
                return fragmentList.size();
            }
        };
        myViewPager.setAdapter(fragmentPagerAdapter);
    }
    /**
     * 功能：设置监听器
     */
    private void setEvent(){
        imgBtnHome.setOnClickListener(this);
        imgBtnOrder.setOnClickListener(this);
        imgBtnPersonal.setOnClickListener(this);
        myViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //当ViewPager滑动的时候
                int currentPage = myViewPager.getCurrentItem();
                switch (currentPage){
                    case 0:
                        resetImg();
                        imgBtnHome.setImageResource(R.mipmap.img_home_on);
                        break;
                    case 1:
                        resetImg();
                        imgBtnOrder.setImageResource(R.mipmap.img_orders_on);
                        break;
                    case 2:
                        resetImg();
                        imgBtnPersonal.setImageResource(R.mipmap.img_personal_on);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void resetImg(){
        imgBtnHome.setImageResource(R.mipmap.img_home_off);
        imgBtnOrder.setImageResource(R.mipmap.img_orders_off);
        imgBtnPersonal.setImageResource(R.mipmap.img_personal_off);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.id_tab_homeImg:
                myViewPager.setCurrentItem(0);
                resetImg();
                imgBtnHome.setImageResource(R.mipmap.img_home_on);
                break;
            case R.id.id_tab_order_img:
                Utils.setStatusBar(this,false,false);
                myViewPager.setCurrentItem(1);
                resetImg();
                imgBtnOrder.setImageResource(R.mipmap.img_orders_on);
                break;
            case R.id.id_tab_personal_img:
                Utils.setStatusBar(this,false,true);
                myViewPager.setCurrentItem(2);
                resetImg();
                imgBtnPersonal.setImageResource(R.mipmap.img_personal_on);
                break;
            default:
                break;
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
    }

    private void setBaseProperty(){

    }
}
