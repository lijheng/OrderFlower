package com.example.summer.orderflower.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.example.summer.orderflower.bean.Business;
import com.example.summer.orderflower.R;
import com.example.summer.orderflower.activity.ProductsActivity;
import com.example.summer.orderflower.activity.SearchActivity;
import com.example.summer.orderflower.adapter.MyPagerAdapter;
import com.example.summer.orderflower.util.Utils;
import com.example.summer.orderflower.view.DecoratorViewPager;
import com.example.summer.orderflower.view.ListViewInSrcollView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.FindListener;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View view;

//    上方gps图标监控
    private ImageButton btnGpsAddress;
    private TextView tvAddress;
    private AMapLocationClient mapLocationClient;//声明AMapLocationClient对象
//    声明AMapLocationClientOption对象，用于设置定位的模式和参数
    private AMapLocationClientOption mapLocationClientOption;

//    搜索框
    private Button btnSearch;

    //用于ListView
    private ListViewInSrcollView homeListView;
    private SimpleAdapter simpleAdapter;
    private List<Map<String,Object>> dataList;
    private String businessNum1 = null;

//    ViewPager
    private DecoratorViewPager vp;
    private List<ImageView> bannerList = new ArrayList<>();//广告图片容器
    private LinearLayout circle_container;//小圆点容器
    private int mCurrentIndex=0;//当前小圆点的位置

    //用户文件下载
    private final String savePath = Environment.getExternalStorageDirectory()+"/Flower";
    private File folder;
    private OnFragmentInteractionListener mListener;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        为了执行 onCreateOptionsMenu
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    /**
     * 得到广告轮播图片
     * @return
     */
    private int[] getImageRes() {
        return new int[]{R.mipmap.guide_image1,R.mipmap.guide_image2,R.mipmap.guide_image3};
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home,container,false);
        initState();
        init();
/*        ImageButton gpsAddress监听
          当被点击时，定位，并将结果显示到textvView中去*/
        btnGpsAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                初始化AMapLocationClientOption
                mapLocationClientOption = new AMapLocationClientOption();
//                定位模式确定  顶功耗定位
                mapLocationClientOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
//                设置单次定位，获取最近s3内精度最高的一次结果
                mapLocationClientOption.setOnceLocationLatest(true);
//                初始化定位
                mapLocationClient = new AMapLocationClient(view.getContext());
//                给定位客户端对象设置定位参数
                mapLocationClient.setLocationOption(mapLocationClientOption);
//                启动定位
                mapLocationClient.startLocation();
//                异步获取定位结果
                mapLocationClient.setLocationListener(new AMapLocationListener() {
                    @Override
                    public void onLocationChanged(AMapLocation aMapLocation) {
                        if (aMapLocation.getErrorCode()==0){
                            Toast.makeText(view.getContext(), "启动了定位服务", Toast.LENGTH_SHORT).show();
//                            获取定位接到信息
                            String address = aMapLocation.getStreet();
//                            显示到textView中
                            tvAddress.setText(address);
                        }else{
                            Toast.makeText(view.getContext(), "定位失败", Toast.LENGTH_SHORT).show();
                            //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                            Log.e("AmapError","location Error, ErrCode:"
                                    + aMapLocation.getErrorCode() + ", errInfo:"
                                    + aMapLocation.getErrorInfo());
                        }
                    }
                });
            }
        });

//        btnSearch监听,当点击时 进入搜索页面
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(),SearchActivity.class);
                startActivity(intent);
            }
        });
//        适配器加载数据源
        dataList = new ArrayList<>();
        simpleAdapter = new SimpleAdapter(view.getContext(),getData(),R.layout.simple_item,
                new String[]{"pic","name","brief"},new int[]{R.id.pic,R.id.name,R.id.brief});
        /**
         * 通常写法：
         *  LayoutInflater.from(view.getContext()).inflate(R.layout.ad_viewpage_laout,null)
         *  此处这样写出错原因是：丢失了XML布局中根View的LayoutParam
         *  故需要写为
         *  LayoutInflater.from(view.getContext()).inflate(R.layout.ad_viewpage_layout,
         *    homeListView,false);
         */
        //ViewPager

        final int[] imageRes = getImageRes();
        for (int i=0;i<imageRes.length;i++){
//            添加广告图片
            ImageView imgView = new ImageView(view.getContext());
            imgView.setBackgroundResource(imageRes[i]);
            imgView.setScaleType(ImageView.ScaleType.FIT_XY);
            bannerList.add(imgView);
//            添加小圆点
            ImageView dot=new ImageView(view.getContext());
            if (i==mCurrentIndex){
                dot.setImageResource(R.mipmap.selected);//设置当前页的小圆点
            }else {
                dot.setImageResource(R.mipmap.unselected);//设置其他页的小圆点
            }
            LinearLayout.LayoutParams params =
                    new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);
            if (i>0){
                params.leftMargin=10;//设置圆点边距
            }
            dot.setLayoutParams(params);
            circle_container.addView(dot);
        }
        MyPagerAdapter myPagerAdapter = new MyPagerAdapter(bannerList,view.getContext());
//        给viewPager设置adapter
        vp.setAdapter(myPagerAdapter);
//        为ViewPager设置OnPageChangeListener
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i=0;i<circle_container.getChildCount();i++){
                    if (i==position){
                        ((ImageView) circle_container.getChildAt(i)).setImageResource(R.mipmap.selected);
                    }else{
                        ((ImageView) circle_container.getChildAt(i)).setImageResource(R.mipmap.unselected);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
//        视图ListView 加载适配器
        homeListView.setAdapter(simpleAdapter);
        /**
         * 添加ListView的点击Item点击事件
         * 获取点击的Item的商家唯一id传递给下一个Activity
         */
        homeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(view.getContext(),ProductsActivity.class);
                //得到被点击的商家的businessNum
                //通过点击的是哪一个条目在数据库中查询到该条目来找到商家的businessNum
                String businessNum = dataList.get(position).get("num").toString();
                String businessLogoPath = ((File)dataList.get(position).get("pic")).getPath();
                intent.putExtra("businessNum",businessNum);
                intent.putExtra("businessLogoPath",businessLogoPath);
                startActivity(intent);
            }
        });
        /**
         * 添加下拉刷新
         *
         */
        homeListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {

            }
        });
        // Inflate the layout for this fragment
        return view;
    }

    /**
     * 控件的初始化
     */
    private void init(){
        homeListView = (ListViewInSrcollView) view.findViewById(R.id.home_listView);
        //        初始化LinearLayout 此处即是小圆点的布局
        circle_container = (LinearLayout)view.findViewById(R.id.carousel_container);
        //        初始化ViewPager
        vp =(DecoratorViewPager) view.findViewById(R.id.ad_viewPage_listView);
//        gpsAddress ImageButton的初始化
        btnGpsAddress=(ImageButton)view.findViewById(R.id.gps_location);
        tvAddress = (TextView)view.findViewById(R.id.address);
        btnSearch = (Button)view.findViewById(R.id.btn_search);
    }

    /**
     * 取到商家的信息数据
     * @return
     */
    private List<Map<String,Object>> getData(){
//        查询数据库以获取数据
        folder=new File(savePath);
        if (!folder.exists()){
            folder.mkdir();//创建文件夹
        }
        BmobQuery<Business> businessBmobQuery=new BmobQuery<>();
//        businessBmobQuery.addQueryKeys("businessName,businessBrief,businessLogo,businessNum");
        businessBmobQuery.addQueryKeys("businessName,businessBrief,businessLogo,businessNum");

        businessBmobQuery.findObjects(new FindListener<Business>() {
            @Override
            public void done(List<Business> list, BmobException e) {
                if (e==null){
                    for (int i=0;i<list.size();i++){
                        BmobFile tempBmobFile = list.get(i).getBusinessLogo();
                        final File saveFile =new File(savePath+"/"+tempBmobFile.getFilename());
                        final String name = list.get(i).getBusinessName();
                        final String brief = list.get(i).getBusinessBrief();
                        final String num=list.get(i).getBusinessNum();
                        if (saveFile.exists()){
                            Map<String,Object> map=new HashMap<String,Object>();
                            map.put("pic",saveFile);
                            map.put("name",name);
                            map.put("brief",brief);
                            map.put("num",num);
                            dataList.add(map);
                            simpleAdapter = new SimpleAdapter(view.getContext(),dataList,R.layout.simple_item,
                                    new String[]{"pic","name","brief"},new int[]{R.id.pic,R.id.name,R.id.brief});
                            //视图ListView 加载适配器
                            homeListView.setAdapter(simpleAdapter);
                        }else{
                            tempBmobFile.download(saveFile,new DownloadFileListener() {
                                @Override
                                public void done(String s, BmobException e) {
                                    if (e==null){
                                        Map<String,Object> map=new HashMap<String,Object>();
                                        map.put("pic",saveFile);
                                        map.put("name",name);
                                        map.put("brief",brief);
                                        dataList.add(map);
                                        simpleAdapter = new SimpleAdapter(view.getContext(),dataList,R.layout.simple_item,
                                                new String[]{"pic","name","brief"},new int[]{R.id.pic,R.id.name,R.id.brief});
                                        //视图ListView 加载适配器
                                        homeListView.setAdapter(simpleAdapter);
                                    }
                                }
                                @Override
                                public void onProgress(Integer integer, long l) {

                                }
                            });
                        }
                    }
                }else {
                    Log.e("杀杀杀", "done: 错误",e );
                    Toast.makeText(view.getContext(), "刷新失败，请检查网络连接",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        return dataList;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    /**
     * 不使用fitSystemWindows 布局不遮挡状态栏文字
     */
    private void initState(){
//        当系统版本在4.0及以上时
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT){
            Utils.setStatusBar(getActivity(),false,false);

            LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.home_bar);
            linearLayout.setVisibility(View.VISIBLE);
//            获取状态栏高度
            int statusHeight = Utils.getStatusBarHeight(getContext());
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) linearLayout.getLayoutParams();
            params.height = statusHeight;
            linearLayout.setLayoutParams(params);
        }
    }
}
