package com.jeman.myapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.jeman.myapp.R;
import com.jeman.myapp.userInfo;

import java.util.ArrayList;


/**
 * Created by 신영준 on 2016-09-24.
 */public class DailyMenuAdapter extends BaseAdapter {

    private LayoutInflater inflater = null;
    private ArrayList<menuclass> infoList = null;
    private ViewHolder viewHolder = null;
    private Context mContext = null;
    private userInfo userinfo;

    public DailyMenuAdapter(Context c , ArrayList<menuclass> arrays, userInfo userinfo){
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
    public menuclass getItem(int position) {
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
            v = inflater.inflate(R.layout.myschool_dailymenu_listitem, null);
            viewHolder.ed_menu = (TextView)v.findViewById(R.id.dailymenu_li_menu);
            viewHolder.ed_from = (TextView)v.findViewById(R.id.dailymenu_li_from);

            v.setTag(viewHolder);

        }else {
            viewHolder = (ViewHolder)v.getTag();
        }

        viewHolder.ed_menu.setText(getItem(position).getMenu());
        viewHolder.ed_from.setText(getItem(position).getFrom());

        return v;
    }

    // Adapter가 관리하는 Data List를 교체 한다.
    // 교체 후 Adapter.notifyDataSetChanged() 메서드로 변경 사실을
    // Adapter에 알려 주어 ListView에 적용 되도록 한다.
    public void setArrayList(ArrayList<menuclass> arrays){
        this.infoList = arrays;
    }

    public ArrayList<menuclass> getArrayList(){
        return infoList;
    }

    /*
     * ViewHolder
     * getView의 속도 향상을 위해 쓴다.
     * 한번의 findViewByID 로 재사용 하기 위해 viewHolder를 사용 한다.
     */

    class ViewHolder{
        public TextView ed_menu = null;
        public TextView ed_from = null;
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

