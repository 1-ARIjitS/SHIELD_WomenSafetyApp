package com.example.womensafety.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.womensafety.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;

public class AddingPostActivity extends AppCompatActivity {

    ImageView add_post_image;
    Button save_post_button;
    EditText post_caption;

    FirebaseAuth auth;
    FirebaseFirestore firestore;
    StorageReference storageReference;

    Uri postImageUri;

    String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding_post);

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        add_post_image = (ImageView) findViewById(R.id.post_add_logo);
        save_post_button = (Button) findViewById(R.id.post_save_button);
        post_caption = findViewById(R.id.post_caption_edit_text);

        currentUserId = auth.getCurrentUser().getUid();

        add_post_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(10, 10)
                        .setMinCropResultSize(512, 512)
                        .start(AddingPostActivity.this);
            }
        });

        save_post_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String caption = post_caption.getText().toString();
                if (!caption.isEmpty() && postImageUri != null) {
                    final StorageReference postRef = storageReference.child("post_images").child(FieldValue.serverTimestamp().toString() + ".jpg");
                    postRef.putFile(postImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if (task.isSuccessful()) {
                                postRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        HashMap<String, Object> postMap = new HashMap<>();
                                        postMap.put("image", uri.toString());
                                        postMap.put("user", currentUserId);
                                        postMap.put("caption", caption);
                                        postMap.put("time", FieldValue.serverTimestamp());

                                        firestore.collection("Posts").add(postMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(getApplicationContext(), "your post is successfully added !!!", Toast.LENGTH_SHORT).show();
                                                    startActivity(new Intent(AddingPostActivity.this, AdminActivity.class));
                                                    finish();
                                                } else {
                                                    Toast.makeText(getApplicationContext(), task.getException().toString(), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }
                                });
                            } else {
                                Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "Please Add Image And Write Your Caption", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                postImageUri = result.getUri();
                add_post_image.setImageURI(postImageUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(getApplicationContext(), result.getError().toString(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}