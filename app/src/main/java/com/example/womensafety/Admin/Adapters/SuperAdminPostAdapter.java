package com.example.womensafety.Admin.Adapters;

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
import com.example.womensafety.Admin.Activities.UpdatePostActivity;
import com.example.womensafety.Admin.Models.SuperadminPosts;
import com.example.womensafety.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Date;
import java.util.List;
import java.util.Objects;

public class SuperAdminPostAdapter extends RecyclerView.Adapter<SuperAdminPostAdapter.superadminPostViewHolder> {

    public List<SuperadminPosts> mList;
    public FirebaseAuth auth;
    public FirebaseDatabase database;
    public DatabaseReference reference;

    public FirebaseFirestore firestore;
    public Activity context;

    public  String postId;

    public SuperAdminPostAdapter(Activity context, List<SuperadminPosts> mList) {
        this.mList = mList;
        this.context = context;
    }


    @NonNull
    @Override
    public superadminPostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.each_super_admin_post, parent, false);
        return new superadminPostViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull final SuperAdminPostAdapter.superadminPostViewHolder holder, final int position) {

        final SuperadminPosts current_posts = mList.get(position);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("registered_users");
        firestore = FirebaseFirestore.getInstance();


        holder.setPostPic(current_posts.getImage());

        holder.setCaption(current_posts.getCaption());

        long milliseconds = current_posts.getTime().getTime();
        String date = DateFormat.format("dd/MM/yyyy", new Date(milliseconds)).toString();
        holder.setDate(date);

        final String uid = current_posts.getUser();

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

        /*postId = current_posts.PostId;*/
       /* final String currentUserId = Objects.requireNonNull(auth.getCurrentUser()).getUid();*/

 /*       holder.likePic.setOnClickListener(new View.OnClickListener() {
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
        });*/

        //change color of like button

        /*firestore.collection("Posts/" + postId + "/Likes").document(currentUserId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
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
*/
        //set like counts

        postId=current_posts.getId();

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

      /*  holder.share_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String caption = posts.getCaption();
                String post_image = posts.getImage();
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, "check this post :-\n\n" + post_image + "\n\n" + caption);
                context.startActivity(Intent.createChooser(intent, "Share Post"));
            }

        });*/

        // comment posts going to the comment activity

  /*      holder.comment_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, CommentActivity.class);
                intent.putExtra("postid",postId);
                context.startActivity(intent);
            }
        });*/

        /*holder.delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(context);
                builder.setTitle("ARE YOU SURE, YOU WANT TO DELETE THIS POST ?");
                builder.setMessage("clicking on OK will delete this post permanently from the database and you will not be able to retrieve it");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        holder.deletePost();
                    }
                });
                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                AlertDialog ad = builder.create();
                ad.show();
            }
        });*/

/*        holder.edit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, UpdatePostActivity.class);
                intent.putExtra("pots",postId);
                Log.d("unique_post_id",postId);
                context.startActivity(intent);
            }
        });*/

    }


    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class superadminPostViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView username, date, caption, postLikes, share_text, comment_text,edit_button;

        ImageView imageView, likePic;

        /*Button delete_button,edit_button;*/

        View mView;

        public superadminPostViewHolder(@NonNull View itemView) {
            super(itemView);

            mView = itemView;

            likePic = (ImageView) mView.findViewById(R.id.likes_image_button);

            share_text = (TextView) mView.findViewById(R.id.share_feature);

            comment_text = (TextView) mView.findViewById(R.id.comment_feature);

            edit_button=(TextView)mView.findViewById(R.id.edit_post);

            itemView.setOnClickListener(this);
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

        @Override
        public void onClick(View v) {
            SuperadminPosts superadminPosts=mList.get(getAdapterPosition());
            Intent intent=new Intent(context, UpdatePostActivity.class);
            intent.putExtra("pots", superadminPosts);
            /*Log.d("unique_post_id",postId);*/
            context.startActivity(intent);
        }
    }
}
