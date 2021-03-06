package kr.ac.kumoh.ce.s20120675.myanimation;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    boolean isPageOpen = false;
    Animation flow;
    Animation unflow;

    LinearLayout slidingPage01;


    boolean visible=false;
    private static final int REQUEST_FINE_LOCATION = 998;
    private LocationManager locationManager;
    private LocationListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);


        slidingPage01 = (LinearLayout)findViewById(R.id.slidingPage01);

        flow = AnimationUtils.loadAnimation(this,R.anim.flow);
        unflow = AnimationUtils.loadAnimation(this,R.anim.unflow);

        FlowAnimationListener animListener = new FlowAnimationListener();
        flow.setAnimationListener(animListener);
        unflow.setAnimationListener(animListener);

        listener = new LocationListener() {

            @Override
            public void onLocationChanged(Location location) {
                String mylocation;
                String myspeed;
                double myspeed_double;

                mylocation= String.valueOf(location.getLongitude());
                mylocation =mylocation + String.valueOf(location.getLatitude());
                myspeed = String.valueOf(location.getSpeed());
                myspeed_double = location.getSpeed();
                if(myspeed_double>=0.5)
                {

                    if(visible==false) {
                        slidingPage01.setVisibility(View.VISIBLE);
                        slidingPage01.startAnimation(flow);

                    }
                }
                else
                {
                    if(visible==true) {
                        slidingPage01.startAnimation(unflow);
                    }

                }


                //Toast.makeText(MainActivity.this, myspeed, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }
            @Override
            public void onProviderEnabled(String s) {

            }
            @Override
            public void onProviderDisabled(String s) {
                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(i);
            }

        };
        get_location();


    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_FINE_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    get_location();
                }
                break;
            default:
                break;
        }
    }


    void get_location() {
    // first check for permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_FINE_LOCATION);
            }
            return;
        }
        locationManager.requestLocationUpdates("gps", 100, 0, listener);
    }


    private final class FlowAnimationListener implements Animation.AnimationListener{
        public void onAnimationEnd(Animation animation){
            if(visible) {
                slidingPage01.setVisibility(View.INVISIBLE);
                visible = false;
            }else {
                visible = true;
            }
        }

        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }
}
