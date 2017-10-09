package com.jeman.myapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.HashMap;

/**
 * Created by 신영준 on 2016-10-30.
 */
public class Update_info_password extends Activity{
    userInfo userinfo;

    EditText nowpassword;
    EditText newpassword;
    EditText chkpassword;
    Button update_btn;

    TextView message;

    String myJSON;
    private static final String TAG_RESULTS = "result";
    private static final String TAG_EMAIL = "email";
    JSONArray peoples = null;
    ArrayList<HashMap<String, String>> personList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_password);
        userinfo = (userInfo) getIntent().getSerializableExtra("userinfo");

        nowpassword = (EditText)findViewById(R.id.update_password_now);
        newpassword = (EditText)findViewById(R.id.update_password_new);
        chkpassword = (EditText)findViewById(R.id.update_password_chk);
        update_btn = (Button)findViewById(R.id.update_update_btn);
        message = (TextView)findViewById(R.id.update_password_msg);
        update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!newpassword.getText().toString().equals(chkpassword.getText().toString()))
                    message.setText("새 비밀번호가 서로 맞지 않습니다");
                else
                    updateData(userinfo.getEmail(), nowpassword.getText().toString(), newpassword.getText().toString());
            }
        });
    }

    public void updateData(String myparameter1, String myparameter2, String myparameter3) {
        class GetDataJSON extends AsyncTask<String, Void, String> {
            ProgressDialog loading;

            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Update_info_password.this, "Please Wait", null, true, true);
            }

            protected String doInBackground(String... params) {
                BufferedReader bufferedReader = null;
                try {
                    //현재위치 php에 저장
                    String myparam1 = (String) params[0];
                    String myparam2 = (String) params[1];
                    String myparam3 = (String) params[2];

                    Log.v("JSON myparam1 :", myparam1);
                    Log.v("JSON myparam2 :", myparam2);
                    Log.v("JSON myparam3 :", myparam3);

                    String link = "http://bluecarnival.cafe24.com/kindergarten/updatepassword.php";
                    String data = URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(myparam1, "UTF-8");
                    data += "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(myparam2, "UTF-8");
                    data += "&" + URLEncoder.encode("new_password", "UTF-8") + "=" + URLEncoder.encode(myparam3, "UTF-8");

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
                if(result.contains("failure"))
                    message.setText("현재 비밀번호가 맞지 않습니다");
                else {
                    finish();
                }
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute(myparameter1,myparameter2,myparameter3);
    }

}
