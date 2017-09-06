package com.example.summer.orderflower.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.summer.orderflower.R;
import com.example.summer.orderflower.adapter.MyPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class GuideActivity extends Activity {
    private View guideActivity;
    private ViewPager viewPager;
    private List<ImageView> imageViewList;
    private Button beginButton;
    private MyPagerAdapter myPagerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        viewPager = (ViewPager)findViewById(R.id.viewpager);
        //数据源
        guideActivity = findViewById(R.id.guide_activity);
        beginButton = (Button) findViewById(R.id.begin_button);
        final int[] imageRes = getImageRes();
        imageViewList = new ArrayList<ImageView>();
        for (int i=0;i<imageRes.length;i++){
            ImageView imageView = new ImageView(this);
            imageView.setBackgroundResource(imageRes[i]);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageViewList.add(imageView);
        }
        myPagerAdapter = new MyPagerAdapter(imageViewList,this);
        viewPager.setAdapter(myPagerAdapter);
        //最后一张图片提示开始使用

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (imageViewList.size()-1 == position){
                    beginButton.setVisibility(View.VISIBLE);
                    beginButton.setText("开始使用");
                    beginButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(GuideActivity.this,LoginActivity.class);
                            startActivity(intent);
                            GuideActivity.this.finish();
                        }
                    });
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private int[] getImageRes() {
        return new int[]{R.mipmap.guide_image1,R.mipmap.guide_image2,R.mipmap.guide_image3};
    }
}
