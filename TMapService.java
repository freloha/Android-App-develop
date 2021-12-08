package com.example.astp;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.skt.Tmap.TMapGpsManager;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapView;

import java.util.Timer;
import java.util.TimerTask;

public class TMapService extends Service implements TMapGpsManager.onLocationChangedCallback {
    public double arrayLat[] = new double[100];
    public double arrayLon[] = new double[100];
    public boolean isStop = true;
    public TMapPoint p;
    TMapGpsManager tmapgps;
    public int total = 0;
    public Context context;
    public static final int MSG_REGISTER_CLIENT = 1;
    //public static final int MSG_UNREGISTER_CLIENT = 2;
    public static final int MSG_SEND_TO_SERVICE = 3;
    public static final int MSG_SEND_TO_ACTIVITY = 4;

    private Messenger mClient = null;
    public TMapService() {

    }

    public double getLon(int count){
        return arrayLon[count];
    }

    public double getLat(int count){
        return arrayLat[count];
    }

    public int getCounter(){
        return total;
    }

    class MyBinder extends Binder {
        TMapService getService() { // 서비스 객체를 리턴
            return TMapService.this;
        }
    }
    private final IBinder mBinder = new MyBinder();

    public void onCreate(){
        super.onCreate();
        startGPS();

        Thread counter = new Thread(new Counter());
        counter.start();
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public interface ICallback {
        public void recvData(); //액티비티에서 선언한 콜백 함수.
        public void recvData(double lat, double lon);
    }
    private ICallback mCallback;

//액티비티에서 콜백 함수를 등록하기 위함.
    public void registerCallback(ICallback cb) {
        mCallback = cb;
    }
//액티비티에서 서비스 함수를 호출하기 위한 함수 생성
    public void myServiceFunc(){
        //서비스에서 처리할 내용
    }

    public void startGPS(){
        tmapgps = new TMapGpsManager(this);
        tmapgps.setMinTime(1000); // 위치변경 인식 최소시간 설정
        tmapgps.setMinDistance(1); // 위치변경 인식 최소거리 설정
        tmapgps.setProvider(tmapgps.NETWORK_PROVIDER);// 위성기반의 위치탐색
        tmapgps.OpenGps(); // 위치 탐색을 시작
    }

    public void onLocationChange(Location location) {
        double lat = location.getLatitude();
        double lon = location.getLongitude();
        //Toast.makeText(getApplicationContext(),  tmapgps.getMinTime() + " " + arrayLat[counter] + " and " + arrayLon[counter],Toast.LENGTH_LONG).show();
        //vie.setLocationPoint(lon, lat);
    }

    public class Counter implements  Runnable{

        private int count;
        private Handler handler = new Handler();

        public void run(){
            int counter = 0;
            while(isStop){
                handler.post(new Runnable(){
                    public void run(){
                        p = tmapgps.getLocation();
                        double lan = p.getLatitude();
                        double lon = p.getLongitude();
                        if(lan != 0) {
                            mCallback.recvData(lan, lon);
                            Toast.makeText(getApplicationContext(), "Latitude : " + lan + " Longitude : " + lon + " counter : " + getCounter(), Toast.LENGTH_LONG).show();
                            total++;
                        }
                    }
                });
                try{
                    Thread.sleep(5000);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }
                counter++;
                if(counter == 50)
                    break;
            }
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        isStop = false;
        Thread.interrupted();
    }
}
