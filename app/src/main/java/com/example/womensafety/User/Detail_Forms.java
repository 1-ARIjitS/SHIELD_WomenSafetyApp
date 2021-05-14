package com.example.womensafety.User;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.media.Image;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.view.MenuItem;
import android.widget.Toast;
import com.example.womensafety.Activities.AboutUsActivity;
import com.example.womensafety.Activities.AddingPostActivity;
import com.example.womensafety.Activities.AdminActivity;
import com.example.womensafety.Activities.LoginActivity;
import com.example.womensafety.Activities.ManageActivity;
import com.example.womensafety.Activities.NextTokinListActivity;
import com.example.womensafety.Activities.SuspectListActivity;
import com.example.womensafety.Activities.TrackingActivity;
import com.example.womensafety.Activities.UserTrackingFragment;
import com.example.womensafety.User.LocationConstants;
import com.example.womensafety.R;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.shivtechs.maplocationpicker.LocationPickerActivity;
import com.shivtechs.maplocationpicker.MapUtility;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class Detail_Forms extends AppCompatActivity {

    Button Access_DisplayTime;
    private int CalenderHour, CalenderMinute;
    String format;
    Calendar calender;
    TimePickerDialog timePickerDialog;
    Button VehicleImageSelector;
    public static final int GET_FROM_GALLERY = 3;
    ImageView imageView;
    TextView timeTextView;

    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference reference;
    String cud;

    Button starting_location_picker;
    Button destination_picker;
    TextView start, end;
    double start_lat=0.0,start_lng=0.0;
    double end_lat=0.0,end_lng=0.0;
    String starting_address,ending_address;
    FusedLocationProviderClient fusedLocationProviderClient;
    int PLACE_PICKER_REQUEST=1;

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;

    TextView tv_latitude, tv_longitude;
    Double latitude, longitude;

    public Uri vehicle_image_uri=null;
    ImageView vehicle_image;

    StorageReference storageReference;
    EditText vehicle;
    String estimated_time;
    String vehicle_num;

    Button save_vehicle;

    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail__forms);

       /* tv_latitude = findViewById(R.id.tv_latitude);
        tv_longitude = findViewById(R.id.tv_longitude);*/

        MapUtility.apiKey = getResources().getString(R.string.google_maps_key);

        auth = FirebaseAuth.getInstance();
        cud= Objects.requireNonNull(auth.getCurrentUser()).getUid();
        database=FirebaseDatabase.getInstance();
        reference=database.getReference().child(cud);
        storageReference= FirebaseStorage.getInstance().getReference();

        starting_location_picker = findViewById(R.id.travelingFrom);
        start = findViewById(R.id.start_loc);
        destination_picker = findViewById(R.id.travelingTo);
        end = findViewById(R.id.destination_text_view);
        vehicle_image=(ImageView)findViewById(R.id.vehicleImage);
        vehicle=(EditText) findViewById(R.id.vehicle_number_edit_text);

        save_vehicle=findViewById(R.id.vehicle_image_saver);

        findViewById(R.id.startButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*if (ContextCompat.checkSelfPermission(
                        getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(
                            Detail_Forms.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            REQUEST_CODE_LOCATION_PERMISSION
                    );
                } else {
                    startLocationService();
                }*/
                vehicle_num=vehicle.getText().toString();

                Intent intent = new Intent(Detail_Forms.this, UserTrackingFragment.class);
                intent.putExtra("start_latitude", start_lat);
                intent.putExtra("start_longitude", start_lng);
                intent.putExtra("end_latitude", end_lat);
                intent.putExtra("end_longitude", end_lng);
                intent.putExtra("start_add",starting_address);
                intent.putExtra("end_add",ending_address);
                if(vehicle_image_uri!=null)
                {intent.putExtra("vehicle_image",vehicle_image_uri.toString());}
                intent.putExtra("veh_num",vehicle_num);
                intent.putExtra("est_time",estimated_time);
                if (start_lat != 0.0 && start_lng != 0.0 && end_lat != 0.0 && end_lng != 0.0) {
                    startActivity(intent);
                    /*Log.d("num",vehicle_num);*/
                } else {
                    Toast.makeText(getApplicationContext(), "Please , Choose your starting and ending locations", Toast.LENGTH_SHORT).show();
                }
            }
        });

        /*findViewById(R.id.reachedSafelyButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopLocationService();
            }
        });*/

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        starting_location_picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(Detail_Forms.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    getLocation();
                } else {
                    ActivityCompat.requestPermissions(Detail_Forms.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
                }

            }
        });

        destination_picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*PlacePicker.IntentBuilder builder=new PlacePicker.IntentBuilder();
                Intent intent;
                try {
                    intent=builder.build(Detail_Forms.this);
                    startActivityForResult(intent,PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }*/

                Intent i = new Intent(Detail_Forms.this, LocationPickerActivity.class);
                startActivityForResult(i, PLACE_PICKER_REQUEST);


            }
        });



        Access_DisplayTime = findViewById(R.id.timePicker);
        timeTextView = findViewById(R.id.timeTextView);


        Access_DisplayTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calender = Calendar.getInstance();
                CalenderHour = calender.get(Calendar.HOUR_OF_DAY);
                CalenderMinute = calender.get(Calendar.MINUTE);

                timePickerDialog = new TimePickerDialog(Detail_Forms.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        if (hourOfDay == 0) {
                            hourOfDay += 12;
                            format = "AM";
                        } else if (hourOfDay == 12) {
                            format = "PM";
                        } else if (hourOfDay > 12) {
                            hourOfDay -= 12;
                            format = "PM";
                        } else {
                            format = "AM";
                        }
                        timeTextView.setVisibility(View.VISIBLE);
                        timeTextView.setText("Estimated time: " + hourOfDay + ":" + minute + " " + format);
                        estimated_time=hourOfDay+":"+minute+format;
                    }
                }, CalenderHour, CalenderMinute, false);
                timePickerDialog.show();
            }
        });

        VehicleImageSelector = findViewById(R.id.vehicleImageSelector);
        VehicleImageSelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*startActivityForResult(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY);*/
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(20, 20)
                        .setMinCropResultSize(512, 512)
                        .start(Detail_Forms.this);
            }

        });

        save_vehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(vehicle_image_uri!=null){
                    StorageReference vehicle_image_ref=storageReference.child("user_vehicle_images").child(FieldValue.serverTimestamp().toString()+".jpg");
                    vehicle_image_ref.putFile(vehicle_image_uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(getApplicationContext(), "your vehicle image is successfully added", Toast.LENGTH_SHORT).show();
                            }else
                            {
                                Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else{
                    Toast.makeText(getApplicationContext(),"please add a vehicle image",Toast.LENGTH_SHORT).show();
                }
            }
        });

        setUpToolbar();
        navigationView = findViewById(R.id.navigationMenu);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.nav_home:
                        startActivity(new Intent(Detail_Forms.this, AdminActivity.class));
                        break;

                    case R.id.travellingALone:
                        break;

                    case R.id.nav_suspectRegistration:
                        startActivity(new Intent(Detail_Forms.this, SuspectListActivity.class));
                        break;
                    case R.id.nav_nextToKin:
                        startActivity(new Intent(Detail_Forms.this, NextTokinListActivity.class));
                        break;

                    case R.id.nav_manageAccount:
                        startActivity(new Intent(Detail_Forms.this, ManageActivity.class));
                        break;

                    case R.id.nav_aboutUs:
                        startActivity(new Intent(Detail_Forms.this, AboutUsActivity.class));
                        break;

                    case R.id.nav_logout:
                        auth.signOut();
                        startActivity(new Intent(Detail_Forms.this, LoginActivity.class));
                        finish();
                        break;
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

    }

    private void getLocation() {
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
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                   Location location=task.getResult();
                   if(location!=null)
                   {
                       try {
                           Geocoder geocoder=new Geocoder(Detail_Forms.this, Locale.getDefault());
                           List<Address> addressList= geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);

                           start.setText(addressList.get(0).getAddressLine(0));
                           start_lat=addressList.get(0).getLatitude();
                           start_lng=addressList.get(0).getLongitude();
                           starting_address=addressList.get(0).getAddressLine(0);
                           Log.d("start_latitude", String.valueOf(start_lat));
                           Log.d("start_longitude", String.valueOf(start_lng));
                       }
                       catch (IOException e) {
                           e.printStackTrace();
                       }
                   }
            }
        });
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            latitude = Double.valueOf(intent.getStringExtra("latitude"));
            longitude = Double.valueOf(intent.getStringExtra("longitude"));


            tv_latitude.setText(latitude + "");
            tv_longitude.setText(longitude + "");
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(broadcastReceiver, new IntentFilter(LocationService.str_receiver));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationService();
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
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


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        /*if (requestCode == GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            imageView = findViewById(R.id.vehicleImage);
            imageView.setVisibility(View.VISIBLE);
            imageView.setImageURI(data.getData());
        }*/

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                vehicle_image.setVisibility(View.VISIBLE);
                vehicle_image_uri = result.getUri();
                vehicle_image.setImageURI(vehicle_image_uri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(getApplicationContext(), result.getError().toString(), Toast.LENGTH_SHORT).show();
            }
        }

        /*if(requestCode==PLACE_PICKER_REQUEST)
        {
            if(requestCode==RESULT_OK)
            {
                *//*Place place=PlacePicker.getPlace(this,data);
                String address=place.getName().toString();
                end_lat=place.getLatLng().latitude;
                end_lng=place.getLatLng().longitude;
                Log.d("end_latitude", String.valueOf(end_lat));
                Log.d("end_longitude", String.valueOf(end_lng));
                end.setText(address);*//*
            }
        }*/

        if (requestCode == PLACE_PICKER_REQUEST) {
            try {
                if (data != null && data.getStringExtra(MapUtility.ADDRESS) != null) {
                    // String address = data.getStringExtra(MapUtility.ADDRESS);
                    end_lat = data.getDoubleExtra(MapUtility.LATITUDE, 0.0);
                    end_lng = data.getDoubleExtra(MapUtility.LONGITUDE, 0.0);
                    Log.d("end_latitude", String.valueOf(end_lat));
                    Log.d("end_longitude", String.valueOf(end_lng));
                    Bundle completeAddress =data.getBundleExtra("fullAddress");
                    /* data in completeAddress bundle
                    "fulladdress"
                    "city"
                    "state"
                    "postalcode"
                    "country"
                    "addressline1"
                    "addressline2"
                     */
                    /*txtAddress.setText(new StringBuilder().append("addressline2: ").append
                            (completeAddress.getString("addressline2")).append("\ncity: ").append
                            (completeAddress.getString("city")).append("\npostalcode: ").append
                            (completeAddress.getString("postalcode")).append("\nstate: ").append
                            (completeAddress.getString("state")).toString());*/
                    end.setText(completeAddress.getString("addressline2"));

                    ending_address=completeAddress.getString("addressline2");

                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

    }


    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void setUpToolbar() {
        drawerLayout = findViewById(R.id.drawerLayout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
        actionBarDrawerToggle.syncState();
    }

}
