package com.example.womensafety.SuperAdmin.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.womensafety.Models.suspect_registered;
import com.example.womensafety.R;

import java.util.ArrayList;

public class suspectSuperAdapter extends ArrayAdapter {

     public  suspectSuperAdapter(@NonNull Activity context, int resource, @NonNull ArrayList<suspect_registered> sus) {
        super(context, 0, sus);
    }

    @NonNull
    @Override

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;

        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.single_sus, parent, false);
        }

        suspect_registered currentSus = (suspect_registered) getItem(position);

        TextView name = listItemView.findViewById(R.id.each_sus_name);
        TextView mobile_num = listItemView.findViewById(R.id.each_sus_mobile_number);
        TextView description = listItemView.findViewById(R.id.each_sus_description);
        TextView identity = listItemView.findViewById(R.id.each_sus_identity);

        String sus_name = "Name :- ";
        String sus_mobile = "Mobile Number :- ";
        String sus_description = "Description :- ";
        String sus_identity = "Identity :- ";

        sus_name += currentSus.getName();
        sus_description += currentSus.getDescription();

        if (currentSus.getMobile_num().isEmpty()) {
            sus_mobile += "UNKNOWN";
        } else {
            sus_mobile += currentSus.getMobile_num();
        }
        if (currentSus.getIdentity().isEmpty()) {
            sus_identity += "UNKNOWN";
        } else {
            sus_identity += currentSus.getIdentity();
        }

        name.setText(sus_name);
        mobile_num.setText(sus_mobile);
        description.setText(sus_description);
        identity.setText(sus_identity);


        return listItemView;
    }
}
