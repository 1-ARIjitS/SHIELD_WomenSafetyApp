package com.example.womensafety.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.womensafety.Adapters.commentsAdapter;
import com.example.womensafety.Models.comments;
import com.example.womensafety.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CommentActivity extends AppCompatActivity {

    EditText add_comment;

    Button add_comment_button;

    RecyclerView commentRecycler;

    FirebaseAuth auth;

    FirebaseFirestore firestore;

    String currentUserId;

    String post_id;

    commentsAdapter adapter;

    List<comments> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        auth=FirebaseAuth.getInstance();

        firestore=FirebaseFirestore.getInstance();

        currentUserId= Objects.requireNonNull(auth.getCurrentUser()).getUid();
        post_id=getIntent().getStringExtra("postid");

        mList=new ArrayList<comments>();

        adapter=new commentsAdapter(CommentActivity.this,mList);

        add_comment=(EditText)findViewById(R.id.comment_add_comment);
        add_comment_button=(Button)findViewById(R.id.comment_button);
        commentRecycler=findViewById(R.id.commentRecycler);
        commentRecycler.setHasFixedSize(true);
        commentRecycler.setLayoutManager(new LinearLayoutManager(this));

        commentRecycler.setAdapter(adapter);

        firestore.collection("Posts/"+post_id+"/Comments").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                assert value != null;
                for (DocumentChange doc:value.getDocumentChanges())
               {
                   if(doc.getType()==DocumentChange.Type.ADDED)
                   {
                       comments comments=doc.getDocument().toObject(com.example.womensafety.Models.comments.class);
                       mList.add(comments);
                   }
                   adapter.notifyDataSetChanged();
               }
            }
        });


        add_comment_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment=add_comment.getText().toString();
                int comment_size=comment.length();

                if(comment_size>0)
                {
                    Map<String,Object> commentMap=new HashMap<>();
                    commentMap.put("comment",comment);
                    commentMap.put("time", FieldValue.serverTimestamp());
                    commentMap.put("user", currentUserId);
                   firestore.collection("Posts/"+post_id+"/Comments").add(commentMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                       @Override
                       public void onComplete(@NonNull Task<DocumentReference> task) {
                          if(task.isSuccessful())
                          {
                              Toast.makeText(getApplicationContext(),"comment added",Toast.LENGTH_SHORT).show();
                          }
                          else
                          {
                              Toast.makeText(getApplicationContext(), Objects.requireNonNull(task.getException()).getMessage(),Toast.LENGTH_SHORT).show();
                          }
                       }
                   });
                }else{
                    Toast.makeText(getApplicationContext(),"please write a comment for the post",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}