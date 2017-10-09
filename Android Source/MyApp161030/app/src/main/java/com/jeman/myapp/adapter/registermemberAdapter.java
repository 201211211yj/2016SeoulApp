package com.jeman.myapp.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.jeman.myapp.MySchoolActivity;
import com.jeman.myapp.R;
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
 */public class registermemberAdapter extends BaseAdapter {

    private LayoutInflater inflater = null;
    private ArrayList<infoclass> infoList = null;
    private ViewHolder viewHolder = null;
    private Context mContext = null;
    private userInfo userinfo;

    public registermemberAdapter(Context c , ArrayList<infoclass> arrays, userInfo userinfo){
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
    public infoclass getItem(int position) {
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
            v = inflater.inflate(R.layout.register_members_item, null);
            viewHolder.tv_mail = (TextView)v.findViewById(R.id.regimember_email);
            viewHolder.tv_date = (TextView)v.findViewById(R.id.regimember_date);
            viewHolder.cancel_button = (Button)v.findViewById(R.id.regimember_cancelbtn);
            viewHolder.regi_button = (Button)v.findViewById(R.id.regimember_regibtn);

            v.setTag(viewHolder);

        }else {
            viewHolder = (ViewHolder)v.getTag();
        }

        viewHolder.tv_mail.setText(getItem(position).getMail());
        viewHolder.tv_date.setText(getItem(position).getDate());

        viewHolder.cancel_button.setTag(position);
        viewHolder.cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registering(getItem(position).getMail(),"register");
                infoList.remove(position);
                notifyDataSetInvalidated();
            }
        });

        viewHolder.regi_button.setTag(position);
        viewHolder.regi_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registering(getItem(position).getMail(),"cancel");
                infoList.remove(position);
                notifyDataSetInvalidated();
            }
        });

        return v;
    }

    // Adapter가 관리하는 Data List를 교체 한다.
    // 교체 후 Adapter.notifyDataSetChanged() 메서드로 변경 사실을
    // Adapter에 알려 주어 ListView에 적용 되도록 한다.
    public void setArrayList(ArrayList<infoclass> arrays){
        this.infoList = arrays;
    }

    public ArrayList<infoclass> getArrayList(){
        return infoList;
    }

    /*
     * ViewHolder
     * getView의 속도 향상을 위해 쓴다.
     * 한번의 findViewByID 로 재사용 하기 위해 viewHolder를 사용 한다.
     */

    public void registering(String email,String param) {
        class GetDataJSON extends AsyncTask<String, Void, String> {
            ProgressDialog loading;

            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(mContext, "Please Wait", null, true, true);
            }

            protected String doInBackground(String... params) {
                BufferedReader bufferedReader = null;
                try {
                    //현재위치 php에 저장
                    String email_param = (String) params[0];
                    String param = (String) params[1];
                    Log.v("JSON kinder_id_param :", email_param);

                    String link = "http://bluecarnival.cafe24.com/kindergarten/member/registering.php";
                    String data = URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email_param, "UTF-8");
                    data += "&" + URLEncoder.encode("param", "UTF-8") + "=" + URLEncoder.encode(param, "UTF-8");
                    URL url2nd = new URL(link);
                    URLConnection conn = url2nd.openConnection();

                    conn.setDoOutput(true);
                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                    wr.write(data);
                    wr.flush();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line = null;

                    //
                    // Read Server Response
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                        break;
                    }
                    return sb.toString();
                } catch (Exception e) {
                    return new String("Exception: " + e.getMessage());
                }
            }

            protected void onPostExecute(String result) {
                loading.dismiss();
                super.onPostExecute(result);
                Log.v("savenoticeresult", result);
            }
        }

        GetDataJSON g = new GetDataJSON();
        g.execute(email,param);

    }

    class ViewHolder{
        public TextView tv_mail = null;
        public TextView tv_date = null;
        public Button regi_button = null;
        public Button cancel_button = null;
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

