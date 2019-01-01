package com.example.lenovo.recitewords.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;


import com.example.lenovo.recitewords.R;
import com.example.lenovo.recitewords.TranslateActivity;
import com.example.lenovo.recitewords.WordActivity;
import com.example.lenovo.recitewords.activity.CalendarActivity;
import com.example.lenovo.recitewords.activity.VideoActivity;
import com.example.lenovo.recitewords.activity.WordListActivity;
import com.leon.lib.settingview.LSettingItem;

import java.util.Date;

public class FourthPager extends AppCompatActivity {

    LSettingItem calendar;
    LSettingItem content;
    LSettingItem myword;
    View view;
    private String phonenumber;
    private int sequence1,sequence2,sequence3,wordnum,hintnum;
    private Date date1,date2;
    private String date3,date4;

    BottomNavigationView navigation;
    BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {

                case R.id.navigation_home:
                    Intent intent1 = new Intent(FourthPager.this, WordActivity.class);
                    intent1.putExtra("phonenumber",phonenumber );
                    intent1.putExtra("sequence1",String.valueOf(sequence1 ));
                    intent1.putExtra("sequence2",String.valueOf(sequence2) );
                    intent1.putExtra("sequence3",String.valueOf(sequence3 ));
                    intent1.putExtra("wordnum",String.valueOf(wordnum ));
                    intent1.putExtra("hintnum",String.valueOf(hintnum ));

                    startActivity(intent1);
                    return true;
                case R.id.navigation_dashboard:
                    Intent intent = new Intent(FourthPager.this, TranslateActivity.class);
                    intent.putExtra("phonenumber",phonenumber );
                    intent.putExtra("sequence1",String.valueOf(sequence1 ));
                    intent.putExtra("sequence2",String.valueOf(sequence2) );
                    intent.putExtra("sequence3",String.valueOf(sequence3 ));
                    intent.putExtra("wordnum",String.valueOf(wordnum ));
                    intent.putExtra("hintnum",String.valueOf(hintnum ));

                    startActivity(intent);

                    return true;
                case R.id.navigation_notifications:


                case R.id.navigation_video:
                    Intent intent2= new Intent(FourthPager.this, VideoActivity.class);
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
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mefragment);
        initComponents();

        phonenumber= getIntent().getStringExtra("phonenumber");
        sequence1= Integer.parseInt(getIntent().getStringExtra("sequence1"));
        sequence2= Integer.parseInt(getIntent().getStringExtra("sequence2"));
        sequence3= Integer.parseInt(getIntent().getStringExtra("sequence3"));
        wordnum= Integer.parseInt(getIntent().getStringExtra("wordnum"));
        hintnum= Integer.parseInt(getIntent().getStringExtra("hintnum"));

        content.setmOnLSettingItemClick(new LSettingItem.OnLSettingItemClick() {
            @Override
            public void click(boolean isChecked) {
                Intent intent = new Intent(FourthPager.this,WordListActivity.class);
                startActivity(intent);
            }
        });
        calendar.setmOnLSettingItemClick(new LSettingItem.OnLSettingItemClick() {
            @Override
            public void click(boolean isChecked) {
                Intent intent = new Intent(FourthPager.this,CalendarActivity.class);
                startActivity(intent);
            }
        });
        myword.setmOnLSettingItemClick(new LSettingItem.OnLSettingItemClick() {
            @Override
            public void click(boolean isChecked) {
                //Intent intent = new Intent(FourthPager.this,MyWordActivity.class);
                //startActivity(intent);
            }
        });
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        view = inflater.inflate(R.layout.mefragment, container, false);
//        initComponents();
//        content.setmOnLSettingItemClick(new LSettingItem.OnLSettingItemClick() {
//            @Override
//            public void click(boolean isChecked) {
//                Intent intent = new Intent(getActivity(),WordListActivity.class);
//                startActivity(intent);
//            }
//        });
//        calendar.setmOnLSettingItemClick(new LSettingItem.OnLSettingItemClick() {
//            @Override
//            public void click(boolean isChecked) {
//                Intent intent = new Intent(getActivity(),CalendarActivity.class);
//                startActivity(intent);
//            }
//        });
//        return view;
//    }
    void initComponents(){
        setContentView(R.layout.mefragment);
        content =this.findViewById(R.id.content1);
        calendar = this.findViewById(R.id.calendar);
        myword = this.findViewById(R.id.myword);
    }

}


