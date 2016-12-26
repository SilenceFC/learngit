package com.ut.learn.custom;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    SwipeRefreshLayout swipe;
    ListView list;
    Button bt;
    ArrayAdapter<String> mAdapter;
    ArrayList<String> mList = new ArrayList<String>(Arrays.asList("java","javaScript","C++","Ruby","Json"));

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what ==0){
                mList.addAll(Arrays.asList("Android","iOS","YunOS"));
                mAdapter.notifyDataSetChanged();
                swipe.setRefreshing(false);
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        setView();
    }


    private void setView() {
        swipe.setColorSchemeColors(Color.YELLOW,Color.GREEN,Color.RED,Color.BLUE);
        swipe.setOnRefreshListener(MainActivity.this);
        mAdapter = new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_1,mList);
        list.setAdapter(mAdapter);
    }

    private void initView() {
        bt = (Button) findViewById(R.id.bt);
        swipe = (SwipeRefreshLayout) findViewById(R.id.swipe);
        list = (ListView) findViewById(R.id.list);

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,SecondActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onRefresh() {
        mHandler.sendEmptyMessageDelayed(0,2000);
    }
}
