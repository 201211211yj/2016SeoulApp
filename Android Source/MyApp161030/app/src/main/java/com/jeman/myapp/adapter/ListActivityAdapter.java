package com.jeman.myapp.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.jeman.myapp.R;
import com.jeman.myapp.School_Info_Activity;
import com.jeman.myapp.userInfo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;


/**
 * Created by 신영준 on 2016-09-24.
 */public class ListActivityAdapter extends BaseAdapter {

    private LayoutInflater inflater = null;
    private ArrayList<kinderinfoclass> infoList = null;
    private ViewHolder viewHolder = null;
    private Context mContext = null;
    private userInfo userinfo;

    public ListActivityAdapter(Context c , ArrayList<kinderinfoclass> arrays, userInfo userinfo){
        this.mContext = c;
        this.inflater = LayoutInflater.from(c);
        this.infoList = arrays;
        this.userinfo = userinfo;
    }

    // Adapter가 관리할 Data의 개수를 설정 합니다.
    @Override
    public int getCount() {
        return infoList.size();
    }

    // Adapter가 관리하는 Data의 Item 의 Position을 <객체> 형태로 얻어 옵니다.
    @Override
    public kinderinfoclass getItem(int position) {
        return infoList.get(position);
    }

    // Adapter가 관리하는 Data의 Item 의 position 값의 ID 를 얻어 옵니다.
    @Override
    public long getItemId(int position) {
        return position;
    }

    // ListView의 뿌려질 한줄의 Row를 설정 합니다.
    @Override
    public View getView(final int position, View convertview, ViewGroup parent) {

        View v = convertview;

        if(v == null){
            viewHolder = new ViewHolder();
            v = inflater.inflate(R.layout.list_item, null);
            viewHolder.tv_name = (TextView)v.findViewById(R.id.list_name);
            viewHolder.tv_phone = (TextView)v.findViewById(R.id.list_phone);
            viewHolder.tv_kindertype = (TextView)v.findViewById(R.id.list_type);
            viewHolder.tv_address = (TextView)v.findViewById(R.id.list_address);
            viewHolder.tv_limit = (TextView)v.findViewById(R.id.list_limit);
            viewHolder.tv_cctv = (TextView)v.findViewById(R.id.list_cctv);
            viewHolder.tv_bus = (TextView)v.findViewById(R.id.list_bus);
            viewHolder.button = (Button)v.findViewById(R.id.list_button);

            v.setTag(viewHolder);

        }else {
            viewHolder = (ViewHolder)v.getTag();
        }

        viewHolder.tv_name.setText(getItem(position).getName());
        viewHolder.tv_phone.setText(getItem(position).getPhone());
        viewHolder.tv_address.setText(getItem(position).getAddress());
        viewHolder.tv_kindertype.setText(getItem(position).getKindertype());
        viewHolder.tv_cctv.setText(getItem(position).getCctv());
        viewHolder.tv_limit.setText(getItem(position).getChildlimit());
        viewHolder.tv_bus.setText(getItem(position).getBus());

        viewHolder.button.setTag(position);
        viewHolder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, School_Info_Activity.class);
                intent.putExtra("userinfo",userinfo);
                intent.putExtra("schoolid",getItem(position).getId());
                intent.putExtra("schoolname",getItem(position).getName());
                mContext.startActivity(intent);
            }
        });
        return v;
    }

    // Adapter가 관리하는 Data List를 교체 한다.
    // 교체 후 Adapter.notifyDataSetChanged() 메서드로 변경 사실을
    // Adapter에 알려 주어 ListView에 적용 되도록 한다.
    public void setArrayList(ArrayList<kinderinfoclass> arrays){
        this.infoList = arrays;
    }

    public ArrayList<kinderinfoclass> getArrayList(){
        return infoList;
    }

    /*
     * ViewHolder
     * getView의 속도 향상을 위해 쓴다.
     * 한번의 findViewByID 로 재사용 하기 위해 viewHolder를 사용 한다.
     */

    class ViewHolder{
        public TextView tv_name = null;
        public TextView tv_phone = null;
        public TextView tv_address = null;
        public TextView tv_kindertype = null;
        public TextView tv_limit = null;
        public TextView tv_cctv = null;
        public TextView tv_bus = null;
        public Button button = null;
    }
    @Override
    protected void finalize() throws Throwable {
        free();
        super.finalize();
    }

    private void free(){
        inflater = null;
        infoList = null;
        viewHolder = null;
        mContext = null;
    }
}

