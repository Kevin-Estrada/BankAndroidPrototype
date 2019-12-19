package com.example.bankprototype2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity {

    public static String TAG = "Reminder";
    private EditText firtNameText;
    private EditText lastNameText;
    private EditText emailText;
    private EditText passwordText;
    private EditText confirmPassText;
    private Button registerAccount;
    private FirebaseAuth mAuth;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();

        firtNameText = findViewById(R.id.firstname);
        lastNameText = findViewById(R.id.lastname);
        emailText = findViewById(R.id.emailsignup);
        passwordText = findViewById(R.id.passsignup);
        confirmPassText = findViewById(R.id.confirmpass);
        registerAccount = findViewById(R.id.registeruser);

        registerAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(firtNameText.length() == 0 || firtNameText.length() < 2){
                    firtNameText.setError("Enter at least 3 characters.");
                } else if(lastNameText.length() == 0 || lastNameText.length()< 2){
                    lastNameText.setError("Enter at least 3 characters.");
                } else if(emailText.length() == 0){
                    emailText.setError("Enter a valid email.");
                } else if(passwordText.length() == 0 || passwordText.length() < 5){
                    passwordText.setError("Enter at least a 6 character password.");
                } else if(!(confirmPassText.getText().toString().contentEquals(passwordText.getText()))){
                    confirmPassText.setError("Passwords do not match.");
                } else {
                    registerNewUser();
                }
            }
        });
    }
    private void registerNewUser(){

        String pass = passwordText.getText().toString();
        String email = emailText.getText().toString();
        mAuth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(),"Sign up Succesful!",Toast.LENGTH_LONG).show();
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            saveUserData();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(),"Sign up failed. Try Again.",Toast.LENGTH_LONG).show();
                        }

                        // ...
                    }
                });


    }

    private void saveUserData() {
        String firstName = firtNameText.getText().toString();
        String lastName = lastNameText.getText().toString();
        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();
        double balance = 0;

        Map<String, Object> user = new HashMap<>();
        user.put("First Name", firstName);
        user.put("Last Name", lastName);
        user.put("Email Address", email);
        user.put("Password",password);
        user.put("Balance",balance);

        db.collection("Users").document(firstName)
                .set(user)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Log.d("User Register", "Document has been saved!");
                        } else {
                            Log.w("User Register","Document was not saved !",task.getException());
                        }
                    }
                });
                    }
    }