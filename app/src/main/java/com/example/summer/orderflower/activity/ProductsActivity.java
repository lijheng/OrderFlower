package com.example.summer.orderflower.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.summer.orderflower.adapter.GridViewAdapter;
import com.example.summer.orderflower.bean.Products;
import com.example.summer.orderflower.R;
import com.example.summer.orderflower.util.BlurBitmapUtil;
import com.example.summer.orderflower.util.Utils;
import com.example.summer.orderflower.view.GridViewInScrollView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.FindListener;


public class ProductsActivity extends AppCompatActivity {

    private final String TAG="ProductsActivity调试";

    private final String imgPath = Environment.getExternalStorageDirectory().getPath()+"/Flower/pImg";
    private GridViewInScrollView productsGridView;
    private GridViewAdapter gridViewAdapter;
    private List<Map<String,Object>> productsDataList;

//    接受intent传递过来的参数
    private String businessNum;
    private String businessLogoPath;

    private LinearLayout productsTitleLinearLayout;
    private ImageView headImageView;
    private ImageView imageView;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);
        Utils.setStatusBar(this,false,true);
        init();
//        接受Intent传送的值
        Intent mIntent = getIntent();
        businessNum = mIntent.getStringExtra("businessNum");
        businessLogoPath = mIntent.getStringExtra("businessLogoPath");
//      设置页面头ImageView
        setImageView(businessLogoPath);
        productsDataList = new ArrayList<>();
        productsGridView = (GridViewInScrollView) findViewById(R.id.products_gridView);
//        添加适配器
        gridViewAdapter  = new GridViewAdapter(this,getProductsMessage(businessNum));
        productsGridView.setAdapter(gridViewAdapter);
//        添加item点击监听
        productsGridView.setOnItemClickListener(new MyOnItemClickListener());
    }

    /**
     * 控件初始化
     */
    private void init(){
        imageView = (ImageView) findViewById(R.id.products_gridView_image);
        productsTitleLinearLayout = (LinearLayout) findViewById(R.id.products_title);
        headImageView = (ImageView) findViewById(R.id.head_img);
    }
    /**
     * item点击内部类
     */
    private class MyOnItemClickListener implements AdapterView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Log.d(TAG, "此时点击的是onItemClick: "+productsDataList.get(position).get("name"));
        }
    }

    /**
     * 得到商品的信息,并更新UI
     * 创建文件夹pImag存储商品图片信息
     * @param businessNum 商店的编号
     */
    private List<Map<String,Object>> getProductsMessage(String businessNum){
        final File pImg = new File(imgPath);//存储商品图片信息
        if (!pImg.exists()){
            pImg.mkdir();
        }
        BmobQuery<Products> productsBmobQuery = new BmobQuery<>();
        productsBmobQuery.addWhereEqualTo("businessNum",businessNum);
        productsBmobQuery.findObjects(new FindListener<Products>() {
            @Override
            public void done(List<Products> list, BmobException e) {
                if (e==null){
                    for (int i=0;i<list.size();i++)
                    {
                        final File tempImg = new File(pImg+"/"+list.get(i).getProductsImg().getFilename());
                        final String name = list.get(i).getProductsName();
                        final String price = list.get(i).getProductsPrice();
                        //如果该图片在文件夹中已经有缓存了 则直接加载
                        if (tempImg.exists()){
                            Map<String,Object> map = new HashMap<>();
                            map.put("name",name);
                            map.put("price","￥"+price);
                            map.put("pic",tempImg);
                            productsDataList.add(map);
                            gridViewAdapter.notifyDataSetChanged();
                        }else{
//                            下载图片文件，并更新UI
                            final BmobFile tempBombFile = list.get(i).getProductsImg();
                            tempBombFile.download(tempImg, new DownloadFileListener() {
                                @Override
                                public void done(String s, BmobException e) {
                                    if (e==null){
                                        Map<String,Object> map=new HashMap<>();
                                        map.put("pic",tempImg);
                                        map.put("name",name);
                                        map.put("price","￥"+price);
                                        Log.d("测试是否有数据2", "done: "+map.get("name"));
                                        productsDataList.add(map);
                                        gridViewAdapter.notifyDataSetChanged();
                                    }
                                }

                                @Override
                                public void onProgress(Integer integer, long l) {

                                }
                            });
                        }
                    }
                }else {
                    Log.e("请检查网络连接", "done: ",e );
                }
            }
        });
        return productsDataList;
    }

    /**
     * 设置工具栏
     */
    private void setToolBar(){
        toolbar = (Toolbar) findViewById(R.id.products_toolbar);
        if (toolbar==null) return;
        toolbar.setTitle(R.string.app_name);
        toolbar.setNavigationIcon(R.mipmap.ic_launcher);
//        如果需要给toolBar设置监听，则需要将toolbar设置为actionBar
        setSupportActionBar(toolbar);

    }

    /**
     * 设置本地文件为ImageView源文件
     * @param logoPath 本地文件路径
     */
    private void setImageView(String logoPath){
        Bitmap bitmap = BitmapFactory.decodeFile(logoPath);
//        Bitmap bitmap1 = BlurBitmapUtil.blurBitmap(this,bitmap,3.0f);
//        透明图片
        Bitmap bitmap2 = BlurBitmapUtil.getTransparentBitmap(bitmap,90);
        headImageView.setImageBitmap(bitmap2);
    }


}
