//package shape;
//
//import android.content.Context;
//import android.content.res.TypedArray;
//import android.text.TextUtils;
//import android.util.AttributeSet;
//import android.view.LayoutInflater;
//import android.widget.ImageView;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.example.lenovo.recitewords.R;
//
//public class PageBack extends RelativeLayout {
//
//    private TextView t0;
//    private ImageView IV1;
//
//    private String boshuText;
//    private Integer pic1;
//
//    public PageBack(Context context,AttributeSet attrs) {
//        super(context,attrs);
//
//        //加载视图布局
//        LayoutInflater.from(context).inflate(R.layout.pageback,this,true);
//
//        //加载自定义的属性
//        TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.PageBack);
//        boshuText=a.getString(R.styleable.PageBack_text9);
//        pic1=a.getResourceId(R.styleable.PageBack_picture9,R.drawable.fanhui);
//
//        //回收资源
//        a.recycle();
//    }
//
//    //此方法会在所有控件都从xml文件中加载完成后调用，实现“获取子控件对象"功能
//    protected void onFinishInflate(){
//        super.onFinishInflate();
//
//        //获取子控件
//        t0 = (TextView)findViewById(R.id.textview00);
//        IV1=(ImageView)findViewById(R.id.back_image);
//
//        //将从资源文件中加载的属性设置给子控件
//        if(!TextUtils.isEmpty(boshuText))
//        {
//            setPageBoshuText(boshuText);
//        }
//        setPageImage1(pic1);
//    }
//    public void setPageBoshuText(String text){
//        t0.setText(text);
//    }
//    public void setPageImage1(int resId){
//        IV1.setBackgroundResource(resId);
//    }
//}
