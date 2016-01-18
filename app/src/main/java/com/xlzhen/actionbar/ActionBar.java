package com.xlzhen.actionbar;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by xlzhen on 1/18 0018.
 * 自制ActionBar 兼容老系统
 */
public class ActionBar extends LinearLayout {
    private Context mContext;//上下文
    private LinearLayout linearLayout;//中间按钮部分的布局
    private ImageView leftButton;//title左边的按钮
    private View noticeView;//顶部透明栏
    private TextView titleView;//title,可从外部设置其content，默认显示APP名称
    private int leftPadding;//中间部分按钮图标的padding，用以规整外观

    public ActionBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext=context;
        int linePadding=dp2px(2);
        setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp2px(56) + getStatusHeight() + 5));
        setOrientation(VERTICAL);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = ((Activity)context).getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);

            noticeView=new View(mContext);
            noticeView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getStatusHeight()));

            addView(noticeView);
        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            ((Activity)context).getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            noticeView=new View(mContext);
            noticeView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getStatusHeight()));

            addView(noticeView);
        }

        linearLayout=new LinearLayout(mContext);
        linearLayout.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp2px(56)));
        linearLayout.setPadding(linePadding, linePadding, linePadding, linePadding);
        linearLayout.setOrientation(HORIZONTAL);
        addView(linearLayout);

        leftButton=new ImageView(context);
        leftButton.setClickable(true);
        leftButton.setLayoutParams(new LayoutParams(dp2px(56), dp2px(56)));
        leftPadding=dp2px(14);
        leftButton.setPadding(leftPadding, leftPadding, leftPadding, leftPadding);
        linearLayout.addView(leftButton);

        titleView=new TextView(mContext);
        LayoutParams lp= new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        lp.weight=1;
        titleView.setLayoutParams(lp);
        titleView.setGravity(Gravity.CENTER_VERTICAL);
        titleView.setTextColor(Color.WHITE);
        titleView.setTextSize(22);
        titleView.setText(getApplicationName());
        linearLayout.addView(titleView);

        View gradientView=new View(mContext);
        gradientView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,5));
        gradientView.setBackgroundResource(R.mipmap.actionbar_bottom_gradient);
        addView(gradientView);
    }
    /**
    * @param resImg 图标
    * @param listener 按钮响应事件
    */
    public void setLeftButton(int resImg,OnClickListener listener){
        leftButton.setImageResource(resImg);
        leftButton.setOnClickListener(listener);
    }
    /**
     * @param color actionbar的背景色
     * @param resImg 图标
     * @param listener 按钮响应事件
     */
    public void setLinearLayout(int color, final int[] resImg, final OnItemClickListener listener){
        linearLayout.setBackgroundColor(color);
        LayoutParams lp=new LayoutParams(dp2px(56), dp2px(56));
        if(resImg!=null)
            for(int i=0;i<resImg.length;i++){
                ImageView imageView=new ImageView(mContext);
                imageView.setClickable(true);
                imageView.setPadding(leftPadding, leftPadding, leftPadding, leftPadding);
                imageView.setImageResource(resImg[i]);
                final int finalI = i;
                imageView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onItemClick(v,resImg[finalI]);
                    }
                });
                imageView.setLayoutParams(lp);
                linearLayout.addView(imageView);
            }
    }
    /**
     * @param color actionbar的背景色
     */
    public void setLinearLayout(int color){
        this.setLinearLayout(color,null,null);
    }
    /**
     * @param color 通知栏的背景色
     */
    public void setNoticeView(int color){
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT)
            return ;
        noticeView.setBackgroundColor(color);
    }
    /**
     * @param text 手动设置title的内容
     */
    public void setTitleView(CharSequence text){
        titleView.setText(text);
    }

    /*以下为接口*/

    public interface OnItemClickListener{
        void onItemClick(View view, int resImg);
    }

    /*以下为用到的工具方法，可自行提取到您的工具类内*/

    /**
     * dp转px
     */
    public int dp2px(float dpVal) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dpVal, mContext.getResources()
                        .getDisplayMetrics());
    }

    /**
     * 获取通知栏高度
     */
    public int getStatusHeight(){

        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT)
            return 0;

        Activity activity=(Activity)mContext;
        int statusHeight = 0;
        Rect localRect = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);
        statusHeight = localRect.top;
        if (0 == statusHeight){
            Class<?> localClass;
            try {
                localClass = Class.forName("com.android.internal.R$dimen");
                Object localObject = localClass.newInstance();
                int i5 = Integer.parseInt(localClass.getField("status_bar_height").get(localObject).toString());
                statusHeight = activity.getResources().getDimensionPixelSize(i5);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (NumberFormatException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
        return statusHeight;
    }
    /*
    *获取APP名称
    */
    public String getApplicationName() {
        PackageManager packageManager = null;
        ApplicationInfo applicationInfo = null;
        try {
            packageManager = mContext.getApplicationContext().getPackageManager();
            applicationInfo = packageManager.getApplicationInfo(mContext.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            applicationInfo = null;
        }
        String applicationName =
                (String) packageManager.getApplicationLabel(applicationInfo);
        return applicationName;
    }
}
