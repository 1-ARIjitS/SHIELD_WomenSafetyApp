package com.example.womensafety.Activities;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.speech.RecognizerIntent;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.womensafety.Adapters.postAdapter;
import com.example.womensafety.Services.SosService;
import com.example.womensafety.Services.service;
import com.example.womensafety.User.Detail_Forms;
import com.example.womensafety.Models.posts;
import com.example.womensafety.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class AdminActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;
    View hView;
    TextView Username;
    int counter = 0;

    /*SwitchCompat dark_mode_switch;*/
    FloatingActionButton sos;

    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference reference;
    DatabaseReference kinContacts;
    DatabaseReference uvcReference;
    FirebaseFirestore firestore;
    RecyclerView post_rec;
    FloatingActionButton fab;

    //location latitude and longitude
    FusedLocationProviderClient fusedLocationProviderClient;
    String sos_message_address;
    double sos_message_latitude = 0.0;
    double sos_message_longitude = 0.0;
    String kinCon;
    List<String> contactsList;
    String sos_message;

    List<posts> mList;
    postAdapter adapter;
    String user;
    String cud;

    public Query query;

    public ListenerRegistration listenerRegistration;

    String unique_verification_code;

    //speech to text values
    public static final String HELP_SOS_STRING="help help help";
    String result_string;

    //sos service buttons
    Button sos_start,sos_end;


    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);


        auth = FirebaseAuth.getInstance();
        cud = Objects.requireNonNull(auth.getCurrentUser()).getUid();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("registered_users");
        uvcReference = database.getReference("registered_users").child(cud);
        kinContacts = database.getReference("Next To kin").child(cud);
        firestore = FirebaseFirestore.getInstance();

        //location values
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        post_rec = (RecyclerView) findViewById(R.id.post_recycler);
        fab = (FloatingActionButton) findViewById(R.id.fab_button);

        sos = (FloatingActionButton) findViewById(R.id.sos_fab);


        setUpToolbar();
        navigationView = findViewById(R.id.navigationMenu);

       /* hView = navigationView.getHeaderView(0);
        Username = hView.findViewById(R.id.header_username);

        final String cud = Objects.requireNonNull(auth.getCurrentUser()).getUid();

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    user = Objects.requireNonNull(snapshot.child(cud).child("full_name").getValue()).toString();
                } catch (Exception e) {
                    Intent intent = getIntent();
                    user = intent.getStringExtra("user");
                }
                Username.setText(user);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/

        //kin contacts snap from firebase realtime database

        contactsList = new ArrayList<String>();
        kinContacts.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot kinConSnap : snapshot.getChildren()) {
                    kinCon = Objects.requireNonNull(kinConSnap.child("mobile_number").getValue()).toString();
                    Log.d("contacts", kinCon);
                    contactsList.add(kinCon);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        uvcReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                unique_verification_code= Objects.requireNonNull(snapshot.child("mUVC").getValue()).toString();
                Log.d("uvc_db",unique_verification_code);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.nav_home:
                        break;
                    case R.id.travellingALone:
                        startActivity( new Intent(AdminActivity.this, Detail_Forms.class));
                        break;

                    case R.id.nav_suspectRegistration:
                        startActivity(new Intent(AdminActivity.this, SuspectListActivity.class));
                        break;

                    case R.id.nav_nextToKin:
                        startActivity(new Intent(AdminActivity.this, NextTokinListActivity.class));
                        break;

                    case R.id.nav_aboutUs:
                        startActivity(new Intent(AdminActivity.this, AboutUsActivity.class));
                        break;

                    case R.id.nav_emergencyContacts:
                        startActivity(new Intent(AdminActivity.this, EmergencyContactListActivity.class));
                        break;

                    case R.id.nav_settings:
                        startActivity(new Intent(AdminActivity.this, SettingsActivity.class));
                        break;
                    case R.id.nav_manageAccount:
                        startActivity(new Intent(AdminActivity.this, ManageActivity.class));
                        break;

                    case R.id.nav_travelLog:
                        startActivity(new Intent(AdminActivity.this, TravelLogContent.class));
                        break;

                    case R.id.nav_logout:
                        auth.signOut();
                        startActivity(new Intent(AdminActivity.this, LoginActivity.class));
                        finish();
                        break;
                }

                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        //sos service switch operations starting here

        ActivityCompat.requestPermissions(AdminActivity.this, new String[]{Manifest.permission.SEND_SMS}, PackageManager.PERMISSION_GRANTED);
        ActivityCompat.requestPermissions(AdminActivity.this, new String[]{Manifest.permission.RECORD_AUDIO}, PackageManager.PERMISSION_GRANTED);
        ActivityCompat.requestPermissions(AdminActivity.this, new String[]{Manifest.permission.FOREGROUND_SERVICE}, PackageManager.PERMISSION_GRANTED);
        Dexter.withContext(getApplicationContext())
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        getLocation();
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

        /*Intent intent = new Intent(AdminActivity.this, SosService.class);*/

        //sos fab button operations start here
        sos.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.DONUT)
            @Override
            public void onClick(View v) {

                //close navigation drawer
                drawerLayout.closeDrawer(GravityCompat.START);

                //popup message for verifying uvc starts here

                /*EditText uvc=new EditText(v.getContext());
                uvc.setHint("Unique Verification Code");*/
                AlertDialog.Builder uvc_verification=new AlertDialog.Builder(v.getContext());
                View mView=getLayoutInflater().inflate(R.layout.custom_alert_dialog,null);
                EditText uvc=(EditText)mView.findViewById(R.id.uvc_edit_text);
                Button confirm=mView.findViewById(R.id.confirm);
                /*uvc_verification.setTitle("ARE YOU SURE,YOU WANT TO SEND AN SOS MESSAGE?");
                uvc_verification.setMessage("Enter your Unique Verification Code to send an SOS message");*/
                uvc_verification.setView(mView);

                confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String uvcEntered=uvc.getText().toString();
                        if (sos_message_address != null && sos_message_latitude != 0.0 && sos_message_longitude != 0.0 && uvcEntered.equals(unique_verification_code)) {
                            sos_message = "HELP!!!" + "\n"
                                    + "SHIELD SOS SERVICE " + "\n"
                                    + "Here is the LATITUDE and LONGITUDE of the user along with the CURRENT LOCATION  PLEASE HELP " + "\n"
                                    + "LATITUDE:- " + sos_message_latitude + "\n"
                                    + "LONGITUDE:- " + sos_message_longitude + "\n"
                                    + "CURRENT LOCATION:- " + sos_message_address;
                    /*for(int i=0;i<contactsList.size();i++) {
                        try{*/
                       /* }catch (Exception e)
                        {
                            if (e.toString().contains(Manifest.permission.READ_PHONE_STATE) && ContextCompat
                                    .checkSelfPermission(AdminActivity.this, Manifest.permission.READ_PHONE_STATE)!=
                                    PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(AdminActivity.this, new String[] {Manifest.permission
                                        .READ_PHONE_STATE}, PackageManager.PERMISSION_GRANTED);
                        }}}*/

                            Log.d("sos_message", sos_message);
                            Toast.makeText(getApplicationContext(), "Sending SOS Messages .....", Toast.LENGTH_SHORT).show();
                            for (String mob_numbers : contactsList) {
                                SmsManager smsManager = SmsManager.getDefault();
                                ArrayList<String> parts = smsManager.divideMessage(sos_message);
                                smsManager.sendMultipartTextMessage(mob_numbers, null, parts, null, null);
                            }
                            Toast.makeText(getApplicationContext(), "Emergency SOS Messages Sent", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(getApplicationContext(), "Message can not be sent,please check if location is turned on", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                uvc_verification.create().show();


                /*if (sos_message_address != null && sos_message_latitude != 0.0 && sos_message_longitude != 0.0) {
                    sos_message = "HELP!!!" + "\n"
                            + "SHIELD SOS SERVICE " + "\n"
                            + "Here is the LATITUDE and LONGITUDE of the user along with the CURRENT LOCATION  PLEASE HELP " + "\n"
                            + "LATITUDE:- " + sos_message_latitude + "\n"
                            + "LONGITUDE:- " + sos_message_longitude + "\n"
                            + "CURRENT LOCATION:- " + sos_message_address;
                    *//*for(int i=0;i<contactsList.size();i++) {
                        try{*//*
                       *//* }catch (Exception e)
                        {
                            if (e.toString().contains(Manifest.permission.READ_PHONE_STATE) && ContextCompat
                                    .checkSelfPermission(AdminActivity.this, Manifest.permission.READ_PHONE_STATE)!=
                                    PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(AdminActivity.this, new String[] {Manifest.permission
                                        .READ_PHONE_STATE}, PackageManager.PERMISSION_GRANTED);
                        }}}*//*

                    Log.d("sos_message", sos_message);

                    for (String mob_numbers : contactsList) {
                        SmsManager smsManager = SmsManager.getDefault();
                        ArrayList<String> parts = smsManager.divideMessage(sos_message);
                        smsManager.sendMultipartTextMessage(*//*contactsList.get(i)*//*mob_numbers, null, parts, null, null);
                    }
                    Toast.makeText(getApplicationContext(), "Emergency SOS Messages Sent to next to kin", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(getApplicationContext(), "Message can not be sent", Toast.LENGTH_SHORT).show();
                }*/
                    }
                });


        //on long press listener for google text to speech that would open up/trigger sos message for users

        sos.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,Locale.getDefault());
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"speak HELP 3 Times To Trigger The SOS Message");
                try {
                    startActivityForResult(intent,1);
                }catch (ActivityNotFoundException e)
                {
                    e.printStackTrace();
                }
                return false;
            }
        });

        //sos service events starting here

        sos_start=findViewById(R.id.start_sos);
        sos_end=findViewById(R.id.stop_sos);

        sos_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder autostartDialog=new AlertDialog.Builder(v.getContext());
                autostartDialog.setTitle("ENABLE APP AUTOSTART");
                autostartDialog.setMessage("Enable the autostart settings for shield SOS services to work perfectly");

                autostartDialog.setPositiveButton("ENABLE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);
                    }
                });

                autostartDialog.setNegativeButton("ALREADY ENABLED", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent serviceIntent=new Intent(AdminActivity.this, SosService.class);
                        startForegroundService(serviceIntent);
                    }
                });

                autostartDialog.create().show();
            }
        });

        //ending the sos service
        sos_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent serviceIntent=new Intent(AdminActivity.this, SosService.class);

                stopService(serviceIntent);
            }
        });


        //recycler view operations starting here


        mList = new ArrayList<posts>();

        adapter = new postAdapter(AdminActivity.this, mList);

        post_rec.setHasFixedSize(true);
        post_rec.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        post_rec.setAdapter(adapter);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminActivity.this, AddingPostActivity.class));
            }
        });

        if (auth.getCurrentUser() != null) {

            post_rec.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    boolean isBottom;
                    isBottom = !post_rec.canScrollVertically(1);
                    if (isBottom)
                        Toast.makeText(AdminActivity.this, "no more posts to show", Toast.LENGTH_SHORT).show();
                }
            });


            query = firestore.collection("Posts").orderBy("time", Query.Direction.DESCENDING);

            listenerRegistration = query.addSnapshotListener(AdminActivity.this, new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                    assert value != null;
                    for (DocumentChange doc : value.getDocumentChanges()) {
                        if (doc.getType() == DocumentChange.Type.ADDED) {
                            String postId = doc.getDocument().getId();
                            posts posts = doc.getDocument().toObject(posts.class).withId(postId);
                            mList.add(posts);
                        }
                        adapter.notifyDataSetChanged();
                    }

                    listenerRegistration.remove();
                }
            });
        }


    }

    /*private void sendSms() {
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(*//*contactsList.get(i)*//*,null,sos_message,null,null);
        Toast.makeText(getApplicationContext(),"Emergency SOS Message Sent",Toast.LENGTH_SHORT).show();
    }*/

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
                Location location = task.getResult();
                if (location != null) {
                    Geocoder geocoder = new Geocoder(AdminActivity.this, Locale.getDefault());
                    try {
                        List<Address> addressList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        sos_message_latitude = addressList.get(0).getLatitude();
                        sos_message_longitude = addressList.get(0).getLongitude();
                        sos_message_address = addressList.get(0).getAddressLine(0);

                        Log.d("lat", String.valueOf(sos_message_latitude));
                        Log.d("long", String.valueOf(sos_message_longitude));
                        Log.d("address", sos_message_address);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }


    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(startMain);
        }
    }

    public void setUpToolbar() {
        drawerLayout = findViewById(R.id.drawerLayout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.textColor));
        actionBarDrawerToggle.syncState();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_POWER) {
            counter++;
            if (counter == 3) {
                for (String mob_numbers : contactsList) {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(/*contactsList.get(i)*/mob_numbers, null, sos_message, null, null);
                }
                Toast.makeText(getApplicationContext(), "Emergency SOS Messages Sent to next to kin", Toast.LENGTH_SHORT).show();
                counter = 0;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if (resultCode == RESULT_OK && null != data) {
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                result_string = result.get(0);

                if(result_string.equals(HELP_SOS_STRING))
                {
                    //close navigation drawer
                    drawerLayout.closeDrawer(GravityCompat.START);
                    //open alert dialog
                    AlertDialog.Builder uvc_verification=new AlertDialog.Builder(this);
                    View mView=getLayoutInflater().inflate(R.layout.custom_alert_dialog,null);
                    EditText uvc=(EditText)mView.findViewById(R.id.uvc_edit_text);
                    Button confirm=mView.findViewById(R.id.confirm);
                    uvc_verification.setView(mView);
                    confirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String uvcEntered=uvc.getText().toString();
                            if (sos_message_address != null && sos_message_latitude != 0.0 && sos_message_longitude != 0.0 && uvcEntered.equals(unique_verification_code)) {
                                sos_message = "HELP!!!" + "\n"
                                        + "SHIELD SOS SERVICE " + "\n"
                                        + "Here is the LATITUDE and LONGITUDE of the user along with the CURRENT LOCATION  PLEASE HELP " + "\n"
                                        + "LATITUDE:- " + sos_message_latitude + "\n"
                                        + "LONGITUDE:- " + sos_message_longitude + "\n"
                                        + "CURRENT LOCATION:- " + sos_message_address;
                                Toast.makeText(getApplicationContext(), "Sending SOS Messages .....", Toast.LENGTH_SHORT).show();
                                for (String mob_numbers : contactsList) {
                                    SmsManager smsManager = SmsManager.getDefault();
                                    ArrayList<String> parts = smsManager.divideMessage(sos_message);
                                    smsManager.sendMultipartTextMessage(mob_numbers, null, parts, null, null);
                                }
                                Toast.makeText(getApplicationContext(), "Emergency SOS Messages Sent", Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(getApplicationContext(), "Message can not be sent,please check if location is turned on", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    uvc_verification.create().show();}
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}

