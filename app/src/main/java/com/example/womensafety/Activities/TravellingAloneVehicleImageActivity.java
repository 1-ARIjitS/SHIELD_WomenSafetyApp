package com.example.womensafety.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.womensafety.R;
import com.example.womensafety.User.Detail_Forms;
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
import java.util.Objects;

public class TravellingAloneVehicleImageActivity extends AppCompatActivity {

    ImageView add_vehicle_image;

    Button save_vehicle_image_button;

    FirebaseAuth auth;
    FirebaseFirestore firestore;
    StorageReference storageReference;

    Uri vehicleImageUri;

    String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travelling_alone_vehicle_image);

        auth = FirebaseAuth.getInstance();
        currentUserId = Objects.requireNonNull(auth.getCurrentUser()).getUid();
        firestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        add_vehicle_image=findViewById(R.id.add_vehicle_image);

        save_vehicle_image_button=findViewById(R.id.save_vehicle_image_button);

        add_vehicle_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(20, 10)
                        .setMinCropResultSize(512, 512)
                        .start(TravellingAloneVehicleImageActivity.this);
            }
        });


        save_vehicle_image_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ( vehicleImageUri != null) {
                    final StorageReference vehicle_image_ref = storageReference.child("vehicle_images").child(FieldValue.serverTimestamp().toString() + ".jpg");
                    vehicle_image_ref.putFile(vehicleImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if (task.isSuccessful()) {
                                vehicle_image_ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        /*HashMap<String, Object> postMap = new HashMap<>();
                                        postMap.put("image", uri.toString());
                                        postMap.put("user", currentUserId);
                                        postMap.put("caption", caption);
                                        postMap.put("time", FieldValue.serverTimestamp());*/

                                        /*firestore.collection("Posts").add(postMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
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
                                        });*/

                                        Toast.makeText(getApplicationContext(), "your vehicle image successfully added !!!", Toast.LENGTH_SHORT).show();
                                        Intent intent=new Intent(TravellingAloneVehicleImageActivity.this, Detail_Forms.class);
                                        intent.putExtra("vehicle_image_url",uri.toString());
                                        startActivity(intent);
                                        finish();
                                    }
                                });
                            } else {
                                Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "Please Add Your Vehicle Image To Continue", Toast.LENGTH_SHORT).show();
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
                vehicleImageUri = result.getUri();
                add_vehicle_image.setImageURI(vehicleImageUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(getApplicationContext(), result.getError().toString(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
