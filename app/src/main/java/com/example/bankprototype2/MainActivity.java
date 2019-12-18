package com.example.bankprototype2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    public static String TAG = "KEVIN";
    private EditText passwordText;
    private EditText emailText;
    private Button signInButton;
    private Button signUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        passwordText = findViewById(R.id.password);
        emailText = findViewById(R.id.email);
        signInButton = findViewById(R.id.signInButton);
        signUpButton = findViewById(R.id.signUpButton);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });

    }
    private void signIn(){
        String pass = passwordText.getText().toString();
        String email = emailText.getText().toString();
        Log.d(TAG, "signIn:  " + pass + " " + email);
    }

    private void signUp(){
        String pass = passwordText.getText().toString();
        String email = emailText.getText().toString();
    }

}
