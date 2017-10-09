package com.jeman.myapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jeman.myapp.adapter.DailyMenuAdapter;
import com.jeman.myapp.adapter.menuclass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by 신영준 on 2016-09-13.
 */
public class MySchool_Dailymenu extends Fragment implements View.OnClickListener {

    String myJSON;

    private static final String TAG_RESULTS = "result";
    private static final String TAG_MENU = "menu";
    private static final String TAG_FROM = "from";

    JSONArray peoples = null;
    ArrayList<menuclass> menuclass;

    userInfo userinfo;

    DatePickerDialog dialog;
    View.OnClickListener LeftOnClickListener;
    View.OnClickListener RightOnClickListener;
    Button newbtn;
    Button datebtn;
    TextView datetv;
    ListView listview;
    String kinder_id;
    String date;

    public static MySchool_Dailymenu newInstance(userInfo userinfo) {

        Bundle args = new Bundle();

        MySchool_Dailymenu fragment = new MySchool_Dailymenu();
        args.putSerializable("INFO_PARAM", userinfo);
        fragment.setArguments(args);
        return fragment;
    }

    public MySchool_Dailymenu() {

    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        menuclass = new ArrayList<menuclass>();

        LeftOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                date = dialog.getDate();
                datetv.setText(date);
                dialog.dismiss();
                menuclass.clear();
                getData(kinder_id,date);
                Log.v("dialog_date",date);
            }
        };
        RightOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        };
        if (getArguments() != null) {
            userinfo = (userInfo) getArguments().getSerializable("INFO_PARAM");
            kinder_id = userinfo.getKinderid();

        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.myschool_dailymenu, container, false);

        date = "";
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        date += Integer.toString(year) + "-";
        int month = calendar.get(Calendar.MONTH);
        date += Integer.toString(month + 1) + "-";
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        date += Integer.toString(day);

        listview  = (ListView)v.findViewById(R.id.dailymenu_listview);
        datetv = (TextView)v.findViewById(R.id.dailymenu_date);
        datetv.setText(date);
        newbtn = (Button)v.findViewById(R.id.dailymenu_newbtn);
        newbtn.setOnClickListener(this);
        if(userinfo.isTeacher()==false)
                newbtn.setVisibility(View.INVISIBLE);
        datebtn = (Button)v.findViewById(R.id.dailymenu_datebtn);
        datebtn.setOnClickListener(this);
        getData(kinder_id, date);
        super.onCreate(savedInstanceState);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    public void setListview() {
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            peoples = jsonObj.getJSONArray(TAG_RESULTS);

            for (int i = 0; i < peoples.length(); i++) {
                JSONObject c = peoples.getJSONObject(i);
                for (int j = 1; j < 11; j++) {
                    if ((c.getString(TAG_MENU + String.valueOf(j))).equals("null")||(c.getString(TAG_MENU + String.valueOf(j))).equals(""))
                        break;
                    else
                        menuclass.add(new menuclass(c.getString(TAG_MENU + String.valueOf(j)), c.getString(TAG_FROM + String.valueOf(j))));
                }
            }
            listview.setAdapter(new DailyMenuAdapter(getActivity(), menuclass, userinfo));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void getData(String kinder_id, String date) {
        class GetDataJSON extends AsyncTask<String, Void, String> {
            ProgressDialog loading;

            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(getActivity(), "Please Wait", null, true, true);
            }

            protected String doInBackground(String... params) {
                BufferedReader bufferedReader = null;
                try {
                    //현재위치 php에 저장
                    String id_param = (String) params[0];
                    String date_param = (String) params[1];
                    Log.v("JSON id_param :", id_param);
                    Log.v("JSON date_param :", date_param);

                    String link = "http://bluecarnival.cafe24.com/kindergarten/daily/getdailymenu.php";
                    String data = URLEncoder.encode("date", "UTF-8") + "=" + URLEncoder.encode(date_param, "UTF-8");
                    data += "&" + URLEncoder.encode("kinder_id", "UTF-8") + "=" + URLEncoder.encode(id_param, "UTF-8");

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
                Log.v("JSON Result :", result);
                myJSON = result;
                setListview();
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute(kinder_id, date);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.dailymenu_newbtn :
                if(!userinfo.isTeacher())
                    Toast.makeText(getActivity(), "교사만 가능합니다.", Toast.LENGTH_SHORT).show();
                else {
                    Intent intent = new Intent(getActivity(), MySchool_Dailymenu_Edit.class);
                    intent.putExtra("userinfo", userinfo);
                    intent.putExtra("daily_date", date);
                    startActivity(intent);
                }
                break;
            case R.id.dailymenu_datebtn :
                dialog = new DatePickerDialog(getActivity(),LeftOnClickListener,RightOnClickListener);
                dialog.show();
                break;
        }
    }
}
