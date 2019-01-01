package com.example.lenovo.recitewords;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lenovo.recitewords.activity.VideoActivity;
import com.example.lenovo.recitewords.activity.WordListActivity;
import com.example.lenovo.recitewords.fragment.FourthPager;
import com.youdao.sdk.app.EncryptHelper;
import com.youdao.sdk.app.Language;
import com.youdao.sdk.app.LanguageUtils;
import com.youdao.sdk.common.Constants;
import com.youdao.sdk.ydonlinetranslate.LanguageOcrTranslate;
import com.youdao.sdk.ydonlinetranslate.OCRTranslateResult;
import com.youdao.sdk.ydonlinetranslate.OcrTranslate;
import com.youdao.sdk.ydonlinetranslate.OcrTranslateListener;
import com.youdao.sdk.ydonlinetranslate.OcrTranslateParameters;
import com.youdao.sdk.ydonlinetranslate.Region;
import com.youdao.sdk.ydonlinetranslate.SpeechTranslate;
import com.youdao.sdk.ydonlinetranslate.SpeechTranslateParameters;
import com.youdao.sdk.ydonlinetranslate.Translator;
import com.youdao.sdk.ydtranslate.Translate;
import com.youdao.sdk.ydtranslate.TranslateErrorCode;
import com.youdao.sdk.ydtranslate.TranslateListener;
import com.youdao.sdk.ydtranslate.TranslateParameters;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import fragment.OCRFragment;
import fragment.SRFragment;
import fragment.TranslateFragment;
import shape.BorderTextView;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class TranslateActivity extends FragmentActivity implements OCRFragment.OnFragmentInteractionListener,
        TranslateFragment.OnFragmentInteractionListener, SRFragment.OnFragmentInteractionListener {

    private TextView mTextMessage;
    public Fragment translateFragment = new TranslateFragment();
    public Fragment ocrFragment = new OCRFragment();
    public Fragment srFragment = new SRFragment();

    private TextView original;
    private Uri currentUri;
    private String filePath;
    private ImageView imageView;
    private BorderTextView borderTextView;
    private Handler handler = new Handler();

    private File audioFile;
    private Translate tr = null;
    ExtAudioRecorder recorder;
    SpeechTranslateParameters tps;
    Handler handler2 = new Handler();
    private String phonenumber;
    private int sequence1,sequence2,sequence3,wordnum,hintnum;


    int alpha = 255;

    // 查询列表
    private ListView translateList;

    private TranslateAdapter adapter;

    private List<TranslateData> list = new ArrayList<TranslateData>();
    private List<Translate> trslist = new ArrayList<Translate>();


    private ProgressDialog progressDialog = null;

    private Handler waitHandler = new Handler();

    private EditText fanyiInputText;

    private InputMethodManager imm;

    private TextView fanyiBtn;

    TextView languageSelectFrom;

    TextView languageSelectTo;

    private Translator translator;
    Handler handler3 = new Handler();


    public static final int UNUSED_REQUEST_CODE = 255;  // Acceptable range is [0, 255]

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Intent intent = new Intent(TranslateActivity.this, WordActivity.class);
                    intent.putExtra("phonenumber",phonenumber );
                    intent.putExtra("sequence1",String.valueOf(sequence1 ));
                    intent.putExtra("sequence2",String.valueOf(sequence2));
                    intent.putExtra("sequence3",String.valueOf(sequence3 ));
                    intent.putExtra("wordnum",String.valueOf(wordnum ));
                    intent.putExtra("hintnum",String.valueOf(hintnum ));

                    startActivity(intent);
                    TranslateActivity.this.finish();

                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_notifications:
                    Intent intent1 = new Intent(TranslateActivity.this, FourthPager.class);
                    intent1.putExtra("phonenumber",phonenumber );
                    intent1.putExtra("sequence1",String.valueOf(sequence1 ));
                    intent1.putExtra("sequence2",String.valueOf(sequence2) );
                    intent1.putExtra("sequence3",String.valueOf(sequence3 ));
                    intent1.putExtra("wordnum",String.valueOf(wordnum ));
                    intent1.putExtra("hintnum",String.valueOf(hintnum ));

                    startActivity(intent1);
                    return true;

                case R.id.navigation_video:
                    Intent intent2= new Intent(TranslateActivity.this, VideoActivity.class);
                    intent2.putExtra("phonenumber",phonenumber );
                    intent2.putExtra("sequence1",String.valueOf(sequence1 ));
                    intent2.putExtra("sequence2",String.valueOf(sequence2) );
                    intent2.putExtra("sequence3",String.valueOf(sequence3 ));
                    intent2 .putExtra("wordnum",String.valueOf(wordnum ));
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
        setContentView(R.layout.activity_translate);

        checkIfCPUx86();

        phonenumber= getIntent().getStringExtra("phonenumber");
        sequence1= Integer.parseInt(getIntent().getStringExtra("sequence1"));
        sequence2= Integer.parseInt(getIntent().getStringExtra("sequence2"));
        sequence3= Integer.parseInt(getIntent().getStringExtra("sequence3"));
        wordnum= Integer.parseInt(getIntent().getStringExtra("wordnum"));
        hintnum= Integer.parseInt(getIntent().getStringExtra("hintnum"));

        //如果targetSdkVersion设置为>=23的值，则需要申请权限
        if (!isPermissionGranted(this, WRITE_EXTERNAL_STORAGE)) {
            String[] perssions = {WRITE_EXTERNAL_STORAGE, ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION};
            ActivityCompat.requestPermissions(this, perssions, UNUSED_REQUEST_CODE);
        }
        if (!isPermissionGranted(this, RECORD_AUDIO)) {
            String[] perssions = {RECORD_AUDIO};
            ActivityCompat.requestPermissions(this, perssions, UNUSED_REQUEST_CODE);
        }

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        FragmentManager fm= getSupportFragmentManager();

        FragmentTransaction ft= fm.beginTransaction();
        ft.add(R.id.frame_layout1,translateFragment,"");

        ft.commit();

        imm = (InputMethodManager) this.getSystemService(INPUT_METHOD_SERVICE);


    }



    public void TranslateTo(View view) {

        // 获取辅助碎片管理器
        FragmentManager fm= getSupportFragmentManager();

        FragmentTransaction ft= fm.beginTransaction();

        switch(view.getId()){
            case R.id.CamPic:
            case R.id.CamTrans:
                ft.replace(R.id.frame_layout1,ocrFragment,"");
                break;
            case R.id.RecordPic:
            case R.id.RecordTrans:
                ft.replace(R.id.frame_layout1,srFragment,"");
                break;
            case R.id.back_button:
                ft.replace(R.id.frame_layout1,translateFragment,"");
                break;
            case  R.id.fanyiBtn:
                query();
                break;

        }

        // 提交事务
        ft.commit();

    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public static boolean checkIfCPUx86() {
        //1. Check CPU architecture: arm or x86
        if (getSystemProperty("ro.product.cpu.abi", "arm").contains("x86")) {
            //The CPU is x86
            return true;
        } else {
            return false;
        }
    }

    private static String getSystemProperty(String key, String defaultValue) {
        String value = defaultValue;
        try {
            Class<?> clazz = Class.forName("android.os.SystemProperties");
            Method get = clazz.getMethod("get", String.class, String.class);
            value = (String) (get.invoke(clazz, key, ""));
            Log.i("背呗", "CPU类型--------------->" + value);
        } catch (Exception e) {
        }

        return value;
    }

    OcrTranslateListener translateListener = new OcrTranslateListener() {

        @Override
        public void onError(final TranslateErrorCode error, String requestId) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    original = (TextView) ocrFragment.getView().findViewById(R.id.original);
                    original.setText("翻译失败:" + error.toString());
                }
            });
        }

        @Override
        public void onResult(final OCRTranslateResult result, String input, String requestId) {
            handler.post(new Runnable() {
                @Override
                public void run() {

                    original = (TextView) ocrFragment.getView().findViewById(R.id.original);
                    borderTextView = (BorderTextView) ocrFragment.getView().findViewById(R.id.resultText);
                    original.setText("翻译结果:");
                    String text = getResult(result);
                    SpannableString spannableString = new SpannableString(text);
                    int start = 0;
                    while (start < text.length() && start >= 0) {
                        int s = text.indexOf("段落", start);
                        int end = text.indexOf("：", s) + 1;
                        if (s >= 0) {
                            ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#808080"));
                            AbsoluteSizeSpan sizeSpan = new AbsoluteSizeSpan(35);
                            UnderlineSpan underlineSpan = new UnderlineSpan();
                            spannableString.setSpan(sizeSpan, s, end, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                            spannableString.setSpan(colorSpan, s, end, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                            spannableString.setSpan(underlineSpan, s, end - 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                            start = end;
                        } else {
                            break;
                        }
                    }
                    borderTextView.setVisibility(View.VISIBLE);
                    borderTextView.setText(spannableString);
                }
            });
        }
    };

    public void takePhoto(View view) {
        String state = Environment.getExternalStorageState(); // 判断是否存在sd卡
        if (state.equals(Environment.MEDIA_MOUNTED)) { // 直接调用系统的照相机
            Intent intent = new Intent(
                    "android.media.action.IMAGE_CAPTURE");
            filePath = getFileName();
            Uri uri = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                uri = FileProvider.getUriForFile(this, "com.examle.fileprovider", new File(filePath));
            } else {
                uri = Uri.fromFile(new File(filePath));
            }
            intent.putExtra(MediaStore.EXTRA_OUTPUT,
                    uri);
            startActivityForResult(intent, 1);
        } else {
            Toast.makeText(TranslateActivity.this, "请检查手机是否有SD卡",
                    Toast.LENGTH_LONG).show();
        }
    }

    private String getFileName() {
        String saveDir = Environment.getExternalStorageDirectory() + "/myPic";
        File dir = new File(saveDir);
        if (!dir.exists()) {
            dir.mkdir(); // 创建文件夹
        }
        // 用日期作为文件名，确保唯一性
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String fileName = saveDir + "/" + formatter.format(date) + ".png";
        return fileName;
    }

    public void getImage(View view) {
        Intent intent = new Intent();
        /* 开启Pictures画面Type设定为image */
        intent.setType("image/*");
        /* 使用Intent.ACTION_GET_CONTENT这个Action */
        intent.setAction(Intent.ACTION_GET_CONTENT);
        /* 取得相片后返回本画面 */
        startActivityForResult(intent, 2);
    }

    public void recognize(View view) {
        if (currentUri == null) {
            Toast.makeText(TranslateActivity.this, "请拍摄或选择图片", Toast.LENGTH_LONG).show();
            return;
        }
        try {
            Bitmap bitmap = ImageUtils.compressBitmap(TranslateActivity.this, currentUri);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
            byte[] datas = baos.toByteArray();
            String bases64 = EncryptHelper.getBase64(datas);
            int count = bases64.length();
            while (count > 1.4 * 1024 * 1024) {
                quality = quality - 10;
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
                datas = baos.toByteArray();
                bases64 = EncryptHelper.getBase64(datas);
            }
            startTranslate(bases64);
        } catch (Exception e) {
        }
    }

    public void startTranslate(final String base64) {
        OcrTranslateParameters ocrP = new OcrTranslateParameters.Builder().timeout(6000).from(LanguageOcrTranslate.AUTO).to(LanguageOcrTranslate.CHINESE).build(); //zh-en
        OcrTranslate.getInstance(ocrP).lookup(base64, "requestid", translateListener);
        Toast.makeText(TranslateActivity.this, "开始识别，请稍等片刻", Toast.LENGTH_LONG).show();
    }

    private String getResult(OCRTranslateResult result) {
        StringBuilder sb = new StringBuilder();
        int i = 1;
        //按文本识别
        List<Region> regions = result.getRegions();
        for (Region region : regions) {
            sb.append("段落" + i++ + "：\n");
            sb.append(region.getContext()).append("\n");
            sb.append("翻译：\n");
            sb.append(region.getTranContent()).append("\n");
            sb.append("\n");
        }
        return sb.toString();
    }

    //得到照片或音频
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        TranslateActivity activity = TranslateActivity.this;
        Fragment current = activity.getSupportFragmentManager().findFragmentById(R.id.frame_layout1);
        if (current instanceof OCRFragment){
            if (resultCode == RESULT_OK) {
                imageView = (ImageView) ocrFragment.getView().findViewById(R.id.imageView);
                Uri uri = null;
                if (data != null) {
                    uri = data.getData();
                }
                if (uri == null && !TextUtils.isEmpty(filePath)) {
                    uri = Uri.parse(filePath);
                }
                Log.e("uri", uri.toString());
                Bitmap bitmap = ImageUtils.compressBitmap(this, uri);
                if (bitmap != null) {
                    imageView.setImageBitmap(bitmap);
                    imageView.postInvalidate();
                }
                currentUri = uri;
                imageView.setVisibility(View.VISIBLE);
            }
        }
        else if (current instanceof SRFragment){
            if (resultCode == RESULT_OK) {
                Uri uri = null;
                if (data != null) {
                    uri = data.getData();
                    TextView filePathText = srFragment.getView().findViewById(R.id.filepath);
                    filePathText.setText(FileUtils.getPath(this, uri));
                }

            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public static boolean isPermissionGranted(final Context context,
                                              final String permission) {
        return ContextCompat.checkSelfPermission(context, permission) ==
                PackageManager.PERMISSION_GRANTED;
    }

    /**
     * 录音失败的提示
     */
    ExtAudioRecorder.RecorderListener listener = new ExtAudioRecorder.RecorderListener() {
        @Override
        public void recordFailed(int failRecorder) {
            if (failRecorder == 0) {
                Toast.makeText(TranslateActivity.this, "录音失败，请检查是否开启相关应用权限", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(TranslateActivity.this, "发生未知错误！", Toast.LENGTH_SHORT).show();
            }
        }
    };

    public void select(View view) {
        TextView resultText = srFragment.getView().findViewById(R.id.shibietext);
        resultText.setText("");
        Intent intent = new Intent();
        intent.setType("audio/*"); //选择音频
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    public void record(View view) {
        try {
            TextView resultText = srFragment.getView().findViewById(R.id.shibietext);
            resultText.setText("");
            audioFile = File.createTempFile("record_", ".wav");
            AuditRecorderConfiguration configuration = new AuditRecorderConfiguration.Builder()
                    .recorderListener(listener)
                    .handler(handler2)
                    .rate(Constants.RATE_16000)
                    .uncompressed(true)
                    .builder();

            recorder = new ExtAudioRecorder(configuration);
            recorder.setOutputFile(audioFile.getAbsolutePath());
            recorder.prepare();
            recorder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void recordstop(View view) {
        try {
            int time = recorder.stop();
            if (time > 0) {
                if (audioFile != null) {
                    TextView filePathText = srFragment.getView().findViewById(R.id.filepath);
                    filePathText.setText(audioFile.getAbsolutePath());
                }
            }
            recorder.release();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void recognize2(View view) {
        TextView filePathText = srFragment.getView().findViewById(R.id.filepath);
        final String text = (String) filePathText.getText();
        if (TextUtils.isEmpty(text)) {
            Toast.makeText(TranslateActivity.this, "请录音或选择音频文件", Toast.LENGTH_LONG)
                    .show();
            return;
        }
        try {
            TextView resultText = srFragment.getView().findViewById(R.id.shibietext);
            resultText.setText("开始识别，请稍等片刻");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    startRecognize(text);
                }
            }).start();
        } catch (Exception e) {
        }
    }

    private void startRecognize(String filePath) {
        byte[] datas = null;
        try {
            datas = FileUtils.getContent(filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        final String bases64 = EncryptHelper.getBase64(datas);

        //输入和输出音频格式都为wav格式
        tps = new SpeechTranslateParameters.Builder().source("youdaovoicetranslate")
                .from(Language.ENCH).to(Language.ENGLISH)
                .rate(Constants.RATE_16000)//输入音频码率，支持8000,16000
                .voice(Constants.VOICE_NEW)
                .timeout(100000)//超时时间
                .build();

        SpeechTranslate.getInstance(tps).lookup(bases64,"requestId",
                new TranslateListener() {
                    @Override
                    public void onResult(final Translate result, String input, String requestId) {
                        handler2.post(new Runnable() {
                            @Override
                            public void run() {
                                TextView resultText = srFragment.getView().findViewById(R.id.shibietext);
                                resultText.setText("翻译结果:\n" + result.getQuery());
                                tr = result;
                            }
                        });
                    }

                    @Override
                    public void onError(final TranslateErrorCode error, String requestId) {
                        handler2.post(new Runnable() {
                            @Override
                            public void run() {
                                tr = null;
                                TextView resultText = srFragment.getView().findViewById(R.id.shibietext);

                                resultText.setText("翻译失败:" + error.toString());
                            }
                        });
                    }

                    @Override
                    public void onResult(List<Translate> results, List<String> inputs, List<TranslateErrorCode> errors, String requestId) {

                    }
                });


    }


    public void Clear(View view) {
        EditText fanyiInputText = translateFragment.getView().findViewById(R.id.fanyiInputText);
        fanyiInputText.setText("");

    }

    // 单词/句子翻译

    private void selectLanguage(final TextView languageSelect) {
        final String str[] = LanguageUtils.langs;
        List<String> items = new ArrayList<String>();
        for (String s : str) {
            items.add(s);
        }

        SwListDialog exitDialog = new SwListDialog(TranslateActivity.this,
                items);
        exitDialog.setItemListener(new SwListDialog.ItemListener() {

            @Override
            public void click(int position, String title) {

                String language = languageSelect.getText().toString();
                languageSelect.setText(title);
                String from = languageSelectFrom.getText().toString();
                String to = languageSelectTo.getText().toString();

                String lan = "中文";
            }
        });
        exitDialog.show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        TranslateActivity activity = TranslateActivity.this;
        Fragment current = activity.getSupportFragmentManager().findFragmentById(R.id.frame_layout1);
        if (current instanceof TranslateFragment) {
            fanyiInputText = translateFragment.getView().findViewById(R.id.fanyiInputText);

            translateList = translateFragment.getView().findViewById(R.id.commentList);

            adapter = new TranslateAdapter(this, list, trslist);
            translateList.setAdapter(adapter);

            languageSelectFrom = translateFragment.getView().findViewById(R.id.languageSelectFrom);
            languageSelectFrom.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectLanguage(languageSelectFrom);
                }
            });
            languageSelectTo = translateFragment.getView().findViewById(R.id.languageSelectTo);
            languageSelectTo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectLanguage(languageSelectTo);
                }
            });
        }
    }

    public void query() {

        showLoadingView("正在查询");

        // 源语言或者目标语言其中之一必须为中文,目前只支持中文与其他几个语种的互译
        String from = languageSelectFrom.getText().toString();
        String to = languageSelectTo.getText().toString();
        final String input = fanyiInputText.getText().toString();

        Language langFrom = LanguageUtils.getLangByName(from);
        // 若设置为自动，则查询自动识别源语言，自动识别不能保证完全正确，最好传源语言类型
        // Language langFrosm = LanguageUtils.getLangByName("自动");

        Language langTo = LanguageUtils.getLangByName(to);

        TranslateParameters tps = new TranslateParameters.Builder()
                .source("youdao").from(langFrom).to(langTo).sound(Constants.SOUND_OUTPUT_MP3).voice(Constants.VOICE_BOY_UK).timeout(3000).build();// appkey可以省
        final long start = System.currentTimeMillis();

        translator = Translator.getInstance(tps);
        translator.lookup(input, "requestId", new TranslateListener() {
            @Override
            public void onResult(final Translate result, String input, String requestId) {
                handler3.post(new Runnable() {
                    @Override
                    public void run() {
                        TranslateData td = new TranslateData(
                                System.currentTimeMillis(), result);

                        long end = System.currentTimeMillis();
                        long time = end-start;

                        list.add(td);
                        trslist.add(result);
                        adapter.notifyDataSetChanged();
                        translateList.setSelection(list.size() - 1);
                        dismissLoadingView();
                        imm.hideSoftInputFromWindow(
                                fanyiInputText.getWindowToken(), 0);

                        Translate2Activity.open(TranslateActivity.this, td, result);

                    }
                });
            }

            @Override
            public void onError(final TranslateErrorCode error, String requestId) {
                handler3.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(TranslateActivity.this, "查询错误:" + error.name(), Toast.LENGTH_LONG)
                                .show();
                        dismissLoadingView();
                    }
                });
            }
            @Override
            public void onResult(List<Translate> results, List<String> inputs, List<TranslateErrorCode> errors, String requestId) {

            }
        });
    }

    private void showLoadingView(final String text) {
        waitHandler.post(new Runnable() {

            @Override
            public void run() {
                if (progressDialog != null && !progressDialog.isShowing()) {
                    progressDialog.setMessage(text);
                    progressDialog.show();
                }
            }
        });

    }

    private void dismissLoadingView() {
        waitHandler.post(new Runnable() {

            @Override
            public void run() {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
            }
        });

    }
}
