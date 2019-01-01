package com.example.lenovo.recitewords.Util;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lenovo.recitewords.R;
import com.example.lenovo.recitewords.activity.CalendarActivity;
import com.example.lenovo.recitewords.activity.WordListActivity;
import com.leon.lib.settingview.LSettingItem;

public class FourthPager extends Fragment {

    LSettingItem calendar;
    LSettingItem content;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.mefragment, container, false);
        initComponents();
        content.setmOnLSettingItemClick(new LSettingItem.OnLSettingItemClick() {
            @Override
            public void click(boolean isChecked) {
                Intent intent = new Intent(getActivity(),WordListActivity.class);

                startActivity(intent);
            }
        });
        calendar.setmOnLSettingItemClick(new LSettingItem.OnLSettingItemClick() {
            @Override
            public void click(boolean isChecked) {
                Intent intent = new Intent(getActivity(),CalendarActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }
    void initComponents(){
        content = view.findViewById(R.id.content);
        calendar = view.findViewById(R.id.calendar);
    }

}


