package com.jeman.myapp.Register;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jeman.myapp.R;

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
 * Created by 신영준 on 2016-08-31.
 */
public class Register_email extends Activity implements View.OnClickListener{

    String myJSON;
    private static final String TAG_RESULTS = "result";
    private static final String TAG_EMAIL = "email";

    JSONArray peoples = null;
    ArrayList<HashMap<String, String>> personList;
    EditText edittext;
    Button button;
    Editable editable;

    Intent intent;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_register_email);
        edittext = (EditText)findViewById(R.id.regi_email_editemail);
        button = (Button)findViewById(R.id.regi_email_checkbtn);
        button.setOnClickListener(this);

        intent = new Intent(this,Register_info.class);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.regi_email_checkbtn :
                editable = edittext.getText();
                if(editable.toString().contains("@")==false)
                    Toast.makeText(getApplicationContext(),"E-mail형식이 아닙니다.", Toast.LENGTH_SHORT).show();
                else
                    getData(editable.toString());
                break;
        }
    }

    public void showcan() {
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            peoples = jsonObj.getJSONArray(TAG_RESULTS);
            String email = "";
            for (int i = 0; i < peoples.length(); i++) {
                JSONObject c = peoples.getJSONObject(i);
                email = c.getString(TAG_EMAIL);
            }

            if (email == "") {
                intent.putExtra("regi_email", editable.toString());
                startActivity(intent);
            } else
                Toast.makeText(getApplicationContext(), "이미 존재하는 계정입니다.", Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getData(String myparameter) {
        class GetDataJSON extends AsyncTask<String, Void, String> {
            ProgressDialog loading;

            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Register_email.this, "Please Wait", null, true, true);
            }

            protected String doInBackground(String... params) {
                BufferedReader bufferedReader = null;
                try {
                    //현재위치 php에 저장
                    String myparam = (String) params[0];
                    Log.v("JSON myparam :", myparam);
                    String link = "http://bluecarnival.cafe24.com/kindergarten/register/getemail.php";
                    String data = URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(myparam, "UTF-8");

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
                showcan();
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute(myparameter);
    }

}
