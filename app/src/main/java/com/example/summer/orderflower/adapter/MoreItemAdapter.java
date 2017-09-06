package com.example.summer.orderflower.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.summer.orderflower.R;
import com.example.summer.orderflower.view.DecoratorViewPager;
import com.example.summer.orderflower.view.VagueTextView;

import java.util.List;
import java.util.Map;

/**
 * Created by summer on 2017/7/30.
 */

public class MoreItemAdapter extends BaseAdapter {
    private String[] str;
    private Context context;
    private List<Map<String,Object>> dataList;
    private List<Map<String,Object>> bannerList;
    private LayoutInflater mInflater;
//    初始化Item标志常量
    private static final int TYPE_BANNERS =0;
    private static final int TYPE_BUSINESS_MESS =1;
    private static final int TYPE_MAX_COUNT=TYPE_BUSINESS_MESS+1;

    public MoreItemAdapter(Context context,List<Map<String,Object>> dataList,List<Map<String,Object>> bannerList){
        this.context = context;
        this.dataList = dataList;
        this.bannerList = bannerList;
        mInflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return dataList.size()+1;
    }

    @Override
    public Object getItem(int position) {
        if (position==0){
            return bannerList;
        }else {
            return dataList.get(position);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        if (position==0){
            return TYPE_BANNERS;
        }else
            return TYPE_BUSINESS_MESS;
    }

    @Override
    public int getViewTypeCount() {
        return TYPE_MAX_COUNT;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BannerViewHolder bannerViewHolder = null;
        BusinessMessViewHolder businessMessViewHolder=null;
        switch (getItemViewType(position)){
            case TYPE_BANNERS:
                if (convertView==null){
                    bannerViewHolder = new BannerViewHolder();
                    MyPagerAdapter myPagerAdapter = new MyPagerAdapter(bannerList,context);
                    convertView = View.inflate(context, R.layout.carousel_advertising_item,null);
                    bannerViewHolder.decoratorViewPager = (DecoratorViewPager) convertView.findViewById(R.id.ad_viewPage_listView);
                    bannerViewHolder.linearLayout =  (LinearLayout)convertView.findViewById(R.id.carousel_container);
                    bannerViewHolder.decoratorViewPager.setAdapter(myPagerAdapter);
//                    bannerViewHolder.linearLayout.setBackground(bannerList.);
                    convertView.setTag(convertView);
                }else {
                    bannerViewHolder=(BannerViewHolder)convertView.getTag();
                }
                break;
            case TYPE_BUSINESS_MESS:
                if (convertView==null){
                    convertView = View.inflate(context,R.layout.simple_item,null);
                    businessMessViewHolder.img_icon =(ImageView)convertView.findViewById(R.id.pic);
                    businessMessViewHolder.businessName=(VagueTextView)convertView.findViewById(R.id.name);
                    businessMessViewHolder.businessBrief=(TextView)convertView.findViewById(R.id.brief);
                }else{
                    businessMessViewHolder=(BusinessMessViewHolder)convertView.getTag();
                }
                break;
        }
        return null;
    }

    class BannerViewHolder{
        private DecoratorViewPager decoratorViewPager;
        private LinearLayout linearLayout;
    }
    class BusinessMessViewHolder{
        private ImageView img_icon;
        private VagueTextView businessName;
        private TextView businessBrief;
    }

    class MyPagerAdapter extends PagerAdapter {
        private List<Map<String,Object>> imageViewList;
        private Context mContext;
        public MyPagerAdapter(List<Map<String,Object>> imageViewList,Context mContext){
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
            container.removeView((View) imageViewList.get(position).get("pic"));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView((View) imageViewList.get(position).get("pic"));
            return imageViewList.get(position);
        }
    }
}
