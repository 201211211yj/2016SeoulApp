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
import android.widget.TextView;
import android.widget.Toast;

import com.jeman.myapp.LoginActivity;
import com.jeman.myapp.R;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.SecureRandom;

/**
 * Created by 신영준 on 2016-09-03.
 */
public class Register_info extends Activity{
    EditText editname;
    Editable name;
    EditText editpswrd;
    Editable pswrd;
    EditText editpswrdchk;
    Editable pswrdchk;
    EditText editnumber;
    EditText editchildname;
    Editable childname;
    Button chk_button;
    TextView emailtxtView;
    Intent intent;
    String email;
    String randomstring;
    static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    static SecureRandom rnd = new SecureRandom();

    String randomString( int len ){
        StringBuilder sb = new StringBuilder( len );
        for( int i = 0; i < len; i++ )
            sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );
        return sb.toString();
    }
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_register_info);
        Intent getintent = getIntent();
        email = getintent.getStringExtra("regi_email");

        editchildname = (EditText)findViewById(R.id.regi_info_childname);
        emailtxtView = (TextView)findViewById(R.id.regi_info_email);
        emailtxtView.setText(email);
        editname = (EditText)findViewById(R.id.regi_info_name);
        editpswrd = (EditText)findViewById(R.id.regi_info_pwrd);
        editpswrdchk = (EditText)findViewById(R.id.regi_info_pwrdchk);
        chk_button = (Button)findViewById(R.id.regi_info_check);

        editnumber = (EditText)findViewById(R.id.regi_editnumber);
        intent = new Intent(this,LoginActivity.class);
    }

    public void regi_info_randnum(View view){
        randomstring = randomString(10);
        String mailbody = "인증번호는 "+ randomstring+"입니다.";
        randnummail(email,mailbody);
        Log.v("params",email +'/'+randomstring);
    }
    public void regi_info_check(View view) {
        name = editname.getText();
        pswrd = editpswrd.getText();
        pswrdchk = editpswrdchk.getText();
        childname = editchildname.getText();

        if(childname==null || name == null || pswrd ==null || pswrdchk == null|| editnumber == null||randomstring == null)
            Toast.makeText(getApplicationContext(), "입력이 안된 부분이 있습니다.", Toast.LENGTH_SHORT).show();
        else if(pswrd.toString().equals(pswrdchk.toString())==false)
            Toast.makeText(getApplicationContext(), "비밀번호가 맞지 않습니다.", Toast.LENGTH_SHORT).show();
        else if(!randomstring.equals(editnumber.getText().toString())) {
            Toast.makeText(getApplicationContext(), "인증번호가 맞지 않습니다.", Toast.LENGTH_SHORT).show();
            Log.v("randomstring",randomstring);
            Log.v("editnumber",editnumber.getText().toString());
        }
        else {
            insertToDatabase(email,name.toString(),pswrd.toString(),childname.toString());
            startActivity(intent);
            finish();
        }
    }

    private void insertToDatabase(String email,String name,String password, String childname){

        class InsertData extends AsyncTask<String, Void, String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Register_info.this, "Please Wait", null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
            }

            @Override
            protected String doInBackground(String... params) {

                try{
                    String email = (String)params[0];
                    String name = (String)params[1];
                    String password = (String)params[2];
                    String childname = (String)params[3];

                    String link="http://bluecarnival.cafe24.com/kindergarten/register/insertuser.php";
                    String data  = URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8");
                    data += "&" + URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(name, "UTF-8");
                    data += "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");
                    data += "&" + URLEncoder.encode("child_name", "UTF-8") + "=" + URLEncoder.encode(childname, "UTF-8");

                    URL url = new URL(link);
                    URLConnection conn = url.openConnection();

                    conn.setDoOutput(true);
                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                    wr.write( data );
                    wr.flush();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                    StringBuilder sb = new StringBuilder();
                    String line = null;

                    // Read Server Response
                    while((line = reader.readLine()) != null)
                    {
                        sb.append(line);
                        break;
                    }
                    return sb.toString();
                }
                catch(Exception e){
                    return new String("Exception: " + e.getMessage());
                }

            }
        }

        InsertData task = new InsertData();
        task.execute(email,name,password,childname);
    }

    private void randnummail(String email,String mailbody){
        class InsertData extends AsyncTask<String, Void, String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Register_info.this, "Please Wait", null, true, true);
            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                Log.v("sendresult",s);
                loading.dismiss();
            }
            @Override
            protected String doInBackground(String... params) {
                try{
                    String email = (String)params[0];
                    String mailbody = (String)params[1];
                    String link="http://bluecarnival.cafe24.com/kindergarten/mail/sendnumber.php";
                    String data  = URLEncoder.encode("sendto", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8");
                    data += "&" + URLEncoder.encode("mailbody", "UTF-8") + "=" + URLEncoder.encode(mailbody, "UTF-8");
                    URL url = new URL(link);
                    URLConnection conn = url.openConnection();
                    conn.setDoOutput(true);
                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                    wr.write( data );
                    wr.flush();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                    StringBuilder sb = new StringBuilder();
                    String line = null;

                    // Read Server Response
                    while((line = reader.readLine()) != null)
                    {
                        sb.append(line);
                        break;
                    }
                    return sb.toString();
                }
                catch(Exception e){
                    return new String("Exception: " + e.getMessage());
                }

            }
        }
        InsertData task = new InsertData();
        task.execute(email,mailbody);
    }
}