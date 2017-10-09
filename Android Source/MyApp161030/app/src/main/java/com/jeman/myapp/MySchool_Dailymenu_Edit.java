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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jeman.myapp.adapter.DailyEditAdapter;
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
import java.util.HashMap;

/**
 * Created by 신영준 on 2016-09-08.
 */
public class MySchool_Dailymenu_Edit extends Activity {
    public TextView datetextview;
    public Button savebutton;
    public Button newbutton;
    public ListView listview;
    public Button exitbutton;

    userInfo userinfo;
    String myJSON;
    JSONArray peoples = null;
    private static final String TAG_RESULTS = "result";
    private static final String TAG_MENU = "menu";
    private static final String TAG_FROM = "from";
    String textViewText;
    String date;
    String kinder_id;

    int savebeforesize;
    int saveaftersize;

    ArrayList<menuparam> menuparam;
    ArrayList<menuclass> menuclass;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myschool_dailymenu_edit);

        userinfo = (userInfo) getIntent().getSerializableExtra("userinfo");

        menuclass = new ArrayList<menuclass>();
        menuparam = new ArrayList<menuparam>();

        listview = (ListView) findViewById(R.id.dailymenu_listview);
        date = getIntent().getStringExtra("daily_date");
        Log.v("daily_date", date);

        kinder_id = userinfo.getKinderid();

        savebutton = (Button) findViewById(R.id.dailyedit_chkbtn);
        newbutton = (Button) findViewById(R.id.dailyedit_newbtn);
        exitbutton = (Button) findViewById(R.id.dailyedit_exitbtn);
        datetextview = (TextView) findViewById(R.id.dailyedit_date);

        menuparam.add(new menuparam("menu1", "from1"));
        menuparam.add(new menuparam("menu2", "from2"));
        menuparam.add(new menuparam("menu3", "from3"));
        menuparam.add(new menuparam("menu4", "from4"));
        menuparam.add(new menuparam("menu5", "from5"));
        menuparam.add(new menuparam("menu6", "from6"));
        menuparam.add(new menuparam("menu7", "from7"));
        menuparam.add(new menuparam("menu8", "from8"));
        menuparam.add(new menuparam("menu9", "from9"));
        menuparam.add(new menuparam("menu10", "from10"));


    }

    @Override
    protected void onStart() {
        super.onStart();
        datetextview.setText(date);
        getData(kinder_id, date);
    }
    public void daily_edit_exit(View view){
        finish();
    }
    public void daily_edit_save(View view){
        for(int i = 0; i<menuclass.size(); i++) {
            EditText menu = (EditText) listview.getChildAt(i).findViewById(R.id.dailyed_li_menu);
            EditText from = (EditText) listview.getChildAt(i).findViewById(R.id.dailyed_li_from);
            menuclass.get(i).setFrom(from.getText().toString());
            menuclass.get(i).setMenu(menu.getText().toString());
            Log.v("saveFROM?",menuclass.get(i).getFrom());
            Log.v("saveMENU?",menuclass.get(i).getMenu());
        }
        insertData(kinder_id,date);

    }

    public void daily_edit_new(View view){
        if(menuclass.size() == 10)
            Toast.makeText(this, "최대 10개 등록 가능합니다.", Toast.LENGTH_SHORT).show();
        else {
            for (int i = savebeforesize; i<menuclass.size();i++){
                EditText menu = (EditText) listview.getChildAt(i).findViewById(R.id.dailyed_li_menu);
                EditText from = (EditText) listview.getChildAt(i).findViewById(R.id.dailyed_li_from);
                menuclass.get(i).setFrom(from.getText().toString());
                menuclass.get(i).setMenu(menu.getText().toString());
            }
            menuclass.add(new menuclass("", ""));
            listview.setAdapter(new DailyEditAdapter(this, menuclass, userinfo));
         }
    }

    public void setListview() {
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            peoples = jsonObj.getJSONArray(TAG_RESULTS);

            textViewText = null;
            for (int i = 0; i < peoples.length(); i++) {
                JSONObject c = peoples.getJSONObject(i);
                for (int j = 1;j<11;j++) {
                    if((c.getString(TAG_MENU + String.valueOf(j))).equals("null")||(c.getString(TAG_MENU + String.valueOf(j))).equals(""))
                        break;
                    else
                        menuclass.add(new menuclass(c.getString(TAG_MENU + String.valueOf(j)), c.getString(TAG_FROM + String.valueOf(j))));
                }
                savebeforesize = menuclass.size();
            }
            listview.setAdapter(new DailyEditAdapter(this,menuclass,userinfo));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void getData(String kinder_id, String date) {
        class GetDataJSON extends AsyncTask<String, Void, String> {
            ProgressDialog loading;

            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(MySchool_Dailymenu_Edit.this, "Please Wait", null, true, true);
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

    public void insertData(String kinder_id, String date) {
        class GetDataJSON extends AsyncTask<String, Void, String> {
            ProgressDialog loading;

            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(MySchool_Dailymenu_Edit.this, "Please Wait", null, true, true);
            }

            protected String doInBackground(String... params) {
                BufferedReader bufferedReader = null;
                try {
                    //현재위치 php에 저장
                    String id_param = (String) params[0];
                    String date_param = (String) params[1];
                    Log.v("JSON id_param :", id_param);
                    Log.v("JSON date_param :", date_param);

                    String link = "http://bluecarnival.cafe24.com/kindergarten/daily/insertdailymenu.php";
                    String data = URLEncoder.encode("date", "UTF-8") + "=" + URLEncoder.encode(date_param, "UTF-8");
                    data += "&" + URLEncoder.encode("kinder_id", "UTF-8") + "=" + URLEncoder.encode(id_param, "UTF-8");

                    for(int i = 0; i<menuclass.size() ; i++) {
                        data += "&" + URLEncoder.encode(menuparam.get(i).getMenunum(), "UTF-8") + "=" + URLEncoder.encode(menuclass.get(i).getMenu(), "UTF-8");
                        data += "&" + URLEncoder.encode(menuparam.get(i).getFromnum(),"UTF-8") + "=" + URLEncoder.encode(menuclass.get(i).getFrom(), "UTF-8");
                    }

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
                Log.v("savediaryresult","돌아가나");
                Log.v("savediaryresult",result);

                AlertDialog.Builder alert = new AlertDialog.Builder(MySchool_Dailymenu_Edit.this);

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
        g.execute(kinder_id, date);
    }
}

class menuparam{
    String menunum;
    String fromnum;
    String getMenunum(){
        return menunum;
    }
    String getFromnum(){
        return fromnum;
    }
    menuparam(String menunum, String fromnum){
        this.menunum = menunum;
        this.fromnum = fromnum;
    }
}