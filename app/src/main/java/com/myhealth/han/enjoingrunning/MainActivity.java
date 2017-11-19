package com.myhealth.han.enjoingrunning;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements LocationListener {


    private LocationManager mLocationManager;
    private Location mLastLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,},1000);
        } else {
            locationStart();

            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    0, 0, this);
        }
    }

    private void locationStart(){
        Log.d("debug","locationStart()");

        // LocationManager インスタンス生成
        mLocationManager = (LocationManager)getSystemService(LOCATION_SERVICE);

        if(mLocationManager != null && mLocationManager.isProviderEnabled(
                LocationManager.GPS_PROVIDER)){
            Log.d("debug","location manager Enabled");
        } else {
            // GPSを設定するように促す
                Intent settingIntent =
                        new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(settingIntent);
                Log.d("debug", "not gpsEnable, startActivity");
        }

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,},1000);

            Log.d("debug","checkSelfPermission false");
            return;
        }

        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,this);
    }
    @Override
    public void onLocationChanged(Location location) {
        // 緯度の表示
        TextView textView1 = (TextView) findViewById(R.id.text_view1);
        String str1 = "緯度:"+location.getLatitude();
        textView1.setText(str1);

        // 経度の表示
        TextView textView2 = (TextView) findViewById(R.id.text_view2);
        String str2 = "経度:"+location.getLongitude();
        textView2.setText(str2);

        //calcul manually speed
        double speed = 0;
        if(this.mLastLocation != null) {
            speed = Math.sqrt(
                    Math.pow(location.getLongitude() - mLastLocation.getLongitude(),2)
                    +Math.pow(location.getLatitude() - mLastLocation.getLatitude(),2)
                    /(location.getTime() - this.mLastLocation.getTime())
            );
        }
        if(location.hasSpeed()) {
            //get location speed;
            speed = location.getSpeed();
        }


        // 速度の表示
        TextView textView3 = (TextView) findViewById(R.id.text_view3);
        String str3 = "速度:"+String.valueOf(speed);
        textView3.setText(str3);

        // 時間の表示
        TextView textView4 = (TextView) findViewById(R.id.text_view4);
        String str4 = "時間:"+location.getTime();
        textView4.setText(str4);

        if(this.mLastLocation != null) {
            TextView textView5 = (TextView)findViewById(R.id.text_view5);
            TextView textView6 = (TextView)findViewById(R.id.text_view6);
            TextView textView7 = (TextView)findViewById(R.id.text_view7);
            TextView textView8 = (TextView)findViewById(R.id.text_view8);
            String str5 = String.valueOf(Math.pow(location.getLongitude() - mLastLocation.getLongitude(),2));
            String str6 = String.valueOf(Math.pow(location.getLatitude() - mLastLocation.getLatitude(),2));
            String str7 = String.valueOf( Math.sqrt(Math.pow(location.getLongitude() - mLastLocation.getLongitude(),2)+Math.pow(location.getLatitude() - mLastLocation.getLatitude(),2)));
            String str8 = String.valueOf(location.getTime() - this.mLastLocation.getTime());

            textView5.setText(str5);
            textView6.setText(str6);
            textView7.setText(str7);
            textView8.setText(str8);
        }
        this.mLastLocation = location;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle bundle) {
        switch (status) {
            case LocationProvider.AVAILABLE:
                Log.d("debug", "LocationProvider.AVAILABLE");
                break;
            case LocationProvider.OUT_OF_SERVICE:
                Log.d("debug", "LocationProvider.OUT_OF_SERVICE");
                break;
            case LocationProvider.TEMPORARILY_UNAVAILABLE:
                Log.d("debug", "LocationProvider.TEMPORARILY_UNAVAILABLE");
                break;
        }
    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }


    // 結果の受け取り
    /**
     * Android Quickstart:
     * https://developers.google.com/sheets/api/quickstart/android
     *
     * Respond to requests for permissions at runtime for API 23 and above.
     * @param requestCode The request code passed in
     *     requestPermissions(android.app.Activity, String, int, String[])
     * @param permissions The requested permissions. Never null.
     * @param grantResults The grant results for the corresponding permissions
     *     which is either PERMISSION_GRANTED or PERMISSION_DENIED. Never null.
     */
    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 1000) {
            //使用が許可された時
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("debug","checkSelfPermission true");

                locationStart();
            } else {
                //それでも拒否された時の対応
                Toast toast = Toast.makeText(this,
                        "これ以上何もできません" , Toast.LENGTH_SHORT);
                toast.show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
