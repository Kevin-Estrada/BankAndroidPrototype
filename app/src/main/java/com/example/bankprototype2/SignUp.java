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
    // Declaring needed variables for program
    private EditText firtNameText;
    private EditText lastNameText;
    private EditText emailText;
    private EditText passwordText;
    private EditText confirmPassText;
    private Button registerAccount;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;

    FirebaseFirestore dataBase;

    // onCreate begins the activity with all associated XML files.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Create an instance to use Firebase authentication and Firestore database.
        dataBase = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        //Find Ids of objects within the associated XML file.
        firtNameText = findViewById(R.id.firstname);
        lastNameText = findViewById(R.id.lastname);
        emailText = findViewById(R.id.emailsignup);
        passwordText = findViewById(R.id.passsignup);
        confirmPassText = findViewById(R.id.confirmpass);
        registerAccount = findViewById(R.id.registeruser);
        progressBar = findViewById(R.id.progressBar);

        //Account register will be successful or fail according to the fields within the signUpCredentials method.
        registerAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUpCredentials();
            }
        });
    }

    //This method contains all the restrictions to each field of the form, with the inclusion of the
    //registerNewUser method.
    private void signUpCredentials(){
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

    //This method creates the email and password associated with a UID number to distinguish itself from others.
    //If the creation is successful, then the user information is obtained and then carried over to the
    //saveDataUser method to create the document with users information.
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

    //This method creates the document with users first name, last name, and a starting balance of 0.
    //The users UID is then added to the collection as its own document ID, that way it can be referenced later.
    //The information is then uploaded to Firestore if user was registered successfully.
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