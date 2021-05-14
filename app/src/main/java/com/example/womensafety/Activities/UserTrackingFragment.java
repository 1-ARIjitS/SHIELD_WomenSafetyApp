package com.example.womensafety.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.audiofx.BassBoost;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.womensafety.Models.userLocationTracking;
import com.example.womensafety.R;
import com.example.womensafety.User.Detail_Forms;
import com.example.womensafety.User.LocationConstants;
import com.example.womensafety.User.LocationService;
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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Objects;


public class UserTrackingFragment extends AppCompatActivity {

    public static final long MIN_TIME = 10000;
    public static final long MIN_DISTANCE = 50;
    private GoogleMap mMap;
    SupportMapFragment smf;

    LocationRequest locationRequest;
    FusedLocationProviderClient client;
    LocationCallback locationCallback;

    double start_lat,bottom_boundary;
    double start_lng,left_boundary;
    double end_lat,top_boundary;
    double end_lng,right_boundary;
    String start_address,end_address,reached_safely_address;
    List<LatLng> latLngList = new ArrayList<LatLng>();
    List<LatLng> full_path_list = new ArrayList<LatLng>();


    Button stop_tracking;

    private Marker currentPositionMarker = null;


    public Polyline route;
    public Polyline full_track;

    Button save_travel;

    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference reference;
    StorageReference storageReference;
    String cud;

    String vehicle_image=null;
    String vehicle_number;
    String estimated_time;
    String actual_date;
    String actual_time;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_user_tracking);

        smf = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        stop_tracking = (Button) findViewById(R.id.reached_safely_button);

        client = LocationServices.getFusedLocationProviderClient(this);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);

                Location location = locationResult.getLastLocation();

            }
        };

        auth=FirebaseAuth.getInstance();
        cud= Objects.requireNonNull(auth.getCurrentUser()).getUid();
        database=FirebaseDatabase.getInstance();
        storageReference= FirebaseStorage.getInstance().getReference();
        reference=database.getReference("user_tracking_details").child(cud);

        start_lat = getIntent().getDoubleExtra("start_latitude", 0.0);
        bottom_boundary=start_lat;
        start_lng = getIntent().getDoubleExtra("start_longitude", 0.0);
        left_boundary=start_lng;
        end_lat = getIntent().getDoubleExtra("end_latitude", 0.0);
        top_boundary=end_lat;
        end_lng = getIntent().getDoubleExtra("end_longitude", 0.0);
        right_boundary=end_lng;
        start_address=getIntent().getStringExtra("start_add");
        end_address=getIntent().getStringExtra("end_add");
        vehicle_image=getIntent().getStringExtra("vehicle_image");
        vehicle_number=getIntent().getStringExtra("vehicle_number");
        estimated_time=getIntent().getStringExtra("est_time");

        //calender things start here

        Calendar calendar=Calendar.getInstance();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd-MMM-yyyy");
        actual_date=simpleDateFormat.format(calendar.getTime());
        SimpleDateFormat simpleTimeFormat=new SimpleDateFormat("hh:mm:ss a");
        actual_time=simpleTimeFormat.format(calendar.getTime());


        Log.d("s_lat", String.valueOf(start_lat));
        Log.d("b_bound", String.valueOf(bottom_boundary));
        Log.d("s_lng", String.valueOf(start_lng));
        Log.d("l_bound", String.valueOf(left_boundary));
        Log.d("e_lat", String.valueOf(end_lat));
        Log.d("t_bound", String.valueOf(top_boundary));
        Log.d("e_lng", String.valueOf(end_lng));
        Log.d("r_bound", String.valueOf(right_boundary));
        Log.d("start",start_address);
        Log.d("end",end_address);

        save_travel=(Button)findViewById(R.id.save_travel_log);

        Dexter.withContext(getApplicationContext())
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        Toast.makeText(getApplicationContext(), "Permission Granted", Toast.LENGTH_SHORT).show();
                        getMyLoc();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), "");
                        intent.setData(uri);
                        startActivity(intent);
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
            public void onSuccess(final Location location) {
                smf.getMapAsync(new OnMapReadyCallback() {

                    @Override
                    public void onMapReady(@NonNull GoogleMap googleMap) {
                        mMap = googleMap;
                        mMap.getUiSettings().setZoomControlsEnabled(true);
                        mMap.getUiSettings().setAllGesturesEnabled(true);

                        LatLng start_lat_lng;
                        LatLng end_lat_lng;
                        start_lat_lng=new LatLng(start_lat,start_lng);
                        end_lat_lng=new LatLng(end_lat,end_lng);
                        full_path_list.add(start_lat_lng);
                        full_path_list.add(end_lat_lng);

                        mMap.addMarker(new MarkerOptions()
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                                .position(start_lat_lng)
                                .title("Starting Location")
                        .zIndex(10));

                        mMap.addMarker(new MarkerOptions()
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                                .position(end_lat_lng)
                                .title("Destination")
                        .zIndex(5));

                        /*PolylineOptions polylineOptions=new PolylineOptions().add(start_lat_lng).add(end_lat_lng);
                        mMap.addPolyline(polylineOptions);*/


                        /*LatLngBounds user_tracking_bounds=new LatLngBounds(
                               new LatLng(start_lat,start_lng),
                                new LatLng(end_lat,end_lng)
                        );*/

                        full_track=mMap.addPolyline(new PolylineOptions().width(20).color(Color.BLUE));
                        full_track.setPoints(full_path_list);

                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(start_lat_lng,17));
/*
                        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(user_tracking_bounds,1));*/

                        latLngList.add(start_lat_lng);

                        final LocationListener locationListener = new LocationListener() {
                            @Override
                            public void onLocationChanged(@NonNull Location location) {
                                try {
                                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                                    CameraPosition cameraPosition = new CameraPosition.Builder()
                                            .target(latLng).zoom(20).build();
                                    latLngList.add(latLng);

                                    // mMap.clear(); // Call if You need To Clear Map
                                    /*if(mMap!=null)
                                        mMap.clear();*/
                                    if (currentPositionMarker == null)
                                    {currentPositionMarker = mMap.addMarker(new MarkerOptions()
                                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                                                .position(latLng)
                                                .title("Your Current Location")
                                                .zIndex(20));}
                                    else
                                    {currentPositionMarker.setPosition(latLng);}

                                    route = mMap.addPolyline(new PolylineOptions().color(Color.GREEN));
                                    route.setPoints(latLngList);

                                    mMap.animateCamera(CameraUpdateFactory
                                            .newCameraPosition(cameraPosition));
                                } catch (SecurityException e) {
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

                        //adding the location manager most important tool to manage location tracking and access
                        final LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                        try {

                            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DISTANCE, locationListener);

                        } catch (SecurityException e) {
                            e.printStackTrace();
                        }

                        //stopping the tracking activity
                        stop_tracking.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                /*stopLocationService();*/
                                if (ActivityCompat.checkSelfPermission(UserTrackingFragment.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(UserTrackingFragment.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                    // TODO: Consider calling
                                    //    ActivityCompat#requestPermissions
                                    // here to request the missing permissions, and then overriding
                                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                    //                                          int[] grantResults)
                                    // to handle the case where the user grants the permission. See the documentation
                                    // for ActivityCompat#requestPermissions for more details.
                                    return;
                                }
                                Location destination=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                                LatLng destination_latLng=new LatLng(destination.getLatitude(),destination.getLongitude());
                                double reached_safely_lat=destination.getLatitude();
                                double reached_safely_lng=destination.getLongitude();
                                try {
                                    reached_safely_address=getAddress(reached_safely_lat,reached_safely_lng);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                /*LatLng destination_latLng=new LatLng(location.getLatitude(),location.getLongitude());*/
                                latLngList.add(destination_latLng);
                                route.setPoints(latLngList);
                                mMap.addMarker(new MarkerOptions()
                                        .position(destination_latLng)
                                        .title("You Reached Here Safely")
                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));
                                currentPositionMarker.setVisible(false);
                                /*mMap.snapshot(new GoogleMap.SnapshotReadyCallback() {
                                    @Override
                                    public void onSnapshotReady(@Nullable Bitmap bitmap) {
                                        //to store the bitmap in firebase realtime database
                                    }
                                });*/
                                Toast.makeText(getApplicationContext(),"tracking stopped",Toast.LENGTH_SHORT).show();
                                locationManager.removeUpdates(locationListener);
                            }
                        });

                    }
                });
            }
        });

        save_travel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!reached_safely_address.isEmpty()){
                userLocationTracking userLocationTracking=new userLocationTracking(start_address,end_address,vehicle_number,estimated_time,actual_time,actual_date,vehicle_image);
                reference.child(actual_date).child(actual_time).setValue(userLocationTracking);
                    Toast.makeText(getApplicationContext(),"Travel log saved successfully",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(UserTrackingFragment.this,Detail_Forms.class));}
                else
                {
                    Toast.makeText(getApplicationContext(),"first stop location tracking to save travel log",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean isLocationServiceRunning() {
        ActivityManager activityManager =
                (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        if (activityManager != null) {
            for (ActivityManager.RunningServiceInfo service :
                    activityManager.getRunningServices(Integer.MAX_VALUE)) {
                if (LocationService.class.getName().equals(service.service.getClassName())) {
                    if (service.foreground) {
                        return true;
                    }
                }
            }
            return false;
        }
        return false;
    }

    private void startLocationService() {
        if (!isLocationServiceRunning()) {
            Intent intent = new Intent(getApplicationContext(), LocationService.class);
            intent.setAction(LocationConstants.ACTION_START_LOCATION_SERVICE);
            startService(intent);
            Toast.makeText(this, "Tracking Starts", Toast.LENGTH_SHORT).show();
        }
    }

    private void stopLocationService() {
        if (isLocationServiceRunning()) {
            Intent intent = new Intent(getApplicationContext(), LocationService.class);
            intent.setAction(LocationConstants.ACTION_STOP_LOCATION_SERVICE);
            startService(intent);
            Toast.makeText(this, "Tracking Stops", Toast.LENGTH_SHORT).show();
        }
    }

    private String getAddress(Double lat,Double lng) throws IOException {

        String address="";
        Geocoder geocoder=new Geocoder(UserTrackingFragment.this, Locale.getDefault());
        try{

            List<Address>addresses=geocoder.getFromLocation(lat,lng,1);

            if(addresses!=null)
            {
                Address returnAddress=addresses.get(0);
                StringBuilder stringBuilder=new StringBuilder("");

                for(int i=0;i<=returnAddress.getMaxAddressLineIndex();i++)
                {
                    stringBuilder.append(returnAddress.getAddressLine(i)).append("\n");
                }

                address=stringBuilder.toString();
            }

        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return address;
    }

}
