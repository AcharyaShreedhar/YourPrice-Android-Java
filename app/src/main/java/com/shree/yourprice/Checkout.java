package com.shree.yourprice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Checkout extends AppCompatActivity {
    private EditText etName, etEmail, etCreditCardNumber, etSecurityCode;
    private Button btnPlaceOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        // Find the Toolbar and set it as the support action bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set the title for the action bar (optional)
        getSupportActionBar().setTitle("Checkout");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etCreditCardNumber = findViewById(R.id.etCreditCardNumber);
        etSecurityCode = findViewById(R.id.etSecurityCode);
        btnPlaceOrder = findViewById(R.id.btnPlaceOrder);

        btnPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the input values from the EditText fields
                String name = etName.getText().toString();
                String email = etEmail.getText().toString();
                String creditCardNumber = etCreditCardNumber.getText().toString();
                String securityCode = etSecurityCode.getText().toString();

                // Check if any field is empty
                if (name.isEmpty() || email.isEmpty() || creditCardNumber.isEmpty() || securityCode.isEmpty()) {
                    Toast.makeText(Checkout.this, "All fields are required.", Toast.LENGTH_SHORT).show();
                    return; // Stop further processing
                }
                // Validate email format
                if (!isValidEmail(email)) {
                    Toast.makeText(Checkout.this, "Invalid email format.", Toast.LENGTH_SHORT).show();
                    return; // Stop further processing
                }

                // Validate credit card number format (for example, a simple check here)
                if (!creditCardNumber.matches("\\d{16}")) {
                    Toast.makeText(Checkout.this, "Invalid credit card number.", Toast.LENGTH_SHORT).show();
                    return; // Stop further processing
                }

                // Validate security code format (for example, a simple check here)
                if (!securityCode.matches("\\d{3}")) {
                    Toast.makeText(Checkout.this, "Invalid security code.", Toast.LENGTH_SHORT).show();
                    return; // Stop further processing
                }

//                // All validations passed, create the message and show toast
//                String message = "Name: " + name + "\n" +
//                        "Email: " + email + "\n" +
//                        "Credit Card Number: " + creditCardNumber + "\n" +
//                        "Security Code: " + securityCode;
//
//                Toast.makeText(Checkout.this, message, Toast.LENGTH_LONG).show();
                Toast.makeText(Checkout.this, "Thank you for shopping our products. How do you like the services? Please provide feedback and reviews.", Toast.LENGTH_LONG).show();

                // Start MainActivity
                Intent intent = new Intent(Checkout.this, MainActivity.class);
                startActivity(intent);
                finish(); // Optional: Finish the current activity if needed
            }
        });


    }

    // Function to validate email format
    private boolean isValidEmail(CharSequence target) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle the back button click
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}