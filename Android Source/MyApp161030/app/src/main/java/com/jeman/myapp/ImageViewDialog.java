package com.jeman.myapp;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class ImageViewDialog extends Dialog {

    private boolean teacher_bool;
    private Bitmap bitmap;
    private Button mLeftButton;
    private Button mRightButton;
    private ImageView imageview;
    private View.OnClickListener mLeftClickListener;
    private View.OnClickListener mRightClickListener;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 다이얼로그 외부 화면 흐리게 표현
        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.8f;
        getWindow().setAttributes(lpWindow);

        setContentView(R.layout.imageview_dialog);
        imageview = (ImageView)findViewById(R.id.imgdialog_imgview);
        imageview.setImageBitmap(bitmap);

        mLeftButton = (Button) findViewById(R.id.imgdialog_btn_left);
        mRightButton = (Button) findViewById(R.id.imgdialog_btn_right);
        if(teacher_bool==false){
            mLeftButton.setVisibility(View.INVISIBLE);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            mRightButton.setLayoutParams(params);
        }
        if (mLeftClickListener != null && mRightClickListener != null) {
            mLeftButton.setOnClickListener(mLeftClickListener);
            mRightButton.setOnClickListener(mRightClickListener);
        } else if (mLeftClickListener != null
                && mRightClickListener == null) {
            mLeftButton.setOnClickListener(mLeftClickListener);
        } else {

        }
    }

    public ImageViewDialog(Context context,
                        View.OnClickListener singleListener) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        this.mLeftClickListener = singleListener;
    }

    public ImageViewDialog(Context context, Bitmap bitmap, View.OnClickListener leftListener,
                        View.OnClickListener rightListener, boolean teacher_bool) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        this.bitmap = bitmap;
        this.mLeftClickListener = leftListener;
        this.mRightClickListener = rightListener;
        this.teacher_bool = teacher_bool;
    }
}

