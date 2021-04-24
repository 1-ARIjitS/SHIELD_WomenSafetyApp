package com.example.womensafety.SuperAdmin.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.womensafety.Models.suspect_registered;
import com.example.womensafety.Models.users;
import com.example.womensafety.R;

import java.util.ArrayList;

public class ManageUsersAdapter extends ArrayAdapter {
    public ManageUsersAdapter(@NonNull Activity context, int resource, @NonNull ArrayList<users> users) {
        super(context, 0, users);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView=convertView;

        if(listItemView==null)
        {
            listItemView= LayoutInflater.from(getContext()).inflate(R.layout.single_user,parent,false);
        }

        users currentUser = (users) getItem(position);

        TextView username=(TextView)listItemView.findViewById(R.id.user_name);
        TextView userAge=(TextView)listItemView.findViewById(R.id.user_age);
        TextView userAddress=(TextView)listItemView.findViewById(R.id.user_address);
        TextView userPhone=(TextView)listItemView.findViewById(R.id.user_phone);
        TextView userEmailId=(TextView)listItemView.findViewById(R.id.user_email);

        String name="Name :- ";
        String age="Age :- ";
        String address="Address :- ";
        String phone="Mobile Number :- ";
        String emailId="Email-Id :- ";

        name+=currentUser.getFull_name();
        age+=currentUser.getAge();
        address+=currentUser.getAddress();
        phone+=currentUser.getMobile_number();
        emailId+=currentUser.getEmail_id();

        username.setText(name);
        userAge.setText(age);
        userAddress.setText(address);
        userEmailId.setText(emailId);
        userPhone.setText(phone);


        return listItemView;
    }
}
