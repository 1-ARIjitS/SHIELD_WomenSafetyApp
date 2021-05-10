package com.example.womensafety.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.womensafety.R;
import com.example.womensafety.User.Detail_Forms;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.ArrayList;
import java.util.List;


public class UserTrackingFragment extends AppCompatActivity {

    public static final int DEFAULT_UPDATE_INTERVAL = 6;
    private static final long MIN_TIME =5000 ;
    private static final long MIN_DISTANCE = 5;
    private GoogleMap mMap;
    SupportMapFragment smf;

    LocationRequest locationRequest;
    Polyline gpsTrack;
    Marker mMarker;
    FusedLocationProviderClient client;
    LocationCallback locationCallback;

    LatLng latLng;
    LatLng last_known_lat_lng;
    List<Marker>markerList=new ArrayList<Marker>();
    List<LatLng>latLngList=new ArrayList<LatLng>();

    Button stop_tracking;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_user_tracking);

        smf = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        stop_tracking=(Button)findViewById(R.id.track_stop_tracking_button);

        /*locationRequest=new LocationRequest();
        locationRequest.setInterval(10000 * DEFAULT_UPDATE_INTERVAL);
        locationRequest.setFastestInterval(1000 * DEFAULT_UPDATE_INTERVAL);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);*/

        client = LocationServices.getFusedLocationProviderClient(this);

        locationCallback=new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);

                Location location=locationResult.getLastLocation();

            }
        };

       /* ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PackageManager.PERMISSION_GRANTED);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PackageManager.PERMISSION_GRANTED);*/

        Dexter.withContext(getApplicationContext())
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        getMyLoc();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
    }

    private void getMyLoc() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Task<Location> task = client.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                smf.getMapAsync(new OnMapReadyCallback() {

                    @Override
                    public void onMapReady(@NonNull GoogleMap googleMap) {
                        mMap=googleMap;
                       /*latLng=new LatLng(location.getLatitude(),location.getLongitude());
                       final MarkerOptions markerOptions=new MarkerOptions().position(latLng).title("starting location");

                       googleMap.addMarker(markerOptions);
                       googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));

                       final PolylineOptions polylineOptions= new PolylineOptions();
                       polylineOptions.color(android.R.color.holo_green_dark);
                       polylineOptions.width(10);
                       gpsTrack=googleMap.addPolyline(polylineOptions);

                       latLngList.add(latLng);*/

                        LocationListener locationListener = new LocationListener() {
                            @Override
                            public void onLocationChanged(@NonNull Location location) {
                                try{
                                last_known_lat_lng=new LatLng(location.getLatitude(),location.getLongitude());
                                MarkerOptions marker=new MarkerOptions().position(last_known_lat_lng).title("current location");
                                mMarker=mMap.addMarker(marker);
                                mMap.addMarker(marker);
                                mMap.moveCamera(CameraUpdateFactory.newLatLng(last_known_lat_lng));
                                latLngList.add(last_known_lat_lng);
                                markerList.add(mMarker);
                                }
                                catch (SecurityException e)
                                {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onStatusChanged(String provider, int status, Bundle extras) {

                            }

                            @Override
                            public void onProviderEnabled(@NonNull String provider) {

                            }

                            @Override
                            public void onProviderDisabled(@NonNull String provider) {

                            }
                        };

                        LocationManager locationManager=(LocationManager)getSystemService(LOCATION_SERVICE);
                        try{

                            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,MIN_TIME,MIN_DISTANCE,locationListener);

                        }catch (SecurityException e)
                        {
                            e.printStackTrace();
                        }




                        /*stop_tracking.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                LatLng lat=new LatLng(location.getLatitude(),location.getLongitude());
                                MarkerOptions marker=new MarkerOptions().position(lat).title("Destination - You Stopped Here");
                                googleMap.addMarker(marker);
                                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(lat,-15));
                                Toast.makeText(getApplicationContext(),"Location Tracking Is Being Stopped",Toast.LENGTH_SHORT).show();
                                stopUpdateLocation();
                                startActivity(new Intent(getApplicationContext(), Detail_Forms.class));
                            }
                        });*/

                    }
                });
            }
        });
    }

    private void updateGpsTrack() {
        latLngList=gpsTrack.getPoints();
        latLngList.add(last_known_lat_lng);
        gpsTrack.setPoints(latLngList);
    }

    private void stopUpdateLocation() {
        client.removeLocationUpdates(locationCallback);
    }

}
