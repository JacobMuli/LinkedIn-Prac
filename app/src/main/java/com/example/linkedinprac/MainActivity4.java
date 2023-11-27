/**
 * Represents a popup window activity that facilitates email sending and phone call functionalities.
 */
package com.example.linkedinprac;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity4 extends AppCompatActivity {
    Button emailButton;
    Button mobileCallButton;
    EditText emailTextView;
    String emailContent, zeroPaddedMobileNumber;

    static final int PERMISSION_CODE = 100;

    /**
     * Initializes the popup window activity layout and functionality.
     *
     * @param savedInstanceState The saved instance state bundle.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width = (int) (displayMetrics.widthPixels * 0.99);
        int height = (int) (displayMetrics.heightPixels * .25);

        getWindow().setLayout(width, height);

        emailButton = findViewById(R.id.send_email_button);
        mobileCallButton = findViewById(R.id.call_button);
        emailTextView = findViewById(R.id.popup_window_email_input);

        // Retrieves email and phone number data from intent extras
        Intent intent = getIntent();

        String userEmail = intent.getStringExtra("email");
        String mobileNo = intent.getStringExtra("phone");

        if (mobileNo != null) Log.d("MOBILE", mobileNo);

        // Request permission to make phone calls if not granted
        if (ContextCompat.checkSelfPermission(MainActivity4.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity4.this, new String[]{android.Manifest.permission.CALL_PHONE}, PERMISSION_CODE);
        }

        // Sets onClickListener for the mobileCallButton to initiate a phone call
        mobileCallButton.setOnClickListener(v -> {
            if (mobileNo != null && !mobileNo.isEmpty()) {
                zeroPaddedMobileNumber = padMobileNumber(mobileNo);

                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + zeroPaddedMobileNumber));

                if (callIntent.getData() != null) {
                    Log.d("MOBILE_URI", callIntent.getData().toString());
                    startActivity(callIntent);
                } else {
                    Log.e("MOBILE_URI", "Call URI is null");
                }
            } else {
                Log.e("MOBILE_URI", "Mobile number is null or empty");
            }
        });

        emailButton.setOnClickListener(v -> {
            emailContent = String.valueOf(emailTextView.getText()).trim();
            if (checkInputField(emailTextView)) {
                sendEmail(emailContent, userEmail);
            } else {
                Toast.makeText(MainActivity4.this, "Invalid email input", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Sends an email using the device's email client.
     *
     * @param emailBody The body content of the email to be sent.
     * @param to_email  The recipient's email address.
     */
    public void sendEmail(String emailBody, String to_email) {
        // Subject of the email
        String subject = "Message from LinkedN Mock user";

        // Create an intent to send an email
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        // Set recipient email address
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{to_email});

        // Set email subject
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);

        // Set email body content
        emailIntent.putExtra(Intent.EXTRA_TEXT, emailBody);

        // Set email type to RFC822 (standard for email messages)
        emailIntent.setType("message/rfc822");

        // Start the activity to choose an email client to send the email
        startActivity(Intent.createChooser(emailIntent, "Choose email client:"));
    }

    /**
     * Checks if the input field is valid.
     *
     * @param editText The input field to be checked.
     * @return True if the input is valid, false otherwise.
     */
    private boolean checkInputField(EditText editText) {
        return editText != null && editText.getText() != null && editText.getText().length() > 0;
    }

    /**
     * Pads the mobile number based on certain conditions.
     *
     * @param mobileNo The mobile number to be padded.
     * @return The padded mobile number.
     */
    private String padMobileNumber(String mobileNo) {
        if (mobileNo.indexOf("7") == 0 || mobileNo.indexOf("1") == 0) {
            return "0" + mobileNo;
        } else if (mobileNo.contains("254")) {
            return "+" + mobileNo;
        } else {
            return mobileNo;
        }
    }
}
