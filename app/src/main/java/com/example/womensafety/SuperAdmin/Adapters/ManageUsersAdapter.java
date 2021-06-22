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

import com.example.womensafety.Interfaces.userListItemClickInterface;
import com.example.womensafety.Models.suspect_registered;
import com.example.womensafety.Models.users;
import com.example.womensafety.R;
import com.example.womensafety.SuperAdmin.Models.Admins;
import com.example.womensafety.SuperAdmin.Models.SuperAdmins;

import java.util.ArrayList;

public class ManageUsersAdapter extends RecyclerView.Adapter<ManageUsersAdapter.myViewHolder>{

    ArrayList<users> users_list;
    Context context;
    userListItemClickInterface userListItemClickInterface;

    public ManageUsersAdapter(Context context,ArrayList<users>users_list,userListItemClickInterface userListItemClickInterface)
    {
        this.context=context;
        this.users_list=users_list;
        this.userListItemClickInterface=userListItemClickInterface;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.single_user,parent,false);

        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {


        users currentUser=users_list.get(position);

        String name=" ";
        String age=" Age - ";
        String address=" ";
        String phone=" Mobile Number - ";
        String emailId=" ";

        name+=currentUser.getFull_name();
        age+=currentUser.getAge();
        address+=currentUser.getAddress();
        phone+=currentUser.getMobile_number();
        emailId+=currentUser.getEmail_id();

        holder.username.setText(name);
        holder.userAge.setText(age);
        holder.userAddress.setText(address);
        holder.userEmailId.setText(emailId);
        holder.userPhone.setText(phone);

        boolean isExpandable = users_list.get(position).isExpandable();
        holder.expandablelayout.setVisibility(isExpandable ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        return users_list.size();
    }

    class myViewHolder extends RecyclerView.ViewHolder
    {
        TextView username,userPhone,userAge,userEmailId,userAddress;
        LinearLayout linearLayout;
        RelativeLayout expandablelayout;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            username = (TextView) itemView.findViewById(R.id.user_name);
            userAge = (TextView) itemView.findViewById(R.id.user_age);
            userAddress = (TextView) itemView.findViewById(R.id.user_address);
            userPhone = (TextView) itemView.findViewById(R.id.user_phone);
            userEmailId = (TextView) itemView.findViewById(R.id.user_email);

            linearLayout = itemView.findViewById(R.id.linearLayout);
            expandablelayout = itemView.findViewById(R.id.expandable_layout);

            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    users currentUser = users_list.get(getAdapterPosition());
                    currentUser.setExpandable(!currentUser.isExpandable());
                    notifyItemChanged(getAdapterPosition());
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    userListItemClickInterface.onItemClick(getAdapterPosition());
                }
            });
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
*/