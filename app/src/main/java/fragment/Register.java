package fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lenovo.recitewords.MainActivity;
import com.example.lenovo.recitewords.R;

import bmob.MyUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.smssdk.EventHandler;
import shape.ClearEditText;
import shape.Password_EditText;

import cn.smssdk.SMSSDK;

public class Register extends Fragment {
    ClearEditText account;
    Password_EditText c1,c2;
    Button registerButton;
    private String password1,password2,phone0,name;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view_ = inflater.inflate(R.layout.register, container, false);
        getActivity().findViewById(R.id.navigation).setVisibility(View.GONE);
        //初始化
        account=view_.findViewById(R.id.account_name);
        c1=view_.findViewById(R.id.password_3);
        c2=view_.findViewById(R.id.password_4);
        registerButton=view_.findViewById(R.id.button_zhuce);
        phone0=((MainActivity)getActivity()).getParaPhone();

        final Context context=view_.getContext();


        //用户名不能为空
        account.addTextChangedListener(new TextWatcher(){
            Toast toast=Toast.makeText(context, "用户名长度不少于4位", Toast.LENGTH_SHORT);
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                password1=c1.getText().toString();
                password2=c2.getText().toString();
                name=account.getText().toString();
                toast.setGravity(Gravity.CENTER_VERTICAL,0,250);//起点位置,水平向右位移,垂直向下位移
                if(name.length()>4&&isRightPassword(password1)&&isRightPassword(password2)
                        &&password1.equals(password2)){
                    registerButton.setBackgroundColor(Color.parseColor("#5bb19a"));
                    registerButton.setEnabled(true);
                    toast.cancel();
                }else{
                    registerButton.setEnabled(false);
                    registerButton.setBackgroundColor(Color.parseColor("#FF504D4D"));
                    if(!isRightPassword(password1))
                        toast.show();
                    if(isRightPassword(password1))
                        toast.cancel();
                }
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            public void afterTextChanged(Editable s) {
            }
        });

        //密码
        c1.addTextChangedListener(new TextWatcher(){
            Toast toast=Toast.makeText(context, "密码必须包含字母和数字且不少于6位", Toast.LENGTH_SHORT);
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                password1=c1.getText().toString();
                password2=c2.getText().toString();
                name=account.getText().toString();
                toast.setGravity(Gravity.CENTER_VERTICAL,0,250);//起点位置,水平向右位移,垂直向下位移
                if(name.length()>4&&isRightPassword(password1)&&isRightPassword(password2)
                        &&password1.equals(password2)){
                    registerButton.setBackgroundColor(Color.parseColor("#5bb19a"));
                    registerButton.setEnabled(true);
                    toast.cancel();
                }else{
                    registerButton.setEnabled(false);
                    registerButton.setBackgroundColor(Color.parseColor("#FF504D4D"));
                    if(!isRightPassword(password1))
                        toast.show();
                    if(isRightPassword(password1))
                        toast.cancel();
                }
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            public void afterTextChanged(Editable s) {
            }
        });

        //判断两次密码是否相等
        c2.addTextChangedListener(new TextWatcher(){
            Toast toast=Toast.makeText(context, "密码必须一致", Toast.LENGTH_SHORT);
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                password1=c1.getText().toString();
                password2=c2.getText().toString();
                name=account.getText().toString();
                toast.setGravity(Gravity.CENTER_VERTICAL,0,250);//起点位置,水平向右位移,垂直向下位移
                if(name.length()>4&&isRightPassword(password1)&&isRightPassword(password2)
                        &&password1.equals(password2)){
                    registerButton.setBackgroundColor(Color.parseColor("#5bb19a"));
                    registerButton.setEnabled(true);
                    toast.cancel();
                }else{
                    registerButton.setEnabled(false);
                    registerButton.setBackgroundColor(Color.parseColor("#FF504D4D"));
                    if(!isRightPassword(password2)||password1.equals(password2))
                        toast.show();
                    if(isRightPassword(password2)&&password1.equals(password2))
                        toast.cancel();
                }
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            public void afterTextChanged(Editable s) {
            }
        });

        //确认注册监听
        registerButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0){
                password2=c2.getText().toString();
                name=account.getText().toString();
                MyUser myuser=new MyUser();
                myuser.setUsername(phone0);
                myuser.setMobilePhoneNumber(phone0);
                myuser.setAccountName(name);
                myuser.setPassword(password2);

                myuser.signUp(new SaveListener<MyUser>() {
                    @Override
                    public void done(MyUser s, BmobException e) {
                        if(e==null){
                            getFragmentManager()
                                    .beginTransaction()
                                    .addToBackStack(null)
                                    .replace(R.id.content_frame,new LogIn())
                                    .commit();
                        }else{
                            Toast.makeText(context, "注册失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        return view_;
    }
    //判断密码格式是否正确
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
}
