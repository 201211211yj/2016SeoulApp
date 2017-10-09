package com.jeman.myapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
public class MySchool_Notice_Edit extends Activity {
    public TextView datetextview;
    public EditText edittitle;
    public EditText edittext;
    public Button savebutton;
    public Button exitbutton;

    String myJSON;
    JSONArray peoples = null;
    private static final String TAG_RESULTS = "result";
    private static final String TAG_NOTICE_TITLE = "notice_title";
    private static final String TAG_NOTICE_TEXT = "notice_text";
    private static final String TAG_KINDER_ID = "kinder_id";

    String textViewText;
    String date;

    String kinder_id;
    String notice_id;
    String notice_title;
    String notice_text;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myschool_notice_edit);

        notice_id = getIntent().getStringExtra("notice_id");

        savebutton = (Button) findViewById(R.id.notice_savebtn);
        exitbutton = (Button) findViewById(R.id.notice_exitbtn);
        edittitle = (EditText) findViewById(R.id.notice_edittitle);
        edittext = (EditText) findViewById(R.id.notice_edittext);
        if (notice_id.equals(""))
            kinder_id = getIntent().getStringExtra("kinder_id");
        else
            getData(notice_id);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public void notice_edit_clicksave(View view) {
        insertData(kinder_id, edittitle.getText().toString(), edittext.getText().toString(),notice_id);
    }
    public void notice_edit_clickexit(View view) {finish();}

    public void setString() {
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            peoples = jsonObj.getJSONArray(TAG_RESULTS);

            for (int i = 0; i < peoples.length(); i++) {
                JSONObject c = peoples.getJSONObject(i);
                String notice_title = c.getString(TAG_NOTICE_TITLE);
                String notice_text = c.getString(TAG_NOTICE_TEXT);
                String kinder_id = c.getString(TAG_KINDER_ID);

                this.kinder_id = kinder_id;
                this.notice_title = notice_title;
                this.notice_text = notice_text;
            }
            edittitle.setText(notice_title);
            edittext.setText(notice_text);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void getData(String notice_id) {
        class GetDataJSON extends AsyncTask<String, Void, String> {
            ProgressDialog loading;

            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(MySchool_Notice_Edit.this, "Please Wait", null, true, true);
            }

            protected String doInBackground(String... params) {
                BufferedReader bufferedReader = null;
                try {
                    //현재위치 php에 저장
                    String id_param = (String) params[0];
                    Log.v("JSON id_param :", id_param);

                    String link = "http://bluecarnival.cafe24.com/kindergarten/notice/getnoticeedit.php";
                    String data = URLEncoder.encode("notice_id", "UTF-8") + "=" + URLEncoder.encode(id_param, "UTF-8");

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
                setString();
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute(notice_id);
    }

    public void insertData(String kinder_id, String notice_title, String notice_text, String notice_id) {
        class GetDataJSON extends AsyncTask<String, Void, String> {
            ProgressDialog loading;

            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(MySchool_Notice_Edit.this, "Please Wait", null, true, true);
            }

            protected String doInBackground(String... params) {
                BufferedReader bufferedReader = null;
                try {
                    //현재위치 php에 저장
                    String kinder_id_param = (String) params[0];
                    String title_param = (String) params[1];
                    String text_param = (String) params[2];
                    String notice_id_param = (String) params[3];

                    Log.v("JSON kinder_id_param :", kinder_id_param);
                    Log.v("JSON title_param :", title_param);
                    Log.v("JSON text_param :", text_param);
                    Log.v("JSON notice_id_param :", notice_id_param);

                    String link = "http://bluecarnival.cafe24.com/kindergarten/notice/savenoticedata.php";
                    String data = URLEncoder.encode("kinder_id", "UTF-8") + "=" + URLEncoder.encode(kinder_id_param, "UTF-8");
                    data += "&" + URLEncoder.encode("notice_title", "UTF-8") + "=" + URLEncoder.encode(title_param, "UTF-8");
                    data += "&" + URLEncoder.encode("notice_text", "UTF-8") + "=" + URLEncoder.encode(text_param, "UTF-8");
                    data += "&" + URLEncoder.encode("notice_id", "UTF-8") + "=" + URLEncoder.encode(notice_id_param, "UTF-8");

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
                Log.v("savenoticeresult",result);

                AlertDialog.Builder alert = new AlertDialog.Builder(MySchool_Notice_Edit.this);

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
                else {
                    setResult(Activity.RESULT_OK,new Intent(MySchool_Notice_Edit.this,MySchoolActivity.class));
                    finish();
                }
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute(kinder_id, notice_title,notice_text,notice_id);

    }
}