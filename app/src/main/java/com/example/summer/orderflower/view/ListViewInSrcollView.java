package com.example.summer.orderflower.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by summer on 2017/7/31.
 * 解决SrcollView中使用ListView只显示一条数据
 */

public class ListViewInSrcollView extends ListView {
    public ListViewInSrcollView(Context context) {
        super(context);
    }

    public ListViewInSrcollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ListViewInSrcollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int mExpandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, mExpandSpec);
    }
}
