package com.jeman.myapp;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;

public class DatePickerDialog extends Dialog {

    private Button mLeftButton;
    private Button mRightButton;
    private DatePicker datePicker;
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

        setContentView(R.layout.datepicker_dialog);

        datePicker = (DatePicker) findViewById(R.id.datedialog_datepicker);

        mLeftButton = (Button) findViewById(R.id.datedialog_btn_left);
        mRightButton = (Button) findViewById(R.id.datedialog_btn_right);

        // 제목과 내용을 생성자에서 셋팅한다.

        // 클릭 이벤트 셋팅
        if (mLeftClickListener != null && mRightClickListener != null) {
            mLeftButton.setOnClickListener(mLeftClickListener);
            mRightButton.setOnClickListener(mRightClickListener);
        } else if (mLeftClickListener != null
                && mRightClickListener == null) {
            mLeftButton.setOnClickListener(mLeftClickListener);
        } else {

        }
    }

    // 클릭버튼이 하나일때 생성자 함수로 클릭이벤트를 받는다.
    public DatePickerDialog(Context context,
                            View.OnClickListener singleListener) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        this.mLeftClickListener = singleListener;
    }

    // 클릭버튼이 확인과 취소 두개일때 생성자 함수로 이벤트를 받는다
    public DatePickerDialog(Context context, View.OnClickListener leftListener,
                            View.OnClickListener rightListener) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        this.mLeftClickListener = leftListener;
        this.mRightClickListener = rightListener;
    }

    public String getDate(){
        String date = this.datePicker.getYear() + "-" + (this.datePicker.getMonth()+1) + "-" + this.datePicker.getDayOfMonth();
        return date;
    }
}

