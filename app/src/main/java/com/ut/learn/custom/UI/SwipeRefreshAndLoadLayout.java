package com.ut.learn.custom.UI;

import android.content.Context;
import android.service.notification.Condition;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AbsListView;
import android.widget.ListView;

import com.ut.learn.custom.R;

/**
 * 自定义view继承SwipeRefreshLayout,使其具有上拉加载以及下拉刷新的功能
 * Created by admin on 2016/12/20.
 */

public class SwipeRefreshAndLoadLayout extends SwipeRefreshLayout {
    private final int TYPE_MOVE = 0;
    private final int TYPE_UP = 1;
    private final String TAG ="RefreshAndLoad";
    private final int mScaleTouchSlop;
    private final View mFootView;
    private ListView mListView;
    private OnLoadListener mOnLoadListener;

    /**
     * 控件加载状态
     */
    private boolean isLoading = false;

    public SwipeRefreshAndLoadLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mScaleTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mFootView = View.inflate(context, R.layout.view_footer,null);
        Log.e(TAG, "mScaleTouchSlop:"+mScaleTouchSlop );
    }

    /**
     * 在此方法中获取自定义控件中的listview，并为此listview设置监听状态
     *
     */

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        // 判断listview是否初始化
        if(mListView == null){
            // listview未被初始化，判断父控件中是否含有子空间
            if(getChildCount()>0){
                //判断子控件是否属于listview
                if(getChildAt(0) instanceof ListView){
                    // 初始化listview，并为它设置滑动监听方法
                    mListView = (ListView) getChildAt(0);
                    setListViewListener();
                }
            }
        }
    }

    /**
     * 在分发事件的时候处理子控件的触摸事件
     * @param ev
     * @return
     */
    private float mDownY;
    private float mMoveY;
    private float mUpY;
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                mDownY = ev.getY();
            break;
            case MotionEvent.ACTION_MOVE:
                mMoveY = ev.getY();
                if(canLoadMore()){
                    loadData();
                }
            break;
            case MotionEvent.ACTION_POINTER_UP:
                mUpY = ev.getY();
            break;
        }
        return super.dispatchTouchEvent(ev);
    }




    private void setListViewListener() {
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

                        if(canLoadMore()) {
                            loadData();
                        }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }

    /**
     * 在listView滑动过程中：
     * 1.是否是上拉过程（手指按下Y坐标-手指抬起Y坐标>mScaleTouchSlop）
     * 2.判断listview是否滑动到最后一个条目
     * 3.isLoading是否开启
     */
    private boolean canLoadMore() {

        float lastY = mMoveY;


        boolean condition1 = (mDownY - lastY) > mScaleTouchSlop;
        if(condition1){
            Log.e(TAG, "canLoadMore:滑动距离超过判断距离" );
        }

        boolean condition2 = false;
        if(mListView!=null&&mListView.getAdapter()!=null){
            condition2 = mListView.getLastVisiblePosition() == (mListView.getAdapter().getCount()-1);
        }
        if(condition2){
            Log.e(TAG, "canLoadMore: 处于最后一个条目" );
        }

        boolean condition3 = !isLoading;
        if(condition3){
            Log.e(TAG, "canLoadMore: 没有处于加载状态" );
        }
        return condition1 && condition2 && condition3;
    }

    private void loadData() {
        Log.e(TAG, "loadData: 加载数据" );
        if(mOnLoadListener!=null){
            setLoading(true);
            mOnLoadListener.onLoad();
        }
    }

    /**
     * 设置加载动画，true则开始加载动画，false则结束加载动画
     * @param loading
     */
    public void setLoading(boolean loading){
        isLoading = loading;
        if(isLoading){
            mListView.addFooterView(mFootView);
        }else {
            mListView.removeFooterView(mFootView);
            Log.e(TAG, "移除了加载动画");
            mDownY = 0;
            mMoveY = 0;
            mUpY   = 0;
        }
    }






    public interface OnLoadListener{
        void onLoad();
    }

    public void setOnLoadListener(OnLoadListener listener){
        mOnLoadListener = listener;
    }
}
