package com.example.womensafety.SuperAdmin.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.drawerlayout.widget.DrawerLayout;
import com.example.womensafety.Activities.LoginActivity;
import com.example.womensafety.Activities.RegisterActivity;
import com.example.womensafety.Activities.SuspectListActivity;
import com.example.womensafety.R;
import com.example.womensafety.SuperAdmin.Models.Admins;
import com.example.womensafety.User.Detail_Forms;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.vikktorn.picker.City;
import com.vikktorn.picker.CityPicker;
import com.vikktorn.picker.Country;
import com.vikktorn.picker.CountryPicker;
import com.vikktorn.picker.OnCityPickerListener;
import com.vikktorn.picker.OnCountryPickerListener;
import com.vikktorn.picker.OnStatePickerListener;
import com.vikktorn.picker.State;
import com.vikktorn.picker.StatePicker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RegisterAdminActivity extends AppCompatActivity implements OnStatePickerListener, OnCountryPickerListener, OnCityPickerListener {

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;

    MaterialSpinner spinner;
    EditText name,age,address,email,mobile,password;
    String m_gender="Male";
    String m_name,m_age,m_address,m_email,m_mobile,m_password;
    Button create_new_admin_button;

    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference reference;

    public static int countryID, stateID;
    private Button pickStateButton, pickCountry, pickCity;
    TextView txtCountry,txtState,txtCity;
    // Pickers
    private CountryPicker countryPicker;
    private StatePicker statePicker;
    private CityPicker cityPicker;
    // arrays of state object
    public static List<State> stateObject;
    // arrays of city object
    public static List<City> cityObject;

    String m_country,m_state,m_city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_admin);

//
        txtCountry = (TextView)findViewById(R.id.admin_country_text);
        txtState = findViewById(R.id.admin_state_text);
        txtCity = findViewById(R.id.admin_city_text);

        pickStateButton = (Button) findViewById(R.id.admin_state);
        pickCountry = (Button) findViewById(R.id.admin_country);
        pickCity = (Button) findViewById(R.id.admin_city);
        stateObject = new ArrayList<>();
        cityObject = new ArrayList<>();

//

        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        reference=database.getReference("registered_admins");



        setUpToolbar();
        navigationView = findViewById(R.id.navigationMenu);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.superadmin_homepage:
                        startActivity(new Intent(RegisterAdminActivity.this, SuperAdminHomepage.class));
                        break;

                    case R.id.superadmin_home:
                        startActivity(new Intent(RegisterAdminActivity.this, SuperAdminDashboardActivity.class));
                        break;
                    case R.id.superadmin_manage_account:
                        startActivity( new Intent(RegisterAdminActivity.this, ManageSuperAdminAccountActivity.class));
                        break;

                    case R.id.superadmin_manage_admin:
                        break;

                    case R.id.superadmin_manage_users:
                        startActivity(new Intent(RegisterAdminActivity.this, SuperAdminUsersActivity.class));
                        break;

                    case R.id.superadmin_manage_superadmin:
                        startActivity(new Intent(RegisterAdminActivity.this, ManageSuperAdminActivity.class));
                        break;

                    case R.id.superadmin_settings:
                        startActivity(new Intent(RegisterAdminActivity.this, SuperAdminSettingsActivity.class));
                        break;
                    case R.id.superadmin_logout:
                        auth.signOut();
                        startActivity(new Intent(RegisterAdminActivity.this, LoginActivity.class));
                        finish();
                        break;
                }

                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });



        spinner = (MaterialSpinner) findViewById(R.id.create_admin_gender_spinner);
        spinner.setItems("Male", "Female");
        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                if(item=="Male")
                    m_gender="Male";
                else
                    m_gender="Female";

            }
        });

        create_new_admin_button=(Button)findViewById(R.id.create_admin_button);

        name=findViewById(R.id.create_admin_name);
        age=findViewById(R.id.create_admin_age);
        address=findViewById(R.id.create_admin_address);
        email=findViewById(R.id.create_admin_email);
        mobile=findViewById(R.id.create_admin_mob);
        password=findViewById(R.id.create_admin_password);

        try {
            getStateJson();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // get City from assets JSON
        try {
            getCityJson();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // initialize country picker
        countryPicker = new CountryPicker.Builder().with(this).listener(this).build();

        // initialize listeners
        setListener();
        setCountryListener();
        setCityListener();

        create_new_admin_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m_name=name.getText().toString();
                m_address=address.getText().toString();
                m_age=age.getText().toString();
                m_email=email.getText().toString();
                m_mobile=mobile.getText().toString();
                m_password=password.getText().toString();
                m_country=txtCountry.getText().toString();
                m_state=txtState.getText().toString();
                m_city=txtCity.getText().toString();

                final Admins admins=new Admins(m_name,m_age,m_address,m_email,m_mobile,m_gender,m_password,m_country,m_state,m_city);

                if (m_name.isEmpty() && m_age.isEmpty() && m_address.isEmpty() && m_email.isEmpty() && m_mobile.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "INVALID,Blank Fields", Toast.LENGTH_SHORT).show();
                } else {
                    if (m_name.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "INVALID,please enter an username", Toast.LENGTH_SHORT).show();
                    }
                    if (m_address.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "INVALID,please enter an address", Toast.LENGTH_SHORT).show();
                    }
                    if (m_age.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "INVALID,please enter an age", Toast.LENGTH_SHORT).show();
                    }
                    if (m_mobile.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "INVALID,please enter a mobile number", Toast.LENGTH_SHORT).show();
                    }
                    if (m_email.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "INVALID,please enter an email id", Toast.LENGTH_SHORT).show();
                    } else if (m_mobile.length() > 0 && m_mobile.length() < 10) {
                        Toast.makeText(getApplicationContext(), "INVALID,mobile number is too short", Toast.LENGTH_SHORT).show();
                    } else if (m_mobile.length() > 10) {
                        Toast.makeText(getApplicationContext(), "INVALID,mobile number is too long", Toast.LENGTH_SHORT).show();
                    } else if (Integer.parseInt(m_age) > 130 || Integer.parseInt(m_age) < 0) {
                        Toast.makeText(getApplicationContext(), "INVALID,please enter a valid age", Toast.LENGTH_SHORT).show();
                    } else {
                        auth.createUserWithEmailAndPassword(m_email, m_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    reference.child(m_mobile).setValue(admins);
                                    startActivity(new Intent(getApplicationContext(), ManageAdminActivity.class));
                                    Toast.makeText(getApplicationContext(), "A New Admin Account Is Successfully Created", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), Objects.requireNonNull(task.getException()).getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
            }
        });
    }

    // SET STATE LISTENER
    private void setListener() {
        pickStateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statePicker.showDialog(getSupportFragmentManager());
            }
        });
    }
    //SET COUNTRY LISTENER
    private void setCountryListener() {
        pickCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countryPicker.showDialog(getSupportFragmentManager());
            }
        });
    }
    //SET CITY LISTENER
    private void setCityListener() {
        pickCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cityPicker.showDialog(getSupportFragmentManager());
            }
        });
    }
    @Override
    public void onSelectCountry(Country country) {
        // get country name and country ID
        String countryName=country.getName();
        txtCountry.setText(countryName);
        txtCountry.setVisibility(View.VISIBLE);
        Toast.makeText(RegisterAdminActivity.this, countryName, Toast.LENGTH_SHORT).show();

        countryID = country.getCountryId();
        statePicker.equalStateObject.clear();
        cityPicker.equalCityObject.clear();

        //set state name text view and state pick button invisible
        pickStateButton.setVisibility(View.VISIBLE);
        // set text on main view



        // GET STATES OF SELECTED COUNTRY
        for(int i = 0; i < stateObject.size(); i++) {
            // init state picker
            statePicker = new StatePicker.Builder().with(this).listener(this).build();
            State stateData = new State();
            if (stateObject.get(i).getCountryId() == countryID) {

                stateData.setStateId(stateObject.get(i).getStateId());
                stateData.setStateName(stateObject.get(i).getStateName());
                stateData.setCountryId(stateObject.get(i).getCountryId());
                stateData.setFlag(country.getFlag());
                statePicker.equalStateObject.add(stateData);
            }
        }
    }
    // ON SELECTED STATE ADD CITY TO PICKER
    @Override
    public void onSelectState(State state) {
        String stateName=state.getStateName();
        txtState.setText(stateName);
        txtState.setVisibility(View.VISIBLE);

        cityPicker.equalCityObject.clear();
        Toast.makeText(RegisterAdminActivity.this, stateName, Toast.LENGTH_SHORT).show();

        //stateNameTextView.setText(state.getStateName());
        stateID = state.getStateId();

        for(int i = 0; i < cityObject.size(); i++) {
            cityPicker = new CityPicker.Builder().with(this).listener(this).build();
            City cityData = new City();
            if (cityObject.get(i).getStateId() == stateID) {
                cityData.setCityId(cityObject.get(i).getCityId());
                cityData.setCityName(cityObject.get(i).getCityName());
                cityData.setStateId(cityObject.get(i).getStateId());

                cityPicker.equalCityObject.add(cityData);
            }
        }
    }
    // ON SELECTED CITY
    @Override
    public void onSelectCity(City city)
    {
        String cityName=city.getCityName();
        txtCity.setText(cityName);
        txtCity.setVisibility(View.VISIBLE);

        Toast.makeText(RegisterAdminActivity.this, city.getCityName(), Toast.LENGTH_SHORT).show();

    }
    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
    // GET STATE FROM ASSETS JSON
    public void getStateJson() throws JSONException {
        String json = null;
        try {
            InputStream inputStream = getAssets().open("states.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            json = new String(buffer, "UTF-8");

        } catch (IOException e) {
            e.printStackTrace();
        }


        JSONObject jsonObject = new JSONObject(json);
        JSONArray events = jsonObject.getJSONArray("states");
        for (int j = 0; j < events.length(); j++) {
            JSONObject cit = events.getJSONObject(j);
            State stateData = new State();

            stateData.setStateId(Integer.parseInt(cit.getString("id")));
            stateData.setStateName(cit.getString("name"));
            stateData.setCountryId(Integer.parseInt(cit.getString("country_id")));
            stateObject.add(stateData);
        }
    }
    // GET CITY FROM ASSETS JSON
    public void getCityJson() throws JSONException {
        String json = null;
        try {
            InputStream inputStream = getAssets().open("cities.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            json = new String(buffer, "UTF-8");

        } catch (IOException e) {
            e.printStackTrace();
        }


        JSONObject jsonObject = new JSONObject(json);
        JSONArray events = jsonObject.getJSONArray("cities");
        for (int j = 0; j < events.length(); j++) {
            JSONObject cit = events.getJSONObject(j);
            City cityData = new City();

            cityData.setCityId(Integer.parseInt(cit.getString("id")));
            cityData.setCityName(cit.getString("name"));
            cityData.setStateId(Integer.parseInt(cit.getString("state_id")));
            cityObject.add(cityData);
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
        actionBarDrawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.textColor));
        actionBarDrawerToggle.syncState();
    }
}