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
import androidx.recyclerview.widget.RecyclerView;

import com.example.womensafety.Models.kin_registered;
import com.example.womensafety.Models.suspect_registered;
import com.example.womensafety.R;

import java.util.ArrayList;
import java.util.List;

/*public class kinAdapter extends ArrayAdapter{
    public kinAdapter(@NonNull Activity context, int resource, @NonNull ArrayList<kin_registered> kin) {
        super(context, 0, kin);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;

        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.single_kin, parent, false);
        }

        kin_registered currentKin = (kin_registered) getItem(position);

        TextView name = (TextView) listItemView.findViewById(R.id.kin_name);
        TextView mobile_number = (TextView) listItemView.findViewById(R.id.kin_phone);

        String kin_name = "Name:- ";
        String kin_mobile_number = "Mobile Number :-";

        kin_name += currentKin.getName();
        kin_mobile_number += currentKin.getMobile_number();

        name.setText(kin_name);
        mobile_number.setText(kin_mobile_number);

        return listItemView;
    }
}*/

public class kinAdapter extends RecyclerView.Adapter<kinAdapter.myViewHolder>{

    ArrayList<kin_registered> kin_list;
    Context context;

    public kinAdapter(Context context,ArrayList<kin_registered>kin_list)
    {
        this.context=context;
        this.kin_list=kin_list;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(context).inflate(R.layout.single_kin,parent,false);

        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {


        kin_registered currentKin = kin_list.get(position);

        String kin_name = "Name:- ";
        String kin_mobile_number = "Mobile Number :-";

        kin_name += currentKin.getName();
        kin_mobile_number += currentKin.getMobile_number();

        holder.name.setText(kin_name);
        holder.mobile_num.setText(kin_mobile_number);


    }

    @Override
    public int getItemCount() {
        return kin_list.size();
    }

    class myViewHolder extends RecyclerView.ViewHolder
    {
        TextView name,mobile_num;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.kin_name);
            mobile_num = itemView.findViewById(R.id.kin_phone);

        }
    }
}

