package com.example.bankprototype2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.text.DecimalFormat;

public class HomeScreen extends AppCompatActivity {

    // Declaring needed variables for program
    private TextView nameOfUser;
    private TextView balance;
    private EditText deposit;
    private EditText withdraw;
    private Button confirm_amount;
    private Button logOutButton;
    private FirebaseAuth mAuth;
    String firstName;
    double userBalance;

    private FirebaseFirestore dataBase;
    private DocumentReference DocRef;

    // onCreate begins the activity with all associated XML files.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        // Create an instance to use Firebase authentication and Firestore database with the addition
        //of referencing document with associated UID.
        dataBase = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        DocRef = dataBase.collection("users").document(mAuth.getCurrentUser().getUid());

        //Find Ids of objects within the associated XML file.
        nameOfUser = findViewById(R.id.name_of_user);
        balance = findViewById(R.id.display);
        deposit = findViewById(R.id.deposit);
        withdraw = findViewById(R.id.withdraw);
        confirm_amount = findViewById(R.id.confirmAmount);
        logOutButton = findViewById(R.id.logOut);

        deposit.setText(String.format("%.2f", 0.00));
        withdraw.setText(String.format("%.2f", 0.00));

        //If confirm amount button is clicked with fields filled in, it will run the balanceUpdate method.
        confirm_amount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(deposit.length() == 0) {
                    deposit.setError("Enter Number");
                }
                else if(withdraw.length() == 0) {
                    withdraw.setError("Enter Number");
                } else {
                    balanceUpdate();
                }
            }

        });

        //If logout button is clicked then activity will end and user will be returned to MainActivity.
        //Balance will be saved even after logging out and logging back in.
        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                endActivity();
            }
        });

    }

    //This onStart is used to display the necessary fields to app, such as first name associated with
    //logged in user as well as the associated balance that is saved on the database
    @Override
    protected void onStart() {
        super.onStart();
        DocRef.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                if (e != null) {
                    Toast.makeText(HomeScreen.this, "error while coding!", Toast.LENGTH_LONG);
                    Log.d("Error", e.toString());
                    return;
                }
                //If the document exists, then the text will show the users first name and balance.
                if (documentSnapshot.exists()) {
                    firstName = documentSnapshot.getString("First Name");
                    userBalance = documentSnapshot.getDouble("Balance");
                    nameOfUser.setText("Hello " + firstName);
                    balance.setText(String.format("%.2f",userBalance));                }
            }
        });
    }

    //Once this is called, the balance will update according to what is in the text fields of the deposit and
    //withdraw boxes. The result is added to the overall userBalance which will get updated in real time
    //on Firestore database.
    private void balanceUpdate(){

        double depositInput = Double.parseDouble(deposit.getText().toString());
        double withdrawInput = Double.parseDouble(withdraw.getText().toString());

            double result = depositInput - withdrawInput;

            userBalance += result;

            DocRef.update("Balance",userBalance);
            balance.setText(String.format("%.2f",userBalance));
            }

    private void endActivity(){
        finish();
    }

    @Override
    public void onBackPressed()
    {
    }


}
