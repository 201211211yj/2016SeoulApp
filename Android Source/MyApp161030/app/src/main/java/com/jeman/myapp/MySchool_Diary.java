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
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.Calendar;

/**
 * Created by 신영준 on 2016-08-29.
 */
public class MySchool_Diary extends Fragment implements View.OnClickListener{

    TextView textView;
    String textViewText;
    Button button;
    Button button2;

    String myJSON;
    String date;

    View.OnClickListener LeftOnClickListener;
    View.OnClickListener RightOnClickListener;
    DatePickerDialog dialog;
    TextView datetv;
    JSONArray peoples = null;
    private static final String TAG_RESULTS = "result";
    private static final String TAG_DIARY = "diary";

    userInfo userinfo;

    public static MySchool_Diary newInstance(userInfo userinfo) {

        Bundle args = new Bundle();

        MySchool_Diary fragment = new MySchool_Diary();
        args.putSerializable("INFO_PARAM",userinfo);
        fragment.setArguments(args);
        return fragment;
    }
    public MySchool_Diary(){

    }
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userinfo = (userInfo) getArguments().getSerializable("INFO_PARAM");
        }
        LeftOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                date = dialog.getDate();
                datetv.setText(date);
                dialog.dismiss();
                getData(userinfo.getKinderid(), date);
                Log.v("dialog_date",date);
            }
        };
        RightOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        };
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.myschool_diary, container, false);
        textView = (TextView)v.findViewById(R.id.A2_diary_text);
        button = (Button)v.findViewById(R.id.A2_diary_check);
        button.setOnClickListener(this);
        button2 = (Button)v.findViewById(R.id.A2_diary_edit);

        date = "";
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        date += Integer.toString(year) + "-";
        int month = calendar.get(Calendar.MONTH);
        date += Integer.toString(month + 1) + "-";
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        date += Integer.toString(day);

        datetv = (TextView)v.findViewById(R.id.A2_diary_date);
        datetv.setText(date);

        if(userinfo.isTeacher())
            button2.setOnClickListener(this);
        else {
            button2.setVisibility(View.INVISIBLE);
        } //2016-09-21

        return v;
    }

    public void onStart() {
        super.onStart();
        getData(userinfo.getKinderid(),date);
    }

    public void onResume(){
        super.onResume();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.A2_diary_check :
                dialog = new DatePickerDialog(getActivity(),LeftOnClickListener,RightOnClickListener);
                dialog.show();
                break;
            case R.id.A2_diary_edit :
                Intent intent = new Intent (getActivity(), MySchool_Diary_Edit.class);
                intent.putExtra("diary_date",date);
                intent.putExtra("diary_kinderid",userinfo.getKinderid());
                startActivity(intent);
                break;
        }
    }
    public void textview_setText() {
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            peoples = jsonObj.getJSONArray(TAG_RESULTS);

            textView.setText("");
            textViewText = null;
            for (int i = 0; i < peoples.length(); i++) {
                JSONObject c = peoples.getJSONObject(i);
                String diary = c.getString(TAG_DIARY);
                textViewText = diary;
            }
            textView.setText(textViewText);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    public void getData(String kinder_id,String date) {
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
                    Log.v("JSON id_param :",id_param);
                    Log.v("JSON date_param :",date_param);

                    String link = "http://bluecarnival.cafe24.com/kindergarten/diary/getdiarydata.php";
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
                Log.v("JSON Result :",result);
                myJSON = result;
                textview_setText();
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute(kinder_id,date);
    }

}


