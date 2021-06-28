package com.example.womensafety.SuperAdmin.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.womensafety.Activities.LoginActivity;
import com.example.womensafety.Activities.SelectUserActivity;
import com.example.womensafety.Interfaces.userListItemClickInterface;
import com.example.womensafety.Models.users;
import com.example.womensafety.R;
import com.example.womensafety.SuperAdmin.Adapters.ManageUsersAdapter;
import com.example.womensafety.User.Detail_Forms;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SuperAdminUsersActivity extends AppCompatActivity implements userListItemClickInterface {

    RecyclerView userList;
    ArrayList<users> users_reg;

    ManageUsersAdapter adapter;

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;

    TextView total_users;
    FirebaseDatabase database;
    DatabaseReference reference;

    int num=0;

    public String mobile;
    public String name;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_super_admin_users);

        total_users=(TextView)findViewById(R.id.total_users);

        auth=FirebaseAuth.getInstance();


        database=FirebaseDatabase.getInstance();
        reference=database.getReference("registered_users");

        setUpToolbar();
        navigationView = findViewById(R.id.navigationMenu);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.superadmin_homepage:
                        startActivity(new Intent(SuperAdminUsersActivity.this, SuperAdminHomepage.class));
                        break;

                    case R.id.superadmin_home:
                        startActivity(new Intent(SuperAdminUsersActivity.this, SuperAdminDashboardActivity.class));
                        break;
                    case R.id.superadmin_manage_account:
                        startActivity( new Intent(SuperAdminUsersActivity.this, ManageSuperAdminAccountActivity.class));
                        break;

                    case R.id.superadmin_manage_admin:
                        startActivity(new Intent(SuperAdminUsersActivity.this, ManageAdminActivity.class));
                        break;

                    case R.id.superadmin_manage_users:
                        break;

                    case R.id.superadmin_manage_superadmin:
                        startActivity(new Intent(SuperAdminUsersActivity.this, ManageSuperAdminActivity.class));
                        break;

                    case R.id.superadmin_settings:
                        startActivity(new Intent(SuperAdminUsersActivity.this, SuperAdminSettingsActivity.class));
                        break;
                    case R.id.superadmin_logout:
                        auth.signOut();
                        startActivity(new Intent(SuperAdminUsersActivity.this, LoginActivity.class));
                        finish();
                        break;
                }

                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        //count for total number of users

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    num=(int)snapshot.getChildrenCount();
                    total_users.setText( "Total Users registered - "+Integer.toString(num));
                }else
                {
                    total_users.setText("Total Users registered - 0");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        // list view operations starting here

        userList=(RecyclerView) findViewById(R.id.users_list);

        userList.setLayoutManager(new LinearLayoutManager(this));

        users_reg=new ArrayList<users>();

        adapter=new ManageUsersAdapter(SuperAdminUsersActivity.this,users_reg,this);

        userList.setAdapter(adapter);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot userSnap : snapshot.getChildren())
                {
                    users user=userSnap.getValue(users.class);
                    users_reg.add(user);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //setting on click listeners on the list items so that it will be redirected to the details of that particular user
       /* userList
        .setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                users user=users_reg.get(position);
                mobile=user.getMobile_number();
                name=user.getFull_name();
                *//*Log.d("user_e_mail", e_mail );*//*
                final Intent intent=new Intent(SuperAdminUsersActivity.this,UserDetailsActivity.class);
                intent.putExtra("mob",mobile);
                intent.putExtra("username",name);
*//*
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot use : snapshot.getChildren())
                        {
                            if(use.child("email_id").getValue().toString().equals(e_mail)){
                                user_specific_id= use.getKey();
                                *//**//*uidg=uidg+user_specific_id;
                                intent.putExtra("user_id",uidg);*//**//*
         *//**//*intent.putExtra("user_id",""+user_specific_id);*//**//*
         *//**//*Log.d("uid", user_specific_id );
                                Log.d("user_email",use.child("email_id").getValue().toString() );*//**//*
                                Log.d("userId",use.getKey());
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
                uidg="";
                uidg+=user_specific_id;
                intent.putExtra("user_id",uidg);
*//**//*              Intent intent=new Intent(SuperAdminUsersActivity.this,UserDetailsActivity.class);
                intent.putExtra("username",name);*//**//*
         *//**//*intent.putExtra("userId",user_specific_id);*//*
                startActivity(intent);
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
        actionBarDrawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.textColor));
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onItemClick(int position) {
        users user=users_reg.get(position);

        mobile=user.getMobile_number();
        name=user.getFull_name();

        /*Log.d("user_e_mail", e_mail );*/

        final Intent intent=new Intent(SuperAdminUsersActivity.this,UserDetailsActivity.class);
        intent.putExtra("mob",mobile);
        intent.putExtra("username",name);
/*
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot use : snapshot.getChildren())
                        {
                            if(use.child("email_id").getValue().toString().equals(e_mail)){
                                user_specific_id= use.getKey();
                                *//*uidg=uidg+user_specific_id;
                                intent.putExtra("user_id",uidg);*//*
         *//*intent.putExtra("user_id",""+user_specific_id);*//*
         *//*Log.d("uid", user_specific_id );
                                Log.d("user_email",use.child("email_id").getValue().toString() );*//*
                                Log.d("userId",use.getKey());
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
                uidg="";
                uidg+=user_specific_id;
                intent.putExtra("user_id",uidg);
*//*              Intent intent=new Intent(SuperAdminUsersActivity.this,UserDetailsActivity.class);
                intent.putExtra("username",name);*//*
         *//*intent.putExtra("userId",user_specific_id);*/
        startActivity(intent);
    }

    @Override
    public void onLongItemClick(int position) {

    }
}












/*
package com.example.womensafety.SuperAdmin.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.womensafety.Activities.LoginActivity;
import com.example.womensafety.Activities.SelectUserActivity;
import com.example.womensafety.Models.users;
import com.example.womensafety.R;
import com.example.womensafety.SuperAdmin.Adapters.ManageUsersAdapter;
import com.example.womensafety.User.Detail_Forms;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SuperAdminUsersActivity extends AppCompatActivity {

    ListView userList;

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;

    TextView total_users;
    FirebaseDatabase database;
    DatabaseReference reference;

    int num=0;

    public String mobile;
    public String name;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_super_admin_users);

        total_users=(TextView)findViewById(R.id.total_users);

        auth=FirebaseAuth.getInstance();


        database=FirebaseDatabase.getInstance();
        reference=database.getReference("registered_users");

        setUpToolbar();
        navigationView = findViewById(R.id.navigationMenu);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.superadmin_homepage:
                        startActivity(new Intent(SuperAdminUsersActivity.this, SuperAdminHomepage.class));
                        break;

                    case R.id.superadmin_home:
                        startActivity(new Intent(SuperAdminUsersActivity.this, SuperAdminDashboardActivity.class));
                        break;
                    case R.id.superadmin_manage_account:
                        startActivity( new Intent(SuperAdminUsersActivity.this, ManageSuperAdminAccountActivity.class));
                        break;

                    case R.id.superadmin_manage_admin:
                        startActivity(new Intent(SuperAdminUsersActivity.this, ManageAdminActivity.class));
                        break;

                    case R.id.superadmin_manage_users:
                        break;

                    case R.id.superadmin_manage_superadmin:
                        startActivity(new Intent(SuperAdminUsersActivity.this, ManageSuperAdminActivity.class));
                        break;

                    case R.id.superadmin_settings:
                        startActivity(new Intent(SuperAdminUsersActivity.this, SuperAdminSettingsActivity.class));
                        break;
                    case R.id.superadmin_logout:
                        auth.signOut();
                        startActivity(new Intent(SuperAdminUsersActivity.this, LoginActivity.class));
                        finish();
                        break;
                }

                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        //count for total number of users

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                 if(snapshot.exists())
                 {
                     num=(int)snapshot.getChildrenCount();
                     total_users.setText( "Total Users registered - "+Integer.toString(num));
                 }else
                 {
                     total_users.setText("Total Users registered - 0");
                 }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        // list view operations starting here

        userList=(ListView)findViewById(R.id.users_list);

        final ArrayList<users> users_reg=new ArrayList<users>();

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot userSnap : snapshot.getChildren())
                {
                    users user=userSnap.getValue(users.class);
                    users_reg.add(user);
                }

                ManageUsersAdapter adapter=new ManageUsersAdapter(SuperAdminUsersActivity.this,0,users_reg);

                userList.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //setting on click listeners on the list items so that it will be redirected to the details of that particular user

        userList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                users user=users_reg.get(position);

                mobile=user.getMobile_number();
                name=user.getFull_name();

                //Log.d("user_e_mail", e_mail );

                final Intent intent=new Intent(SuperAdminUsersActivity.this,UserDetailsActivity.class);
                intent.putExtra("mob",mobile);
                intent.putExtra("username",name);
/*
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot use : snapshot.getChildren())
                        {
                            if(use.child("email_id").getValue().toString().equals(e_mail)){
                                user_specific_id= use.getKey();
                                *//*uidg=uidg+user_specific_id;
                                intent.putExtra("user_id",uidg);*//*
                                *//*intent.putExtra("user_id",""+user_specific_id);*//*
                                *//*Log.d("uid", user_specific_id );
                                Log.d("user_email",use.child("email_id").getValue().toString() );*//*
                                Log.d("userId",use.getKey());
                            }
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                uidg="";
                uidg+=user_specific_id;
                intent.putExtra("user_id",uidg);

*//*              Intent intent=new Intent(SuperAdminUsersActivity.this,UserDetailsActivity.class);
                intent.putExtra("username",name);*//*
                *//*intent.putExtra("userId",user_specific_id);
                startActivity(intent);
            }
        });


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
        actionBarDrawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.black));
        actionBarDrawerToggle.syncState();
    }

}
*/