package com.example.summer.orderflower.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.summer.orderflower.R;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * Created by summer on 2017/8/23.
 * 用于GridView的适配器
 * 主要规范化图片，防止item之间的相互覆盖
 */

public class GridViewAdapter extends BaseAdapter {
    private List<Map<String,Object>> dataList;
    private LayoutInflater mInflater;
    private Activity activity;
    private static int newWidth;
    private static int newHeight;

    public GridViewAdapter(Activity activity, List<Map<String,Object>> dataList){
        this.dataList = dataList;
        mInflater = LayoutInflater.from(activity);
        this.activity = activity;
    }
    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder =null;
//        判断是否缓存
        if (convertView==null){
            holder = new ViewHolder();
//            通过LayoutInflater实例化布局
            convertView = mInflater.inflate(R.layout.gridview_item,null);
            holder.image = (ImageView) convertView.findViewById(R.id.products_gridView_image);
            holder.tvName = (TextView) convertView.findViewById(R.id.products_gridView_name);
            holder.tvPrice= (TextView) convertView.findViewById(R.id.products_gridView_price);
            convertView.setTag(holder);
        }else {
//            通过tag找到缓存布局
            holder = (ViewHolder) convertView.getTag();
        }
//        设置布局中控件要显示的视图
        File tempFile = (File) dataList.get(position).get("pic");
        Bitmap bitmap = BitmapFactory.decodeFile(tempFile.getPath());
        holder.image.setImageBitmap(adjustBitmap(bitmap));
        bitmap.recycle();//关闭bitmap
        holder.tvName.setText(dataList.get(position).get("name").toString());
        holder.tvPrice.setText(dataList.get(position).get("price").toString());
//        动态设置TextView 的宽度
        holder.tvName.setWidth(newWidth);
        return convertView;
    }

    public class ViewHolder{
        public ImageView image;
        public TextView tvName;
        public TextView tvPrice;
    }

    /**
     * 动态设置Bitmap的高宽，height = width = screenWidth/0.5
     */
    private Bitmap adjustBitmap(Bitmap bitmap){
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        final int DIVIER=2;//分割线的宽度
        Point point = new Point();
        WindowManager windowManager = activity.getWindowManager();
        windowManager.getDefaultDisplay().getSize(point);
        newWidth = point.x/2-(2*DIVIER);
        newHeight = newWidth;
//        计算缩放比
        float scaleWidth = 1.0f*newWidth/width;
        float scaleHeight = 1.0f*newHeight/height;
//        获取想要缩放的Matrix
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth,scaleHeight);
//        获取新的bitmap
        Bitmap newBitmap = Bitmap.createBitmap(bitmap,0,0,width,height,matrix,true);
        return newBitmap;

    }
}
