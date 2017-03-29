package com.example.bin.bannerdemo;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    private List<ImageView> pointList;
    private int pointCurrentItem;
    private boolean startBanner = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        LinearLayout pointLayout = (LinearLayout) findViewById(R.id.point_layout);

        List<TextView> views = new ArrayList<>();
        TextView textView;
        for (int i = 0; i < 3; i++) {
            textView = new TextView(this);
            textView.setText(i + "个");
            textView.setTextSize(16.0f);
            textView.setGravity(Gravity.CENTER);
            views.add(textView);
        }

        viewPager.setAdapter(new MyAdapter(views));
        viewPager.addOnPageChangeListener(this);
        viewPager.setCurrentItem(0);

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (startBanner) {
                    SystemClock.sleep(2000);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                        }
                    });
                }
            }
        }).start();

        //设置轮播的点
        pointList = new ArrayList<>();
        for (int i = 0; i < views.size(); i++) {
            ImageView point = new ImageView(this);
//            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-2, -2);
//            layoutParams.rightMargin = 12; //设置边距
//            layoutParams.leftMargin = 12; //设置边距
//            point.setLayoutParams(layoutParams);
            point.setPadding(5, 0, 5, 0);
//            if (i == 0) {
//                point.setImageResource(R.drawable.banner_point_select);
//            } else {
//                point.setImageResource(R.drawable.banner_point_no_select);
//            }
            point.setImageResource(R.drawable.banner_point_no_select);
            pointLayout.addView(point);
            pointList.add(point);
        }
        pointList.get(pointCurrentItem).setImageResource(R.drawable.banner_point_select);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        Log.e("position", "onPageSelected: " + position);
        int newPoint = position % pointList.size();
        pointList.get(pointCurrentItem).setImageResource(R.drawable.banner_point_no_select);
        pointList.get(newPoint).setImageResource(R.drawable.banner_point_select);
        pointCurrentItem = newPoint;
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    private class MyAdapter extends PagerAdapter {

        private List<TextView> list;

        MyAdapter(List<TextView> list) {
            this.list = list;
        }

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

          container.addView(list.get(position%list.size()));
            return list.get(position % list.size());
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(list.get(position % list.size()));
        }
    }

    @Override
    protected void onPause() {
        startBanner = false;
        super.onPause();
    }

    @Override
    protected void onResume() {
        startBanner = true;
        super.onResume();
    }
}
