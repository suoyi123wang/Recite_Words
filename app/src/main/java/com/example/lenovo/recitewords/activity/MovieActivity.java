package com.example.lenovo.recitewords.activity;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.example.lenovo.recitewords.R;
import com.example.lenovo.recitewords.activity.DisplayUtils;

import java.util.HashMap;

public class MovieActivity extends AppCompatActivity {
    public static final int UPDATA_VIDEO_NUM = 1;
    private VideoView videoView;
    private MediaController controller;//控制器
    private RelativeLayout videoLayout;
    private LinearLayout controllerLayout;//播放器的总控制布局
    private SeekBar play_seek, volume_seek;//播放进度和音量控制进度
    private ImageView play_controller_image, screen_image,volume_Image,first_image;
    private TextView current_time_tv, totally_time_tv,info;
    private String path;
    private int screen_width, screen_height;
    private AudioManager audioManager;//音量控制器
    private boolean screen_flag = true;//判断屏幕转向


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //实例化音量控制器
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        setContentView(R.layout.layout_videocard);

        initView();
        initData();
        initViewOnClick();
//        controller = new MediaController(this);//实例化控制器
//        path = Environment.getExternalStorageDirectory().getAbsolutePath()+"jiaoxue.mp4";
        /**
         * 本地播放
         */
//        videoView.setVideoPath("");
        /**
         * 网络播放
         */
        String uri=getIntent().getStringExtra("uri_video");
        videoView.setVideoURI(Uri.parse(uri));
        //视频播放时开始刷新
//        videoView.start();
        play_controller_image.setImageResource(R.drawable.play);
//        handler.sendEmptyMessage(UPDATA_VIDEO_NUM);
        /**
         * 将控制器和播放器进行互相关联
         */
//        controller.setMediaPlayer(videoView);
//        videoView.setMediaController(controller);
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeMessages(UPDATA_VIDEO_NUM);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeMessages(UPDATA_VIDEO_NUM);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // 判断当前屏幕的横竖屏状态
        int screenOritentation = getResources().getConfiguration().orientation;
        if (screenOritentation == Configuration.ORIENTATION_LANDSCAPE) {
            //横屏时处理
            info.setVisibility(View.INVISIBLE);
            info.setTextColor(0x00000000);
            //videoView.start();

            volume_seek.setVisibility(View.VISIBLE);
            volume_Image.setVisibility(View.VISIBLE);
            screen_flag = false;
            //清除全屏标记，重新添加
            Display display= getWindow().getWindowManager().getDefaultDisplay();
            DisplayMetrics dm=new DisplayMetrics();
            display.getMetrics(dm);
            int mWidth=dm.widthPixels;
            int mHeight=(int)(dm.heightPixels*0.74);
            setVideoScreenSize(mWidth, dm.heightPixels);
            getWindow().clearFlags((WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN));
            getWindow().addFlags((WindowManager.LayoutParams.FLAG_FULLSCREEN));

            LayoutParams lp=(LayoutParams)first_image.getLayoutParams();
//将宽度设置为屏幕的1/3
            lp.height=mHeight;
            first_image.setLayoutParams(lp);

        } else {
            //竖屏时处理
            setVideoScreenSize(ViewGroup.LayoutParams.MATCH_PARENT, DisplayUtils.dp2px(this.getApplicationContext(),240));
            screen_flag = true;
            //videoView.start();
            volume_seek.setVisibility(View.GONE);
            volume_Image.setVisibility(View.GONE);
            info.setVisibility(View.VISIBLE);
            info.setTextColor(0xFF000000);
            //清除全屏标记，重新添加
            getWindow().clearFlags((WindowManager.LayoutParams.FLAG_FULLSCREEN));
            getWindow().addFlags((WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN));
            Display display= getWindow().getWindowManager().getDefaultDisplay();
            DisplayMetrics dm=new DisplayMetrics();
            display.getMetrics(dm);
            int mWidth=dm.widthPixels;
            int mHeight=videoView.getHeight();
            LayoutParams lp=(LayoutParams)first_image.getLayoutParams();
//将宽度设置为屏幕的1/3
            lp.height=490;
            first_image.setLayoutParams(lp);

        }
    }

    /**
     * 通过handler对播放进度和时间进行更新
     */
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == UPDATA_VIDEO_NUM) {
                //获取视频播放的当前时间
                int currentTime = videoView.getCurrentPosition();
                //获取视频的总时间
                int totally = videoView.getDuration();
                //格式化显示时间
                updataTimeFormat(totally_time_tv, totally);
                updataTimeFormat(current_time_tv, currentTime);
                //设置播放进度
                play_seek.setMax(totally);
                play_seek.setProgress(currentTime);
                //自己通知自己更新
                handler.sendEmptyMessageDelayed(UPDATA_VIDEO_NUM, 500);//500毫秒刷新
            }
        }
    };

    /**
     * 设置横竖屏时的视频大小
     *
     * @param width
     * @param height
     */
    private void setVideoScreenSize(int width, int height) {
        //获取视频控件的布局参数
        ViewGroup.LayoutParams videoViewLayoutParams = videoView.getLayoutParams();
        //设置视频范围
        videoViewLayoutParams.width = width;
        videoViewLayoutParams.height = height;


        videoView.setLayoutParams(videoViewLayoutParams);
        //设置视频和控制组件的layout
        ViewGroup.LayoutParams videoLayoutLayoutParams= videoLayout.getLayoutParams();
        videoLayoutLayoutParams.width = width;
        videoLayoutLayoutParams.height = height;
        videoLayout.setLayoutParams(videoLayoutLayoutParams);
    }

    /**
     * 时间格式化
     *
     * @param textView    时间控件
     * @param millisecond 总时间 毫秒
     */
    private void updataTimeFormat(TextView textView, int millisecond) {
        //将毫秒转换为秒
        int second = millisecond / 1000;
        //计算小时
        int hh = second / 3600;
        //计算分钟
        int mm = second % 3600 / 60;
        //计算秒
        int ss = second % 60;
        //判断时间单位的位数
        String str = null;
        if (hh != 0) {//表示时间单位为三位
            str = String.format("%02d:%02d:%02d", hh, mm, ss);
        } else {
            str = String.format("%02d:%02d", mm, ss);
        }
        //将时间赋值给控件
        textView.setText(str);
    }

    /**
     * 按钮点击事件
     */
    private void initViewOnClick() {
        //播放按钮事件
        play_controller_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断播放按钮的状态
                if (videoView.isPlaying()) {

                    videoView.setVisibility(View.VISIBLE);
                    first_image.setVisibility(View.GONE);
                    play_controller_image.setImageResource(R.drawable.play);
                    //视频暂停
                    videoView.pause();
                    //当视频处于暂停状态，停止handler的刷新
                    handler.removeMessages(UPDATA_VIDEO_NUM);


                } else {
                    play_controller_image.setImageResource(R.drawable.pause);
                    videoView.start();


                    first_image.setVisibility(View.GONE);
                    videoView.setVisibility(View.VISIBLE);


                    //当视频播放时，通知刷新
                    handler.sendEmptyMessage(UPDATA_VIDEO_NUM);
                }
            }
        });
        //播放进度条事件
        play_seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //设置当前的播放时间
                updataTimeFormat(current_time_tv, progress);
                if (videoView.getDuration() == progress) {
                    play_controller_image.setImageResource(R.drawable.play);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //拖动视频进度时，停止刷新
                handler.removeMessages(UPDATA_VIDEO_NUM);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //停止拖动后，获取总进度
                int totall = seekBar.getProgress();
                //设置VideoView的播放进度
                videoView.seekTo(totall);
                //重新handler刷新
                handler.sendEmptyMessage(UPDATA_VIDEO_NUM);

            }
        });
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
        {
            @Override
            public void onCompletion(MediaPlayer mp)
            {
                //播放结束后的动作
                first_image.setVisibility(View.VISIBLE);
                videoView.setVisibility(View.GONE);
                play_controller_image.setImageResource(R.drawable.play);


            }
        });
        //音量控制条事件
        volume_seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //设置音量变动后系统的值
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,progress,0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //设置全屏按钮点击事件
        screen_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(screen_flag){
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//控制屏幕竖屏
                    screen_flag=false;

                }else {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//控制屏幕横屏
                    screen_flag=true;
                }
            }
        });
    }


    protected void initView() {
        videoView = (VideoView) findViewById(R.id.main_video);
        videoLayout = (RelativeLayout) findViewById(R.id.act_testmovie_videolayout);
        controllerLayout = (LinearLayout) findViewById(R.id.main_controller_liner);
        play_seek = (SeekBar) findViewById(R.id.main_play_seek);
        volume_seek = (SeekBar) findViewById(R.id.main_volume_seek);
        current_time_tv = (TextView) findViewById(R.id.main_current_time);
        totally_time_tv = (TextView) findViewById(R.id.main_totally_time);
        play_controller_image = (ImageView) findViewById(R.id.play_pasue_image);
        screen_image = (ImageView) findViewById(R.id.main_screen_image);
        volume_Image = (ImageView) findViewById(R.id.act_testmovies_volume_image);
        first_image = (ImageView) findViewById(R.id.imageView10);
        info = (TextView) findViewById(R.id.textView9);

        DisplayMetrics metric = new DisplayMetrics();
        screen_width = metric.widthPixels;
        screen_height = metric.heightPixels;
        String uri=getIntent().getStringExtra("uri_video");
        // String uri1=getIntent().getStringExtra("uri_first");
        videoView.setVisibility(View.GONE);
        first_image.setVisibility(View.VISIBLE);
        //first_image.bringToFront();
        info.setText(getIntent().getStringExtra("uri_introduction"));


        /**
         * MediaMetadataRetriever class provides a unified interface for retrieving
         * frame and meta data from an input media file.
         */
        //通过getVideoThumbnail方法取得视频中的第一帧图片，该图片是一个bitmap对象
        Bitmap bitmap=getVideoThumbnail(uri);
//将bitmap对象转换成drawable对象
        Drawable drawable=new BitmapDrawable(bitmap);
//将drawable对象设置给视频播放窗口surfaceView控件作为背景图片
        first_image.setImageBitmap(bitmap);//.setBackgroundDrawable(drawable);

        //first_image.setImageURI(Uri.parse(uri1));

        //first_image.setVisibility(View.VISIBLE);
    }

    public Bitmap getVideoThumbnail(String url) {
        Bitmap bitmap = null;
        Bitmap bitmap1=null;
//MediaMetadataRetriever 是android中定义好的一个类，提供了统一
//的接口，用于从输入的媒体文件中取得帧和元数据；
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            //（）根据文件路径获取缩略图
//retriever.setDataSource(filePath);
            retriever.setDataSource(url, new HashMap());
            //获得第一帧图片
            bitmap = retriever.getFrameAtTime();

            bitmap1 =zoomBitmap(bitmap, videoView.getWidth(),videoView.getHeight());       //filePath:文件路径

        }
        catch(IllegalArgumentException e) {
            e.printStackTrace();
        }
        catch (RuntimeException e) {
            e.printStackTrace();
        }
        finally {
            try {
                retriever.release();
            }
            catch (RuntimeException e) {
                e.printStackTrace();
            }
        }

        return bitmap;
    }
    public Bitmap setImgSize(Bitmap bm, int newWidth ,int newHeight){
        // 获得图片的宽高.
        int width = bm.getWidth();
        int height = bm.getHeight();
        // 计算缩放比例.
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 取得想要缩放的matrix参数.
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth,scaleHeight);
        // 得到新的图片.
        Bitmap newbm;
        synchronized(Bitmap.class) {
            newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        }
        return newbm;
    }

    protected void initData() {
        //获取设置音量的最大值
        int volumeMax = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        volume_seek.setMax(volumeMax);
        //获取设置当前音量
        int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        volume_seek.setProgress(currentVolume);
    }
    public static Bitmap zoomBitmap(Bitmap bitmap, int w, int h) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float scaleWidht, scaleHeight, x, y;
        Bitmap newbmp;
        Matrix matrix = new Matrix();
        if (width > height) {
            scaleWidht = ((float) h / height);
            scaleHeight = ((float) h / height);
            x = (width - w * height / h) / 2;//获取bitmap源文件中x做表需要偏移的像数大小
            y = 0;
        } else if (width < height) {
            scaleWidht = ((float) w / width);
            scaleHeight = ((float) w / width);
            x = 0;
            y = (height - h * width / w) / 2;//获取bitmap源文件中y做表需要偏移的像数大小
        } else {
            scaleWidht = ((float) w / width);
            scaleHeight = ((float) w / width);
            x = 0;
            y = 0;
        }
        matrix.postScale(scaleWidht, scaleHeight);
        try {
            newbmp = Bitmap.createBitmap(bitmap, (int) x, (int) y, (int) (width - x), (int) (height - y), matrix,
                    true);//createBitmap()方法中定义的参数x+width要小于或等于bitmap.getWidth()，y+height要小于或等于bitmap.getHeight()
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return newbmp;
    }


}
