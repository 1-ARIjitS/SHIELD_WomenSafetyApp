package com.example.womensafety.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.womensafety.Models.kin_registered;
import com.example.womensafety.R;
import com.example.womensafety.SuperAdmin.Models.EmergencyContacts;

import java.util.ArrayList;
import java.util.List;

/*
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
*/
public class EmergencyContactsAdapter extends RecyclerView.Adapter<EmergencyContactsAdapter.myViewHolder>{

    ArrayList<EmergencyContacts> con_list;
    Context context;

    public EmergencyContactsAdapter(Context context,ArrayList<EmergencyContacts>con_list)
    {
        this.context=context;
        this.con_list=con_list;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(context).inflate(R.layout.single_emergency_contact,parent,false);

        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {


        EmergencyContacts currentContacts = con_list.get(position);

        String service_name=" ";
        String service_phone="Contact - ";

        service_name+=currentContacts.getName();
        service_phone+=currentContacts.getMobile();

        holder.service.setText(service_name);
        holder.mobile_num.setText(service_phone);

        boolean isExpandable = con_list.get(position).isExpandable();
        try {
            holder.expandablelayout.setVisibility(isExpandable ? View.VISIBLE : View.GONE);
        } catch(NullPointerException ignored) {

        }
        //holder.expandablelayout.setVisibility(isExpandable ? View.VISIBLE : View.GONE);


    }

    @Override
    public int getItemCount() {
        return con_list.size();
    }

    class myViewHolder extends RecyclerView.ViewHolder
    {
        TextView service,mobile_num;
        LinearLayout linearLayout;
        RelativeLayout expandablelayout;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            service = itemView.findViewById(R.id.emergency_service);
            mobile_num = itemView.findViewById(R.id.emergency_phone);

            linearLayout = itemView.findViewById(R.id.linearLayout);
            expandablelayout = itemView.findViewById(R.id.expandable_layout);

            try {
                linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EmergencyContacts currentContacts = con_list.get(getAdapterPosition());
                        currentContacts.setExpandable(!currentContacts.isExpandable());
                        notifyItemChanged(getAdapterPosition());
                    }
                });
            } catch (NullPointerException ignored) {

            }
        }
    }
}

