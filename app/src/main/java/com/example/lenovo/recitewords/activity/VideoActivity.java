package com.example.lenovo.recitewords.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.Window;
import android.widget.TextView;

import com.example.lenovo.recitewords.R;
import com.example.lenovo.recitewords.TranslateActivity;
import com.example.lenovo.recitewords.WordActivity;
import com.example.lenovo.recitewords.fragment.FourthPager;
import com.example.lenovo.recitewords.fragment.VideoFragment;

import java.util.ArrayList;

public class VideoActivity extends AppCompatActivity implements VideoFragment.OnFragmentInteractionListener{

    private TextView mTextMessage;
    private MyAdapter myAdapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ArrayList<Fragment> listFragment;
    private String phonenumber;
    private int sequence1,sequence2,sequence3,wordnum,hintnum;
    BottomNavigationView navigation;

    BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {

                case R.id.navigation_home:
                    Intent intent1 = new Intent(VideoActivity.this, WordActivity.class);
                    intent1.putExtra("phonenumber",phonenumber );
                    intent1.putExtra("sequence1",String.valueOf(sequence1 ));
                    intent1.putExtra("sequence2",String.valueOf(sequence2) );
                    intent1.putExtra("sequence3",String.valueOf(sequence3 ));
                    intent1.putExtra("wordnum",String.valueOf(wordnum ));
                    intent1.putExtra("hintnum",String.valueOf(hintnum ));

                    startActivity(intent1);
                    return true;
                case R.id.navigation_dashboard:
                    Intent intent = new Intent(VideoActivity.this, TranslateActivity.class);
                    intent.putExtra("phonenumber",phonenumber );
                    intent.putExtra("sequence1",String.valueOf(sequence1 ));
                    intent.putExtra("sequence2",String.valueOf(sequence2) );
                    intent.putExtra("sequence3",String.valueOf(sequence3 ));
                    intent.putExtra("wordnum",String.valueOf(wordnum ));
                    intent.putExtra("hintnum",String.valueOf(hintnum ));
                    startActivity(intent);

                    return true;


                case R.id.navigation_notifications:
                    Intent intent2= new Intent(VideoActivity.this, FourthPager.class);
                    intent2.putExtra("phonenumber",phonenumber );
                    intent2.putExtra("sequence1",String.valueOf(sequence1 ));
                    intent2.putExtra("sequence2",String.valueOf(sequence2) );
                    intent2.putExtra("sequence3",String.valueOf(sequence3 ));
                    intent2.putExtra("wordnum",String.valueOf(wordnum ));
                    intent2.putExtra("hintnum",String.valueOf(hintnum ));

                    startActivity(intent2);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //设置子菜单（课程类型）
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);

        listFragment= new ArrayList<>();

        phonenumber= getIntent().getStringExtra("phonenumber");
        sequence1= Integer.parseInt(getIntent().getStringExtra("sequence1"));
        sequence2= Integer.parseInt(getIntent().getStringExtra("sequence2"));
        sequence3= Integer.parseInt(getIntent().getStringExtra("sequence3"));
        wordnum= Integer.parseInt(getIntent().getStringExtra("wordnum"));
        hintnum= Integer.parseInt(getIntent().getStringExtra("hintnum"));


        VideoFragment fragment1 = new VideoFragment();
        Bundle bundle1 = new Bundle();
        bundle1.putString("type","精品课程");

        fragment1.setArguments(bundle1);
        listFragment.add(fragment1);

        VideoFragment fragment2 = new VideoFragment();
        Bundle bundle2 = new Bundle();
        bundle2.putString("type","记忆方法");

        fragment2.setArguments(bundle2);
        listFragment.add(fragment2);

        VideoFragment fragment3 = new VideoFragment();
        Bundle bundle3 = new Bundle();
        bundle3.putString("type","兴趣技能");

        fragment3.setArguments(bundle3);
        listFragment.add(fragment3);

        FragmentManager fm=getSupportFragmentManager();
        myAdapter = new MyAdapter(fm);
        viewPager.setAdapter(myAdapter);
        //ViewPager的预加载解决办法
        viewPager.setOffscreenPageLimit(listFragment.size());
        //TabLayout和ViewPager进行联动
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.removeAllTabs();
        tabLayout.addTab(tabLayout.newTab().setText("精品课程"));
        tabLayout.addTab(tabLayout.newTab().setText("记忆方法"));
        tabLayout.addTab(tabLayout.newTab().setText("兴趣技能"));


    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    class MyAdapter extends FragmentPagerAdapter {

        private MyAdapter(FragmentManager fm) {
            super(fm);
        }
        @Override
        public Fragment getItem(int position) {
            return listFragment.get(position);
        }

        @Override
        public int getCount() {
            return listFragment.size();
        }

    }

}
