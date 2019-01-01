package com.example.lenovo.recitewords;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lenovo.recitewords.R;
import com.example.lenovo.recitewords.activity.VideoActivity;
import com.example.lenovo.recitewords.fragment.FourthPager;
import com.youdao.sdk.app.Language;
import com.youdao.sdk.app.LanguageUtils;
import com.youdao.sdk.common.Constants;
import com.youdao.sdk.ydonlinetranslate.Translator;
import com.youdao.sdk.ydtranslate.Translate;
import com.youdao.sdk.ydtranslate.TranslateErrorCode;
import com.youdao.sdk.ydtranslate.TranslateListener;
import com.youdao.sdk.ydtranslate.TranslateParameters;

import bmob.Word;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import android.widget.AdapterView.OnItemClickListener;

public class WordActivity extends AppCompatActivity {
    private List<String> values1;
    private List<String> values2;
    private List<String> values3;
    Handler handler4 = new Handler();
    int random;
    ListView lv;
    TextView view1;
    int sequence1=0;
    int sequence2=0,sequence3=0,symbolbit=0;
    TextView mTextMessage;
    BottomNavigationView navigation;
    private String phonenumber;
    private int wordnum,hintnum;
    private TextView t;

    private int m=0;
    //        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {

                case R.id.navigation_home:

                    return true;
                case R.id.navigation_dashboard:
                    Intent intent = new Intent(WordActivity.this, TranslateActivity.class);
                    intent.putExtra("phonenumber",phonenumber );
                    intent.putExtra("sequence1",String.valueOf(sequence1));
                    intent.putExtra("sequence2",String.valueOf(sequence2 ));
                    intent.putExtra("sequence3",String.valueOf(sequence3 ));
                    intent.putExtra("wordnum",String.valueOf(wordnum) );
                    intent.putExtra("hintnum",String.valueOf(hintnum ));

                    startActivity(intent);
                    WordActivity.this.finish();
                    return true;
                case R.id.navigation_notifications:
                    Intent intent1 = new Intent(WordActivity.this, FourthPager.class);
                    intent1.putExtra("phonenumber",phonenumber );
                    intent1.putExtra("sequence1",String.valueOf(sequence1));
                    intent1.putExtra("sequence2",String.valueOf(sequence2) );
                    intent1.putExtra("sequence3",String.valueOf(sequence3 ));
                    intent1.putExtra("wordnum",String.valueOf(wordnum ));
                    intent1.putExtra("hintnum",String.valueOf(hintnum ));

                    startActivity(intent1);
                    return true;
                case R.id.navigation_video:
                    Intent intent2= new Intent(WordActivity.this, VideoActivity.class);
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
    List<Map<String, Object>> ls= new ArrayList<Map<String, Object>>();
    private String currentusername;
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            values1 = new ArrayList<>();
            values2 = new ArrayList<>();
            values3 = new ArrayList<>();



            setContentView(R.layout.activity_word);
        //ListView lv=(ListView) findViewById(R.id.lv);
        //sequence1=Integer.parseInt(getIntent().getStringExtra("sequence1"));

            lv=(ListView) findViewById(R.id.lv);
            t=(TextView) findViewById(R.id.textView3);


            phonenumber= getIntent().getStringExtra("phonenumber");
            sequence1= Integer.parseInt(getIntent().getStringExtra("sequence1"));
            sequence2= Integer.parseInt(getIntent().getStringExtra("sequence2"));
            sequence3= Integer.parseInt(getIntent().getStringExtra("sequence3"));
            wordnum= Integer.parseInt(getIntent().getStringExtra("wordnum"));
            hintnum= Integer.parseInt(getIntent().getStringExtra("hintnum"));

            t.setText(String.valueOf(wordnum));


        mTextMessage = (TextView) findViewById(R.id.message);
       // currentusername = getIntent().getStringExtra("username");

            Spinner spinner1 = (Spinner) findViewById(R.id.spinner);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                               WordActivity.this, android.R.layout.simple_spinner_item,
                                getData());
                  // 把定义好的Adapter设定到spinner中
                  spinner1.setAdapter(adapter);
            spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                  @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                   int position, long id) {
                                     // 在选中之后触发
                                   Toast.makeText(WordActivity.this,
                                                   parent.getItemAtPosition(position).toString(),
                                                    Toast.LENGTH_SHORT).show();
                                   if(position==0){
                                       symbolbit=0;


                                   }
                                   else if(position==1){
                                       symbolbit=1;

                                   }
                                   else{
                                       symbolbit=2;

                                   }
                      if(values1.size()>0) {
                          it();
                      }

                          }

                   @Override
           public void onNothingSelected(AdapterView<?> parent) {
                                   // 这个一直没有触发，我也不知道什么时候被触发。
                                   //在官方的文档上说明，为back的时候触发，但是无效，可能需要特定的场景
                            }
        });

        Button btn=(Button)findViewById(R.id.button2);//跳转到登录
      // btn.setOnClickListener(listener);

            navigation = (BottomNavigationView) findViewById(R.id.navigation);
            navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


            BmobQuery<Word> query = new BmobQuery<Word>();
/*
            query.addWhereEqualTo("username",currentusername);
            query.findObjects(new FindListener<MyUser>() {
                @Override
                public void done(List<MyUser> list, BmobException e) {
                    if(e == null){
                        if(list.size()!= 0){
                           sequence1=list.get(0).getsequence1();
                        }
                    }0f
                    else{
                         sequence1=0;
                    }
                }
            });
            */
        query.findObjects(new FindListener<Word>() {
            @Override
            public void done(List<Word> list, BmobException e) {
                if(e == null) {
                    for (int i =50; i >= 0 ; i--) {
                            values1.add(list.get(i).getwords());
                            values2.add(list.get(i).getMeaning());
                            values3.add(list.get(i).getLx());
                    }
                    it();
                }
                else{
                    System.out.println(e);
                    Toast ts = Toast.makeText(WordActivity.this,getResources().getString(getResources().getIdentifier("单词获取失败", "string", getPackageName())), Toast.LENGTH_LONG);
                    ts.show() ;
                }
            }
        });




           // ArrayAdapter adapter =new ArrayAdapter(this,android.R.layout.simple_list_item_1,ls);



            Button btn3=(Button)findViewById(R.id.button2);
            btn3.setOnClickListener(listener2);

            Button btn4=(Button)findViewById(R.id.button3);//跳转到登录
            btn4.setOnClickListener(listener);

            Button btn5=(Button)findViewById(R.id.button4);//跳转到登录
            btn5.setOnClickListener(listener4);

            ImageView image=(ImageView)findViewById(R.id.imageView);//跳转到登录
            image.setOnClickListener(listener5);

            view1=(TextView)findViewById(R.id.textView);//跳转到登录

        }
    Button.OnClickListener listener = new Button.OnClickListener() {
        public void onClick(View v) {
            m=1;
            if(symbolbit==0) {
                sequence1--;
                if (sequence1 < 0) {
                    sequence1 = 0;
                }
            }
            else if(symbolbit==1){
                sequence2--;
                if (sequence2 < 0) {
                    sequence2 = 0;
                }
            }
            else{
                sequence3--;
                if (sequence3< 0) {
                    sequence3 = 0;
                }
            }
            it();
        }
    };


    Button.OnClickListener listener2= new Button.OnClickListener() {
        public void onClick(View v) {
            if(hintnum<5) {
                if (symbolbit == 0) {
                    initPopupWindow(values1.get(sequence1), values2.get(sequence1));
                } else if (symbolbit == 1) {
                    initPopupWindow(values1.get(sequence2), values2.get(sequence2));
                } else {
                    initPopupWindow(values1.get(sequence3), values2.get(sequence3));
                }
                showPopWindow();
                hintnum++;
            }
            else{
                Toast.makeText(WordActivity.this, "已到达今日提示上限，请充值!", Toast.LENGTH_SHORT).show();
            }

        }
    };
    Button.OnClickListener listener4= new Button.OnClickListener() {
        public void onClick(View v) {
            if(symbolbit ==0) {
                String strs[] = (values3.get(sequence1)).split("\r\n");
                view1.setText(values3.get(sequence1).replace("/r/n", "\r\n"));
            }
            else   if(symbolbit ==1) {
                String strs[] = (values3.get(sequence2)).split("\r\n");
                view1.setText(values3.get(sequence2).replace("/r/n", "\r\n"));
            }
            else{
                String strs[] = (values3.get(sequence3)).split("\r\n");
                view1.setText(values3.get(sequence3).replace("/r/n", "\r\n"));
            }


        }
    };
    ImageView.OnClickListener listener5= new Button.OnClickListener() {
        public void onClick(View v) {
            if(symbolbit==0) {
                queryWord(values1.get(sequence1));
            }
            else if(symbolbit==1){
                queryWord(values1.get(sequence2));
            }
            else{
                queryWord(values1.get(sequence3));
            }
        }
    };

    PopupWindow popupWindow; View view;
    private void initPopupWindow(String word,String translation) { //要在布局中显示的布局
        view=LayoutInflater.from(this).inflate(
                R.layout.activity_bottom, null,false);
        TextView str1 = (TextView) view.findViewById(R.id.word);
        TextView str2 = (TextView) view.findViewById(R.id.translation);
        str1.setText(word);
        str2.setText(translation);

//实例化PopupWindow并设置宽高
        popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, 200);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
//点击外部消失，这里因为PopupWindow填充了整个窗口，所以这句代码就没用了
        popupWindow.setOutsideTouchable(true); //设置可以点击
        popupWindow.setTouchable(true); //进入退出的动画

    }

    private void showPopWindow() {
        fitPopupWindowOverStatusBar(true);
        View rootview = LayoutInflater.from(WordActivity.this).inflate(R.layout.activity_bottom,
                null); popupWindow.showAtLocation(rootview, Gravity.BOTTOM, 0, 0);
    }

    public void fitPopupWindowOverStatusBar(boolean needFullScreen) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            try { //利用反射重新设置mLayoutInScreen的值，当mLayoutInScreen为true时则PopupWindow覆盖全屏。
                Field mLayoutInScreen = PopupWindow.class.getDeclaredField("mLayoutInScreen");
                mLayoutInScreen.setAccessible(true);
                mLayoutInScreen.set(popupWindow, needFullScreen);
            } catch (NoSuchFieldException e) { e.printStackTrace();
            } catch (IllegalAccessException e) { e.printStackTrace();
            }
        }
    }


    private void initPopupWindow1(String example1,String translation1,String example2,String translation2) { //要在布局中显示的布局
        view=LayoutInflater.from(this).inflate(
                R.layout.activity_example, null,false);
        TextView str1 = (TextView) view.findViewById(R.id.example1);
        TextView str2 = (TextView) view.findViewById(R.id.translation1);
        TextView str3 = (TextView) view.findViewById(R.id.example2);
        TextView str4= (TextView) view.findViewById(R.id.translation2);
        str1.setText(example1);
        str2.setText(translation1);
        str1.setText(example2);
        str2.setText(translation2);

//实例化PopupWindow并设置宽高
        popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, 400);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
//点击外部消失，这里因为PopupWindow填充了整个窗口，所以这句代码就没用了
        popupWindow.setOutsideTouchable(true); //设置可以点击
        popupWindow.setTouchable(true); //进入退出的动画

    }

    private void showPopWindow1() {
        fitPopupWindowOverStatusBar(true);
        View rootview = LayoutInflater.from(WordActivity.this).inflate(R.layout.activity_example,
                null); popupWindow.showAtLocation(rootview, Gravity.BOTTOM, 0, 0);
    }
private void it(){
        if(symbolbit==0) {

                mTextMessage.setText(values1.get(sequence1));
                queryWord(values1.get(sequence1));

        }
        else if(symbolbit==1){

                mTextMessage.setText(values2.get(sequence2));
                queryWord(values1.get(sequence2));

        }
        else{

                mTextMessage.setText("");
                queryWord(values1.get(sequence3));

        }
    view1.setText("");

    random=(int)(Math.random()*10)%3+1;
    Map<String, Object> map = new HashMap<String, Object>();
    Map<String, Object> map1= new HashMap<String, Object>();
    Map<String, Object> map2 = new HashMap<String, Object>();
    Map<String, Object> map3 = new HashMap<String, Object>();
    int sequence;
    if(symbolbit==0){
        sequence=sequence1;
    }
    else  if(symbolbit==1){
        sequence=sequence2;
    }
    else{
        sequence=sequence3;
    }
    if(random==1) {
        if(symbolbit==1){

        map.put("aa", values1.get(sequence));

        map1.put("aa", values1.get((sequence + 20)%50));
        map2.put("aa", values1.get((sequence + 30)%50));
        map3.put("aa", values1.get((sequence + 40)%50));
        }
        else {
        map.put("aa", values2.get(sequence));

        map1.put("aa", values2.get((sequence + 20) % 50));
        map2.put("aa", values2.get((sequence + 30) % 50));
        map3.put("aa", values2.get((sequence + 40) % 50));
    }
    }
    else if(random==2) {
        if(symbolbit==1) {
            map.put("aa", values1.get((sequence + 20) % 50));
            map1.put("aa", values1.get(sequence));
            map2.put("aa", values1.get((sequence + 30) % 50));
            map3.put("aa", values1.get((sequence + 40) % 50));
        }
        else{
            map.put("aa", values2.get((sequence + 20) % 50));
            map1.put("aa", values2.get(sequence));
            map2.put("aa", values2.get((sequence + 30) % 50));
            map3.put("aa", values2.get((sequence + 40) % 50));
        }
    }
    else if(random==3) {
        if(symbolbit==1) {
            map.put("aa", values1.get((sequence + 20) % 50));
            map1.put("aa", values1.get((sequence + 30) % 50));
            map2.put("aa", values1.get(sequence));
            map3.put("aa", values1.get((sequence + 40) % 50));
        }else{
            map.put("aa", values2.get((sequence + 20) % 50));
            map1.put("aa", values2.get((sequence + 30) % 50));
            map2.put("aa", values2.get(sequence));
            map3.put("aa", values2.get((sequence + 40) % 50));
        }
    }
    else{
        if(symbolbit==1) {
            map.put("aa", values2.get((sequence + 20) % 50));
            map1.put("aa", values2.get((sequence + 30) % 50));
            map2.put("aa", values2.get((sequence + 40) % 50));
            map3.put("aa", values2.get(sequence));
        }
        else{
            map.put("aa", values1.get((sequence + 20) % 50));
            map1.put("aa", values1.get((sequence + 30) % 50));
            map2.put("aa", values1.get((sequence + 40) % 50));
            map3.put("aa", values1.get(sequence));
        }
    }
    ls.clear();
    ls.add(map);
    ls.add(map1);
    ls.add(map2);
    ls.add(map3);
    lv.setAdapter(new SimpleAdapter(this,ls,R.layout.list_item,new String[]{"aa"},new int[]{R.id.aa}));
    lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    lv.setOnItemClickListener(listener1);
    /*
    lv.setOnItemClickListener(new OnItemClickListener(){
        //list点击事件
        @Override
        public void onItemClick(AdapterView<?> p1, View p2, int p3, long p4)
        {
            // TODO: Implement this method
            switch(p3){
                case 0://第一个item
                    if(random==1){
                        //切换界面
                    }
                    else {
                        Toast.makeText(WordActivity.this, "Wrong!", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 1://第二个item
                    if(random==2){
                        //切换界面
                    }
                    else {
                        Toast.makeText(WordActivity.this, "Wrong!", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 2://第三个item
                    if(random==3){
                        //切换界面
                    }
                    else {
                        Toast.makeText(WordActivity.this, "Wrong!", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 3://第三个item
                    if(random==1){
                        //切换界面
                    }
                    else {
                        Toast.makeText(WordActivity.this, "Wrong!", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }


    });*/
    }

     OnItemClickListener listener1 =new OnItemClickListener(){
         public void onItemClick(AdapterView<?> p1, View p2, int p3, long p4)
         {
             switch(p3){
                 case 0://第一个item
                     if(random==1){
                         //切换界面
                         Toast.makeText(WordActivity.this, "Right", Toast.LENGTH_SHORT).show();
                         if (symbolbit==0) {
                             sequence1++;

                         }
                         else  if (symbolbit==1) {
                             sequence2++;

                         }
                         else{
                             sequence3++;

                         }
                         if(m==0) {
                             wordnum++;
                             t.setText(String.valueOf(wordnum));
                         }
                         else{
                             m=0;
                         }
                         it();
                     }
                     else {
                         Toast.makeText(WordActivity.this, "Wrong", Toast.LENGTH_SHORT).show();
                     }
                     break;
                 case 1://第二个item
                     if(random==2){
                         //切换界面
                         Toast.makeText(WordActivity.this, "Right", Toast.LENGTH_SHORT).show();

                         if (symbolbit==0) {
                             sequence1++;
                         }
                         else  if (symbolbit==1) {
                             sequence2++;
                         }
                         else{
                             sequence3++;
                         }
                         if(m==0) {
                             wordnum++;
                             t.setText(String.valueOf(wordnum));
                         }
                         else{
                             m=0;
                         }
                         it();
                     }
                     else {
                         Toast.makeText(WordActivity.this, "Wrong", Toast.LENGTH_SHORT).show();
                     }
                     break;
                 case 2://第三个item
                     if(random==3){
                         //切换界面
                         Toast.makeText(WordActivity.this, "Right", Toast.LENGTH_SHORT).show();

                         if (symbolbit==0) {
                             sequence1++;
                         }
                         else  if (symbolbit==1) {
                             sequence2++;
                         }
                         else{
                             sequence3++;
                         }
                         if(m==0) {
                             wordnum++;
                             t.setText(String.valueOf(wordnum));
                         }
                         else{
                             m=0;
                         }
                         it();
                     }
                     else {
                         Toast.makeText(WordActivity.this, "Wrong", Toast.LENGTH_SHORT).show();
                     }
                     break;
                 case 3://第三个item
                     if(random==4){
                         //切换界面
                         Toast.makeText(WordActivity.this, "Right", Toast.LENGTH_SHORT).show();

                         if (symbolbit==0) {
                             sequence1++;
                         }
                         else  if (symbolbit==1) {
                             sequence2++;
                         }
                         else{
                             sequence3++;
                         }
                         if(m==0) {
                             wordnum++;
                             t.setText(String.valueOf(wordnum));
                         }
                         else{
                             m=0;
                         }
                         it();
                     }
                     else {
                         Toast.makeText(WordActivity.this, "Wrong", Toast.LENGTH_SHORT).show();
                     }
                     break;
             }
        }
    };
    public void queryWord(final String word){
        Language langFrom = LanguageUtils.getLangByName("英文");
        Language langTo = LanguageUtils.getLangByName("自动");

        TranslateParameters tps = new TranslateParameters.Builder()
                .source("youdao").from(langFrom).to(langTo).sound(Constants.SOUND_OUTPUT_MP3).voice(Constants.VOICE_BOY_UK).timeout(3000).build();// appkey可以省


        Translator translator2 = Translator.getInstance(tps);
        translator2.lookup(word, "requestId", new TranslateListener() {
            @Override
            public void onResult(final Translate result, String input, String requestId) {
                handler4.post(new Runnable() {
                    @Override
                    public void run() {
                        TranslateData td = new TranslateData(
                                System.currentTimeMillis(), result);
                        if (!TextUtils.isEmpty(td.getTranslate().getPhonetic())) {
                            String spell_url = td.getTranslate().getSpeakUrl();
                            AudioMgr.startPlayVoice(spell_url, new AudioMgr.SuccessListener() {
                                @Override
                                public void success() {

                                }

                                @Override
                                public void playover() {

                                }
                            });
                        }
                        else{
                            Toast.makeText(WordActivity.this, "发音获取失败:", Toast.LENGTH_LONG)
                                    .show();
                        }
                    }
                });
            }

            @Override
            public void onError(final TranslateErrorCode error, String requestId) {
                handler4.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WordActivity.this, "发音获取失败:" + error.name(), Toast.LENGTH_LONG)
                                .show();

                    }
                });
            }
            @Override
            public void onResult(List<Translate> results, List<String> inputs, List<TranslateErrorCode> errors, String requestId) {

            }
        });
    }
    private List<String> getData() {
              // 数据源
                List<String> dataList = new ArrayList<String>();
                dataList.add("英译汉");
                dataList.add("汉译英");
                dataList.add("听音识词");
                return dataList;
            }
}
