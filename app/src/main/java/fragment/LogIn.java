package fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lenovo.recitewords.MainActivity;
import com.example.lenovo.recitewords.R;
import com.example.lenovo.recitewords.SplashActivity;
import com.example.lenovo.recitewords.TranslateActivity;
import com.example.lenovo.recitewords.WordActivity;
import java.util.Calendar;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import bmob.MyUser;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.gui.RegisterPage;
import shape.ClearEditText;
import shape.Password_EditText;

//import cn.sharesdk.onekeyshare.OnekeyShare;

public class LogIn extends Fragment {
    public static final String TEMP_CODE = "1319972";
    private ClearEditText c1;
    private Password_EditText c2;
    private String phonenumber, password;
    private Button loginButton;
    private TextView sms, reg;
    private Register register;
    EventHandler eventHandler;
    private int sequence1, sequence2, sequence3, wordnum, hintnum;
    private Date date1, date2;
    private String date3, date4;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view_ = inflater.inflate(R.layout.login, container, false);
        getActivity().findViewById(R.id.navigation).setVisibility(View.GONE);//导航栏消失

        //初始化
        c1 = view_.findViewById(R.id.phone_1);
        c2 = view_.findViewById(R.id.password_1);
        sms = view_.findViewById(R.id.sms_login);
        reg = view_.findViewById(R.id.text_reg);
        loginButton = view_.findViewById(R.id.button_1);
        register = new Register();
        final Context context = view_.getContext();
        c1.addTextChangedListener(new TextWatcher() {
            Toast toast = Toast.makeText(context, "手机号必须为11位", Toast.LENGTH_SHORT);

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                phonenumber = c1.getText().toString();
                password = c2.getText().toString();
                toast.setGravity(Gravity.CENTER_VERTICAL, 0, -53);//起点位置,水平向右位移,垂直向下位移
                if (phonenumber.length() == 11 && isRightPassword(password)) {
                    loginButton.setBackgroundColor(Color.parseColor("#5bb19a"));
                    loginButton.setEnabled(true);
                    toast.cancel();
                } else {
                    loginButton.setEnabled(false);
                    loginButton.setBackgroundColor(Color.parseColor("#FF504D4D"));
                    if (phonenumber.length() != 11)
                        toast.show();
                    if (phonenumber.length() == 11)
                        toast.cancel();
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
            }
        });
        c2.addTextChangedListener(new TextWatcher() {
            Toast toast = Toast.makeText(context, "密码必须包含字母和数字且不少于6位", Toast.LENGTH_SHORT);

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                phonenumber = c1.getText().toString();
                password = c2.getText().toString();
                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 200);//起点位置,水平向右位移,垂直向下位移
                if (phonenumber.length() == 11 && isRightPassword(password)) {
                    loginButton.setBackgroundColor(Color.parseColor("#5bb19a"));
                    loginButton.setEnabled(true);
                    toast.cancel();
                } else {
                    loginButton.setEnabled(false);
                    loginButton.setBackgroundColor(Color.parseColor("#FF504D4D"));
                    if (!isRightPassword(password))
                        toast.show();
                    if (isRightPassword(password))
                        toast.cancel();
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
            }
        });
        //用手机验证码登录
        sms.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                // 打开注册页面
                RegisterPage registerPage = new RegisterPage();
                // 使用自定义短信模板(不设置则使用默认模板)
                registerPage.setTempCode(null);
                registerPage.setRegisterCallback(new EventHandler() {
                    public void afterEvent(int event, int result, Object data) {
                        // 解析注册结果
                        if (result == SMSSDK.RESULT_COMPLETE) {
                            @SuppressWarnings("unchecked")
                            HashMap<String, Object> phoneMap = (HashMap<String, Object>) data;
                            String country = (String) phoneMap.get("country");
                            String phone = (String) phoneMap.get("phone");
                            Toast.makeText(context, "登陆成功", Toast.LENGTH_SHORT).show();
                        } else {//验证失败
                            Log.i("bmob", "验证失败");
                        }
                    }
                });
                registerPage.show(getActivity());

            }
        });
        //登陆按钮监听
        loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                phonenumber = c1.getText().toString();
                password = c2.getText().toString();


//                OnekeyShare oks = new OnekeyShare();
//                oks.setTitle("趣分享");
//                oks.setText("我为技术带盐，我骄傲，我自豪");
//                oks.setImageUrl("http://7sby7r.com1.z0.glb.clouddn.com/CYSJ_02.jpg");
//                oks.setTitleUrl("http://wwww.baidu.com");
//                oks.show(getContext());

//                Intent intent = new Intent(getContext(),TranslateActivity.class);
//                startActivity(intent);
                BmobQuery<MyUser> query = new BmobQuery<MyUser>();
                query.addWhereEqualTo("accountName", phonenumber);
                query.findObjects(new FindListener<MyUser>() {
                    @Override
                    public void done(List<MyUser> list, BmobException e) {
                        if (e == null) {
                            if (list.size() != 0) {

                                sequence1 = list.get(0).getSequence1();
                                sequence2 = list.get(0).getSequence2();
                                sequence3 = list.get(0).getSequence3();
                                wordnum = list.get(0).getwordnum();
                                hintnum = list.get(0).gethintnum();
                                date1 = list.get(0).getdate1();
                                date2 = list.get(0).getdate2();

                                Date date = new Date();


                                if (isSame(date, date1)) {
                                    date3 = String.valueOf(date1);
                                } else {
                                    date3 = "0";
                                }
                                if (isSame(date, date2)) {
                                    date4 = String.valueOf(date2);
                                } else {
                                    date4 = "0";
                                }


                            } else {

                            }
                        }
                    }
                });
                /*
                Intent intent = new Intent(getContext(), WordActivity.class);
                intent.putExtra("phonenumber", "15910866378");
                intent.putExtra("sequence1", "0");
                intent.putExtra("sequence2", "0");
                intent.putExtra("sequence3", "0");
                intent.putExtra("wordnum","0");
                intent.putExtra("hintnum", "0");

                startActivity(intent);
                */

                MyUser myUser = new MyUser();
                myUser.setUsername(phonenumber);
                myUser.setPassword(password);
                myUser.login(new SaveListener<MyUser>() {
                    @Override
                    public void done(MyUser bmobUser, BmobException e) {
                        if (e == null) {
                            Intent intent = new Intent(getContext(), WordActivity.class);
                            intent.putExtra("phonenumber", phonenumber);
                            intent.putExtra("sequence1", String.valueOf(sequence1));
                            intent.putExtra("sequence2", String.valueOf(sequence2));
                            intent.putExtra("sequence3", String.valueOf(sequence3));
                            intent.putExtra("wordnum", String.valueOf(wordnum));
                            intent.putExtra("hintnum",String.valueOf( hintnum));

                            startActivity(intent);
//                            Toast.makeText(context, "登陆成功", Toast.LENGTH_SHORT).show();
//                            getFragmentManager()
//                                    .beginTransaction()
//                                    .addToBackStack(null)
//                                    .replace(R.id.content_frame,f_allcourse)
//                                    .commit();
                        } else {
                            Toast.makeText(context, "用户名获密码错误", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


            }
        });

        //注册监听
        reg.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                // 打开注册页面
                RegisterPage registerPage = new RegisterPage();
                // 使用自定义短信模板(不设置则使用默认模板)
                registerPage.setTempCode(null);
                registerPage.setRegisterCallback(new EventHandler() {
                    public void afterEvent(int event, int result, Object data) {
                        // 解析注册结果
                        if (result == SMSSDK.RESULT_COMPLETE) {
                            @SuppressWarnings("unchecked")
                            HashMap<String, Object> phoneMap = (HashMap<String, Object>) data;
                            String country = (String) phoneMap.get("country");
                            String phone = (String) phoneMap.get("phone");
                            getFragmentManager()
                                    .beginTransaction()
                                    .addToBackStack(null)
                                    .replace(R.id.content_frame, register)
                                    .commit();
                            //Fragment 传值
                            ((MainActivity) getActivity()).setParaPhone(phone);
                        } else {//验证失败
                            Log.i("bmob", "验证失败");
                        }
                    }
                });
                registerPage.show(getActivity());
            }
        });
        return view_;
    }

    public boolean isRightPassword(String str) {
        boolean isLetterOrDigit = false;//定义一个boolean值，用来表示是否包含字母或数字
        boolean isLetter = false, isDigit = false;
        for (int i = 0; i < str.length(); i++) {
            if (Character.isLetter(str.charAt(i))) {   //用char包装类中的判断数字的方法判断每一个字符
                isLetter = true;
            }
            if (Character.isDigit(str.charAt(i)))
                isDigit = true;
        }
        if (isLetter && isDigit)
            isLetterOrDigit = true;
        boolean isRight = isLetterOrDigit && str.length() > 5;
        return isRight;
    }

    public static boolean isSame(Date date1, Date date2) {
        if (date1 != null && date2 != null) {
            Calendar cal1 = Calendar.getInstance();
            cal1.setTime(date1);
            Calendar cal2 = Calendar.getInstance();
            cal2.setTime(date2);
            return isSameDay(cal1, cal2);
        } else {
            throw new IllegalArgumentException("The date must not be null");
        }
    }

    public static boolean isSameDay(Calendar cal1, Calendar cal2) {
        if (cal1 != null && cal2 != null) {
            return cal1.YEAR == cal2.YEAR && cal1.MONTH == cal2.MONTH && cal1.DAY_OF_MONTH == cal2.DAY_OF_MONTH;
        } else {
            throw new IllegalArgumentException("The date must not be null");
        }


    }
}