package com.example.womensafety.Admin.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.womensafety.Admin.Models.SuperadminPosts;
import com.example.womensafety.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

public class UpdatePostActivity extends AppCompatActivity {

    public EditText caption_edit;
    Button delete;
    Button update;

    FirebaseFirestore firestore;

    SuperadminPosts posts;

    String caption;

    String id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_post);

        firestore=FirebaseFirestore.getInstance();


        caption_edit=(EditText)findViewById(R.id.edit_post_text_box);

        delete=(Button)findViewById(R.id.delete_post_button);

        update=(Button)findViewById(R.id.update_post_button);

        posts  = (SuperadminPosts) getIntent().getSerializableExtra("pots");

        caption=posts.getCaption();

        caption_edit.setText(caption);

        id=posts.getId();



        Log.d("post_id",id);

        /*DocumentReference documentReference = firestore.document("Posts/"+postsId).document(postsId);

        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists())
                {
                    caption=documentSnapshot.getString("caption");
                    caption_edit.setText(caption);
                }
            }
        });

        caption_edit.setText(post.getCaption());

        postsId=post.PostId;*/

        //updating the post

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!caption_edit.getText().toString().isEmpty())
                {
                    firestore.collection("Posts")
                            .document(id).update(
                                    "caption",caption_edit.getText().toString()
                    ).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(UpdatePostActivity.this,"caption of the post is updated successfully",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(UpdatePostActivity.this, SuperAdminDashboardActivity.class));
                        }
                    });
                }else{
                    Toast.makeText(UpdatePostActivity.this,"INVALID, Caption Is Empty, Please Enter A Valid Caption",Toast.LENGTH_SHORT).show();
                }
            }
        });

        //deleting the post

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(UpdatePostActivity.this);
                builder.setTitle("ARE YOU SURE, YOU WANT TO DELETE THIS POST ?");
                builder.setMessage("clicking on OK will delete this post permanently from the database and you will not be able to retrieve it");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       deletePost();
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
        });
    }

    private void deletePost() {
        firestore.collection("Posts").document(id).delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(UpdatePostActivity.this,"Post Deleted Successfully",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(UpdatePostActivity.this, SuperAdminDashboardActivity.class));
                        }
                    }
                });
    }
}