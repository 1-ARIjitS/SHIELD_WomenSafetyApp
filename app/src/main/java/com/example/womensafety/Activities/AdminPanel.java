package com.example.womensafety.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.womensafety.R;
import com.example.womensafety.SuperAdmin.Activities.SuperAdminSettingsActivity;
import com.example.womensafety.SuperAdmin.Activities.SuperAdminUsersActivity;
import com.example.womensafety.SuperAdmin.Adapters.SuperAdminPostAdapter;
import com.example.womensafety.SuperAdmin.Models.SuperadminPosts;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AdminPanel extends AppCompatActivity {
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;
    FirebaseAuth auth;
    FirebaseFirestore firestore;
    FirebaseDatabase database;
    DatabaseReference reference;
    RecyclerView super_admin_post_rec;
    List<SuperadminPosts> mList;
    SuperAdminPostAdapter adapter;
    String user;
    public Query query;
    public ListenerRegistration listenerRegistration;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);
        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        reference=database.getReference("registered_users");
        firestore=FirebaseFirestore.getInstance();
        setUpToolbar();
        navigationView = findViewById(R.id.navigationMenu);
        /*hView=navigationView.getHeaderView(0);
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
        });*/

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.superadmin_home:
                        break;
                    case R.id.superadmin_manage_account:
                        startActivity( new Intent(AdminPanel.this, SelectUserActivity.class));
                        break;
                    case R.id.superadmin_manage_users:
                        startActivity(new Intent(AdminPanel.this, SuperAdminUsersActivity.class));
                        break;
                    case R.id.superadmin_settings:
                        startActivity(new Intent(AdminPanel.this, SuperAdminSettingsActivity.class));
                        break;
                    case R.id.superadmin_logout:
                        auth.signOut();
                        startActivity(new Intent(AdminPanel.this, SelectUserActivity.class));
                        finish();
                        break;
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
        //recycler view operations starting here

        super_admin_post_rec=(RecyclerView)findViewById(R.id.super_admin_post_recycler);

        mList=new ArrayList<SuperadminPosts>();

        adapter=new SuperAdminPostAdapter(AdminPanel.this,mList);

        super_admin_post_rec.setHasFixedSize(true);
        super_admin_post_rec.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        super_admin_post_rec.setAdapter(adapter);

        super_admin_post_rec.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                boolean isBottom;
                isBottom=!super_admin_post_rec.canScrollVertically(1);
                if(isBottom)
                    Toast.makeText(AdminPanel.this,"no more posts to show",Toast.LENGTH_SHORT).show();
            }
        });


        query=firestore.collection("Posts").orderBy("time", Query.Direction.DESCENDING);

        listenerRegistration=query.addSnapshotListener(AdminPanel.this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                assert value != null;
                for(DocumentChange doc:value.getDocumentChanges())
                {
                    if(doc.getType()==DocumentChange.Type.MODIFIED||doc.getType()==DocumentChange.Type.REMOVED||doc.getType()==DocumentChange.Type.ADDED)
                    {
                        /*String postId=doc.getDocument().getId();
                        posts posts= doc.getDocument().toObject(posts.class).withId(postId);
                        mList.add(posts);*/
                        SuperadminPosts p=doc.getDocument().toObject(SuperadminPosts.class);
                        p.setId(doc.getDocument().getId());
                        mList.add(p);
                    }
                    adapter.notifyDataSetChanged();
                }

                listenerRegistration.remove();
            }
        });


        /*firestore.collection("Posts").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                   if(!queryDocumentSnapshots.isEmpty())
                   {
                       List<DocumentSnapshot>list=queryDocumentSnapshots.getDocuments();

                       for (DocumentSnapshot d : list)
                       {
                           SuperadminPosts p=d.toObject(SuperadminPosts.class);
                           mList.add(p);
                       }
                   }
            }
        });*/

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