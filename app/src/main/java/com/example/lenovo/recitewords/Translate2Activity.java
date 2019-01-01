package com.example.lenovo.recitewords;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.youdao.sdk.common.YouDaoLog;
import com.youdao.sdk.ydtranslate.Translate;


public class Translate2Activity extends AppCompatActivity {

    public static void open(Activity activity, TranslateData news, Translate trs) {
        Intent in = new Intent(activity, Translate2Activity.class);
        in.putExtra("news", news);
        in.putExtra("trs", trs);
        activity.startActivity(in);
    }

    TranslateData translateData;
    Translate trs;

    private TextView mTextMessage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translate2);

        mTextMessage = (TextView) findViewById(R.id.message);
        setResult(Activity.RESULT_OK);
        try {
            getWindow().requestFeature(Window.FEATURE_PROGRESS);
            getWindow().setFeatureInt(Window.FEATURE_PROGRESS,
                    Window.PROGRESS_VISIBILITY_ON);
        } catch (Exception e) {}
        setContentView(R.layout.activity_translate2);

        translateData = (TranslateData) this.getIntent().getSerializableExtra(
                "news");
        trs = (Translate) this.getIntent().getSerializableExtra(
                "trs");
        TextView input = (TextView) findViewById(R.id.fanyiInputText);
        AppCompatImageView image1 = findViewById(R.id.startBtn_i1);
        Button start = (Button)findViewById(R.id.startBtn);
        if(TextUtils.isEmpty(trs.getSpeakUrl()))
        {
            image1.setVisibility(View.GONE);
            start.setVisibility(View.GONE);
        }
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playVoice(trs.getSpeakUrl());
            }
        });

        AppCompatImageView image2 = findViewById(R.id.startBtn_i2);
        Button usstart = (Button)findViewById(R.id.usstartBtn);
        if(TextUtils.isEmpty(trs.getUSSpeakUrl()))
        {
            image2.setVisibility(View.GONE);
            usstart.setVisibility(View.GONE);
        }
        usstart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playVoice(trs.getUSSpeakUrl());
            }
        });

        AppCompatImageView image3 = findViewById(R.id.startBtn_i3);
        Button ukstart = (Button)findViewById(R.id.ukstartBtn);
        if(TextUtils.isEmpty(trs.getUKSpeakUrl()))
        {
            image3.setVisibility(View.GONE);
            ukstart.setVisibility(View.GONE);
        }
        ukstart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playVoice(trs.getUKSpeakUrl());
            }
        });

        TextView translation = (TextView) findViewById(R.id.translation);
        Button start1 = (Button)findViewById(R.id.startBtn1);
        if(TextUtils.isEmpty(trs.getResultSpeakUrl()))
        {
            start1.setVisibility(View.GONE);
        }
        start1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playVoice(trs.getResultSpeakUrl());
            }
        });
        TextView spell = (TextView) findViewById(R.id.spell);
        TextView ukSpell = (TextView) findViewById(R.id.ukSpell);
        TextView usSpell = (TextView) findViewById(R.id.usSpell);
        TextView means = (TextView) findViewById(R.id.means);
        TextView webmeans = (TextView) findViewById(R.id.webmeans);
        TextView moreBtn = (TextView) findViewById(R.id.moreBtn);

        if (!TextUtils.isEmpty(translateData.getTranslate().getQuery())) {
            input.setText("输入：" + translateData.getQuery());
        }

        if (!TextUtils.isEmpty(translateData.translates())) {
            translation.setText("结果："+ translateData.translates());
        }

        if (!TextUtils.isEmpty(translateData.getTranslate().getPhonetic())) {
            spell.setText("发音：" + translateData.getTranslate().getPhonetic());
        }

        if (!TextUtils.isEmpty(translateData.getTranslate().getUkPhonetic())) {
            ukSpell.setText("英式发音："
                    + translateData.getTranslate().getUkPhonetic());
        }

        if (!TextUtils.isEmpty(translateData.getTranslate().getUsPhonetic())) {
            usSpell.setText("美式发音："
                    + translateData.getTranslate().getUsPhonetic());
        }

        if (!TextUtils.isEmpty(translateData.means())) {
            means.setText("查词结果：\n" + translateData.means());
        }

        if (!TextUtils.isEmpty(translateData.webMeans())) {
            webmeans.setText("网络释义：\n" + translateData.webMeans());
        }
        moreBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                trs.openMore(Translate2Activity.this);
            }
        });

    }

    public synchronized void playVoice(String speakUrl) {
        YouDaoLog.e(AudioMgr.PLAY_LOG + "TranslateDetailActivity click to playVoice speakUrl = " + speakUrl);
        if (!TextUtils.isEmpty(speakUrl) && speakUrl.startsWith("http")) {
            Toast.makeText(Translate2Activity.this, "正在发音",
                    Toast.LENGTH_LONG).show();
            AudioMgr.startPlayVoice(speakUrl, new AudioMgr.SuccessListener() {
                @Override
                public void success() {
                    YouDaoLog.e(AudioMgr.PLAY_LOG + "TranslateDetailActivity playVoice success");
                }

                @Override
                public void playover() {
                    YouDaoLog.e(AudioMgr.PLAY_LOG + "TranslateDetailActivity playover");
                }
            });
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void finish() {
        super.finish();
    }

    public void Clear2(View view) {
        onBackPressed();
    }
}
