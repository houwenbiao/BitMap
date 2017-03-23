package com.gree.bitmap;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import libcore.io.ImageAdapter;
import libcore.io.ImageLoader;

public class MainActivity extends AppCompatActivity implements AbsListView.OnScrollListener
{
    private List<String> mUrList = new ArrayList<>();
    private ImageLoader mImageLoader;
    private GridView mGridView;
    private ImageView imageView;
    private Bitmap bitmap;
    private ImageAdapter adapter;

    //GridView是否正在滑动
    public static boolean mIsGridViewIdle = true;
    /*最大滑动速度*/
    private static final int MAX_SCROLLING_SPEED = 30;
    private int mSpeedCheckSwitch = 0;
    private int mPreviousFirstVisibleItem = 0;
    private long mPreviousEventTime = 0;
    private double mScrollSpeed = 0;
    private static final Executor CACHED_THREAD_POOL = Executors.newCachedThreadPool();


    /*测试loadBitmap*/
    /*private Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            if(msg.what == 01)
            {
                Bitmap bitmap = (Bitmap) msg.obj;
                imageView.setImageBitmap(bitmap);
            }
        }
    };*/

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mImageLoader = ImageLoader.build(this);
        imageView = (ImageView) findViewById(R.id.image1);
        /*测试bindBitmap*/
        //        mImageLoader.bindBitmap("http://imgc.wbiao.cn/201202/17/8747_50083.jpg", imageView, 100, 100);

        /*测试loadBitmap*/
        /*Thread thread = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                bitmap = mImageLoader.loadBitmap("http://i1.hdslb.com/bfs/face/22e761f3a6117ce40153d1b006bfc4a3fb51d59e.jpg", 50, 50);
                Message message = new Message();
                message.what = 01;
                message.obj = bitmap;
                handler.sendMessage(message);
            }
        });
        thread.start();*/

        /*测试图片墙*/
        initData();
        mGridView = (GridView) findViewById(R.id.gridView);
        adapter = new ImageAdapter(this, mUrList);
        mGridView.setAdapter(adapter);
    }

    private void initData()
    {
        for(int i = 0; i < 10000; i++)
        {
            mUrList.add("http://img1.imgtn.bdimg.com/it/u=4182224651,3680935464&fm=21&gp=0.jpg");
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState)
    {
        /*当前滑动停止时更新可见item*/
        if(scrollState == SCROLL_STATE_IDLE)
        {
            mIsGridViewIdle = true;
            adapter.notifyDataSetChanged();
        }
    }


    /**
     * @param view
     * @param firstVisibleItem:当前能看见的第一个item
     * @param visibleItemCount:当前能看见的列表项个数
     * @param totalItemCount:列表项总数
     */
    @Override
    public void onScroll(AbsListView view, final int firstVisibleItem, int visibleItemCount, int totalItemCount)
    {
        mSpeedCheckSwitch++;
        if(mSpeedCheckSwitch == 2)
        {
            CACHED_THREAD_POOL.execute(new Runnable()
            {
                @Override
                public void run()
                {
                    //滑动速度较快时候不更新item，防止快速滑动过程中产生大量异步更新任务，导致卡顿
                    if(mPreviousFirstVisibleItem != firstVisibleItem)
                    {
                        /*计算滑动每个item的时间*/
                        long currentTime = System.currentTimeMillis();
                        long timeScrollOneElement = currentTime - mPreviousEventTime;
                        mScrollSpeed = (1d / timeScrollOneElement) * 1000;
                        Log.i("zxxx", "mScrollSpeed:" + mScrollSpeed);
                        /*时间和首个item重新赋值*/
                        mPreviousFirstVisibleItem = firstVisibleItem;
                        mPreviousEventTime = currentTime;

                        if(mScrollSpeed > MAX_SCROLLING_SPEED)
                        {
                            mIsGridViewIdle = false;
                        }
                        else
                        {
                            mIsGridViewIdle = true;
                        }
                    }
                }
            });
            mSpeedCheckSwitch = 0;
        }
    }
}
