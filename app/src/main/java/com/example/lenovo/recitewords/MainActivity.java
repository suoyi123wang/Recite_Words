package com.example.lenovo.recitewords;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.lenovo.recitewords.activity.WordListActivity;
import com.example.lenovo.recitewords.fragment.FourthPager;
import com.mob.MobSDK;
import com.youdao.sdk.app.YouDaoApplication;

import cn.bmob.v3.Bmob;
import fragment.LogIn;
import fragment.Register;
import cn.bmob.v3.exception.BmobException;
//import com.example.lenovo.recitewords.R;


public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private String para_phone;

    public String getParaPhone() {
        return para_phone;
    }

    public void setParaPhone(String para_phone) {
        this.para_phone = para_phone;
    }
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Intent intent = new Intent(MainActivity.this, WordActivity.class);
                    intent.putExtra("sequence","0");
                    startActivity(intent);
                    MainActivity.this.finish();
                    return true;
                case R.id.navigation_dashboard:
                     intent = new Intent(MainActivity.this, TranslateActivity.class);
                    intent.putExtra("sequence","0");
                    startActivity(intent);
                    MainActivity.this.finish();
                    return true;
                case R.id.navigation_notifications:
                    Intent intent1 = new Intent(MainActivity.this, FourthPager.class);
                    startActivity(intent1);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bmob.initialize(this, "d1dbda52dee22e0fbd765545b705e2e7");
        //这是啥？
        StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        //分享
        MobSDK.init(this,"moba6b6c6d6","b89d2427a3bc7ad1aea1e1e8c1d36bf3");
        setContentView(R.layout.activity_main);

        YouDaoApplication.init(this,"zhudytest123");

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame,
                new LogIn()).commit();
    }
}
