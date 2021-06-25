package com.example.womensafety.Adapters;

import android.app.Activity;

import android.content.Intent;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.womensafety.Activities.CommentActivity;
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
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    public void onBindViewHolder(@NonNull final postViewHolder holder, final int position) {

        final posts posts = mList.get(position);

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

        //like_button

        final String postId = posts.PostId;
        final String currentUserId = Objects.requireNonNull(auth.getCurrentUser()).getUid();

        holder.likePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firestore.collection("Posts/" + postId + "/Likes").document(currentUserId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (!task.getResult().exists()) {
                            Map<String, Object> likesMap = new HashMap<>();
                            likesMap.put("timestamp", FieldValue.serverTimestamp());
                            firestore.collection("Posts/" + postId + "/Likes").document(currentUserId).set(likesMap);
                        } else {
                            firestore.collection("Posts/" + postId + "/Likes").document(currentUserId).delete();
                        }
                    }
                });
            }
        });


        //change color of like button

        firestore.collection("Posts/" + postId + "/Likes").document(currentUserId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error == null) {
                    assert value != null;
                    if (value.exists()) {
                        holder.likePic.setImageDrawable(context.getDrawable(R.drawable.ic_baseline_favorite_24));
                    } else {
                        holder.likePic.setImageDrawable(context.getDrawable(R.drawable.ic_baseline_favorite_border_24));
                    }
                }

            }
        });

        //set like counts

        firestore.collection("Posts/" + postId + "/Likes").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error == null) {
                    assert value != null;
                    if (!value.isEmpty()) {
                        int c = value.size();
                        holder.setPostLikes(c);
                    } else {
                        holder.setPostLikes(0);
                    }
                }
            }
        });

        //share posts

        holder.share_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String caption = posts.getCaption();
                String post_image = posts.getImage();
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, "check this post :-\n\n" + post_image + "\n\n" + caption);
                context.startActivity(Intent.createChooser(intent, "Share Post"));
            }

        });

        // comment posts going to the comment activity

        holder.comment_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, CommentActivity.class);
                intent.putExtra("postid",postId);
                context.startActivity(intent);
            }
        });

    }


    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class postViewHolder extends RecyclerView.ViewHolder {

        TextView username, date, caption, postLikes;

        ImageView imageView, likePic, share_text, comment_text;

        View mView;

        public postViewHolder(@NonNull View itemView) {
            super(itemView);

            mView = itemView;

            likePic = (ImageView) mView.findViewById(R.id.likes_image_button);

            share_text = (ImageView) mView.findViewById(R.id.share_feature);

            comment_text = (ImageView) mView.findViewById(R.id.comment_feature);

        }

        public void setPostLikes(int count) {
            postLikes = mView.findViewById(R.id.like_feature);
            String l = "";
            if (count > 1)
                l += " Likes";
            else
                l += " Like";
            postLikes.setText(count + l);
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
