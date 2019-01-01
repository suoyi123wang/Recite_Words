//package shape;
//
//import android.content.Context;
//import android.content.res.TypedArray;
//import android.text.TextUtils;
//import android.util.AttributeSet;
//import android.view.LayoutInflater;
//import android.widget.ImageView;
//import android.widget.TableLayout;
//import android.widget.TextView;
//
//import com.example.lenovo.recitewords.R;
//
//public class ImageTextView extends TableLayout {
//
//    private TextView textview;
//    private ImageView img1;
//
//    private String boshuText;
//    private Integer pic1;
//
//    public ImageTextView(Context context, AttributeSet attrs) {
//        super(context, attrs);
//
//        //加载视图布局
//        LayoutInflater.from(context).inflate(R.layout.activity_image_text_view,this,true);
//
//        //加载自定义的属性
//        TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.ImageTextView);
//        boshuText=a.getString(R.styleable.ImageTextView_Text_);
//        pic1=a.getResourceId(R.styleable.ImageTextView_picture_,R.drawable.yujia);
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
//        textview = (TextView)findViewById(R.id.textview);
//        img1=(ImageView)findViewById(R.id.imageview);
//
//        //将从资源文件中加载的属性设置给子控件
//        if(!TextUtils.isEmpty(boshuText))
//        {
//            setPageBoshuText(boshuText);
//        }
//        setPageImage1(pic1);
//    }
//    public void setPageBoshuText(String text){
//        textview.setText(text);
//    }
//    public void setPageImage1(int resId){
//        img1.setBackgroundResource(resId);
//    }
//}
