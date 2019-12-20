package com.example.bankprototype2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.SetOptions;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class HomeScreen extends AppCompatActivity {

    private TextView nameOfUser;
    private TextView balance;
    private EditText deposit;
    private EditText withdraw;
    private Button confirm_amount;
    private Button logOutButton;
    private boolean clicked;
    private FirebaseAuth mAuth;
    double sumResult;
    String firstName;
    String userBalance;

    private static DecimalFormat df2 = new DecimalFormat("#.##");

    private FirebaseFirestore dataBase = FirebaseFirestore.getInstance();
    private DocumentReference DocRef = dataBase.collection("users").document("IxTR9f5pdYvEyey91l18");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

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
                clicked = false;
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
                    userBalance = documentSnapshot.getString("Balance");
                    nameOfUser.setText("Hello " + firstName);
                    balance.setText(userBalance);
                }
            }
        });
    }


    private void balanceUpdate(){
            double depositInput = Double.parseDouble(deposit.getText().toString());
            double withdrawInput = Double.parseDouble(withdraw.getText().toString());
            double totalBalance = Double.parseDouble(userBalance);


            double result = depositInput - withdrawInput;

            totalBalance += result;

            df2.format(totalBalance);

            String s = (totalBalance + "");

            userBalance = s;

            DocRef.update("Balance",userBalance);
            balance.setText(userBalance);
            }

    private void endActivity(){

        Intent signout = new Intent(getBaseContext(), MainActivity.class);
        startActivity(signout);
        }

}
