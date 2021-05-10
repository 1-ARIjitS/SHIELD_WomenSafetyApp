package com.example.womensafety.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.womensafety.R;
import com.example.womensafety.SuperAdmin.Models.EmergencyContacts;

import java.util.ArrayList;
import java.util.List;

public class EmergencyContactsAdapter extends ArrayAdapter {
    public EmergencyContactsAdapter(@NonNull Activity context, int resource, @NonNull ArrayList<EmergencyContacts> contacts) {
        super(context, resource, contacts);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView=convertView;

        if(listItemView==null)
        {
            listItemView= LayoutInflater.from(getContext()).inflate(R.layout.single_emergency_contact,parent,false);
        }

        EmergencyContacts currentContacts= (EmergencyContacts) getItem(position);

        TextView service = listItemView.findViewById(R.id.emergency_service);
        TextView mobile = listItemView.findViewById(R.id.emergency_phone);

        String service_name="Emergency Service:-";
        String service_phone="Emergency contact:-";

        service_name+=currentContacts.getName();
        service_phone+=currentContacts.getMobile();

        service.setText(service_name);
        mobile.setText(service_phone);

        return listItemView;
    }
}
