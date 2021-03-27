package com.example.womensafety.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DownloadManager;
import android.view.MenuItem;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.womensafety.Adapters.postAdapter;
import com.example.womensafety.Detail_Forms;
import com.example.womensafety.Models.posts;
import com.example.womensafety.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
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

public class AdminActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;

    FirebaseAuth auth;
    FirebaseFirestore firestore;
    RecyclerView post_rec;
    FloatingActionButton fab;

    List<posts> mList;
    postAdapter adapter;

    public Query query;

    public ListenerRegistration listenerRegistration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        auth = FirebaseAuth.getInstance();
        firestore=FirebaseFirestore.getInstance();

        post_rec = (RecyclerView) findViewById(R.id.post_recycler);
        fab = (FloatingActionButton) findViewById(R.id.fab_button);

        setUpToolbar();
        navigationView = findViewById(R.id.navigationMenu);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.nav_home:
                        break;

                    case R.id.travellingALone:
                        startActivity(new Intent(AdminActivity.this, Detail_Forms.class));
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

