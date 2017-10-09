package com.jeman.myapp;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class ListCheckDialog extends Dialog {

    private Context context;
    private CheckBox buschkbox;
    private CheckBox cctvchkbox;
    private Spinner locspinner;
    private Spinner typespinner;
    private View.OnClickListener mLeftClickListener;
    private View.OnClickListener mRightClickListener;
    private Button mLeftButton;
    private Button mRightButton;
    ArrayAdapter locadapter;
    ArrayAdapter typeadapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 다이얼로그 외부 화면 흐리게 표현
        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.8f;
        getWindow().setAttributes(lpWindow);

        setContentView(R.layout.listcheck_dialog);

        buschkbox = (CheckBox)findViewById(R.id.listcheck_buschk);
        cctvchkbox = (CheckBox)findViewById(R.id.listcheck_cctvchk);

        typespinner = (Spinner)findViewById(R.id.listcheck_typespin);
        locspinner = (Spinner)findViewById(R.id.listcheck_locspinn);

        typeadapter = ArrayAdapter.createFromResource(context,R.array.type,android.R.layout.simple_spinner_item);
        locadapter = ArrayAdapter.createFromResource(context,R.array.gu,android.R.layout.simple_spinner_item);

        typeadapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        locadapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

        typespinner.setAdapter(typeadapter);
        locspinner.setAdapter(locadapter);

        mLeftButton = (Button) findViewById(R.id.listcheck_btn_left);
        mRightButton = (Button) findViewById(R.id.listcheck_btn_right);

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

    public boolean getCCTVchk(){
        return cctvchkbox.isChecked();
    }
    public boolean getBuschk(){
        return buschkbox.isChecked();
    }
    public String getLoc(){
        return locspinner.getSelectedItem().toString();
    }
    public String getType(){
        return typespinner.getSelectedItem().toString();
    }
    // 클릭버튼이 확인과 취소 두개일때 생성자 함수로 이벤트를 받는다
    public ListCheckDialog(Context context, View.OnClickListener leftListener,
                           View.OnClickListener rightListener) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        this.context = context;
        this.mLeftClickListener = leftListener;
        this.mRightClickListener = rightListener;
    }
}
