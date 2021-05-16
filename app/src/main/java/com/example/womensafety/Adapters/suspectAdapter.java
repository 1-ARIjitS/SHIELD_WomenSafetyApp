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
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import java.util.ArrayList;
import java.util.List;

public class suspectAdapter extends RecyclerView.Adapter<suspectAdapter.myViewHolder>{

    ArrayList<suspect_registered> sus_list;
    Context context;

    public suspectAdapter(Context context,ArrayList<suspect_registered>sus_list)
    {
        this.context=context;
        this.sus_list=sus_list;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(context).inflate(R.layout.single_sus,parent,false);

        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {


        suspect_registered currentSus=sus_list.get(position);

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

        holder.name.setText(sus_name);
        holder.mobile_num.setText(sus_mobile);
        holder.description.setText(sus_description);
        holder.identity.setText(sus_identity);
    }

    @Override
    public int getItemCount() {
        return sus_list.size();
    }

   /* public suspectAdapter(@NonNull Activity context, int resource, @NonNull ArrayList<suspect_registered> sus) {
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
    }*/

    class myViewHolder extends RecyclerView.ViewHolder
    {
        TextView name,mobile_num,description,identity;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.each_sus_name);
            mobile_num = itemView.findViewById(R.id.each_sus_mobile_number);
            description = itemView.findViewById(R.id.each_sus_description);
            identity = itemView.findViewById(R.id.each_sus_identity);
        }
    }
}
