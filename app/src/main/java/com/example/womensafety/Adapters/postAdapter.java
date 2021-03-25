package com.example.womensafety.Adapters;

import android.app.Activity;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.womensafety.Models.posts;
import com.example.womensafety.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.List;
import java.util.Objects;

public class postAdapter extends RecyclerView.Adapter<postAdapter.postViewHolder> {

    public List<posts> mList;
    public FirebaseAuth auth;
    public FirebaseDatabase database;
    public DatabaseReference reference;

    public FirebaseFirestore firestore;
    public Activity context;

    public postAdapter(Activity context, List<posts> mList) {
        this.mList = mList;
        this.context = context;
    }


    @NonNull
    @Override
    public postViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.each_post, parent, false);
        return new postViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull final postViewHolder holder, int position) {

        posts posts = mList.get(position);

        auth = FirebaseAuth.getInstance();

        database = FirebaseDatabase.getInstance();
        reference = database.getReference("registered_users");

        firestore = FirebaseFirestore.getInstance();
        holder.setPostPic(posts.getImage());

        holder.setCaption(posts.getCaption());

        long milliseconds = posts.getTime().getTime();
        String date = DateFormat.format("dd/MM/yyyy", new Date(milliseconds)).toString();
        holder.setDate(date);

        final String uid = posts.getUser();

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String user = Objects.requireNonNull(snapshot.child(uid).child("full_name").getValue()).toString();
                holder.setUsername(user);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class postViewHolder extends RecyclerView.ViewHolder {

        TextView username, date, caption;

        ImageView imageView;

        View mView;

        public postViewHolder(@NonNull View itemView) {
            super(itemView);

            mView = itemView;
        }

        public void setPostPic(String imageUrl) {
            imageView = mView.findViewById(R.id.each_post_image);
            Glide.with(context).load(imageUrl).into(imageView);
        }

        public void setUsername(String user_name) {
            username = mView.findViewById(R.id.each_post_username);
            username.setText(user_name);
        }

        public void setCaption(String caps) {
            caption = mView.findViewById(R.id.each_post_caption);
            caption.setText(caps);
        }

        public void setDate(String dn) {
            date = mView.findViewById(R.id.each_post_dnt);
            date.setText(dn);
        }

    }
}
