package com.jeman.myapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * Created by 신영준 on 2016-09-08.
 */
public class MySchool_Diary_Edit extends Activity {
    public TextView datetextview;
    public EditText edittext;
    public Button savebutton;
    public Button exitbutton;

    String myJSON;
    JSONArray peoples = null;
    private static final String TAG_RESULTS = "result";
    private static final String TAG_DIARY = "diary";
    String textViewText;
    String date;
    String kinder_id;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myschool_diary_edit);

        date = getIntent().getStringExtra("diary_date");
        Log.v("Diarydate", date);
        kinder_id = getIntent().getStringExtra("diary_kinderid");
        Log.v("Diarykinderid", kinder_id);

        savebutton = (Button) findViewById(R.id.diary_savebtn);
        exitbutton = (Button) findViewById(R.id.diary_exitbtn);
        datetextview = (TextView) findViewById(R.id.diary_datetext);
        edittext = (EditText) findViewById(R.id.diary_edittext);

    }

    @Override
    protected void onStart() {
        super.onStart();
        datetextview.setText(date);
        getData(kinder_id, date);
    }

    public void diary_edit_clicksave(View view) {
        insertData(kinder_id, date, edittext.getText().toString());
    }
    public void diary_edit_clickexit(View view) {finish();}

    public void textview_setText() {
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            peoples = jsonObj.getJSONArray(TAG_RESULTS);

            textViewText = null;
            for (int i = 0; i < peoples.length(); i++) {
                JSONObject c = peoples.getJSONObject(i);
                String diary = c.getString(TAG_DIARY);
                textViewText = diary;
            }
            edittext.setText(textViewText);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void getData(String kinder_id, String date) {
        class GetDataJSON extends AsyncTask<String, Void, String> {
            ProgressDialog loading;

            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(MySchool_Diary_Edit.this, "Please Wait", null, true, true);
            }

            protected String doInBackground(String... params) {
                BufferedReader bufferedReader = null;
                try {
                    //현재위치 php에 저장
                    String id_param = (String) params[0];
                    String date_param = (String) params[1];
                    Log.v("JSON id_param :", id_param);
                    Log.v("JSON date_param :", date_param);

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
                Log.v("JSON Result :", result);
                myJSON = result;
                textview_setText();
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute(kinder_id, date);
    }

    public void insertData(String kinder_id, String date, String diary) {
        class GetDataJSON extends AsyncTask<String, Void, String> {
            ProgressDialog loading;

            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(MySchool_Diary_Edit.this, "Please Wait", null, true, true);
            }

            protected String doInBackground(String... params) {
                BufferedReader bufferedReader = null;
                try {
                    //현재위치 php에 저장
                    String id_param = (String) params[0];
                    String date_param = (String) params[1];
                    String diary_param = (String) params[2];
                    Log.v("JSON id_param :", id_param);
                    Log.v("JSON date_param :", date_param);

                    String link = "http://bluecarnival.cafe24.com/kindergarten/diary/savediarydata.php";
                    String data = URLEncoder.encode("date", "UTF-8") + "=" + URLEncoder.encode(date_param, "UTF-8");
                    data += "&" + URLEncoder.encode("kinder_id", "UTF-8") + "=" + URLEncoder.encode(id_param, "UTF-8");
                    data += "&" + URLEncoder.encode("diary", "UTF-8") + "=" + URLEncoder.encode(diary_param, "UTF-8");

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
                Log.v("savediaryresult",result);

                AlertDialog.Builder alert = new AlertDialog.Builder(MySchool_Diary_Edit.this);

                if(result.equals("failure")) {
                    alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();     //닫기
                        }
                    });
                    alert.setMessage("저장에 실패하였습니다.");
                    alert.show();
                }
                else
                    finish();
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute(kinder_id, date,diary);

    }
}