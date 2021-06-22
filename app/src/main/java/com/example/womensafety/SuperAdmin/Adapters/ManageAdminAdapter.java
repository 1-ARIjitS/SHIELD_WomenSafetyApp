package com.example.womensafety.SuperAdmin.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.womensafety.Models.suspect_registered;
import com.example.womensafety.R;
import com.example.womensafety.SuperAdmin.Models.Admins;
import com.example.womensafety.SuperAdmin.Models.SuperAdmins;

import java.util.ArrayList;

public class ManageAdminAdapter extends RecyclerView.Adapter<ManageAdminAdapter.myViewHolder>{

    ArrayList<Admins> super_admins_list;
    Context context;

    public ManageAdminAdapter(Context context,ArrayList<Admins>super_admins_list)
    {
        this.context=context;
        this.super_admins_list=super_admins_list;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.single_manage_admin,parent,false);

        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {


        Admins currentAdmin=super_admins_list.get(position);

        String name=" ";
        String age=" Age - ";
        String address=" ";
        String phone=" Mobile Number - ";
        String emailId=" ";
        String gender=" ";

        name+=currentAdmin.getR_name();
        age+=currentAdmin.getR_age();
        address+=currentAdmin.getR_address();
        phone+=currentAdmin.getR_mobile();
        emailId+=currentAdmin.getR_email();
        gender+=currentAdmin.getR_gender();

        holder.admin_name.setText(name);
        holder.admin_mobile.setText(phone);
        holder.admin_age.setText(age);
        holder.admin_email.setText(emailId);
        holder.admin_gender.setText(gender);
        holder.admin_address.setText(address);

        boolean isExpandable = super_admins_list.get(position).isExpandable();
        /*try {
            holder.expandablelayout.setVisibility(isExpandable ? View.VISIBLE : View.GONE);
        } catch (NullPointerException ignored) {

        }*/
        holder.expandablelayout.setVisibility(isExpandable ? View.VISIBLE : View.GONE);

    }

    @Override
    public int getItemCount() {
        return super_admins_list.size();
    }

    class myViewHolder extends RecyclerView.ViewHolder
    {
        TextView admin_name,admin_mobile,admin_age,admin_email,admin_address,admin_gender;
        LinearLayout linearLayout;
        RelativeLayout expandablelayout;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            admin_name=(TextView)itemView.findViewById(R.id.manage_admin_name);
            admin_mobile=(TextView)itemView.findViewById(R.id.manage_admin_mobile_number);
            admin_age=(TextView)itemView.findViewById(R.id.manage_admin_age);
            admin_email=(TextView)itemView.findViewById(R.id.manage_admin_email_id);
            admin_gender=(TextView)itemView.findViewById(R.id.manage_admin_gender);
            admin_address=(TextView)itemView.findViewById(R.id.manage_admin_address);

            linearLayout = itemView.findViewById(R.id.linearLayout);
            expandablelayout = itemView.findViewById(R.id.expandable_layout);

            try {
                linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Admins currentAdmin = super_admins_list.get(getAdapterPosition());
                        currentAdmin.setExpandable(!currentAdmin.isExpandable());
                        notifyItemChanged(getAdapterPosition());
                    }
                });
            } catch (NullPointerException ignored) {

            }
            /*
            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Admins currentAdmin = super_admins_list.get(getAdapterPosition());
                    currentAdmin.setExpandable(!currentAdmin.isExpandable());
                    notifyItemChanged(getAdapterPosition());
                }
            });*/
        }
    }
}











/*
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

import com.example.womensafety.R;
import com.example.womensafety.SuperAdmin.Models.Admins;

import java.util.ArrayList;
import java.util.List;

public class ManageAdminAdapter extends ArrayAdapter {
    public ManageAdminAdapter(@NonNull Activity context, int resource, @NonNull ArrayList<Admins> admins) {
        super(context, resource, admins);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView=convertView;
        if(listItemView==null)
        {
            listItemView= LayoutInflater.from(getContext()).inflate(R.layout.single_manage_admin,parent,false);
        }

        Admins currentAdmin= (Admins) getItem(position);

        TextView admin_name=(TextView)listItemView.findViewById(R.id.manage_admin_name);
        TextView admin_mobile=(TextView)listItemView.findViewById(R.id.manage_admin_mobile_number);
        TextView admin_age=(TextView)listItemView.findViewById(R.id.manage_admin_age);
        TextView admin_email=(TextView)listItemView.findViewById(R.id.manage_admin_email_id);
        TextView admin_gender=(TextView)listItemView.findViewById(R.id.manage_admin_gender);
        TextView admin_address=(TextView)listItemView.findViewById(R.id.manage_admin_address);

        String name="Name :- ";
        String age="Age :- ";
        String address="Address :- ";
        String phone="Mobile Number :- ";
        String emailId="Email-Id :- ";
        String gender="Gender :- ";

        name+=currentAdmin.getR_name();
        age+=currentAdmin.getR_age();
        address+=currentAdmin.getR_address();
        phone+=currentAdmin.getR_mobile();
        emailId+=currentAdmin.getR_email();
        gender+=currentAdmin.getR_gender();

        admin_name.setText(name);
        admin_mobile.setText(phone);
        admin_age.setText(age);
        admin_email.setText(emailId);
        admin_gender.setText(gender);
        admin_address.setText(address);


        return listItemView;
    }
}
*/