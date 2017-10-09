package com.jeman.myapp;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.VisibleRegion;
import com.google.android.gms.maps.model.internal.zzf;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,View.OnClickListener, GoogleMap.OnMarkerClickListener {

    private LocationManager mLocMgr;
    private GoogleMap mMap;
    private TextView mTextMsg;
    private Marker myloc_marker;


    String mylatitude="36.444445";
    String mylongitude="127.444555";
    String myJSON;
    JSONArray peoples = null;
    private static final String TAG_ID = "id";
    private static final String TAG_RESULTS = "result";
    private static final String TAG_NAME = "name";
    private static final String TAG_LATITUDE = "latitude";
    private static final String TAG_LONGITUDE = "longitude";
    private static final String TAG_ADDRESS = "address";
    private static final String TAG_TYPE = "type";

    Intent schoolinfo;
    userInfo userinfo;
    Marker selectedMarker;
    public LatLng CameraZoomPoint;

    HashMap<Marker, MyMarker> markerhashmap;

    ArrayList<HashMap<String, String>> personList;
    ArrayList<MyMarker> MyMarkerList;
    TextView tv_marker;
    View marker_root_view;
    TextView myloc;

    ImageView imageView;
    TextView nametextview;
    TextView typetextview;
    TextView numbertextview;


    LinearLayout mapinfo_layout;
    Button map_findbutton;
    Button map_intentbutton;
    Button map_plusbutton;
    Button map_minusbutton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        markerhashmap = new HashMap<>();
        setContentView(R.layout.activity_maps);
        myloc = (TextView)findViewById(R.id.myloc);
        mTextMsg = (TextView)findViewById(R.id.msgText);
        MyMarkerList = new ArrayList<MyMarker>();
        mLocMgr = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);

        imageView = (ImageView)findViewById(R.id.mapfr_imgview);
        map_intentbutton = (Button)findViewById(R.id.mapfr_button);
        nametextview = (TextView)findViewById(R.id.mapfr_name);
        typetextview = (TextView)findViewById(R.id.mapfr_type);
        numbertextview = (TextView)findViewById(R.id.mapfr_number);

        userinfo = (userInfo)getIntent().getSerializableExtra("userinfo");

        map_plusbutton = (Button)findViewById(R.id.mapfr_plus);
        map_plusbutton.setOnClickListener(this);
        map_minusbutton = (Button)findViewById(R.id.mapfr_minus);
        map_minusbutton.setOnClickListener(this);
        map_findbutton = (Button) findViewById(R.id.mapfr_find);
        map_findbutton.setOnClickListener(this);
        map_intentbutton.setOnClickListener(this);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng startingPoint = new LatLng(37.567174, 126.978158);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startingPoint,10));
        mMap.setOnMarkerClickListener(this);
        marker_root_view = LayoutInflater.from(this).inflate(R.layout.marker_layout, null);
        tv_marker = (TextView) marker_root_view.findViewById(R.id.tv_marker);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mapfr_find:
                mMap.clear();
                LatLng cameracenter = mMap.getProjection().getVisibleRegion().latLngBounds.getCenter();
                getData(String.valueOf(cameracenter.latitude), String.valueOf(cameracenter.longitude));
                //getData(String.valueOf(mylatitude), String.valueOf(mylongitude));
                break;
            case R.id.mapfr_button:
                startActivity(schoolinfo);
                break;
            case R.id.mapfr_minus:
                LatLng cameracenter1 = mMap.getProjection().getVisibleRegion().latLngBounds.getCenter();
                float zoom = mMap.getCameraPosition().zoom;
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(cameracenter1,zoom-1));
                break;
            case R.id.mapfr_plus:
                LatLng cameracenter2 = mMap.getProjection().getVisibleRegion().latLngBounds.getCenter();
                float zoom2 = mMap.getCameraPosition().zoom;
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(cameracenter2,zoom2+1));break;
        }
    }
    private String findAddress(Context mcontext, double lat, double lng) {
        String nowlocation = "현재 위치정보 알 수 없음";
        StringBuffer bf = new StringBuffer();
        Geocoder geocoder = new Geocoder(MapsActivity.this, Locale.KOREA);
        List<Address> address;
        try {
            if (geocoder != null) {
                // 세번째 인수는 최대결과값인데 하나만 리턴받도록 설정했다
                address = geocoder.getFromLocation(lat, lng, 1);
                // 설정한 데이터로 주소가 리턴된 데이터가 있으면
                if (address != null && address.size() > 0) {
                    // 주소
                    String currentLocationAddress = address.get(0).getAddressLine(0).toString();
                    // 전송할 주소 데이터 (위도/경도 포함 편집)
                    nowlocation = currentLocationAddress;
                }
            }

        } catch (IOException e) {
            Toast.makeText(MapsActivity.this, "주소취득 실패", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        return nowlocation;
    }
    LocationListener mLocListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            mylatitude = String.valueOf(location.getLatitude());
            Log.v("Location",String.valueOf(location.getLatitude()));
            mylongitude = String.valueOf(location.getLongitude());
            myloc.setText(mylatitude +"/"+ mylongitude);
            CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(),location.getLongitude()));
            mMap.animateCamera(center);

        }

        public void onProviderDisabled(String provider) {
            mTextMsg.setText("Provider Disabled");
        }

        public void onProviderEnabled(String provider) {
            mTextMsg.setText("Provider Enabled");
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
            switch (status) {
                case LocationProvider.OUT_OF_SERVICE:
                    mTextMsg.setText("Provider Out of Service");
                    break;
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    mTextMsg.setText("Provider Temporarily Unavailable");
                    break;
                case LocationProvider.AVAILABLE:
                    mTextMsg.setText("Provider Available");
                    break;
            }
        }
    };

    public static Criteria getCriteria() {
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setSpeedRequired(false);
        criteria.setCostAllowed(false);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        return criteria;
    }


    public void onResume() {
        super.onResume();
        //String locProv = mLocMgr.getBestProvider(new Criteria(), true);
        String locProv = mLocMgr.getBestProvider(getCriteria(), true);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mLocMgr.requestLocationUpdates(locProv, 3000, 3, mLocListener);
        //mLocMgr.requestLocationUpdates( LocationManager.GPS_PROVIDER, 3000, 3, mLocListener );
        mTextMsg.setText("Location Service Start");
    }

    public void onPause() {
        super.onPause();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mLocMgr.removeUpdates(mLocListener);
        mTextMsg.setText("Location Service Stop");
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    ////////////////////////////////////////////////
    protected void newMarkerList() {
        try {
            markerhashmap.clear();
            JSONObject jsonObj = new JSONObject(myJSON);
            peoples = jsonObj.getJSONArray(TAG_RESULTS);
            MyMarkerList.clear();
            for (int i = 0; i < peoples.length(); i++) {
                JSONObject c = peoples.getJSONObject(i);
                String id = c.getString(TAG_ID);
                String name = c.getString(TAG_NAME);
                String type = c.getString(TAG_TYPE);
                String address = c.getString(TAG_ADDRESS);
                String latitude = c.getString(TAG_LATITUDE);
                String longitude = c.getString(TAG_LONGITUDE);
                MyMarkerList.add(new MyMarker(Double.parseDouble(latitude),Double.parseDouble(longitude),name));
                MyMarkerList.get(i).setinfo(type,address);
                MyMarkerList.get(i).setId(id);
            }
            CameraZoomPoint = new LatLng(MyMarkerList.get(0).getlatitude(),MyMarkerList.get(0).getlongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(CameraZoomPoint,mMap.getCameraPosition().zoom));
            for (MyMarker markerItem : MyMarkerList) {
                Marker marker = addMarker(markerItem,false);
                markerhashmap.put(marker,markerItem);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    private Marker addMarker(MyMarker markerItem, boolean isSelectedMarker) {

        LatLng position = new LatLng(markerItem.getlatitude(), markerItem.getlongitude());
        String name = markerItem.getName();
        String id = markerItem.getId();
        if (isSelectedMarker) {
            tv_marker.setBackgroundResource(R.drawable.marker_2);
            tv_marker.setTextColor(Color.WHITE);
        } else {
            tv_marker.setBackgroundResource(R.drawable.marker_1);
            tv_marker.setTextColor(Color.BLACK);
        }

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.title(id);
        markerOptions.position(position);
        markerOptions.snippet(name);
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(this, marker_root_view)));

        return mMap.addMarker(markerOptions);
    }
    private Bitmap createDrawableFromView(Context context, View view) {

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        return bitmap;
    }
    private Marker addMarker(Marker marker, boolean isSelectedMarker) {
        double lat = marker.getPosition().latitude;
        double lon = marker.getPosition().longitude;
        String name = marker.getTitle();
        MyMarker temp = new MyMarker(lat, lon, name);

        return addMarker(temp, isSelectedMarker);
    }

    public boolean onMarkerClick(Marker marker) {
        CameraUpdate center = CameraUpdateFactory.newLatLng(marker.getPosition());
        mMap.animateCamera(center);
        changeSelectedMarker(marker);
        Log.v("marker clicked","true");

        imageView.setVisibility(View.VISIBLE);
        map_intentbutton.setVisibility(View.VISIBLE);
        nametextview.setVisibility(View.VISIBLE);
        typetextview.setVisibility(View.VISIBLE);
        numbertextview.setVisibility(View.VISIBLE);

        nametextview.setText(markerhashmap.get(marker).getName());
        typetextview.setText(markerhashmap.get(marker).getType());
        numbertextview.setText(markerhashmap.get(marker).getAddress());
        schoolinfo = new Intent(MapsActivity.this, School_Info_Activity.class);
        schoolinfo.putExtra("schoolname",marker.getSnippet());
        schoolinfo.putExtra("schoolid",marker.getTitle());
        schoolinfo.putExtra("userinfo",userinfo);

        return true;
    }

    private void changeSelectedMarker(Marker marker) {
        // 선택했던 마커 되돌리기
        if (selectedMarker != null) {
            Marker markeradd = addMarker(selectedMarker, false);
            MyMarker mymarker = markerhashmap.get(selectedMarker);
            markerhashmap.put(markeradd,mymarker);
            selectedMarker.remove();
        }
        // 선택한 마커 표시
        if (marker != null) {
            selectedMarker = addMarker(marker, true);
            MyMarker mymarker = markerhashmap.get(marker);
            markerhashmap.put(selectedMarker,mymarker);
            marker.remove();
        }
    }
    public void onMapClick(LatLng latLng) {
        changeSelectedMarker(null);
    }

    public void getData(String mylatitude,String mylongitude) {
        class GetDataJSON extends AsyncTask<String, Void, String> {
            ProgressDialog loading;

            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(MapsActivity.this, "Please Wait", null, true, true);
            }

            protected String doInBackground(String... params) {
                BufferedReader bufferedReader = null;
                try {
                    //현재위치 php에 저장
                    String mylat = (String) params[0];
                    String mylng = (String) params[1];

                    String link = "http://bluecarnival.cafe24.com/kindergarten/getgpsdata.php";
                    String data = URLEncoder.encode("mylat", "UTF-8") + "=" + URLEncoder.encode(mylat, "UTF-8");
                    data += "&" + URLEncoder.encode("mylng", "UTF-8") + "=" + URLEncoder.encode(mylng, "UTF-8");

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

                newMarkerList();
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute(mylatitude,mylongitude);
    }
}
class MyMarker{
    private String id;
    private double latitude;
    private double longitude;
    private String name;
    private String type;
    private String address;
    MyMarker(double latitude, double longitude, String name){
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
    }
    void setId(String id){
        this.id = id;
    }
    void setLocation(double latitude, double longitude){
        this.latitude = latitude;
        this.longitude= longitude;
    }
    void setinfo(String type,String address){
        this.type = type;
        this.address = address;
    }
    double getlatitude(){
        return latitude;
    }

    double getlongitude(){
        return longitude;
    }

    String getName(){
        return name;
    }
    String getAddress(){return address;}
    String getType(){return type;}
    String getId(){return id;}
}
class mapkinderinfo{
    String name;
    String id;
    String type;
    String number;
    mapkinderinfo(String name, String id, String type, String number){
        this.name = name;
        this.id = id;
        this.type = type;
        this.number = number;
    }
}

