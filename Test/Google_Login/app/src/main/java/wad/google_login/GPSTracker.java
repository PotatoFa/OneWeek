package wad.google_login;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

/**
 * Created by hoon on 2015-10-16.
 */
public class GPSTracker extends Service implements LocationListener {

    String Log_tag = "OneWeeks/GPSTracker";
    String using_provider;

    private final Context mContext;

    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;
    boolean canGetLocation = false;

    Location location; // location
    double latitude; // latitude
    double longitude; // longitude

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 100;
    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000;
    // Declaring a Location Manager
    protected LocationManager locationManager;

    public GPSTracker(Context context) {
        this.mContext = context;
        getLocation();
        //TODO 마시멜로우 대비 런타임권한요청 추가 수정 필요.
    }

    public Location getLocation() {
        try {
            locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);

            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            //GPS사용가능여부
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            //NETWORK사용가능여부
            if (!isGPSEnabled && !isNetworkEnabled) {
                Log.i(Log_tag, " Not Enabled GPS&Network");
                //GPS/NETWORK 둘다 사용하지 못 할때.
                //TODO 위치데이터를 제공받지 못 하는 상황정의
            } else {
                Log.i(Log_tag, " start location Data load");
                //GPS/NETWORK 둘 중 하나라도 사용가능할 경우. 위치값을 불러온다.
                this.canGetLocation = true;
                //위치정보 flag인 canGerLocation을 TRUE로 변환시켜준 후
                if (isNetworkEnabled) {
                    Log.i(Log_tag, " using Network Provider");
                    //NETWORK를 통해 Location불러올때
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    //NetworkProvider의 가장 최신 Location을 로딩
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    //LocationListener를 설정해서 위치데이터를 업데이트 시켜준다.
                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        //locationListener를 설정한 후 다시 한번 위치를 업데이트 시켜준다.
                        if (location != null) {
                            //불러온 Location데이터가 NULL값이 아니라면 lat/lon값으로 저장한다.
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                            using_provider = "network";
                            Log.i(Log_tag, " result network location "+latitude+"/"+longitude);
                        }
                    }
                }
                // if GPS Enabled get lat/long using GPS Services

                if (isGPSEnabled) {
                    Log.i(Log_tag, " using GPS Provider");
                    if (location == null) {
                        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                        if (locationManager != null) {
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                                using_provider = "gps";
                                Log.i(Log_tag, " result GPS location "+latitude+"/"+longitude);
                            }
                        }
                    }
                }
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }

        return location;
    }

    /**
     * Stop using GPS listener
     * Calling this function will stop using GPS in your app
     * */
    public void stopUsingGPS(){
        if(locationManager != null){
            try {
                locationManager.removeUpdates(GPSTracker.this);
            }catch (SecurityException e){
                e.printStackTrace();
            }
        }
    }

    public String getUsingProvider(){
        return using_provider;
    }

    public double getLatitude(){
        if(location != null){
            latitude = location.getLatitude();
        }
        return latitude;
    }

    public double getLongitude(){
        if(location != null){
            longitude = location.getLongitude();
        }
        return longitude;
    }

    public boolean canGetLocation() {
        return this.canGetLocation; //Location 사용가능여부
    }

    public void showSettingsAlert(){
        //TODO 서비스단에서 구현되어 있음. GPSTracker 객체 생성하는 시점으로 이동이 필요함.
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
        // Setting Dialog Title
        alertDialog.setTitle("GPS is settings");
        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");
        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(intent);
            }
        });
        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        // Showing Alert Message
        alertDialog.show();
    }

    @Override
    public void onLocationChanged(Location location) {
        //위치 변동 있을때마다 location 업데이트
        longitude = location.getLongitude();
        latitude = location.getLatitude();
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

}