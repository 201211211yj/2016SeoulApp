package com.jeman.myapp;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.jeman.myapp.adapter.SlidingMenuAdapter;
import com.jeman.myapp.fragment.Home;
import com.jeman.myapp.fragment.Info;
import com.jeman.myapp.fragment.Settings;
import com.jeman.myapp.fragment.register_member;
import com.jeman.myapp.model.ItemSlideMenu;

import java.util.ArrayList;
import java.util.List;



public class MainActivity extends AppCompatActivity {
    boolean bLog = true; // true : 로그아웃 상태

    private List<ItemSlideMenu> listSliding;
    private SlidingMenuAdapter adapter;
    private ListView listViewSliding;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private BackPressCloseHandler backPressCloseHandler;

    public userInfo userinfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(Color.RED);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        userinfo = (userInfo) getIntent().getSerializableExtra("userinfo");
        Log.v("userinfoinput",userinfo.getName());


        if(userinfo.getDate().equals("guest"))
            bLog = true;
        else
            bLog = false;

        //Back button two touch for Exit
        backPressCloseHandler = new BackPressCloseHandler(this);
        //Init Component
        listViewSliding = (ListView)findViewById(R.id.lv_sliding_menu);
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        listSliding = new ArrayList<>();

        //Add item for sliding list
        listSliding.add(new ItemSlideMenu(R.drawable.ic_home, "HOME"));
        listSliding.add(new ItemSlideMenu(R.drawable.ic_face, "내정보"));

        //2016-09-24
        Log.v("Teacher",String.valueOf(userinfo.isTeacher()));
        if(userinfo.isTeacher())
            listSliding.add(new ItemSlideMenu(R.drawable.ic_add,"등록신청자"));

        adapter = new SlidingMenuAdapter(this, listSliding);
        listViewSliding.setAdapter(adapter);

        //Display icon to open/close sliding list
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Set title
        setTitle(listSliding.get(0).getTitle());
        //item selected
        listViewSliding.setItemChecked(0, true);
        //Close menu
        drawerLayout.closeDrawer(listViewSliding);

        //Display fragment
        replaceFragment(0);

        //Handle on item click

        listViewSliding.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Set title
                setTitle(listSliding.get(position).getTitle());
                //item selected
                listViewSliding.setItemChecked(position, true);
                //Replace fragment
                replaceFragment(position);
                //Close menu
                drawerLayout.closeDrawer(listViewSliding);
            }
        });

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_opened, R.string.drawer_closed) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                invalidateOptionsMenu();
            }
        };

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        backPressCloseHandler.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
    public boolean onPrepareOptionsMenu(Menu menu) {

        if(bLog){ // 로그인 한 상태: 로그인은 안보이게, 로그아웃은 보이게
            menu.getItem(0).setEnabled(true);
            menu.getItem(1).setEnabled(false);
        }else{ // 로그 아웃 한 상태 : 로그인 보이게, 로그아웃은 안보이게
            menu.getItem(0).setEnabled(false);
            menu.getItem(1).setEnabled(true);
        }

        return super.onPrepareOptionsMenu(menu);
    }

    final int logout = 12;
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch(item.getItemId()) {
            case R.id.menu_item_1:
                Toast.makeText(getApplicationContext(), "정보를 입력해주세요.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.menu_item_2:
                bLog = true;
                Toast.makeText(getApplicationContext(), "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();

                Intent update = new Intent(this, LoginActivity.class);

                update.putExtra("logout",true);
                startActivity(update);
                finish();

                break;
            case R.id.action_home:
                Intent home = getIntent();
                startActivity(home);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    //Create method replace fragment
    private Fragment getFragment(int idx) {
        Fragment fragment = null;
        switch (idx) {
            case 0:
                fragment = Home.newInstance(userinfo);
                break;
            case 1:
                if(userinfo.getDate().equals("guest")) {
                    Toast.makeText(MainActivity.this, "로그인을 해 주세요.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                }
                else
                    fragment = Info.newInstance(userinfo);
                break;
            //2016-09-24
            case 3:
                fragment = register_member.newInstance(userinfo);
                break;
            default:
                fragment = Home.newInstance(userinfo);
                break;
        }

        return fragment;
    }

    private void replaceFragment(int pos) {
        Fragment fragment = null;
        fragment = getFragment(pos);
        if(null != fragment) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.main_content, fragment);
            //transaction.addToBackStack(null);
            transaction.commit();
        }
    }
}
