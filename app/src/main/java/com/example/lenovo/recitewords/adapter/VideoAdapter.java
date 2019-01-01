package com.example.lenovo.recitewords.adapter;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.lenovo.recitewords.R;
import com.example.lenovo.recitewords.activity.MovieActivity;
import bmob.Videos;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder> {
    private List<String> myList;
    private int rowLayout;
    private Context mContext;


    public VideoAdapter(List<String> myList, int rowLayout, Context context) {
        this.myList = myList;
        this.rowLayout = rowLayout;
        this.mContext = context;
        final Intent intent = new Intent(mContext, MovieActivity.class);

        final String entry = myList.get(0);  //查找Person表里面id为6b6c11c537的数据

        BmobQuery<Videos> query=new BmobQuery<Videos>();
        query.findObjects(new FindListener<Videos>() {
            @Override
            public void done(List<Videos> list, BmobException e) {
                if (e == null) {
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).getName().equals(entry)){
                            intent.putExtra("uri_video",list.get(i).getVideo());
                            intent.putExtra("uri_introduction",list.get(i).getinfo());
                            mContext.startActivity(intent);
                            break;
                        }
                    }
                }
                else{
                    Toast.makeText(mContext, "查找视频失败", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(rowLayout, viewGroup, false);
        return new ViewHolder(v);  //this is the major change here.
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
       final String entry = myList.get(i);
        viewHolder.myName.setText(entry);
        viewHolder.myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // viewHolder.myVideo.start();
                Intent intent = new Intent(mContext, MovieActivity.class);
                intent.putExtra("uri",entry);
                mContext.startActivity(intent);

            }
        });
        //mContext.getDrawable(country.getImageResourceId(mContext))
        //设置视频控制器
        //viewHolder.myVideo.setMediaController(new MediaController(mContext));

        //设置视频路径
        //viewHolder.myVideo.setVideoURI(uri2);

        //播放完成回调
        //viewHolder.myVideo.setOnCompletionListener( new MyPlayerOnCompletionListener());
    }

    @Override
    public int getItemCount() {
        return myList == null ? 0 : myList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView myName;
        public VideoView myVideo;
        public Button myButton; // 对应播放的按钮
        public ViewHolder(View itemView) {
            super(itemView);
           // myName =  itemView.findViewById(R.id.textView);
            myVideo = itemView.findViewById(R.id.main_video);
            //myButton = itemView.findViewById(R.id.imageButton);
        }
    }

    class MyPlayerOnCompletionListener implements MediaPlayer.OnCompletionListener {
        @Override
        public void onCompletion(MediaPlayer mp) {
            Toast.makeText(mContext, "播放完成了", Toast.LENGTH_SHORT).show();
        }
    }
}
