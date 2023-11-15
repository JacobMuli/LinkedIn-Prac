package com.example.linkedinprac;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity2 extends AppCompatActivity {

    EditText username, shortbio, confirmpass, skills, phone_no, Email, password;
    TextView login;
    ImageView profile_pic;
    Button register, prof_pic;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    ProgressDialog progdiag;
    private static final int PICK_IMAGE_REQUEST = 1;


    private DatabaseReference rootdatabaseref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        username = findViewById(R.id.uname);
        Email = findViewById(R.id.email);
        password = findViewById(R.id.pass);
        confirmpass = findViewById(R.id.confirm_pass);
        shortbio = findViewById(R.id.short_bio);
        skills = findViewById(R.id.skills);
        prof_pic = findViewById(R.id.profile_pic);
        profile_pic = findViewById(R.id.profile_pic_inflated);
        phone_no = findViewById(R.id.phone_no);
        progdiag = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        Spinner spinner = findViewById(R.id.gender_spinner);
        register = findViewById(R.id.sgn_register);
        login = findViewById(R.id.login);

        rootdatabaseref = FirebaseDatabase.getInstance().getReference().child("Users");

        prof_pic.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        });

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.gender_array,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(0);

        register.setOnClickListener(view -> {
            String uname = username.getText().toString();
            String short_bio = shortbio.getText().toString();
            String skill = skills.getText().toString();
            String profpic = prof_pic.getText().toString();
            String phoneno = phone_no.getText().toString();
            String gender = spinner.getSelectedItem().toString();

            if (uname.isEmpty() || short_bio.isEmpty() || skill.isEmpty() || profpic.isEmpty() || phoneno.isEmpty() || gender.isEmpty()) {
                Toast.makeText(MainActivity2.this, "Please fill in all the fields", Toast.LENGTH_SHORT).show();
            } else if (phoneno.length() < 10) {
                Toast.makeText(MainActivity2.this, "Phone number must be 10 characters", Toast.LENGTH_SHORT).show();
            }else {

                DatabaseReference userRef = rootdatabaseref.push();

                userRef.child("username").setValue(uname);
                userRef.child("short_bio").setValue(short_bio);
                userRef.child("skills").setValue(skill);
                userRef.child("prof_pic").setValue(profpic);
                userRef.child("phone_no").setValue(phoneno);
                userRef.child("gender").setValue(gender)
                        .addOnSuccessListener(databaseTask -> PerformAuth())
                        .addOnFailureListener(databaseTask -> Toast.makeText(MainActivity2.this, "Error sending details.", Toast.LENGTH_SHORT).show());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            // Get the selected image URI
            Uri selectedImageUri = data.getData();

            // Set the selected image to the ImageView
            profile_pic.setImageURI(selectedImageUri);

            // Make the ImageView visible
            profile_pic.setVisibility(View.VISIBLE);
        }
    }

    private void PerformAuth() {
        String email = Email.getText().toString();
        String pass = password.getText().toString();
        String confirm_pass = confirmpass.getText().toString();

        if (email.isEmpty()||pass.isEmpty()||confirm_pass.isEmpty()) {
            Toast.makeText(MainActivity2.this, "Please fill in all the fields", Toast.LENGTH_SHORT).show();
        } else if (!email.matches(emailPattern)) {
            Toast.makeText(MainActivity2.this, "Invalid Email Address", Toast.LENGTH_SHORT).show();
        } else if (!pass.equals(confirm_pass)) {
            Toast.makeText(MainActivity2.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
        } else {
            progdiag.setMessage("Registering User...");
            progdiag.setTitle("Registration");
            progdiag.setCanceledOnTouchOutside(false);
            progdiag.show();

            mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    progdiag.dismiss();
                    Toast.makeText(MainActivity2.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity2.this, MainActivity3.class);
                    startActivity(intent);
                } else {
                    progdiag.dismiss();
                    Toast.makeText(MainActivity2.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                }
            });
        }

        login.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity2.this, MainActivity.class);
            startActivity(intent);
        });
    }
}