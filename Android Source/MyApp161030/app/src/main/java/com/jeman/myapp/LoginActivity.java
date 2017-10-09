package com.jeman.myapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.jeman.myapp.Register.Register_email;
import com.jeman.myapp.find.findPasswordActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class LoginActivity extends Activity{
    private BackPressCloseHandler backPressCloseHandler;

    String myJSON;
    private static final String TAG_RESULTS = "result";
    private static final String TAG_EMAIL = "email";
    private static final String TAG_NAME = "name";
    private static final String TAG_KINDERID = "kinder_id";
    private static final String TAG_DATE = "update_date";
    private static final String TAG_TEACHER_BOOL = "teacher_bool";
    private static final String TAG_READY_REGISTER = "ready_register";
    private static final String TAG_CHILD_NAME = "child_name";

    JSONArray peoples = null;

    Intent intent;
    Intent intent_login;
    EditText editemail;
    EditText editpassword;
    CheckBox auto_login;
    SharedPreferences setting;
    SharedPreferences.Editor editor;

    public userInfo userinfo;
    int check_num;
    boolean logout=false;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        intent = new Intent(this, MainActivity.class);

        editemail = (EditText) findViewById(R.id.login_edit_email);
        editpassword = (EditText) findViewById(R.id.login_edit_password);
        auto_login = (CheckBox) findViewById(R.id.auto_login);

        backPressCloseHandler = new BackPressCloseHandler(this);

        setting = getSharedPreferences("setting", 0);
        editor = setting.edit();
        logout = getIntent().getBooleanExtra("logout",false);
        Log.v("logout",String.valueOf(logout));
        if(logout == true){
            editor.clear();
            editor.commit();
        }else if(logout == false) {
            if (setting.getBoolean("Auto_Login_enabled", false)) {
                editemail.setText(setting.getString("ID", ""));
                editpassword.setText(setting.getString("PW", ""));
                auto_login.setChecked(true);
                check_num = 3;
                getData(editemail.getText().toString(), editpassword.getText().toString());
            }
        }

        auto_login.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    check_num = 1;
                } else {
                    check_num = 2;
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        backPressCloseHandler.onBackPressed();
    }

    public void email_find(View view){
        Intent intent = new Intent(this,findPasswordActivity.class);
        startActivity(intent);
    }

    public void login(View view) {
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getData(editemail.getText().toString(), editpassword.getText().toString());
    }

    public void login_guest(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("userinfo",new userInfo("guest","guest","guest","guest",false,"guest"));
        editor.clear();
        editor.commit();

        startActivity(intent);

    }

    public void register(View view){
        Intent register = new Intent(LoginActivity.this, Register_email.class);
        startActivity(register);
    }

    protected void setuserinfo() {
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            peoples = jsonObj.getJSONArray(TAG_RESULTS);
            Log.v("userinfo",myJSON);
            if(peoples.length() >0) {
                JSONObject c = peoples.getJSONObject(0);
                String email = c.getString(TAG_EMAIL);
                String name = c.getString(TAG_NAME);
                String kinder_id = c.getString(TAG_KINDERID);
                String update_date = c.getString(TAG_DATE);
                String teacher_bool = c.getString(TAG_TEACHER_BOOL);
                String ready_register = c.getString(TAG_READY_REGISTER);
                String childname = c.getString(TAG_CHILD_NAME);

                boolean ready = false;

                if (ready_register.equals("1"))
                    ready = true;

                userInfo info = new userInfo(email, name, kinder_id, update_date, ready, childname);

                if (teacher_bool.equals("1"))
                    info.setTeacher();


                if(check_num == 1) {
                    String ID = editemail.getText().toString();
                    String PW = editpassword.getText().toString();

                    editor.putString("ID", ID);
                    editor.putString("PW", PW);
                    editor.putBoolean("Auto_Login_enabled", true);
                    editor.commit();
                }
                else if(check_num == 3) {
                    editor.putBoolean("Auto_Login_enabled", true);
                }
                else {
                    editor.clear();
                    editor.commit();
                }

                Toast.makeText(getApplicationContext(), "어서오세요.", Toast.LENGTH_SHORT).show();
                intent.putExtra("userinfo", info);
                startActivityForResult(intent,12);
                finish();
            }
            else {
                Toast.makeText(getApplicationContext(), "정보를 확인해 주십시오.", Toast.LENGTH_SHORT).show();
                //intent_login = new Intent(this, LoginActivity.class);
                //startActivity(intent_login);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    public void getData(String email, String password) {
        class GetDataJSON extends AsyncTask<String, Void, String> {

            protected void onPreExecute() {
                super.onPreExecute();
            }

            protected String doInBackground(String... params) {
                BufferedReader bufferedReader = null;
                try {
                    //현재위치 php에 저장
                    String email = (String) params[0];
                    String password = (String) params[1];

                    Log.v("JSON PARAM :",email +"/" + password);

                    String link = "http://bluecarnival.cafe24.com/kindergarten/getuserinfo.php";
                    String data = URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8");
                    data += "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");

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
                super.onPostExecute(result);
                Log.v("JSON Result :",result);
                myJSON = result;

                setuserinfo();
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute(email,password);
    }
}

