package com.example.summer.orderflower.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.SimpleAdapter;

import com.example.summer.orderflower.R;

import java.util.List;
import java.util.Map;


public class SearchActivity extends Activity {

    private Button btnBack;//返回Button控件
    private SearchView goodsSearch;//搜索SearchView控件
    private Button btnSearch;//搜索Button控件

    private List<Map<String,Object>> dataList;//装载后台数据库中的所有商家、商品名
    private SimpleAdapter simpleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        init();
    }

    /*初始化控件、变量*/
    private void init(){
        btnBack = (Button)findViewById(R.id.btn_back);
        goodsSearch = (SearchView)findViewById(R.id.goods_search);
        btnSearch=(Button)findViewById(R.id.btn_goods_search);
//        simpleAdapter = new SimpleAdapter(this,loading(),R.layout.simple_item,new String[]{"pic","name","brief"},new int[]{R.id.pic,R.id.name,R.id.brief})、

    }

}
