package com.example.womensafety.Activities;

import android.content.Intent;
import android.os.Build;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.womensafety.Adapters.commentsAdapter;
import com.example.womensafety.Models.comments;
import com.example.womensafety.R;
import com.example.womensafety.User.Detail_Forms;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CommentActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;

    /*SwitchCompat dark_mode_switch;*/
    Button dark_mode_switch;

    EditText add_comment;

    Button add_comment_button;

    RecyclerView commentRecycler;

    FirebaseAuth auth;

    FirebaseFirestore firestore;

    String currentUserId;

    String post_id;

    commentsAdapter adapter;

    List<comments> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        auth=FirebaseAuth.getInstance();

        firestore=FirebaseFirestore.getInstance();

        currentUserId= Objects.requireNonNull(auth.getCurrentUser()).getUid();
        post_id=getIntent().getStringExtra("postid");

        mList=new ArrayList<comments>();

        adapter=new commentsAdapter(CommentActivity.this,mList);

        add_comment=(EditText)findViewById(R.id.comment_add_comment);
        add_comment_button=(Button)findViewById(R.id.comment_button);
        commentRecycler=findViewById(R.id.commentRecycler);
        commentRecycler.setHasFixedSize(true);
        commentRecycler.setLayoutManager(new LinearLayoutManager(this));

        commentRecycler.setAdapter(adapter);

        firestore.collection("Posts/"+post_id+"/Comments").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                assert value != null;
                for (DocumentChange doc:value.getDocumentChanges())
               {
                   if(doc.getType()==DocumentChange.Type.ADDED)
                   {
                       comments comments=doc.getDocument().toObject(com.example.womensafety.Models.comments.class);
                       mList.add(comments);
                   }
                   adapter.notifyDataSetChanged();
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
                        startActivity(new Intent(CommentActivity.this, AdminActivity.class));
                        break;

                    case R.id.travellingALone:
                        startActivity(new Intent(CommentActivity.this, Detail_Forms.class));
                        break;

                    case R.id.nav_suspectRegistration:
                        startActivity(new Intent(CommentActivity.this, SuspectListActivity.class));
                        break;
                    case R.id.nav_emergencyContacts:
                        startActivity(new Intent(CommentActivity.this, EmergencyContactListActivity.class));
                        break;

                    case R.id.nav_nextToKin:
                        startActivity(new Intent(CommentActivity.this, NextTokinListActivity.class));
                        break;

                        /*
                    case R.id.nav_manageAccount:
                        startActivity(new Intent(CommentActivity.this, ManageActivity.class));
                        break;


                         */
                    case R.id.nav_settings:
                        startActivity(new Intent(CommentActivity.this, SettingsActivity.class));
                        break;

                    case R.id.nav_travelLog:
                        startActivity(new Intent(CommentActivity.this, TravelLogContent.class));
                        break;

                    case R.id.nav_aboutUs:
                        startActivity(new Intent(CommentActivity.this, AboutUsActivity.class));
                        break;

                    case R.id.nav_logout:
                        auth.signOut();
                        startActivity(new Intent(CommentActivity.this, LoginActivity.class));
                        finish();
                        break;
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });



        add_comment_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment=add_comment.getText().toString();
                int comment_size=comment.length();

                if(comment_size>0)
                {
                    Map<String,Object> commentMap=new HashMap<>();
                    commentMap.put("comment",comment);
                    commentMap.put("time", FieldValue.serverTimestamp());
                    commentMap.put("user", currentUserId);
                   firestore.collection("Posts/"+post_id+"/Comments").add(commentMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                       @Override
                       public void onComplete(@NonNull Task<DocumentReference> task) {
                          if(task.isSuccessful())
                          {
                              Toast.makeText(getApplicationContext(),"comment added",Toast.LENGTH_SHORT).show();
                          }
                          else
                          {
                              Toast.makeText(getApplicationContext(), Objects.requireNonNull(task.getException()).getMessage(),Toast.LENGTH_SHORT).show();
                          }
                       }
                   });
                }else{
                    Toast.makeText(getApplicationContext(),"please write a comment for the post",Toast.LENGTH_SHORT).show();
                }
            }
        });

        dark_mode_switch=(Button) findViewById(R.id.dar);

        dark_mode_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.Q)
                {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                }
                else{
                    if(AppCompatDelegate.getDefaultNightMode()==AppCompatDelegate.MODE_NIGHT_YES)
                    {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    }
                    else
                    {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    }
                }}
        });
    }


    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            startActivity(new Intent(getApplicationContext(),AdminActivity.class));
            /* super.onBackPressed();*/
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
}