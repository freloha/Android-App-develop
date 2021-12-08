package com.example.astp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PointF;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapGpsManager;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPOIItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapPolyLine;
import com.skt.Tmap.TMapTapi;
import com.skt.Tmap.TMapView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

import static android.location.LocationManager.NETWORK_PROVIDER;

public class MainActivity extends BaseActivity implements TMapGpsManager.onLocationChangedCallback {
    private String Key = "ad977055-5c23-4fd9-85a9-4fede937e45d";
    public TMapView tmapview;
    public TMapPoint now;
    public TMapPoint clickP;
    public TMapPoint markerPoint;
    public TMapGpsManager tmapgps;
    public String queryUrl = "http://apis.data.go.kr/B552657/ErmctInfoInqireService/getEmrrmRltmUsefulSckbdInfoInqire?ServiceKey=xtFDDHcxAc5wWiJ55ReFkTdfegoYDn82H184HcD25a%2F6Ji8FOslaeLKCl2H%2FEuMpFgm6QCCzfV9g1UvyU3Vb%2Fg%3D%3D&STAGE1=%EC%84%9C%EC%9A%B8%ED%8A%B9%EB%B3%84%EC%8B%9C&pageNu=100&numOfRows=100";
    public Context context;

    private String data;

    private HospitalInfo hosinfo = new HospitalInfo();
    TMapPolyLine line;
    public int index;
    public String[] b = new String[49];

    public double arrayLat[] = new double[100];
    public double arrayLon[] = new double[100];
    int arrayLen = 0;

    TMapService t;
    private boolean mIsBound;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        TMapSetting();
        initView();
        setHosinfo();

    }

    public void initView(){
        tmapview.setOnClickListenerCallBack(new TMapView.OnClickListenerCallback() {
            @Override
            public boolean onPressEvent(ArrayList<TMapMarkerItem> arrayList, ArrayList<TMapPOIItem> arrayList1, TMapPoint tMapPoint, PointF pointF) {
                for (TMapMarkerItem item : arrayList) {
                    //Toast.makeText(getApplicationContext(), String.valueOf(item.getTMapPoint()), Toast.LENGTH_LONG).show();
                    markerPoint = item.getTMapPoint();
                    final int index2 = Integer.parseInt(item.getCalloutSubTitle());
                    final String phpid = hosinfo.getPhpid(index2);

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            data= getXmlData(phpid);//아래 메소드를 호출하여 XML data를 파싱해서 String 객체로 얻어오기

                            //UI Thread(Main Thread)를 제외한 어떤 Thread도 화면을 변경할 수 없기때문에
                            //runOnUiThread()를 이용하여 UI Thread가 TextView 글씨 변경하도록 함
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    // TODO Auto-generated method stub

                                    b = data.split("\n\n");

                                    for(int i = 0; i < 49; i++){
                                        if(b[i].contains(phpid)) {
                                            Toast.makeText(getApplicationContext(), b[i], Toast.LENGTH_LONG).show();
                                            break;
                                        }
                                    }
                                    //text.setText(data); //TextView에 문자열  data 출력
                                }
                            });
                        }
                    }).start();
                    //Toast.makeText(getApplicationContext(), markerPoint.getLatitude() + " ", Toast.LENGTH_LONG).show();
                    findPath(markerPoint);
                }
                //Toast.makeText(getApplicationContext(), "onPress~!", Toast.LENGTH_SHORT).show();
                index = 0;
                return false;
            }

            @Override
            public boolean onPressUpEvent(ArrayList<TMapMarkerItem> arrayList, ArrayList<TMapPOIItem> arrayList1, TMapPoint tMapPoint, PointF pointF) {

                //Toast.makeText(MapEvent.this, "onPressUp~!", Toast.LENGTH_SHORT).show();
                return false;
            }
        });

// 롱 클릭 이벤트 설정
        tmapview.setOnLongClickListenerCallback(new TMapView.OnLongClickListenerCallback() {
            @Override
            public void onLongPressEvent(ArrayList arrayList, ArrayList arrayList1, TMapPoint tMapPoint) {
                findPath(tMapPoint);
                //startNavi();
                //Toast.makeText(MapEvent.this, "onLongPress~!", Toast.LENGTH_SHORT).show();
            }
        });

// 지도 스크롤 종료
        tmapview.setOnDisableScrollWithZoomLevelListener(new TMapView.OnDisableScrollWithZoomLevelCallback() {
            @Override
            public void onDisableScrollWithZoomLevelEvent(float zoom, TMapPoint centerPoint) {
                //Toast.makeText(MapEvent.this, "zoomLevel=" + zoom + "\nlon=" + centerPoint.getLongitude() + "\nlat=" + centerPoint.getLatitude(), Toast.LENGTH_SHORT).show();
            }
        });
    }



    public void TMapSetting() {
        LinearLayout linearLayout = new LinearLayout(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1); //위치권한 탐색 허용 관련 내용
            }
        }

        tmapview = new TMapView(MainActivity.this);
        tmapview.setSKTMapApiKey(Key);
        tmapview.setLanguage(TMapView.LANGUAGE_KOREAN);
        tmapview.setIconVisibility(true);
        tmapview.setZoomLevel(15);
        tmapview.setMapType(TMapView.MAPTYPE_STANDARD);
        tmapview.setSightVisible(true);
        tmapview.setCompassMode(true);
        tmapview.setTrackingMode(true);

        linearLayout.addView(tmapview);
        setContentView(linearLayout);

        tmapgps = new TMapGpsManager(MainActivity.this);
        tmapgps.setMinTime(1000); // 위치변경 인식 최소시간 설정
        tmapgps.setMinDistance(5); // 위치변경 인식 최소거리 설정
        tmapgps.setProvider(tmapgps.NETWORK_PROVIDER);// 위성기반의 위치탐색
        tmapgps.OpenGps(); // 위치 탐색을 시작
    }

    @Override
    public void onLocationChange(Location location) {
        double lat = location.getLatitude();
        double lon = location.getLongitude();
        tmapview.setLocationPoint(lon, lat);
    }

    public void markLocation(double latitude, double longitude, String name, String address, int index){
        TMapPoint iconPoint = new TMapPoint(latitude, longitude);
        TMapMarkerItem tItem = new TMapMarkerItem();
        tItem.setCanShowCallout(true);
        tItem.setTMapPoint(iconPoint);
        tItem.setName(name);
        tItem.setVisible(TMapMarkerItem.VISIBLE);

        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),R.drawable.icon4);
        tItem.setIcon(bitmap);
        tItem.setEnableClustering(true);

        tItem.setCalloutTitle(name);
        tItem.setCalloutSubTitle(Integer.toString(index));
        tItem.setPosition((float)0.5, (float)0.5);

        tmapview.addMarkerItem(name, tItem);
    }

    public void setHosinfo(){
        for(int n = 0; n < 49; n++){
            markLocation(hosinfo.getLatitude(n), hosinfo.getLongitude(n), hosinfo.getName(n),hosinfo.getAddress(n),n);
        }
    }

    public void findPath(TMapPoint tMapPoint){
        //tmapview.removeAllTMapPolyLine();
        now = tmapview.getLocationPoint();
        //Toast.makeText(getApplicationContext(),now.getLatitude() + " " + now.getLongitude() + "\n"+ clickLat + "" + clickLon,Toast.LENGTH_LONG).show();
        clickP = tMapPoint;

        TMapData tmapdata = new TMapData();

        tmapdata.findPathDataWithType(TMapData.TMapPathType.CAR_PATH, now, clickP, new TMapData.FindPathDataListenerCallback() {
            @Override
            public void onFindPathData(TMapPolyLine polyLine) {
                polyLine.setLineColor(Color.BLUE);
                line = polyLine;
                tmapview.addTMapPath(polyLine);
            }
        });
    }

    public void removePL(View v){
        tmapview.removeTMapPath();
    }

    String getXmlData(String hosPhpid){

        StringBuffer buffer=new StringBuffer();
        //String location = URLEncoder.encode(str);//한글의 경우 인식이 안되기에 utf-8 방식으로 encoding..


        try {
            URL url= new URL(queryUrl);//문자열로 된 요청 url을 URL 객체로 생성.
            InputStream is= url.openStream(); //url위치로 입력스트림 연결

            XmlPullParserFactory factory= XmlPullParserFactory.newInstance();
            XmlPullParser xpp= factory.newPullParser();
            xpp.setInput( new InputStreamReader(is, "UTF-8") ); //inputstream 으로부터 xml 입력받기

            String tag;

            xpp.next();
            int eventType= xpp.getEventType();

            while( eventType != XmlPullParser.END_DOCUMENT ){

                switch( eventType ){
                    case XmlPullParser.START_DOCUMENT:
                        buffer.append("파싱 시작...\n\n");
                        break;

                    case XmlPullParser.START_TAG:
                        tag= xpp.getName();//테그 이름 얻어오기

                        if(tag.equals("item")) ;// 첫번째 검색결과
                        else if (tag.equals("dutyName")) {
                            //if(tag.contains(location)) {
                            buffer.append("기관명 : ");
                            xpp.next();
                            buffer.append(xpp.getText());//title 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            buffer.append("\n"); //줄바꿈 문자 추가
                            //}
                        } else if (tag.equals("dutyTel3")) {
                            buffer.append("응급실전화 :");
                            xpp.next();
                            buffer.append(xpp.getText());//description 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            buffer.append("\n");//줄바꿈 문자 추가
                        } else if (tag.equals("hpid")) {
                            buffer.append("기관코드 :");
                            xpp.next();
                            buffer.append(xpp.getText());//mapy 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            buffer.append("\n"); //줄바꿈 문자 추가
                        } else if (tag.equals("hv1")) {
                            buffer.append("응급실 당직의 직통연락처 :");
                            xpp.next();
                            buffer.append(xpp.getText());//mapy 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            buffer.append("\n"); //줄바꿈 문자 추가
                        } else if (tag.equals("hv10")) {
                            buffer.append("VENTI(소아) :");
                            xpp.next();
                            buffer.append(xpp.getText());//mapy 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            buffer.append("\n"); //줄바꿈 문자 추가
                        } else if (tag.equals("hv11")) {
                            buffer.append("인큐베이터(보육기) :");
                            xpp.next();
                            buffer.append(xpp.getText());//mapy 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            buffer.append("\n"); //줄바꿈 문자 추가
                        } else if (tag.equals("hv12")) {
                            buffer.append("소아당직의 직통연락처 :");
                            xpp.next();
                            buffer.append(xpp.getText());//mapy 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            buffer.append("\n"); //줄바꿈 문자 추가
                        } else if (tag.equals("hv2")) {
                            buffer.append("내과중환자실 :");
                            xpp.next();
                            buffer.append(xpp.getText());//mapy 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            buffer.append("\n"); //줄바꿈 문자 추가
                        } else if (tag.equals("hv3")) {
                            buffer.append("외과중환자실 :");
                            xpp.next();
                            buffer.append(xpp.getText());//mapy 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            buffer.append("\n"); //줄바꿈 문자 추가
                        } else if (tag.equals("hv4")) {
                            buffer.append("외과입원실(정형외과) :");
                            xpp.next();
                            buffer.append(xpp.getText());//mapy 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            buffer.append("\n"); //줄바꿈 문자 추가
                        } else if (tag.equals("hv5")) {
                            buffer.append("신경과입원실 :");
                            xpp.next();
                            buffer.append(xpp.getText());//mapy 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            buffer.append("\n"); //줄바꿈 문자 추가
                        } else if (tag.equals("hv6")) {
                            buffer.append("신경외과중환자실 :");
                            xpp.next();
                            buffer.append(xpp.getText());//mapy 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            buffer.append("\n"); //줄바꿈 문자 추가
                        } else if (tag.equals("hv7")) {
                            buffer.append("약물중환자 :");
                            xpp.next();
                            buffer.append(xpp.getText());//mapy 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            buffer.append("\n"); //줄바꿈 문자 추가
                        } else if (tag.equals("hv8")) {
                            buffer.append("화상중환자 :");
                            xpp.next();
                            buffer.append(xpp.getText());//mapy 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            buffer.append("\n"); //줄바꿈 문자 추가
                        } else if (tag.equals("hv9")) {
                            buffer.append("외상중환자 :");
                            xpp.next();
                            buffer.append(xpp.getText());//mapy 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            buffer.append("\n"); //줄바꿈 문자 추가
                        } else if (tag.equals("rnum")) {
                            buffer.append("일련번호 :");
                            xpp.next();
                            buffer.append(xpp.getText());//mapy 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            buffer.append("\n"); //줄바꿈 문자 추가
                        } else if (tag.equals("phpid")) {
                            buffer.append("기관코드 :");
                            xpp.next();
                            String next = xpp.getText();
                            buffer.append(next);//telephone 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            buffer.append("\n");//줄바꿈 문자 추가
                            if(!hosPhpid.equals(next)){

                            }
                            else {
                                index++;
                            }
                            break;
                        } else if (tag.equals("hvidate")) {
                            buffer.append("입력일시 : ");
                            xpp.next();
                            buffer.append(xpp.getText());//category 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            buffer.append("\n");//줄바꿈 문자 추가
                        } else if (tag.equals("hvec")) {
                            buffer.append("응급실 :");
                            xpp.next();
                            buffer.append(xpp.getText());//address 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            buffer.append("\n");//줄바꿈 문자 추가
                        } else if (tag.equals("hvoc")) {
                            buffer.append("수술실 :");
                            xpp.next();
                            buffer.append(xpp.getText());//mapx 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            buffer.append("\n"); //줄바꿈 문자 추가
                        } else if (tag.equals("hvcc")) {
                            buffer.append("신경중환자 :");
                            xpp.next();
                            buffer.append(xpp.getText());//mapy 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            buffer.append("\n"); //줄바꿈 문자 추가
                        } else if (tag.equals("hvncc")) {
                            buffer.append("신생중환자 :");
                            xpp.next();
                            buffer.append(xpp.getText());//mapy 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            buffer.append("\n"); //줄바꿈 문자 추가
                        } else if (tag.equals("hvccc")) {
                            buffer.append("흉부중환자 :");
                            xpp.next();
                            buffer.append(xpp.getText());//mapy 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            buffer.append("\n"); //줄바꿈 문자 추가
                        } else if (tag.equals("hvicc")) {
                            buffer.append("일반중환자 :");
                            xpp.next();
                            buffer.append(xpp.getText());//mapy 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            buffer.append("\n"); //줄바꿈 문자 추가
                        } else if (tag.equals("hvgc")) {
                            buffer.append("입원실 :");
                            xpp.next();
                            buffer.append(xpp.getText());//mapy 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            buffer.append("\n"); //줄바꿈 문자 추가
                        } else if (tag.equals("hvdnm")) {
                            buffer.append("당직의 :");
                            xpp.next();
                            buffer.append(xpp.getText());//mapy 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            buffer.append("\n"); //줄바꿈 문자 추가
                        } else if (tag.equals("hvctayn")) {
                            buffer.append("CT가용(가/부) :");
                            xpp.next();
                            buffer.append(xpp.getText());//mapy 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            buffer.append("\n"); //줄바꿈 문자 추가
                        } else if (tag.equals("hvmriayn")) {
                            buffer.append("MRI가용(가/부) :");
                            xpp.next();
                            buffer.append(xpp.getText());//mapy 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            buffer.append("\n"); //줄바꿈 문자 추가
                        } else if (tag.equals("hvangioayn")) {
                            buffer.append("조영촬영기가용(가/부) :");
                            xpp.next();
                            buffer.append(xpp.getText());//mapy 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            buffer.append("\n"); //줄바꿈 문자 추가
                        } else if (tag.equals("hvventiayn")) {
                            buffer.append("인공호흡기가용(가/부) :");
                            xpp.next();
                            buffer.append(xpp.getText());//mapy 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            buffer.append("\n"); //줄바꿈 문자 추가
                        } else if (tag.equals("hvamyn")) {
                            buffer.append("구급차가용여부 :");
                            xpp.next();
                            buffer.append(xpp.getText());//mapy 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            buffer.append("\n"); //줄바꿈 문자 추가
                        }

                        break;

                    case XmlPullParser.TEXT:
                        break;

                    case XmlPullParser.END_TAG:
                        tag= xpp.getName(); //테그 이름 얻어오기

                        if(tag.equals("item")) {
                            buffer.append("\n");
                            //counter++;// 첫번째 검색결과종료..줄바꿈
                            break;
                        }
                }

                eventType= xpp.next();
            }

        } catch (Exception e) {
            // TODO Auto-generated catch blocke.printStackTrace();
        }

        buffer.append("파싱 끝\n");
        //counter = 0;

        return buffer.toString();//StringBuffer 문자열 객체 반환

    }//getXmlData method....


    public Intent Service1;
    public void startService(View view) {
        Toast.makeText(getApplicationContext(), "service start", Toast.LENGTH_LONG).show();
        Service1 = new Intent(this, TMapService.class);
        bindService(Service1, mConnection, Context.BIND_AUTO_CREATE);
    }

    public void stopService(View view) {

        Toast.makeText(getApplicationContext(), "service end", Toast.LENGTH_LONG).show();
        //stopService(Service1);
        unbindService(mConnection);
    }

    private ServiceConnection mConnection = new ServiceConnection(){
        public void onServiceConnected(ComponentName componentName, IBinder iBinder){
            TMapService.MyBinder binder = (TMapService.MyBinder) iBinder;

            t = binder.getService(); //서비스 받아옴

            t.registerCallback(mCallback); //콜백 등록
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            t = null;
        }
    };
    private TMapService.ICallback mCallback = new TMapService.ICallback() {
        @Override
        public void recvData() {

        }

        public void recvData(double latitude, double longitude) {
            arrayLat[arrayLen] = latitude;
            arrayLon[arrayLen] = longitude;
            //markMyLocation(latitude, longitude);
            //Toast.makeText(getApplicationContext(), "recv data : " + arrayLat[arrayLen] + " " + arrayLon[arrayLen], Toast.LENGTH_LONG).show();
            arrayLen++;
        }
    };


    public void list(View view){
        //Toast.makeText(getApplicationContext(), "List lat ", Toast.LENGTH_LONG).show();
        for(int i = 0; i < arrayLen; i++){
            //Toast.makeText(getApplicationContext(), arrayLat[i] + " " + arrayLon[i], Toast.LENGTH_LONG).show();
            markMyLocation(arrayLat[i], arrayLon[i]);
        }
    }

    int howMany = 0;
    public void markMyLocation(double latitude, double longitude){
        TMapPoint iconPoint = new TMapPoint(latitude, longitude);
        TMapMarkerItem tItem = new TMapMarkerItem();
        tItem.setCanShowCallout(true);
        tItem.setTMapPoint(iconPoint);
        tItem.setVisible(TMapMarkerItem.VISIBLE);


        Date d = new Date();
        String totalTime = Integer.toString(d.getHours()) + ":" + Integer.toString(d.getMinutes());
        //Toast.makeText(getApplicationContext(), totalTime, Toast.LENGTH_LONG).show();

        tItem.setCalloutTitle(totalTime);

        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),R.drawable.character);
        tItem.setIcon(bitmap);
        tItem.setEnableClustering(true);
        tItem.setPosition((float)0.5, (float)0.5);

        tmapview.addMarkerItem(" ", tItem);
    }
}

