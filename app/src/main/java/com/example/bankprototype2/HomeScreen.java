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

    private TextView nameOfUser;
    private TextView balance;
    private EditText deposit;
    private EditText withdraw;
    private Button confirm_amount;
    private Button logOutButton;
    private FirebaseAuth mAuth;
    String firstName;
    double userBalance;

    private static DecimalFormat df2 = new DecimalFormat("#.##");

    private FirebaseFirestore dataBase;
    private DocumentReference DocRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);


        dataBase = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        DocRef = dataBase.collection("users").document(mAuth.getCurrentUser().getUid());

        nameOfUser = findViewById(R.id.name_of_user);
        balance = findViewById(R.id.display);
        deposit = findViewById(R.id.deposit);
        withdraw = findViewById(R.id.withdraw);
        confirm_amount = findViewById(R.id.confirmAmount);
        logOutButton = findViewById(R.id.logOut);

        deposit.setText(String.format("%.2f", 0.00));
        withdraw.setText(String.format("%.2f", 0.00));

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

        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                endActivity();
            }
        });

    }

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
                if (documentSnapshot.exists()) {
                    firstName = documentSnapshot.getString("First Name");
                    userBalance = documentSnapshot.getDouble("Balance");
                    nameOfUser.setText("Hello " + firstName);
                    balance.setText(String.format("%.2f",userBalance));                }
            }
        });
    }


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
