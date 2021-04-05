package com.example.womensafety.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DownloadManager;
import android.content.SharedPreferences;
import android.view.MenuItem;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.womensafety.Adapters.postAdapter;
import com.example.womensafety.Detail_Forms;
import com.example.womensafety.Models.posts;
import com.example.womensafety.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AdminActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;
    View hView;
    TextView Username;

    SwitchCompat dark_mode_switch;
    SharedPreferences sharedPreferences=null;

    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference reference;
    FirebaseFirestore firestore;
    RecyclerView post_rec;
    FloatingActionButton fab;

    List<posts> mList;
    postAdapter adapter;
    String user;

    public Query query;

    public ListenerRegistration listenerRegistration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);


        auth = FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        reference=database.getReference("registered_users");
        firestore=FirebaseFirestore.getInstance();

        post_rec = (RecyclerView) findViewById(R.id.post_recycler);
        fab = (FloatingActionButton) findViewById(R.id.fab_button);


        setUpToolbar();
        navigationView = findViewById(R.id.navigationMenu);

        hView=navigationView.getHeaderView(0);
        Username=hView.findViewById(R.id.header_username);

        final String cud= Objects.requireNonNull(auth.getCurrentUser()).getUid();

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user= Objects.requireNonNull(snapshot.child(cud).child("full_name").getValue()).toString();
                Username.setText(user);
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
                        Intent intent1 = new Intent(AdminActivity.this, Detail_Forms.class);
                        startActivity(intent1);
                        break;

                    case R.id.nav_suspectRegistration:
                        Intent intent2 = new Intent(AdminActivity.this, SuspectListActivity.class);
                        startActivity(intent2);
                        break;

                    case R.id.nav_nextToKin:
                        Intent intent3 = new Intent(AdminActivity.this, NextTokinListActivity.class);
                        startActivity(intent3);
                        break;

                    case R.id.nav_aboutUs:
                        Intent intent4 = new Intent(AdminActivity.this, AboutUsActivity.class);
                        startActivity(intent4);
                        break;

                    case R.id.nav_logout:
                        auth.signOut();
                        startActivity(new Intent(AdminActivity.this, LoginActivity.class));
                        finish();
                        break;

                    case R.id.nav_switch:
                        item.setActionView(R.layout.dark_mode_switch);
                        dark_mode_switch=item.getActionView().findViewById(R.id.dark_mode_btn);
                        sharedPreferences=getSharedPreferences("night",0);
                        boolean bool=sharedPreferences.getBoolean("night_mode",true);
                        if(bool)
                        {
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                            dark_mode_switch.setChecked(true);
                        }
                        dark_mode_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                if(isChecked)
                                {
                                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                                    dark_mode_switch.setChecked(true);
                                    SharedPreferences.Editor editor= sharedPreferences.edit();
                                    editor.putBoolean("night_mode",true);
                                    editor.apply();
                                }else
                                {
                                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                                    dark_mode_switch.setChecked(false);
                                    SharedPreferences.Editor editor= sharedPreferences.edit();
                                    editor.putBoolean("night_mode",false);
                                    editor.apply();
                                }
                            }
                        });
                        break;

                }

                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        //recycler view operations starting here

        mList= new ArrayList<posts>();

        adapter=new postAdapter(AdminActivity.this,mList);

        post_rec.setHasFixedSize(true);
        post_rec.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        post_rec.setAdapter(adapter);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminActivity.this, AddingPostActivity.class));
            }
        });

        if(auth.getCurrentUser()!=null)
        {

            post_rec.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    Boolean isBottom;
                    isBottom=!post_rec.canScrollVertically(1);
                    if(isBottom)
                        Toast.makeText(AdminActivity.this,"no more posts to show",Toast.LENGTH_SHORT).show();
                }
            });


            query=firestore.collection("Posts").orderBy("time", Query.Direction.DESCENDING);

            listenerRegistration=query.addSnapshotListener(AdminActivity.this, new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                     for(DocumentChange doc:value.getDocumentChanges())
                     {
                         if(doc.getType()==DocumentChange.Type.ADDED)
                         {
                             String postId=doc.getDocument().getId();
                             posts posts= doc.getDocument().toObject(posts.class).withId(postId);
                             mList.add(posts);
                         }
                         adapter.notifyDataSetChanged();
                     }

                     listenerRegistration.remove();
                }
            });
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

