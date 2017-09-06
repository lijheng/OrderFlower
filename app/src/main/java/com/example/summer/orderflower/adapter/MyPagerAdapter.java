package com.example.summer.orderflower.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by summer on 2017/4/5.
 */

public class MyPagerAdapter extends PagerAdapter {
    private List<ImageView> imageViewList;
    private Context mContext;
    public MyPagerAdapter(List<ImageView> imageViewList,Context mContext){
        this.imageViewList = imageViewList;
        this.mContext = mContext;
    }
    @Override
    public int getCount() {
        return imageViewList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(imageViewList.get(position));
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(imageViewList.get(position));
        return imageViewList.get(position);
    }
}
