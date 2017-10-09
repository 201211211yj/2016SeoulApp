package com.jeman.myapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jeman.myapp.CustomDialog;
import com.jeman.myapp.MySchool_Notice_Edit;
import com.jeman.myapp.R;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;


public class NoticeListAdapter extends BaseExpandableListAdapter {

    final int NOTICE_EDIT_RESULT_CODE = 33;

    private Context context;
    private ArrayList<String> groupList = null;
    private ArrayList<String> childList = null;
    private ArrayList<String> noticeList = null;
    private LayoutInflater inflater = null;
    private ViewHolder viewHolder = null;
    private ViewHolder childviewHolder = null;
    private String noticeID;
    private CustomDialog mCustomDialog;
    private boolean teacher_bool;
    public NoticeListAdapter(Context c, ArrayList<String> groupList,
                             ArrayList<String> childList, boolean teacher_bool){
        super();
        this.context = c;
        this.inflater = LayoutInflater.from(c);
        this.groupList = groupList;
        this.childList = childList;
        this.teacher_bool = teacher_bool;
    }
    public NoticeListAdapter(Context c, ArrayList<String> groupList,
                             ArrayList<String> childList, ArrayList<String> noticeList, boolean teacher_bool){
        super();
        this.context = c;
        this.inflater = LayoutInflater.from(c);
        this.groupList = groupList;
        this.childList = childList;
        this.noticeList = noticeList;
        this.teacher_bool = teacher_bool;
    }


    // 그룹 포지션을 반환한다.
    @Override
    public String getGroup(int groupPosition) {
        return groupList.get(groupPosition);
    }

    // 그룹 사이즈를 반환한다.
    @Override
    public int getGroupCount() {
        return groupList.size();
    }

    // 그룹 ID를 반환한다.
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    // 그룹뷰 각각의 ROW
    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded,
                             final View convertView, final ViewGroup parent) {

        View v = convertView;

        if(v == null){
            viewHolder = new ViewHolder();
            v = inflater.inflate(R.layout.myschool_notice_listitem, parent, false);
            viewHolder.tv_groupName = (TextView) v.findViewById(R.id.notice_listitem_group);
            viewHolder.iv_image = (ImageView) v.findViewById(R.id.iv_image);
            viewHolder.editbutton=(Button)v.findViewById(R.id.notice_listitem_editbtn);
            if (teacher_bool==false)
                viewHolder.editbutton.setVisibility(View.INVISIBLE);
            viewHolder.editbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(teacher_bool == true) {
                        mCustomDialog = new CustomDialog(context,
                                null, // 제목
                                "내용을 수정하시겠습니까?", // 내용
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent edit = new Intent(context, MySchool_Notice_Edit.class);
                                        edit.putExtra("notice_id", noticeList.get(groupPosition));
                                        ((Activity)context).startActivityForResult(edit,NOTICE_EDIT_RESULT_CODE);
                                        mCustomDialog.dismiss();

                                    }
                                }, // 왼쪽 버튼 이벤트
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        mCustomDialog.dismiss();
                                    }
                                }); // 오른쪽 버튼 이벤트
                        mCustomDialog.show();
                    }
                    else
                        Toast.makeText(context, "교사만 가능합니다.", Toast.LENGTH_SHORT).show();
                }
            });
            if (teacher_bool==false)
                viewHolder.delbutton.setVisibility(View.INVISIBLE);
            viewHolder.delbutton=(Button)v.findViewById(R.id.notice_listitem_delbtn);

            viewHolder.delbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (teacher_bool) {
                        mCustomDialog = new CustomDialog(context,
                                null, // 제목
                                "삭제하시겠습니까?", // 내용
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        deleteData(noticeList.get(groupPosition));
                                        //Log.v("myJSON after deleteData",myJSON);
                                        //if (myJSON.equals("success")) {
                                            groupList.remove(groupPosition);
                                            childList.remove(groupPosition);
                                            noticeList.remove(groupPosition);
                                            notifyDataSetInvalidated();
                                        //}
                                        mCustomDialog.dismiss();
                                    }
                                }, // 왼쪽 버튼 이벤트
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        mCustomDialog.dismiss();
                                    }
                                }); // 오른쪽 버튼 이벤트
                        mCustomDialog.show();
                    }else
                        Toast.makeText(context, "교사만 가능합니다.", Toast.LENGTH_SHORT).show();//2016-09-21
                }
            });
            v.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)v.getTag();
        }

        // 그룹을 펼칠때와 닫을때 아이콘을 변경해 준다.
        if(isExpanded){
            viewHolder.iv_image.setImageResource(R.drawable.ic_down);
        }else{
            viewHolder.iv_image.setImageResource(R.drawable.ic_right);
        }

        viewHolder.tv_groupName.setText(getGroup(groupPosition));

        return v;
    }

    // 차일드뷰를 반환한다.
    @Override
    public String getChild(int groupPosition, int childPosition) {
        return childList.get(groupPosition);
    }

    // 차일드뷰 사이즈를 반환한다.
    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    // 차일드뷰 ID를 반환한다.
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    // 차일드뷰 각각의 ROW
    @Override
    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        View v = convertView;

        if(v == null){
            childviewHolder = new ViewHolder();
            v = inflater.inflate(R.layout.myschool_notice_listitemchild, null);
            childviewHolder.tv_childName = (TextView) v.findViewById(R.id.notice_listitem_child);
            v.setTag(childviewHolder);
        }else{
            childviewHolder = (ViewHolder)v.getTag();
        }

        childviewHolder.tv_childName.setText(getChild(groupPosition, childPosition));

        return v;
    }

    @Override
    public boolean hasStableIds() { return true; }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) { return true; }

    String myJSON;

    public void deleteData(String notice_id) {

        class GetDataJSON extends AsyncTask<String, Void, String> {

            protected String doInBackground(String... params) {
                BufferedReader bufferedReader = null;
                try {
                    //현재위치 php에 저장
                    String id_param = (String) params[0];
                    Log.v("JSON id_param :",id_param);

                    String link = "http://bluecarnival.cafe24.com/kindergarten/notice/delnoticedata.php";
                    String data = URLEncoder.encode("notice_id", "UTF-8") + "=" + URLEncoder.encode(id_param, "UTF-8");

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

            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute(notice_id);
    }

    class ViewHolder {
        public ImageView iv_image;
        public Button editbutton;
        public Button delbutton;
        public TextView tv_groupName;
        public TextView tv_childName;

    }

}
