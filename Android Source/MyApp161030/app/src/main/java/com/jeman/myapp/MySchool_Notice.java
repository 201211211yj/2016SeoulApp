package com.jeman.myapp;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.jeman.myapp.adapter.NoticeListAdapter;


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
 * Created by 신영준 on 2016-09-13.
 */
public class MySchool_Notice extends Fragment {

    String myJSON;

    final int NOTICE_EDIT_RESULT_CODE = 33;

    private static final String TAG_RESULTS = "result";
    private static final String TAG_NOTICE_TITLE = "notice_title";
    private static final String TAG_NOTICE_TEXT = "notice_text";
    private static final String TAG_NOTICE_ID = "notice_id";

    JSONArray peoples = null;
    ArrayList<HashMap<String, String>> personList;
    CustomDialog mCustomDialog;
    userInfo userinfo;
    String noticeID;
    public static MySchool_Notice newInstance(userInfo userinfo) {

        Bundle args = new Bundle();

        MySchool_Notice fragment = new MySchool_Notice();
        args.putSerializable("INFO_PARAM",userinfo);
        fragment.setArguments(args);
        return fragment;
    }
    public MySchool_Notice(){

    }
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userinfo = (userInfo) getArguments().getSerializable("INFO_PARAM");
        }
    }


    private ArrayList<String> mGroupList = null;
    private ArrayList<String> mChildList = null;

    private ArrayList<String> mIdList = null;

    private ExpandableListView mListView;

    private Button newbutton;

    @Override


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.myschool_notice, container, false);

        mListView = (ExpandableListView)v.findViewById(R.id.notice_ExpList);
        newbutton = (Button)v.findViewById(R.id.notice_newbtn);
        if (userinfo.isTeacher()==false)
            newbutton.setVisibility(View.INVISIBLE);
        newbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userinfo.isTeacher()) {
                    Intent edit = new Intent(getActivity(), MySchool_Notice_Edit.class);
                    edit.putExtra("kinder_id", userinfo.getKinderid());
                    edit.putExtra("notice_id", "");
                    getActivity().startActivityForResult(edit,NOTICE_EDIT_RESULT_CODE);
                }
                else
                    Toast.makeText(getActivity(), "교사만 가능합니다.", Toast.LENGTH_SHORT).show();
            }//2016-09-21
        });
        super.onCreate(savedInstanceState);

        mGroupList = new ArrayList<String>();
        mChildList = new ArrayList<String>();
        mIdList = new ArrayList<String>();

        getData(userinfo.getKinderid());
        mListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return false;
            }
        });
        mListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                return false;
            }
        });

        mListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
            }
        });
        mListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
            }
        });
        return v;
    }
    protected void showList() {
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            peoples = jsonObj.getJSONArray(TAG_RESULTS);

            for (int i = 0; i < peoples.length(); i++) {
                JSONObject c = peoples.getJSONObject(i);
                String title = c.getString(TAG_NOTICE_TITLE);
                String text = c.getString(TAG_NOTICE_TEXT);
                String id = c.getString(TAG_NOTICE_ID);
                HashMap<String, String> persons = new HashMap<String, String>();

                mGroupList.add(title);
                mChildList.add(text);
                mIdList.add(id);
            }

            mListView.setAdapter(new NoticeListAdapter(getActivity(), mGroupList, mChildList,mIdList,userinfo.isTeacher()));

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void getData(String kinder_id) {
        class GetDataJSON extends AsyncTask<String, Void, String> {

            protected String doInBackground(String... params) {
                BufferedReader bufferedReader = null;
                try {
                    //현재위치 php에 저장
                    String id_param = (String) params[0];
                    Log.v("JSON id_param :",id_param);

                    String link = "http://bluecarnival.cafe24.com/kindergarten/notice/getnoticedata.php";
                    String data = URLEncoder.encode("kinder_id", "UTF-8") + "=" + URLEncoder.encode(id_param, "UTF-8");

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
            @Override
            protected void onPostExecute(String result) {

                myJSON = result;
                Log.v("Result",result);
                showList();
            }

        }
        GetDataJSON g = new GetDataJSON();
        g.execute(kinder_id);
    }

}
