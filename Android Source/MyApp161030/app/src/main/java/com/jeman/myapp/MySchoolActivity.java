package com.jeman.myapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

public class MySchoolActivity extends FragmentActivity implements View.OnClickListener {
    final String TAG = "FragmentActivity";

    final int NOTICE_EDIT_RESULT_CODE = 33;
    ArrayList<String> info_context;

    private static final String TAG_RESULTS = "result";

    private static final String TAG_ID = "id";
    private static final String TAG_NAME = "name";
    private static final String TAG_KINDERTYPE = "kindertype";
    private static final String TAG_CHILDLIMIT = "childlimit";
    private static final String TAG_TEACHER = "teacher";
    private static final String TAG_ADDRESS = "address";
    private static final String TAG_PHONE = "phone";
    private static final String TAG_ROOM = "room";
    private static final String TAG_PARK = "park";
    private static final String TAG_CCTV = "cctv";
    private static final String TAG_BUS = "bus";
    private static final String TAG_HOMPAGE = "hompage";

    String myJSON;
    JSONArray peoples = null;


    int mCurrentFragmentIndex;
    public final static int FRAGMENT_ONE = 0;
    public final static int FRAGMENT_TWO = 1;
    public final static int FRAGMENT_THREE = 2;
    public final static int FRAGMENT_FOUR = 3;

    public Button bt_oneFragment;
    public Button bt_twoFragment;
    public Button bt_threeFragment;
    public Button bt_fourFragment;

    public String school_name;
    public Button close_btn;
    public Button info_btn;
    userInfo userinfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myschool_activity);

        userinfo = (userInfo) getIntent().getSerializableExtra("MySchool_userinfo");

        if (userinfo.getKinderid() == "NULL" || userinfo.isReady_register())
            finish(); //2016-09-21

        getData(userinfo.getKinderid());
        close_btn = (Button)findViewById(R.id.myschool_closebtn);
        info_btn = (Button)findViewById(R.id.myschool_infobtn);
        close_btn.setOnClickListener(this);
        info_btn.setOnClickListener(this);
        bt_oneFragment = (Button) findViewById(R.id.act_2nd_bt1);
        bt_oneFragment.setOnClickListener(this);
        bt_twoFragment = (Button) findViewById(R.id.act_2nd_bt2);
        bt_twoFragment.setOnClickListener(this);
        bt_threeFragment = (Button) findViewById(R.id.act_2nd_bt3);
        bt_threeFragment.setOnClickListener(this);


        mCurrentFragmentIndex = FRAGMENT_ONE;

        fragmentReplace(mCurrentFragmentIndex);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.v("requestCode : ",String.valueOf(requestCode));
        Log.v("resultCode : ",String.valueOf(resultCode));
        if (requestCode == NOTICE_EDIT_RESULT_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Fragment newFragment;
                Log.d(TAG, "fragmentReplace " + FRAGMENT_TWO);

                newFragment = getFragment(FRAGMENT_TWO);
                // replace fragment
                final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.l2_fragment, newFragment);
                transaction.commitAllowingStateLoss();
            }
        }
    }

    public void fragmentReplace(int reqNewFragmentIndex) {
        Fragment newFragment;
        Log.d(TAG, "fragmentReplace " + reqNewFragmentIndex);
        newFragment = getFragment(reqNewFragmentIndex);
        // replace fragment
        final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.l2_fragment, newFragment);

        // Commit the transaction
        transaction.commit();
    }

    private Fragment getFragment(int idx) {
        Fragment newFragment = null;
        switch (idx) {
            case FRAGMENT_ONE:
                newFragment = MySchool_Diary.newInstance(userinfo);
                break;
            case FRAGMENT_TWO:
                newFragment = MySchool_Notice.newInstance(userinfo);
                break;
            case FRAGMENT_THREE:
                newFragment = MySchool_Dailymenu.newInstance(userinfo);
                break;
            case FRAGMENT_FOUR:
                //           newFragment = new DiaryInputFragment();
                break;
            default:
                Log.d(TAG, "Unhandle case");
                break;
        }
        return newFragment;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.act_2nd_bt1:
                mCurrentFragmentIndex = FRAGMENT_ONE;
                fragmentReplace(mCurrentFragmentIndex);
                bt_oneFragment.setBackgroundResource(R.drawable.ic_alimzang_click);
                bt_twoFragment.setBackgroundResource(R.drawable.ic_gongzi_unclick);
                bt_threeFragment.setBackgroundResource(R.drawable.ic_sickdan_unclick);
                break;
            case R.id.act_2nd_bt2:
                mCurrentFragmentIndex = FRAGMENT_TWO;
                fragmentReplace(mCurrentFragmentIndex);
                bt_oneFragment.setBackgroundResource(R.drawable.ic_alimzang_unclick);
                bt_twoFragment.setBackgroundResource(R.drawable.ic_gongzi_click);
                bt_threeFragment.setBackgroundResource(R.drawable.ic_sickdan_unclick);
                break;
            case R.id.act_2nd_bt3:
                mCurrentFragmentIndex = FRAGMENT_THREE;
                fragmentReplace(mCurrentFragmentIndex);
                bt_oneFragment.setBackgroundResource(R.drawable.ic_alimzang_unclick);
                bt_twoFragment.setBackgroundResource(R.drawable.ic_gongzi_unclick);
                bt_threeFragment.setBackgroundResource(R.drawable.ic_sickdan_click);
                break;

            case R.id.myschool_infobtn :
                Intent intent = new Intent(this,School_Info_Activity.class);
                intent.putExtra("userinfo",userinfo);
                intent.putExtra("schoolid",userinfo.getKinderid());
                intent.putExtra("schoolname",school_name);
                startActivity(intent);
                break;
            case R.id.myschool_closebtn :
                finish();
                break;
        }
    }

    protected void showList() {
        try {

            Log.v("GetDataStart","true");
            JSONObject jsonObj = new JSONObject(myJSON);
            peoples = jsonObj.getJSONArray(TAG_RESULTS);

            for (int i = 0; i < peoples.length(); i++) {
                JSONObject c = peoples.getJSONObject(i);

                info_context = new ArrayList<String>();

                info_context.add(c.getString(TAG_ID));
                info_context.add(c.getString(TAG_NAME));
                info_context.add(c.getString(TAG_KINDERTYPE));
                info_context.add(c.getString(TAG_CHILDLIMIT));
                info_context.add(c.getString(TAG_TEACHER));
                info_context.add(c.getString(TAG_ADDRESS));
                info_context.add(c.getString(TAG_PHONE));
                info_context.add(c.getString(TAG_ROOM));
                info_context.add(c.getString(TAG_PARK));
                info_context.add(c.getString(TAG_CCTV));
                info_context.add(c.getString(TAG_BUS));
                info_context.add(c.getString(TAG_HOMPAGE));
            }
            TextView textview = (TextView)findViewById(R.id.myschool_name);
            textview.setText(info_context.get(1));
            school_name = info_context.get(1);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    public void getData(String id) {
        class GetDataJSON extends AsyncTask<String, Void, String> {

            protected void onPreExecute() {
                super.onPreExecute();
            }

            protected String doInBackground(String... params) {
                BufferedReader bufferedReader = null;
                try {
                    //현재위치 php에 저장
                    String id = (String) params[0];

                    String link = "http://bluecarnival.cafe24.com/kindergarten/getinfodata.php";
                    String data = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8");

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
                showList();
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute(id);
    }

}
