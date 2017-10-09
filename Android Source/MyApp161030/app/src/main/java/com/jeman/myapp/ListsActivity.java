package com.jeman.myapp;

/**
 * Created by 신영준 on 2016-08-27.
 */

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.jeman.myapp.adapter.ListActivityAdapter;
import com.jeman.myapp.adapter.kinderinfoclass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class ListsActivity extends Activity {

    Intent SchoolInfoActivity;

    String myJSON;

    private static final String TAG_ID = "id";
    private static final String TAG_RESULTS = "result";
    private static final String TAG_NAME = "name";
    private static final String TAG_KINDERTYPE = "kindertype";
    private static final String TAG_PHONE = "phone";
    private static final String TAG_ADD = "address";
    private static final String TAG_CHILDLIMIT = "childlimit";
    private static final String TAG_CCTV = "cctv";
    private static final String TAG_BUS = "bus";

    EditText searchEdit;
    String sqlbus ;
    String sqlcctv ;
    String sqlloc;
    String sqltype;

    JSONArray peoples = null;
    ArrayList<HashMap<String, String>> personList;
    ArrayList<kinderinfoclass> kinderinfolist;
    ListView list;

    ListCheckDialog listCheckDialog;
    public void backClick(View view){
        finish();
    }
    public void findClick(View view){
        listCheckDialog = new ListCheckDialog(this, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("value",listCheckDialog.getLoc()+" " +listCheckDialog.getType()+" "+listCheckDialog.getBuschk()+" "+ listCheckDialog.getCCTVchk());

                boolean cctv = listCheckDialog.getCCTVchk();
                boolean bus = listCheckDialog.getBuschk();
                sqlloc = listCheckDialog.getLoc();
                sqltype = listCheckDialog.getType();
                if (bus = true)
                    sqlbus = "\"Y\"";
                else
                    sqlbus = "\"N\"OR bus = \"N\"";
                if (cctv = true)
                    sqlcctv = ">";
                else
                    sqlcctv= "=";
                listCheckDialog.dismiss();
                getData(sqlloc,sqltype,sqlbus,sqlcctv);
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listCheckDialog.dismiss();
            }
        });
        listCheckDialog.show();
    }
    userInfo userinfo;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //SchoolInfoActivity = new Intent(this, School_InfoActivity.class);
        personList = new ArrayList<HashMap<String, String>>();
        kinderinfolist = new ArrayList<kinderinfoclass>();
        userinfo = (userInfo)getIntent().getSerializableExtra("userinfo");

        setContentView(R.layout.listactivity);

        listCheckDialog = new ListCheckDialog(this, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("value", listCheckDialog.getLoc() + " " + listCheckDialog.getType() + " " + listCheckDialog.getBuschk() + " " + listCheckDialog.getCCTVchk());

                boolean cctv = listCheckDialog.getCCTVchk();
                boolean bus = listCheckDialog.getBuschk();
                sqlloc = listCheckDialog.getLoc();
                sqltype = listCheckDialog.getType();
                if (bus = true)
                    sqlbus = "true";
                else
                    sqlbus = "false";
                if (cctv = true)
                    sqlcctv = "true";
                else
                    sqlcctv = "false";
                listCheckDialog.dismiss();

                searchEdit = (EditText) findViewById(R.id.list_searchEdit);
                searchEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {

                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        switch (actionId) {
                            case EditorInfo.IME_ACTION_SEARCH:
                                getData2(searchEdit.getText().toString());
                                break;
                            default:
                                return false;
                        }
                        return true;
                    }
                });
                getData(sqlloc, sqltype, sqlbus, sqlcctv);
            }
        }

                , new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                listCheckDialog.dismiss();

                searchEdit = (EditText) findViewById(R.id.list_searchEdit);
                searchEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {

                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        switch (actionId) {
                            case EditorInfo.IME_ACTION_SEARCH:
                                getData2(searchEdit.getText().toString());
                                break;
                            default:
                                return false;
                        }
                        return true;
                    }
                });
            }
        }

        );
        listCheckDialog.show();
    }

    public void onStart() {
        super.onStart();
    }


    protected void showList() {
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            peoples = jsonObj.getJSONArray(TAG_RESULTS);
            kinderinfolist.clear();

            for (int i = 0; i < peoples.length(); i++) {
                JSONObject c = peoples.getJSONObject(i);
                String id = c.getString(TAG_ID);
                String name = c.getString(TAG_NAME);
                String kindertype = c.getString(TAG_KINDERTYPE);
                String phone = c.getString(TAG_PHONE);
                String address = c.getString(TAG_ADD);
                String childlimit = c.getString(TAG_CHILDLIMIT);
                String cctv = c.getString(TAG_CCTV);
                String bus = c.getString(TAG_BUS);

                kinderinfolist.add(new kinderinfoclass(id,name,phone,kindertype,address,childlimit,cctv,bus));
            }

            list = (ListView)findViewById(R.id.listView);
            ListActivityAdapter adapter = new ListActivityAdapter(this,kinderinfolist,userinfo);
            list.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void getData(String loc,String type,String bus, String cctv) {
        class GetDataJSON extends AsyncTask<String, Void, String> {
            ProgressDialog loading;

            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ListsActivity.this, "Please Wait", null, true, true);
            }

            protected String doInBackground(String... params) {
                BufferedReader bufferedReader = null;
                try {
                    //현재위치 php에 저장
                    String loc = (String) params[0];
                    String type = (String) params[1];
                    String bus = (String) params[2];
                    String cctv = (String) params[3];

                    String link = "http://bluecarnival.cafe24.com/kindergarten/getlistdata.php";
                    String data = URLEncoder.encode("loc", "UTF-8") + "=" + URLEncoder.encode(loc, "UTF-8");
                    data += "&" + URLEncoder.encode("type", "UTF-8") + "=" + URLEncoder.encode(type, "UTF-8");
                    data += "&" + URLEncoder.encode("bus", "UTF-8") + "=" + URLEncoder.encode(bus, "UTF-8");
                    data += "&" + URLEncoder.encode("cctv", "UTF-8") + "=" + URLEncoder.encode(cctv, "UTF-8");

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
                showList();
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute(loc,type,bus,cctv);
    }

    public void getData2(String search) {
        class GetDataJSON extends AsyncTask<String, Void, String> {
            ProgressDialog loading;

            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ListsActivity.this, "Please Wait", null, true, true);
            }

            protected String doInBackground(String... params) {
                BufferedReader bufferedReader = null;
                try {
                    //현재위치 php에 저장
                    String search = (String) params[0];

                    String link = "http://bluecarnival.cafe24.com/kindergarten/getlistdata2.php";
                    String data = URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(search, "UTF-8");

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
                showList();
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute(search);
    }

}


