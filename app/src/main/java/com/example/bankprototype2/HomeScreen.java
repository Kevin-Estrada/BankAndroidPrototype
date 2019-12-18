package com.example.bankprototype2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class HomeScreen extends AppCompatActivity {

    private TextView balance;
    private EditText deposit;
    private EditText withdraw;
    private Button confirm_amount;
    private Button logOutButton;
    private boolean clicked;
    double sumResult = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

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
                }
                else {
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

    private void balanceUpdate(){
            double depositInput = Double.parseDouble(deposit.getText().toString());
            double withdrawInput = Double.parseDouble(withdraw.getText().toString());

            double result = depositInput - withdrawInput;
            sumResult += result;
            balance.setText(String.format("%.2f", sumResult));
            }

    private void endActivity(){
        Intent signout = new Intent(getBaseContext(), MainActivity.class);
        startActivity(signout);
        }
    }
