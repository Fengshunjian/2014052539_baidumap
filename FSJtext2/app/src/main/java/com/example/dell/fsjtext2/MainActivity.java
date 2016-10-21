package com.example.dell.fsjtext2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private ImageView mlocation;

    //刷新发短信
    private ImageView flush;
    private ArrayList<String> phonedata = new ArrayList<String>();
    private ArrayList<String> ene_phonedata = new ArrayList<String>();
    private fri_phone_list phoneList = new fri_phone_list();
    private ene_phone_list ene_phoneList = new ene_phone_list();

    //监听短信
    private SmsReciver smsReciver = new SmsReciver();

    //定位
    private LocationClient mLocationClient;
    private MyLocationListener mLocationListener;
    private boolean isFirstIn = true;
    private latLng jingweidu = new latLng();

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_main);

        //获取地图控件引用
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        //初始化显示比例
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(10.0f);
        mBaiduMap.setMapStatus(msu);
        //普通地图
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);

        //定位实现
        dingwei();

        //监听短信
        jianting();



        //点击刷新定位
        mlocation = (ImageView) findViewById(R.id.mlocation);
        mlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LatLng latLng = new LatLng(jingweidu.getmLatitude(), jingweidu.getmLongtitude());
                MapStatusUpdate MSU = MapStatusUpdateFactory.newLatLng(latLng);
                mBaiduMap.animateMapStatus(MSU);

            }
        });


        //点击发送短信
        flush = (ImageView) findViewById(R.id.flush);
        flush.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Intent intent = getIntent();
                //Bundle bundle = intent.getExtras();
                phonedata = phoneList.getPhonedata();
                ene_phonedata = ene_phoneList.getPhonedata();
                for(String phone:phonedata) {
                    if(phone!=null){
                    String context = "where are you?";
                    faduanxin(phone,context);
                    }
                }
                for(String phone:ene_phonedata) {
                    if(phone!=null){
                        String context = "where are you?";
                        faduanxin(phone,context);
                    }
                }
            }
        });

        //点击跳转显示好友、敌人界面
        ImageView main_fri_img = (ImageView) findViewById(R.id.main_fri_img1);
        ImageView main_ene_img = (ImageView) findViewById(R.id.main_ene_img);
        main_fri_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_fri = new Intent(MainActivity.this, Friend_show.class);
                intent_fri.putExtra("panduanzi","m");
                startActivity(intent_fri);
            }
        });
        main_ene_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_ene = new Intent(MainActivity.this, Enemy_show.class);
                intent_ene.putExtra("panduanzi","m");
                startActivity(intent_ene);
            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();



    }




    //方法函数部分

    //发短信函数
    public void faduanxin(String phone,String context){
        SmsManager manager = SmsManager.getDefault();
        ArrayList<String> list = manager.divideMessage(context);  //因为一条短信有字数限制，因此要将长短信拆分
        for (String text : list) {
            manager.sendTextMessage(phone, null, text, null, null);
        }

        Toast.makeText(getApplicationContext(), "发送完毕", Toast.LENGTH_SHORT).show();
    }

    //放标记函数
    public void biaoji(double x,double y,String h){
        //定义Maker坐标点
        LatLng point = new LatLng(x,y);
        String x1 = String.valueOf(x);
        String y1 = String.valueOf(y);
        //构建Marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(R.drawable.fri_img);
        //构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions()
                .position(point)
                .icon(bitmap)
                .title(x1+" / "+y1);
        final double dx = x;
        final double dy = y;
        final String dh = h;
        mBaiduMap.addOverlay(option);
        //点击标记跳转详细资料界面
        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Intent intent = new Intent(MainActivity.this, friend_detial_show.class);
                intent.putExtra("jingdu",dx);
                intent.putExtra("weidu",dy);
                intent.putExtra("fri_number",dh);
                intent.putExtra("panduanshu",0);
                startActivity(intent);
                return false;
            }
        });
    }
    //放标记函数
    public void ene_biaoji(double x,double y,String h){
        //定义Maker坐标点
        LatLng point = new LatLng(x,y);
        String x1 = String.valueOf(x);
        String y1 = String.valueOf(y);
        //构建Marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(R.drawable.ene_img);
        //构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions()
                .position(point)
                .icon(bitmap)
                .title(x1+" / "+y1);
        final double dx = x;
        final double dy = y;
        final String dh = h;
        mBaiduMap.addOverlay(option);
        //点击标记跳转详细资料界面
        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Intent intent = new Intent(MainActivity.this, enemy_detial_show.class);
                intent.putExtra("jingdu",dx);
                intent.putExtra("weidu",dy);
                intent.putExtra("ene_number",dh);
                intent.putExtra("panduanshu",0);
                startActivity(intent);
                return false;
            }
        });
    }
    //定位函数
    private void dingwei() {
        mLocationClient = new LocationClient(getApplicationContext());
        mLocationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(mLocationListener);
        LocationClientOption option = new LocationClientOption();
        option.setCoorType("bd09ll");
        option.setIsNeedAddress(true);
        option.setOpenGps(true);
        option.setPriority(LocationClientOption.GpsFirst);      //  设置网络优先
        option.setScanSpan(1000);
        mLocationClient.setLocOption(option);
    }

    //短信监听
    public void jianting(){
        smsReciver.setOnReceivedMessageListener(new SmsReciver.MessageListener() {
            private String xx;
            private String yy;
            @Override
            public void OnReceived(String sender, String body) {


                String x = "" + jingweidu.getmLatitude();
                String y = "" + jingweidu.getmLongtitude();

                if (body.equals("where are you?")) {
                    faduanxin(sender, x + "/" + y);
                }
                if (!body.equals(null) && !body.equals("where are you?")) {
                    String[] xy =new String[2];
                    xy=body.split("/") ;
                    xx = xy[0];
                    yy = xy[1];
                    Double jingdu = Double.parseDouble(xx);
                    Double weidu = Double.parseDouble(yy);
                    for(int i =0;i<phoneList.getPhonedata().size();i++){
                        if(sender.equals(phoneList.getPhonedata().get(i)))
                            biaoji(jingdu,weidu,sender);
                        LatLng pt1 = new LatLng(jingweidu.getmLatitude(),jingweidu.getmLongtitude());
                        LatLng pt2 = new LatLng(jingdu,weidu);
                        Toast.makeText(getApplicationContext(), xx+" "+yy, Toast.LENGTH_SHORT).show();
                            lianxian(pt1,pt2);
                    }
                    for(int j =0;j<ene_phoneList.getPhonedata().size();j++){
                        if(sender.equals(ene_phoneList.getPhonedata().get(j)))
                            ene_biaoji(jingdu,weidu,sender);
                        LatLng pt1 = new LatLng(jingweidu.getmLatitude(),jingweidu.getmLongtitude());
                        LatLng pt2 = new LatLng(jingdu,weidu);
                        Toast.makeText(getApplicationContext(), xx+" "+yy, Toast.LENGTH_SHORT).show();
                        ene_lianxian(pt1,pt2);
                    }


                }
            }
        });
    }

    //连线
    public void lianxian(LatLng pt1,LatLng pt2){
        //构造纹理资源
        BitmapDescriptor custom1 = BitmapDescriptorFactory
                .fromResource(R.drawable.icon_arrow);
        // 定义点
        //LatLng pt1 = new LatLng(39.93923, 116.357428);
        //LatLng pt2 = new LatLng(39.91923, 116.327428);
        //构造纹理队列
        List<BitmapDescriptor> customList = new ArrayList<BitmapDescriptor>();
        customList.add(custom1);

        List<LatLng> points = new ArrayList<LatLng>();
        List<Integer> index = new ArrayList<Integer>();
        points.add(pt1);//点元素
        index.add(0);//设置该点的纹理索引
        points.add(pt2);//点元素
        index.add(0);//设置该点的纹理索引

        //构造对象
        OverlayOptions ooPolyline = new PolylineOptions().width(7).color(0xAAFF0000).points(points).customTextureList(customList).textureIndex(index);
//添加到地图
        mBaiduMap.addOverlay(ooPolyline);
    }
    public void ene_lianxian(LatLng pt1,LatLng pt2){
        //构造纹理资源
        BitmapDescriptor custom1 = BitmapDescriptorFactory
                .fromResource(R.drawable.ene_icon_arrow);
        // 定义点
        //LatLng pt1 = new LatLng(39.93923, 116.357428);
        //LatLng pt2 = new LatLng(39.91923, 116.327428);
        //构造纹理队列
        List<BitmapDescriptor> customList = new ArrayList<BitmapDescriptor>();
        customList.add(custom1);

        List<LatLng> points = new ArrayList<LatLng>();
        List<Integer> index = new ArrayList<Integer>();
        points.add(pt1);//点元素
        index.add(0);//设置该点的纹理索引
        points.add(pt2);//点元素
        index.add(0);//设置该点的纹理索引

        //构造对象
        OverlayOptions ooPolyline = new PolylineOptions().width(7).color(0xAAFF0000).points(points).customTextureList(customList).textureIndex(index);
//添加到地图
        mBaiduMap.addOverlay(ooPolyline);
    }


    //地图相关
    @Override
    public void onStart() {
        super.onStart();
        //开始定位
        mBaiduMap.setMyLocationEnabled(true);
        if (!mLocationClient.isStarted())
            mLocationClient.start();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());

    }

    @Override
    public void onStop() {
        super.onStop();
        //停止定位
        mBaiduMap.setMyLocationEnabled(false);
        mLocationClient.stop();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    //定位类
   private class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            MyLocationData data = new MyLocationData.Builder()//
                    .accuracy(location.getRadius())//
                    .latitude(location.getLatitude())//
                    .longitude(location.getLongitude())//
                    .build();
            mBaiduMap.setMyLocationData(data);

            //mLatitude = location.getLatitude();
            //mLongtitude = location.getLongitude();
            jingweidu.setmLatitude(location.getLatitude());
            jingweidu.setmLongtitude(location.getLongitude());

            if (isFirstIn) {
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
                mBaiduMap.animateMapStatus(msu);
                isFirstIn = false;
            }
        }
    }





}
