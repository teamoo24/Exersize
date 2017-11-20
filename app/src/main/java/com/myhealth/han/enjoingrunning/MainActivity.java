package com.myhealth.han.enjoingrunning;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity implements LocationListener {

    private Location mLastLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,},1000);
            return;
        } else {
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        }

        this.onLocationChanged(null);
    }

    @Override
    public void onLocationChanged(Location location) {
        // 緯度の表示
        TextView textView1 = (TextView) findViewById(R.id.text_view1);
        String str1;
        if(location == null){
            str1 = "経度: -.-";
        } else {
            str1 = "経度:"+location.getLongitude();
        }
        textView1.setText(str1);

        // 経度の表示
        TextView textView2 = (TextView) findViewById(R.id.text_view2);
        String str2;
        if(location == null){
            str2 = "緯度: -.-";
        } else {
            str2 = "緯度:"+location.getLatitude();
        }
        textView2.setText(str2);

        TextView textView3 = (TextView)findViewById(R.id.text_view3);

        if(location == null) {
            textView3.setText("-.- m/s");
        } else {
            float nCurrentSpeed = location.getSpeed();
            textView3.setText(nCurrentSpeed + " m/s");
        }

        // 時間の表示
        TextView textView4 = (TextView) findViewById(R.id.text_view4);
        String str4;
        if(location != null &&  this.mLastLocation!= null) {
            str4 = "測定時間間隔 : "+String.valueOf((location.getTime() - this.mLastLocation.getTime())/1000) + "(秒)";
        } else {
            str4 = "位置情報なし";
        }
        textView4.setText(str4);

        this.mLastLocation = location;
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
