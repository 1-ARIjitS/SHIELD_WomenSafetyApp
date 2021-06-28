package com.example.womensafety.Adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.womensafety.Models.kin_registered;
import com.example.womensafety.Models.userLocationTracking;
import com.example.womensafety.R;

import java.util.ArrayList;

public class travelLogAdapter extends RecyclerView.Adapter<travelLogAdapter.myViewHolder>{

    ArrayList<userLocationTracking> travel_log_list;
    Context context;

    public travelLogAdapter(Context context,ArrayList<userLocationTracking>travel_log_list)
    {
        this.context=context;
        this.travel_log_list=travel_log_list;
    }

    @NonNull
    @Override
    public travelLogAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.single_travel_log,parent,false);

        return new travelLogAdapter.myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull travelLogAdapter.myViewHolder holder, int position) {


        userLocationTracking currentTravelLog = travel_log_list.get(position);

        String actual_date,actual_time,estimated_time,vehicle_number,travelling_from,travelling_to;

        String vehicle_image;

        actual_date="Date- ";
        actual_time="Time of Arrival- ";
        estimated_time="Estimated Time Of Arrival- ";
        vehicle_number="Vehicle Number- ";
        travelling_from="Travelling From- ";
        travelling_to="Travelling To- ";

        actual_date+=currentTravelLog.getActual_date();
        actual_time+=currentTravelLog.getActual_time();
        estimated_time+=currentTravelLog.getEstimated_time();
        vehicle_number+=currentTravelLog.getVehicle_number();
        travelling_from+=currentTravelLog.getTravelling_from();
        travelling_to+=currentTravelLog.getTravelling_to();

        holder.actual_date.setText(actual_date);
        holder.actual_time.setText(actual_time);
        holder.estimated_time.setText(estimated_time);
        holder.vehicle_number.setText(vehicle_number);
        holder.travelling_from.setText(travelling_from);
        holder.travelling_to.setText(travelling_to);

        //setting the vehicle image

        vehicle_image=currentTravelLog.getVehicle_image();
        holder.setVehicleImage(vehicle_image);

    }

    @Override
    public int getItemCount() {
        return travel_log_list.size();
    }

    class myViewHolder extends RecyclerView.ViewHolder
    {
        TextView actual_date,actual_time,estimated_time,vehicle_number,travelling_from,travelling_to;

        ImageView vehicle_image;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            actual_date=itemView.findViewById(R.id.each_travel_log_date);
            actual_time=itemView.findViewById(R.id.each_travel_log_actual_arrival_time);
            estimated_time=itemView.findViewById(R.id.each_travel_log_time);
            vehicle_number=itemView.findViewById(R.id.each_travel_log_vehicle_number);
            travelling_from=itemView.findViewById(R.id.each_travel_log_travelling_from);
            travelling_to=itemView.findViewById(R.id.each_travel_log_travelling_to);
        }

        public void setVehicleImage(String imageString)
        {
          vehicle_image=itemView.findViewById(R.id.each_trael_log_vehicle_image);
          Glide.with(context).load(Uri.parse(imageString)).into(vehicle_image);
        }
    }
}
