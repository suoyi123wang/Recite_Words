package com.example.lenovo.recitewords.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.lenovo.recitewords.R;

public class CalendarActivity extends AppCompatActivity {

    private SignDate signDate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar);
        signDate = findViewById(R.id.signDate);

//        signDate.setOnSignedSuccess(new OnSignedSuccess() {
//            @Override
//            public void OnSignedSuccess() {
//                Log.e("wqf","Success");
//            }
//        });
    }
}
