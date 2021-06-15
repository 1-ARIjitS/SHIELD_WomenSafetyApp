package com.example.womensafety.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.womensafety.R;
import com.example.womensafety.Models.users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hbb20.CountryCodePicker;
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

public class RegisterActivity extends AppCompatActivity implements OnStatePickerListener, OnCountryPickerListener, OnCityPickerListener {
    FirebaseAuth auth;

    FirebaseDatabase rootNode;
    DatabaseReference reference;

    EditText full_name;

    EditText age;

    EditText email_id;

    EditText mobile_number;

    EditText address;

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
    EditText unique;
    EditText confirm_unique;

    EditText password;
    EditText confirm_password;

    Button continue_button;

    Button already_button;

    CountryCodePicker ccp;
    String mFull_name;
    String mEmail_id;
    String country_code;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        auth = FirebaseAuth.getInstance();

        rootNode = FirebaseDatabase.getInstance();

        reference = rootNode.getReference("registered_users");

        full_name = findViewById(R.id.registration_full_name);

        email_id = findViewById(R.id.registration_email);

        age = findViewById(R.id.registration_age);

        mobile_number = findViewById(R.id.registration_phone_number);

        address = findViewById(R.id.registration_address);
        txtCountry = (TextView)findViewById(R.id.txtCountry);
        txtState = findViewById(R.id.txtState);
        txtCity = findViewById(R.id.txtCity);

        unique = findViewById(R.id.registration_uvc);
        confirm_unique = findViewById(R.id.registration_confirm_uvc);

        password = findViewById(R.id.registration_pass);
        confirm_password = findViewById(R.id.registration_confirm_pass);

        already_button = findViewById(R.id.registration_already_an_user);

        continue_button = findViewById(R.id.registration_verify_button);
        pickStateButton = (Button) findViewById(R.id.pickStateButton);
        //set state picker invisible
        //pickStateButton.setVisibility(View.INVISIBLE);
        pickCountry = (Button) findViewById(R.id.pickCountry);
        pickCity = (Button) findViewById(R.id.pickCity);
        stateObject = new ArrayList<>();
        cityObject = new ArrayList<>();

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
        /*try {
            Intent intent = getIntent();
            mFull_name=intent.getStringExtra("user");;
            mEmail_id=intent.getStringExtra("email");;
            full_name.setText(mFull_name);
            email_id.setText(mEmail_id);
            //to make email not editable
            //email_id.setFocusable(false);
        }catch (Exception e){

        }*/
        //ccp=findViewById(R.id.ccp);
        //country_code=ccp.getSelectedCountryCode();

        continue_button.setOnClickListener(v -> {

            mFull_name = full_name.getText().toString();
            String mAge = age.getText().toString();
            mEmail_id = email_id.getText().toString();
            String mMobile_number = mobile_number.getText().toString();
            String mAddress = address.getText().toString();
            String mPassword = password.getText().toString();
            String mUVC = unique.getText().toString();
            String country = txtCountry.getText().toString();
            String state = txtState.getText().toString();
            String city = txtCity.getText().toString();


            final users users = new users(mFull_name, mAge, mEmail_id, mMobile_number, mAddress, mPassword, mUVC,country,state,city);

            if (mFull_name.isEmpty() && mAge.isEmpty() && mEmail_id.isEmpty() && mMobile_number.isEmpty()&& country.isEmpty()&& state.isEmpty()&& city.isEmpty() && mAddress.isEmpty() && mPassword.isEmpty() && mUVC.isEmpty()) {
                Toast.makeText(RegisterActivity.this, "INVALID,Blank Field", Toast.LENGTH_SHORT).show();
            } else if (mMobile_number.length() < 10) {
                Toast.makeText(RegisterActivity.this, "INVALID,mobile number entered is too short", Toast.LENGTH_SHORT).show();
            } else if (mMobile_number.length() > 10) {
                Toast.makeText(RegisterActivity.this, "INVALID,mobile number entered is too long", Toast.LENGTH_SHORT).show();
            } else if (mPassword.length() != confirm_password.getText().toString().length()) {
                Toast.makeText(RegisterActivity.this, "INVALID,password does not match confirm password", Toast.LENGTH_SHORT).show();
            } else if (mUVC.length() != confirm_unique.getText().toString().length()) {
                Toast.makeText(RegisterActivity.this, "INVALID,unique verification password does not match confirm unique verification password", Toast.LENGTH_SHORT).show();
            }else {
                Intent intent = new Intent(RegisterActivity.this, OtpVerification.class);
                intent.putExtra("mobile", mMobile_number);
                intent.putExtra("mEmail_id", mEmail_id);
                intent.putExtra("mPassword", mPassword);
                intent.putExtra("mFull_name", mFull_name);
                intent.putExtra("mAge", mAge);
                intent.putExtra("mAddress", mAddress);
                intent.putExtra("mUVC", mUVC);
                intent.putExtra("country", country);
                intent.putExtra("state", state);
                intent.putExtra("city", city);
                startActivity(intent);

            }
            /*else {
                auth.createUserWithEmailAndPassword(mEmail_id, mPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            reference.child(auth.getCurrentUser().getUid()).setValue(users);
                            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                        } else {
                            Toast.makeText(RegisterActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }*/
        });

        already_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
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
        Toast.makeText(RegisterActivity.this, countryName, Toast.LENGTH_SHORT).show();

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

        cityPicker.equalCityObject.clear();
        Toast.makeText(RegisterActivity.this, stateName, Toast.LENGTH_SHORT).show();

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

        Toast.makeText(RegisterActivity.this, city.getCityName(), Toast.LENGTH_SHORT).show();

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
}
