package com.example.linkedinprac;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
//import com.squareup.picasso.Callback;
//import com.squareup.picasso.Picasso;



public class MainActivity3 extends AppCompatActivity {

    private TextView unameTextView, genderTextView, phoneNoTextView, shortBioTextView, skillsTextView;
    private Button button;
    private FloatingActionButton emailButton;
    private ImageView prof_pic;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        // Initialize TextViews
        unameTextView = findViewById(R.id.uname);
        genderTextView = findViewById(R.id.gender);
        phoneNoTextView = findViewById(R.id.phone_no);
        shortBioTextView = findViewById(R.id.short_bio);
        skillsTextView = findViewById(R.id.skills);
        prof_pic = findViewById(R.id.profile_pic);

        emailButton = findViewById(R.id.email_icon);
        button = findViewById(R.id.logout);
        storageReference = FirebaseStorage.getInstance().getReference();

        // Fetch user details from Firebase
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId);

            userRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        User user = dataSnapshot.getValue(User.class);

                        // Set TextViews with user details
                        unameTextView.setText(user.getUsername());
                        genderTextView.setText(user.getGender());
                        phoneNoTextView.setText(user.getPhoneNo());
                        shortBioTextView.setText(user.getShortBio());
                        skillsTextView.setText(user.getSkills());

                        // using cureent user id to get the image
                        StorageReference profileRef = storageReference.child("users/"+userId+"/profile.jpeg");

                        try {
                            // create temporary file to store the image
                            final File localFile = File.createTempFile("tempImage", "jpeg");

                            // get the image from firebase storage and store it in the temporary file
                            profileRef.getFile(localFile).addOnSuccessListener(taskSnapshot -> {
                               // decode the image using bit map
                                Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                                // set the image view with the image using glide
                                prof_pic.setImageBitmap(bitmap);
                            }).addOnFailureListener(e -> {
                                // Handle any errors
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Handle error
                }
            });
        }

        emailButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity3.this, MainActivity4.class);
            startActivity(intent);
        });

        button.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(MainActivity3.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }
}
