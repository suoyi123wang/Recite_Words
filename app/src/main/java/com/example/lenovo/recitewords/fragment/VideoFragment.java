package com.example.lenovo.recitewords.fragment;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.lenovo.recitewords.R;
import com.example.lenovo.recitewords.Util.DBOpenHelper;
import com.example.lenovo.recitewords.Util.ImageManage;
import com.example.lenovo.recitewords.Util.NetWorkUtil;
import com.example.lenovo.recitewords.adapter.VAdapter;
import com.example.lenovo.recitewords.adapter.VideoAdapter;

import java.util.ArrayList;
import java.util.List;

import bmob.Videos;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link VideoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VideoFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //users
    private View view;
    private RecyclerView view_video;
    private VAdapter vAdapter;
    private String type;
    private int symbol=0;
    private List<byte[]> values3;


    private List<String> values1;
    private List<String> values2;
    private SwipeRefreshLayout refreshLayout;

    private OnFragmentInteractionListener mListener;


    public VideoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment VideoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static VideoFragment newInstance(String param1, String param2) {
        VideoFragment fragment = new VideoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Bundle bundle = this.getArguments();
        if (bundle != null)
        {
            type = bundle.getString("type");

        }
        else{
            type = "";
        }

        view=inflater.inflate(R.layout.fragment_video,null);
        view_video = view.findViewById(R.id.view_video);
        view_video.setLayoutManager(new LinearLayoutManager(getContext()));
        view_video.setItemAnimator(new DefaultItemAnimator());

        values1 = new ArrayList<>();
        values2 = new ArrayList<>();
        values3 = new ArrayList<>();

        refreshLayout = view.findViewById(R.id.refresh_search);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
                refreshLayout.setRefreshing(false);
            }
        });

        refresh();

        return view;
    }

    private void refresh(){
        final ImageManage imageManage = new ImageManage();
        DBOpenHelper helper = new DBOpenHelper(getContext(), "database1.db", null, 1);
        final SQLiteDatabase db = helper.getWritableDatabase();

        System.out.println("refresh");
        values1.clear();
        values2.clear();
        NetWorkUtil netWorkUtil = new NetWorkUtil();
        boolean judge = netWorkUtil.isNetworkAvailable(getContext());
        if (judge) {
            BmobQuery<Videos> query = new BmobQuery<Videos>();
            query.addWhereEqualTo("videotype", type);   //条件查询
            query.findObjects(new FindListener<Videos>() {
                @Override
                public void done(List<Videos> list, BmobException e) {
                    if (e == null) {
                        for (int i = 0; i < list.size(); i++) {
                            values1.add(list.get(i).getName());
                            values2.add(list.get(i).getImage());

                            /* 加参数 */
                            Cursor c = db.query("video", new String[]{"videoname"}, "videoname=?", new String[]{list.get(i).getName()}, null, null, null);

                            if (c.getCount() <1) {
                                //insert data
                                ContentValues values = new ContentValues();
                                values.put("videoname", list.get(i).getName());
                                values.put("videotype", list.get(i).getType());
                                values.put("videoimage", imageManage.bitmabToBytes(list.get(i).getImage()));//图片转为二进制
                                long rowid = db.insert("video", null, values);
                            }
                            c.close();
                        }
                        vAdapter = new VAdapter(type, values1, values2,values3, R.layout.video_card, getContext(),symbol);
                        view_video.setAdapter(vAdapter);
                        db.close();


                    }
                }
            });
        }
        else{
            symbol = 1;
            int i = 0;
            Cursor c = null;
            while (true) {

                c = db.query("video", new String[]{"videoname", "videoimage"}, "videotype=?", new String[]{type}, null, null, null, String.valueOf(i) + ",1");
                i++;
                if (c.moveToNext()) {
                    String videoname = c.getString(c.getColumnIndex("videoname"));

                    values1.add(videoname);
                    byte[] imgData = null;
                    //将Blob数据转化为字节数组
                    imgData = c.getBlob(c.getColumnIndex("videoimage"));
                    values3.add(imgData);
                } else {
                    break;
                }
                c.close();

                if (c != null) {
                    c.close();
                }
            }
            db.close();
            vAdapter = new VAdapter(type, values1, values2,values3, R.layout.video_card, getContext(),symbol);
            view_video.setAdapter(vAdapter);
        }
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
