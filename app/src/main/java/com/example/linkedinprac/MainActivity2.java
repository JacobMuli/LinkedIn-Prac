package com.example.linkedinprac;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Objects;
import java.util.UUID;

public class MainActivity2 extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private String email, password, username, gender, shortBio, skills, phoneNo;
    private EditText usernameEditText, emailEditText, passwordEditText, confirmPasswordEditText, shortBioEditText, skillsEditText, phoneNoEditText;
    private Spinner genderSpinner;
    private Button registerButton, profilePicButton;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private Uri selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        // Initialize Firebase components
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        // Initialize UI elements
        usernameEditText = findViewById(R.id.uname);
        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.pass);
        confirmPasswordEditText = findViewById(R.id.confirm_pass);
        genderSpinner = findViewById(R.id.gender_spinner);
        shortBioEditText = findViewById(R.id.short_bio);
        skillsEditText = findViewById(R.id.skills);
        phoneNoEditText = findViewById(R.id.phone_no);
        registerButton = findViewById(R.id.sgn_register);
        profilePicButton = findViewById(R.id.profile_pic);

        // Assuming you have an array of gender options in resources
        String[] genderOptions = getResources().getStringArray(R.array.gender_array);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, genderOptions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(adapter);

        int defaultGenderIndex = findIndex(genderOptions, "Male");
        genderSpinner.setSelection(defaultGenderIndex);

        // Set onClickListener for registerButton
        registerButton.setOnClickListener(view -> {
            // Retrieve user inputs
            username = usernameEditText.getText().toString().trim();
            email = emailEditText.getText().toString().trim();
            password = passwordEditText.getText().toString().trim();
            String confirmPassword = confirmPasswordEditText.getText().toString().trim();
            gender = genderSpinner.getSelectedItem().toString();
            shortBio = shortBioEditText.getText().toString().trim();
            skills = skillsEditText.getText().toString().trim();
            phoneNo = phoneNoEditText.getText().toString().trim();

            // Validate inputs
            if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || gender.isEmpty() || shortBio.isEmpty() || skills.isEmpty() || phoneNo.isEmpty()) {
                Toast.makeText(MainActivity2.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // Check if passwords match
            if (!password.equals(confirmPassword)) {
                Toast.makeText(MainActivity2.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            if (selectedImageUri != null) {
                uploadProfilePicture(selectedImageUri);
            } else {
                performUserRegistration(null);
            }
        });

        profilePicButton.setOnClickListener(view -> openGallery());
    }

    private void performUserRegistration(@Nullable String imageUrl) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(MainActivity2.this, task -> {
                    if (task.isSuccessful()) {
                        // Upload user details to the database
                        User user = new User(username, email, gender, shortBio, skills, phoneNo);
                        saveUserDetails(user, imageUrl);
                        databaseReference.child(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid()).setValue(user);

                        Toast.makeText(MainActivity2.this, "Registration successful", Toast.LENGTH_SHORT).show();

                        // Start the MainActivity upon successful registration
                        Intent intent = new Intent(MainActivity2.this, MainActivity.class);
                        startActivity(intent);
                        finish(); // Optional: close the current activity if you don't want to go back to it

                    } else {
                        Toast.makeText(MainActivity2.this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void uploadProfilePicture(Uri imageUri) {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference imageRef = storageReference.child("profile_images/" + UUID.randomUUID().toString());

        imageRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    // Image uploaded successfully, get the download URL
                    imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        // Call performUserRegistration with the correct image URL
                        performUserRegistration(uri.toString());
                    }).addOnFailureListener(e -> {
                        // Handle the failure to get the download URL
                        Toast.makeText(MainActivity2.this, "Image upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
                })
                .addOnFailureListener(e -> {
                    // Handle the failure to upload the image
                    Toast.makeText(MainActivity2.this, "Image upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }




    private void saveUserDetails(User user, @Nullable String imageUrl) {
        // Implement the logic to save user details to the Firebase Realtime Database
        // You can use the databaseReference for this purpose
        // Save the user object and the imageUrl to the appropriate location in the database
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();
        }
    }

    private int findIndex(String[] array, String value) {
        for (int i = 0; i < array.length; i++) {
            if (array[i].equals(value)) {
                return i;
            }
        }
        return -1; // Not found
    }
}
