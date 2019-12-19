package com.example.bankprototype2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {


    public static String TAG = "KEVIN";
    private EditText passwordText;
    private EditText emailText;
    private Button signInButton;
    private Button signUpButton;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        passwordText = findViewById(R.id.password);
        emailText = findViewById(R.id.email);
        signInButton = findViewById(R.id.signInButton);
        signUpButton = findViewById(R.id.signUpButton);
        progressBar = findViewById(R.id.progressBar);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(emailText.length() == 0) {
                    emailText.setError("Enter Existing Email");
                }
                else if(passwordText.length() == 0) {
                    passwordText.setError("Enter Existing Password");
                } else {
                    signIn();
                }
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signupactivity = new Intent(getBaseContext(), SignUp.class);
                startActivity(signupactivity);
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            mAuth.signOut();
        }

    }
    private void signIn(){

        progressBar.setVisibility(View.VISIBLE);
        String pass = passwordText.getText().toString();
        String email = emailText.getText().toString();
        Log.d(TAG, "signIn:  " + pass + " " + email);

        mAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(),"Sign in Succesful!",Toast.LENGTH_LONG).show();
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent signinaction = new Intent(getBaseContext(), HomeScreen.class);
                            startActivity(signinaction);


                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(),"Sign in failed. Try Again.",Toast.LENGTH_LONG).show();

                        }

                        // ...
                    }
                });
    }

}
