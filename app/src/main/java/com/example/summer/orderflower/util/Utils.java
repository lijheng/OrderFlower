package com.example.summer.orderflower.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

import com.example.summer.orderflower.R;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by summer on 2017/8/14.
 * 工具类
 */

public class Utils {
    public static int screenWidth;
    public static int screenHeight;
    public static int navigationHeight=0;

    private static DisplayMetrics mMetrics;
    public static final String OME_CURRENT_TAB_POSITION = "HOME_CURRENT_TAB_POSITION";


    /**
     * 通过反射的方式获得状态栏高度
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context){
        try{
//            通过sdk获取
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object obj = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = Integer.parseInt(field.get(obj).toString());
            return context.getResources().getDimensionPixelOffset(x);
        }catch (Exception e){
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取底部导航栏高度
     * @param context
     * @return
     */
    public static int getNavigationBarHeight(Context context){
        Resources resources = context.getResources();
        int resourcesId =resources.getIdentifier("navigation_bar_height","dimen","android");
//        获取NavigationBar的高度
        navigationHeight = resources.getDimensionPixelSize(resourcesId);
        return navigationHeight;
    }

    /**
     * 判断是否存在navigationBar
     * @param context
     * @return
     */
    public static boolean checkDeviceHasNavigationBar(Context context){
        boolean hasNavigationBar = false;
        Resources rs = context.getResources();
        int id = rs.getIdentifier("config_showNavigationBar","bool","android");
        if (id>0){
            hasNavigationBar = rs.getBoolean(id);
        }
        try {
            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get",String.class);
            String navBarOverride = (String)m.invoke(systemPropertiesClass,"qemu.hv.mainkeys");
            if ("1".equals(navBarOverride)) {
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                hasNavigationBar = true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return hasNavigationBar;
    }

    /**
     * 设置状态栏
     * @param activity
     * @param useThemestatusBarColor   是否要状态栏的颜色，不设置则为透明色
     * @param withoutUseStatusBarColor 是否不需要使用状态栏为暗色调
     */
    public static void setStatusBar(Activity activity, boolean useThemestatusBarColor, boolean withoutUseStatusBarColor) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0及以上
            View decorView = activity.getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            if (useThemestatusBarColor) {
                activity.getWindow().setStatusBarColor(activity.getResources().getColor(R.color.white));
            } else {
                activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//4.4到5.0
            WindowManager.LayoutParams localLayoutParams = activity.getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !withoutUseStatusBarColor) {
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

    /**
     * 设置状态栏文字色值为深色调
     * @param useDart 是否使用深色调
     * @param activity
     */
    public static void setStatusTextColor(boolean useDart, Activity activity) {
        if (useDart) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        } else {
            activity.getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
//        设置状态栏和下方布局的距离
        activity.getWindow().getDecorView().findViewById(android.R.id.content).setPadding(0, 0, 0, Utils.navigationHeight);
    }

}
