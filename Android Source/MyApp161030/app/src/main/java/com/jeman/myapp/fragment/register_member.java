package com.jeman.myapp.fragment;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.jeman.myapp.R;
import com.jeman.myapp.adapter.NoticeListAdapter;
import com.jeman.myapp.adapter.registermemberAdapter;
import com.jeman.myapp.adapter.infoclass;
import com.jeman.myapp.userInfo;

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
 * Created by 신영준 on 2016-09-24.
 */public class register_member extends Fragment {
    userInfo userinfo;
    ListView listView;
    ArrayList<infoclass> infoList;

    String myJSON;
    JSONArray peoples = null;

    private static final String TAG_RESULTS = "result";
    private static final String TAG_EMAIL = "email";
    private static final String TAG_CHILD_NAME = "child_name";

    public static register_member newInstance(userInfo info) {

        Bundle args = new Bundle();
        args.putSerializable("INFO_PARAM",info);
        register_member fragment = new register_member();
        fragment.setArguments(args);
        return fragment;
    }
    public register_member(){

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        infoList = new ArrayList<infoclass>();
        if (getArguments() != null) {
            userinfo = (userInfo) getArguments().getSerializable("INFO_PARAM");
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
        View rootView = inflater.inflate(R.layout.register_members, container, false);

        getData(userinfo.getKinderid());

        listView = (ListView)rootView.findViewById(R.id.regimember_listView);

        return rootView;
    }

    protected void showList() {
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            peoples = jsonObj.getJSONArray(TAG_RESULTS);

            for (int i = 0; i < peoples.length(); i++) {
                JSONObject c = peoples.getJSONObject(i);
                String email = c.getString(TAG_EMAIL);
                String child_name = c.getString(TAG_CHILD_NAME);

                infoList.add(new infoclass(email,child_name));
            }

            listView.setAdapter(new registermemberAdapter(getActivity(),infoList,userinfo));

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

                    String link = "http://bluecarnival.cafe24.com/kindergarten/member/getuserlist.php";
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
