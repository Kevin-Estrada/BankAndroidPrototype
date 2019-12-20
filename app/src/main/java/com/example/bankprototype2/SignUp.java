package com.example.bankprototype2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity {

    private EditText firtNameText;
    private EditText lastNameText;
    private EditText emailText;
    private EditText passwordText;
    private EditText confirmPassText;
    private Button registerAccount;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;

    FirebaseFirestore dataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        dataBase = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        firtNameText = findViewById(R.id.firstname);
        lastNameText = findViewById(R.id.lastname);
        emailText = findViewById(R.id.emailsignup);
        passwordText = findViewById(R.id.passsignup);
        confirmPassText = findViewById(R.id.confirmpass);
        registerAccount = findViewById(R.id.registeruser);
        progressBar = findViewById(R.id.progressBar);

        registerAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (firtNameText.length() == 0 || firtNameText.length() < 2) {
                    firtNameText.setError("Enter at least 3 characters.");
                } else if (lastNameText.length() == 0 || lastNameText.length() < 2) {
                    lastNameText.setError("Enter at least 3 characters.");
                } else if (emailText.length() == 0) {
                    emailText.setError("Enter a valid email.");
                } else if (passwordText.length() == 0 || passwordText.length() < 5) {
                    passwordText.setError("Enter at least a 6 character password.");
                } else if (!(confirmPassText.getText().toString().contentEquals(passwordText.getText()))) {
                    confirmPassText.setError("Passwords do not match.");
                } else {
                    registerNewUser();
                }
            }
        });
    }

    private void registerNewUser() {

        progressBar.setVisibility(View.VISIBLE);
        String pass = passwordText.getText().toString();
        String email = emailText.getText().toString();
        mAuth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Sign up Succesful!", Toast.LENGTH_LONG).show();
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(MainActivity.TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            saveUserData(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(MainActivity.TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Sign up failed. Try Again.", Toast.LENGTH_LONG).show();
                        }

                        // ...
                    }
                });


    }

    private void saveUserData(FirebaseUser user) {
        String firstName = firtNameText.getText().toString();
        String lastName = lastNameText.getText().toString();
        Double balance = 0.00;
        String userID = user.getUid();

        Map<String, Object> userData = new HashMap<>();
        userData.put("First Name", firstName);
        userData.put("Last Name", lastName);
        userData.put("Balance", balance);

        dataBase.collection("users").document(userID)
                .set(userData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(MainActivity.TAG,"DocumentSnapshot added with ID:");
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(MainActivity.TAG, "Error adding document",e);
                    }
                });
    }
}