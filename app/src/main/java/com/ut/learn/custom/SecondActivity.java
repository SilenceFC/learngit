package com.ut.learn.custom;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.ut.learn.custom.UI.SwipeRefreshAndLoadLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by admin on 2016/12/20.
 */

public class SecondActivity extends Activity {
    private final String TAG = "SecondActivity";
    private SwipeRefreshAndLoadLayout swipe;
    private ListView listView;
    private StringAdapter mAdapter;

    List<String> mList;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    mList.add(0, "下拉刷新item:" + new Random().nextInt(100));
                    Log.e(TAG, "下拉刷新了五条数据");
                    mAdapter.notifyDataSetChanged();
                    swipe.setRefreshing(false);
                    break;
                case 1:
                    for (int i = 40; i < 45; i++) {
                        mList.add("上拉加载item" + i);
                    }
                    Log.e(TAG, "上拉加载了五条数据");
                    mAdapter.notifyDataSetChanged();
                    swipe.setLoading(false);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        initView();
        initSwipe();
    }

    private void initSwipe() {
        swipe.setColorSchemeColors(Color.YELLOW, Color.GREEN, Color.RED);
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mHandler.sendEmptyMessageDelayed(0, 2000);
            }
        });
        swipe.setOnLoadListener(new SwipeRefreshAndLoadLayout.OnLoadListener() {
            @Override
            public void onLoad() {
                Log.e(TAG, "Activity上拉加载");
                mHandler.sendEmptyMessageDelayed(1, 2000);
            }
        });
    }

    private void initView() {
        swipe = (SwipeRefreshAndLoadLayout) findViewById(R.id.swipeload);
        listView = (ListView) findViewById(R.id.list_swipeload);
        mList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            mList.add("item" + i);
        }
        mAdapter = new StringAdapter();
        listView.setAdapter(mAdapter);
    }

    private class StringAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public Object getItem(int position) {
            return mList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(SecondActivity.this, android.R.layout.simple_list_item_1, null);
            }
            TextView tv = (TextView) convertView;
            tv.setGravity(Gravity.CENTER);
            tv.setPadding(0, 20, 0, 20);
            tv.setText(mList.get(position));
            return convertView;
        }
    }
}
