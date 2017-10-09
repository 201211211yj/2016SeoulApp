package com.jeman.myapp.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.jeman.myapp.ListsActivity;
import com.jeman.myapp.LoginActivity;
import com.jeman.myapp.MapsActivity;
import com.jeman.myapp.MySchoolActivity;
import com.jeman.myapp.R;
import com.jeman.myapp.userInfo;

public class Home extends Fragment implements View.OnClickListener {
    Bundle bundle;
    userInfo userinfo;

    public static Home newInstance(userInfo info) {
        Bundle args = new Bundle();
        args.putSerializable("HOME_PARAM",info);
        Home fragment = new Home();
        fragment.setArguments(args);
        return fragment;
    }
    public Home(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userinfo = (userInfo) getArguments().getSerializable("HOME_PARAM");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {

        Log.v("Home userinfo",userinfo.getName());
        View rootView = inflater.inflate(R.layout.home_activity, container, false);

        Button button1 = (Button) rootView.findViewById(R.id.btn1);
        Button button2 = (Button) rootView.findViewById(R.id.btn2);
        Button button3 = (Button) rootView.findViewById(R.id.btn3);
        Button button4 = (Button) rootView.findViewById(R.id.btn4);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn1:
                if(userinfo.getDate().equals("guest")) {
                    Toast.makeText(getActivity(), "로그인을 해 주세요.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                    break;
                }
                //2016-09-21
                else if (userinfo.getKinderid() == "NULL" || userinfo.isReady_register()) {
                    Toast.makeText(getActivity(), "어린이집 등록을 하지 않았거나 \n등록 대기중인 계정입니다.", Toast.LENGTH_SHORT).show();
                    break;
                }
                else {
                    Intent myschoolintent = new Intent(getActivity(), MySchoolActivity.class);
                    myschoolintent.putExtra("MySchool_userinfo", userinfo);
                    startActivity(myschoolintent);
                    break;
                }
            case R.id.btn2:
                Intent mapintent = new Intent(getActivity(), MapsActivity.class);
                mapintent.putExtra("userinfo",userinfo);
                startActivity(mapintent);
                break;
            case R.id.btn3:
                Intent listintent = new Intent(getActivity(), ListsActivity.class);
                listintent.putExtra("userinfo",userinfo);
                startActivity(listintent);
                break;
            case R.id.btn4:
                Toast.makeText(getActivity(), "서울특별시 보육포털서비스로 이동합니다.", Toast.LENGTH_LONG).show();
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://iseoul.seoul.go.kr/")));
                break;
        }
    }
}
