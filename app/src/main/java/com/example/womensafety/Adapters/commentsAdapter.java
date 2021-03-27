package com.example.womensafety.Adapters;

import android.app.Activity;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.womensafety.Models.comments;
import com.example.womensafety.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class commentsAdapter extends RecyclerView.Adapter<commentsAdapter.commentsViewHolder> {

    private Activity context;

    private List<comments>commentsList;

    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference reference;
    FirebaseFirestore firestore;

    public commentsAdapter(Activity context,List<comments>commentsList)
    {
        this.context=context;
        this.commentsList=commentsList;
    }

    @NonNull
    @Override
    public commentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view= LayoutInflater.from(context).inflate(R.layout.each_comment,parent,false);
       return new commentsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final commentsViewHolder holder, int position) {

        auth=FirebaseAuth.getInstance();

        database=FirebaseDatabase.getInstance();

        reference=database.getReference("registered_users");

        comments comments=commentsList.get(position);

        holder.setComment(comments.getComment());

        final String uid=comments.getUser();

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String user=snapshot.child(uid).child("full_name").getValue().toString();
                holder.setUser(user);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return commentsList.size();
    }

    public class commentsViewHolder extends RecyclerView.ViewHolder{

        TextView mComment,mUser;

        View mView;

        public commentsViewHolder(@NonNull View itemView) {
            super(itemView);

            mView=itemView;
        }

        public void setComment(String comment)
        {
            mComment=mView.findViewById(R.id.comment_comment_pass);
            mComment.setText(comment);
        }

        public void setUser(String user_name)
        {
            mUser=mView.findViewById(R.id.comment_use);
            mUser.setText(user_name);
        }
    }
}
