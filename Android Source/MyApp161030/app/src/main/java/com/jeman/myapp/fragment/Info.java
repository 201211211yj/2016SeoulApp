package com.jeman.myapp.fragment;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jeman.myapp.CustomDialog;

import com.jeman.myapp.LoginActivity;
import com.jeman.myapp.MainActivity;
import com.jeman.myapp.R;
import com.jeman.myapp.School_Info_Activity;
import com.jeman.myapp.Update_info_password;
import com.jeman.myapp.userInfo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class Info extends Fragment {
    boolean bLog = true; // true : 로그아웃 상태

    public userInfo getUserinfo() {
        return userinfo;
    }

    Bundle bundle;
    userInfo userinfo;
    TextView textView;
    EditText name;
    EditText childname;
    CustomDialog dialog;
    public static Info newInstance(userInfo info) {

        Bundle args = new Bundle();
        args.putSerializable("INFO_PARAM",info);
        Info fragment = new Info();
        fragment.setArguments(args);
        return fragment;
    }
    public Info(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userinfo = (userInfo) getArguments().getSerializable("INFO_PARAM");
        }
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
        View rootView = inflater.inflate(R.layout.myinfo_activity, container, false);

        Button button1 = (Button) rootView.findViewById(R.id.btn_logout);
        Button button_password = (Button)rootView.findViewById(R.id.myinfo_password_btn);
        Button button_myschool = (Button)rootView.findViewById(R.id.myinfo_myschool_btn);
        Button button_update = (Button)rootView.findViewById(R.id.myinfo_infoupdt_btn);
        button1.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                dialog = new CustomDialog(getActivity(), "", "로그아웃을 하시겠습니까?", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bLog = true;
                        Toast.makeText(getActivity(), "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();

                        Intent update = new Intent(getActivity(), LoginActivity.class);

                        update.putExtra("logout",true);
                        startActivity(update);

                        getActivity().finish();
                        dialog.dismiss();
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();

            }
        });
        button_myschool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), School_Info_Activity.class);
                intent.putExtra("userinfo",userinfo);
                intent.putExtra("schoolid",userinfo.getKinderid());
                startActivity(intent);
            }
        });
        button_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Update_info_password.class);
                intent.putExtra("userinfo",userinfo);
                startActivity(intent);
            }
        });
        button_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new CustomDialog(getActivity(), "", "정보수정을 하시겠습니까?", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updateData(userinfo.getEmail(),name.getText().toString(),childname.getText().toString());
                        dialog.dismiss();
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
        textView = (TextView)rootView.findViewById(R.id.myinfo_email);
        textView.setText(userinfo.getEmail());
        name = (EditText)rootView.findViewById(R.id.myinfo_name);
        name.setText(userinfo.getName());
        childname = (EditText)rootView.findViewById(R.id.myinfo_childname);
        childname.setText(userinfo.getChildname());

        return rootView;
    }

    public void updateData(String myparameter1, final String myparameter2, final String myparameter3) {
        class GetDataJSON extends AsyncTask<String, Void, String> {
            ProgressDialog loading;

            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(getActivity(), "Please Wait", null, true, true);
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

                    String link = "http://bluecarnival.cafe24.com/kindergarten/updateinfo.php";
                    String data = URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(myparam1, "UTF-8");
                    data += "&" + URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(myparam2, "UTF-8");
                    data += "&" + URLEncoder.encode("child_name", "UTF-8") + "=" + URLEncoder.encode(myparam3, "UTF-8");

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
                if (result.contains("failure"))
                    Toast.makeText(getActivity(), "정보 수정에 실패하였습니다.", Toast.LENGTH_SHORT);
                else {
                    Toast.makeText(getActivity(), "정보 수정이 되었습니다.", Toast.LENGTH_SHORT);
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    userinfo.setName(myparameter2);
                    userinfo.setChildname(myparameter3);
                    intent.putExtra("userinfo",userinfo);
                    startActivity(intent);
                    getActivity().finish();
                }
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute(myparameter1,myparameter2,myparameter3);
    }
}
