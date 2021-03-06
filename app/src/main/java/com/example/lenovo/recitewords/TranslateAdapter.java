package com.example.lenovo.recitewords;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.youdao.sdk.ydtranslate.Translate;

public class TranslateAdapter extends BaseAdapter {

    private List<TranslateData> list;
    private List<Translate> trslist;

    private LayoutInflater inflater;

    private Context context;

    public TranslateAdapter(Context context, List<TranslateData> list, List<Translate> trs) {
        this.list = list;
        this.trslist = trs;
        inflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public int getItemViewType(int position) {
        return 1;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final TranslateData bean = list.get(position);
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.translate_item, parent,
                    false);
            holder.commentItemContent = (TextView) convertView
                    .findViewById(R.id.commentItemContent);
            holder.moreBtn = (ImageView) convertView.findViewById(R.id.more);

            holder.wordBtn = convertView.findViewById(R.id.wordBtn);

            holder.translateText = (TextView) convertView
                    .findViewById(R.id.translateText);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        String timeText = "";
        long time = bean.getCreateTime();
        Date d = new Date(time);
        Date today = new Date();
        if (TimeUtil.isTheDay(d, today)) {
            long interval = today.getTime() - time;
            if (interval / 3600000 > 0) {
                timeText = interval / 3600000 + "小时前";
            } else {
                timeText = "刚刚";
            }
        } else if (TimeUtil.isTheDay(time + 86400000, today)) {
            timeText = "昨天";
        } else {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd",
                    Locale.ENGLISH);
            timeText = format.format(d);
        }

        if (TextUtils.isEmpty(bean.getTranslate().getQuery())) {
            holder.commentItemContent.setVisibility(View.GONE);
        } else {
            holder.commentItemContent.setVisibility(View.VISIBLE);
            holder.commentItemContent.setText(bean.getQuery());
        }

        if (convertView != null) {
            convertView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    Translate tr = trslist.get(position);
                    Translate2Activity.open((Activity)context, bean, tr);
                }
            });
        }

        try {
            if (!TextUtils.isEmpty(bean.translates()) || !TextUtils.isEmpty(bean.means())) {
                String text = TextUtils.isEmpty(bean.translates()) ? bean.means() : bean.translates();
                holder.translateText.setText(text);
                holder.translateText.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {

        }

        return convertView;
    }

    public synchronized void playVoice(String speakUrl) {

    }

    private final class ViewHolder {

        public TextView translateText;

        public TextView commentItemContent;


        public ImageView moreBtn;

        public View wordBtn;
    }
}
