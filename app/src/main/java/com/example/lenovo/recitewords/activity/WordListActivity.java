package com.example.lenovo.recitewords.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.lenovo.recitewords.Data.WordList;
import com.example.lenovo.recitewords.R;
import com.example.lenovo.recitewords.adapter.WordAdapter;
import com.hjq.bar.OnTitleBarListener;
import com.hjq.bar.TitleBar;

import java.util.ArrayList;
import java.util.List;

public class WordListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    List<WordList> wordlist = new ArrayList<WordList>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_list);
        WordAdapter adapter = new WordAdapter(WordListActivity.this, R.layout.word_item, wordlist);
        ListView listView = findViewById(R.id.word_list1);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        TitleBar bar = findViewById(R.id.titlebar);
        bar.setOnTitleBarListener(new OnTitleBarListener() {

            @Override
            public void onLeftClick(View v) {

                WordListActivity.this.finish();
            }
            @Override
            public void onTitleClick(View v) {

            }

            @Override
            public void onRightClick(View v) {

            }
        });

        init();
    }

    private void init(){
        WordList c1 = new WordList("共计1000个单词","android 第一小组","CET4");
        WordList c2 = new WordList("共计999个单词","android 第二小组","CET6");
        WordList c3 = new WordList("共计999个单词","android 第三小组","CET8");
        wordlist.add(c1);
        wordlist.add(c2);
        wordlist.add(c3);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String showText = "点击第" + position + "，ID为：" + id;
        Toast.makeText(this, showText, Toast.LENGTH_LONG).show();
    }
}
