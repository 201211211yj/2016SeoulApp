package com.jeman.myapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by 신영준 on 2016-09-11.
 */
public class School_Info_Activity extends Activity{


    final int REQ_CODE_SELECT_IMAGE=100;

    Bitmap [] bitmap;
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


    String myJSON2;
    JSONArray peoples2 = null;
    ArrayList<String> imgURLList;

    private static final String TAG_RESULTS2 = "result";
    private static final String TAG_IMG_SOURCE = "img_source";

    String myJSON3;

    String myJSON4;

    String id;
    TextView name;
    userInfo userinfo;

    HorizontalScrollView g;
    LinearLayout imageLayout;

    Button regibtn;
    Button exitbtn;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.school_info_activity);

        userinfo = (userInfo)getIntent().getSerializableExtra("userinfo");
        id = getIntent().getStringExtra("schoolid");
        name = (TextView)findViewById(R.id.school_info_name);
        name.setText(getIntent().getStringExtra("schoolname"));
        // adapterView
        g = (HorizontalScrollView)findViewById(R.id.school_info_scrollview);
        imageLayout = new LinearLayout(this);

        imageLayout.setOrientation(LinearLayout.HORIZONTAL);

        imgURLList = new ArrayList<String>();
        regibtn = (Button)findViewById(R.id.school_info_regibtn);
        regibtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getRegisterData(userinfo.getEmail(),id);
            }
        });
        exitbtn = (Button)findViewById(R.id.school_info_exitbtn);
        exitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getData(id);
        getImage(id);

    } // end of onCreate

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Toast.makeText(getBaseContext(), "resultCode : "+resultCode,Toast.LENGTH_SHORT).show();
        if(requestCode == REQ_CODE_SELECT_IMAGE)
        {
            if(resultCode==Activity.RESULT_OK)
            {
                try {
                    //Uri에서 이미지 이름을 얻어온다.
                    final String name_Str = getImageNameToUri(data.getData());
                    //이미지 데이터를 비트맵으로 받아온다.

                    final Bitmap image_bitmap =  MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());

                    Thread uploadthread = new Thread() {
                        public void run() {
                            try {
                                FTPClient mFTP = new FTPClient();
                                Log.v("ftpstart","true");
                                mFTP.enterLocalPassiveMode();
                                mFTP.connect("bluecarnival.cafe24.com", 21);  // ftp로 접속
                                mFTP.login("bluecarnival", "sa!@22996065"); // ftp 로그인 계정/비번
                                mFTP.setFileType(FTP.BINARY_FILE_TYPE); // 바이너리 파일
                                mFTP.setBufferSize(1024 * 1024); // 버퍼 사이즈
                                mFTP.enterLocalPassiveMode();

                                // 업로드 경로 수정 (선택 사항 )

                                mFTP.cwd("www"); // ftp 상의 업로드 디렉토리
                                mFTP.cwd("kindergarten");
                                mFTP.cwd("infoimage");
                                mFTP.mkd(id); // public아래로 files 디렉토리를 만든다
                                mFTP.cwd(id); // public/files 로 이동 (이 디렉토리로 업로드가 진행)

                                File file = new File(name_Str);
                                FileInputStream fis = new FileInputStream(file);
                                Date date = new Date();
                                String root = String.valueOf(date.getTime());
                                insertDBImage("http://bluecarnival.cafe24.com/kindergarten/infoimage/"+id+"/"+root, id);
                                mFTP.storeFile(root,fis);
                                mFTP.disconnect(); // ftp disconnect

                            } catch (SocketException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            } catch (IOException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                    };
                    uploadthread.start();
                    uploadthread.join();
                    //여기에다 업로드 코드 넣고 업로드 실패시 addView 안되게

                    recreate();

                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    public String getImageNameToUri(Uri data) {
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor =  getContentResolver().query(data, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        cursor.moveToFirst();

        String imgPath = cursor.getString(column_index);
        String imgName = imgPath.substring(imgPath.lastIndexOf("/")+1);
        Log.v("imgPath",imgPath);
        Log.v("imgName",imgName);

        return imgPath;
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
            name.setText(info_context.get(1));
            for(int i =0; i<10;i++){
                TextView textview = (TextView)findViewById(R.id.school_info_kindertype+i);
                textview.setText(info_context.get(2+i));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void getData(String id) {
        class GetDataJSON extends AsyncTask<String, Void, String> {
            ProgressDialog loading;

            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(School_Info_Activity.this, "Please Wait", null, true, true);
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
                loading.dismiss();
                super.onPostExecute(result);
                Log.v("JSON Result :",result);
                myJSON = result;
                showList();
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute(id);
    }

    protected void showImg() {
        try {
            JSONObject jsonObj = new JSONObject(myJSON2);
            peoples2 = jsonObj.getJSONArray(TAG_RESULTS2);
            for (int i = 0; i < peoples2.length(); i++) {
                JSONObject c = peoples2.getJSONObject(i);
                imgURLList.add(c.getString(TAG_IMG_SOURCE));
            }
            bitmap = new Bitmap[imgURLList.size()];
            Thread [] imgthread = new Thread[imgURLList.size()];

            for(int i = 0; i<imgURLList.size();i++) {
                final int finalI = i;
                imgthread[i] = new Thread() {
                    public void run() {
                        try {
                            URL url = new URL(imgURLList.get(finalI));
                            HttpURLConnection con = (HttpURLConnection)url.openConnection();
                            con.setDoInput(true);
                            con.connect();
                            InputStream is = con.getInputStream();
                            bitmap[finalI] = BitmapFactory.decodeStream(is);
                        } catch(IOException e){
                            Log.v("IOException","true");
                        }
                    }
                };

                imgthread[i].start();
                imgthread[i].join();

                ImageView imageView = new ImageView(this);
                imageView.setImageBitmap(bitmap[i]);
                imageView.setLayoutParams(new HorizontalScrollView.LayoutParams(150, 100));
                imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                imageView.setOnClickListener(new View.OnClickListener() {
                    ImageViewDialog dialog;
                    @Override
                    public void onClick(View v) {
                        dialog = new ImageViewDialog(School_Info_Activity.this, bitmap[finalI], new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                deleteImage(imgURLList.get(finalI));
                                dialog.dismiss();
                                recreate();
                                //imageLayout.removeViewAt(finalI);
                                //g.invalidate();
                            }
                        }, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        },userinfo.isTeacher());
                        dialog.show();
                    }
                 //   Button button = (Button)dialog.findViewById(R.id.imgdialog_btn_left);
                });

                imageLayout.addView(imageView);
            }
            if(userinfo.getKinderid().equals(id)&&userinfo.isTeacher()) {
                ImageView plusimg = new ImageView(this);
                plusimg.setImageResource(R.drawable.ic_add);
                plusimg.setLayoutParams(new HorizontalScrollView.LayoutParams(150, 100));
                plusimg.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                plusimg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_PICK);
                        intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
                        intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent, REQ_CODE_SELECT_IMAGE);
                    }
                });
                imageLayout.addView(plusimg);
                g.addView(imageLayout);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void getImage(String id) {
        class GetDataJSON extends AsyncTask<String, Void, String> {
            ProgressDialog loading;

            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(School_Info_Activity.this, "Please Wait", null, true, true);
            }

            protected String doInBackground(String... params) {
                BufferedReader bufferedReader = null;
                try {
                    //현재위치 php에 저장
                    String id = (String) params[0];

                    String link = "http://bluecarnival.cafe24.com/kindergarten/getinfoimage.php";
                    String data = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8");
                    Log.v("getimageid",id);
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
                myJSON2= result;
                showImg();
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute(id);
    }

    public void deleteImage(String URL){
        class GetDataJSON extends AsyncTask<String, Void, String> {

            protected void onPreExecute() {
                super.onPreExecute();
            }

            protected String doInBackground(String... params) {
                BufferedReader bufferedReader = null;
                try {
                    //현재위치 php에 저장
                    String img_source = (String) params[0];

                    String link = "http://bluecarnival.cafe24.com/kindergarten/deleteinfoimage.php";
                    String data = URLEncoder.encode("img_source", "UTF-8") + "=" + URLEncoder.encode(img_source, "UTF-8");
                    Log.v("getimageid",img_source);
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
                myJSON3= result;
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute(URL);
    }

    public void insertDBImage(String URL, String id){
        class GetDataJSON extends AsyncTask<String, Void, String> {

            protected void onPreExecute() {
                super.onPreExecute();
            }

            protected String doInBackground(String... params) {
                BufferedReader bufferedReader = null;
                try {
                    //현재위치 php에 저장
                    String img_source = (String) params[0];
                    String id = (String) params[1];

                    String link = "http://bluecarnival.cafe24.com/kindergarten/insertinfoimage.php";
                    String data = URLEncoder.encode("img_source", "UTF-8") + "=" + URLEncoder.encode(img_source, "UTF-8");
                    data += "&" + URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8");
                    Log.v("insert image source",img_source);
                    Log.v("insert image id",id);
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
                Log.v("insert JSON Result :",result);
                myJSON4= result;
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute(URL,id);
    }

    public void getRegisterData(String email,String kinder_id) {
        class GetDataJSON extends AsyncTask<String, Void, String> {
            ProgressDialog loading;

            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(School_Info_Activity.this, "Please Wait", null, true, true);
            }

            protected String doInBackground(String... params) {
                BufferedReader bufferedReader = null;
                try {
                    //현재위치 php에 저장
                    String email = (String) params[0];
                    String kinder_id = (String) params[1];

                    String link = "http://bluecarnival.cafe24.com/kindergarten/member/ready_register.php";
                    String data = URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8");
                    data += "&" + URLEncoder.encode("kinder_id", "UTF-8") + "=" + URLEncoder.encode(kinder_id, "UTF-8");

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
                Log.v("JSON register Result :",result);
                myJSON = result;
                if(result.equals("\"cancel\""))
                    Toast.makeText(getBaseContext(), "이미 어린이집을 등록하였거나\n등록신청중 입니다.",Toast.LENGTH_SHORT).show();
                else if(result.equals("\"failure\""))
                    Toast.makeText(getBaseContext(), "등록에 실패하였습니다. \n다시 시도해주시기 바랍니다.",Toast.LENGTH_SHORT).show();
                else if(result.equals("\"success\""))
                    Toast.makeText(getBaseContext(), "등록신청 되었습니다. \n교사가 확인하면 이용 가능합니다.",Toast.LENGTH_SHORT).show();

            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute(email, kinder_id);
    }

} // end of class

