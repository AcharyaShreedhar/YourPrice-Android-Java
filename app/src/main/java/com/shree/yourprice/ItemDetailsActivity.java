package com.shree.yourprice;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class ItemDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);

        // Find the Toolbar and set it as the support action bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set the title for the action bar (optional)
        getSupportActionBar().setTitle("Details Page");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();

        // Retrieve the details from the intent
        String productName = intent.getStringExtra("productName");
        String productPrice = intent.getStringExtra("productPrice");
        int productImage = intent.getIntExtra("productImage", 0); // Provide a default value if not found
        String productDescription = intent.getStringExtra("productDescription");

        // Find the views in the activity_item_details.xml layout
        ImageView imageView = findViewById(R.id.imgItem);
        TextView nameTextView = findViewById(R.id.tvName);
        TextView priceTextView = findViewById(R.id.tvPrice);
        TextView descriptionTextView = findViewById(R.id.tvDescription);

        // Populate the views with the retrieved details
        nameTextView.setText(productName);
        priceTextView.setText(productPrice);
        descriptionTextView.setText(productDescription);

        // Load the product image using Glide or any other image loading library
        Glide.with(this).load(productImage).into(imageView);
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