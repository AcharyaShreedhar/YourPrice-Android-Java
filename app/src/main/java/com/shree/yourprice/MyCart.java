package com.shree.yourprice;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MyCart extends AppCompatActivity implements OnCartItemRemovedListener {

    private RecyclerView recyclerView;
    private MyCartAdapter myCartAdapter;
    private List<CartItem> cartItemList;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    TextView tvTotalPrice;

    Button btnCheckout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_cart);
        // Find the Toolbar and set it as the support action bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set the title for the action bar (optional)
        getSupportActionBar().setTitle("MyCart");

        drawerLayout = findViewById(R.id.drawerLayout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        NavigationView navigationView = findViewById(R.id.navigationView);
        View headerView = navigationView.getHeaderView(0); // Get the header view
        TextView profileName = headerView.findViewById(R.id.profileName);
        ImageView profileImage = headerView.findViewById(R.id.profileImage);

        UserDetails userDetails = UserManager.getInstance().getUserDetails();
        if (userDetails != null) {
            profileName.setText(userDetails.getName());
            String lowercaseUsername = userDetails.getUsername().toLowerCase();
            int resourceId = getResources().getIdentifier(lowercaseUsername, "drawable", getPackageName());

            Glide.with(this)
                    .load(resourceId)
                    .placeholder(R.drawable.price) // Optional: Add a placeholder image
                    .error(R.drawable.ic_user) // Optional: Add an error image
                    .into(profileImage);

            // Set profile image if needed
        } else {
            // Handle the case when the user is not logged in
            profileName.setText("Username"); // Default text
            // Set default profile image if needed
        }
        // Handle navigation item clicks
        navigationView.setNavigationItemSelectedListener(item -> {
            // Handle navigation item clicks here
            // For example, you can switch fragments or perform actions based on the selected item
            int itemId = item.getItemId();

            if (itemId == R.id.home) {
                Toast.makeText(this, "This clicked page will open " + item + "Page", Toast.LENGTH_LONG).show();
            } else if (itemId == R.id.products) {
                startActivity(new Intent(this, MainActivity.class));
            } else if (itemId == R.id.stores) {
                startActivity(new Intent(this, Stores.class));
            } else if (itemId == R.id.mycart) {
//                startActivity(new Intent(this, MyCart.class));
            } else if (itemId == R.id.mywishlist) {
                // Start the WishlistActivity
                startActivity(new Intent(this, WishlistActivity.class));
                return true;
            } else if (itemId == R.id.contactus) {
                Toast.makeText(this, "This clicked page will open " + item + "Page", Toast.LENGTH_LONG).show();
            } else if (itemId == R.id.support) {
                Toast.makeText(this, "This clicked page will open " + item + "Page", Toast.LENGTH_LONG).show();
            } else if (itemId == R.id.feedback) {
                Toast.makeText(this, "This clicked page will open " + item + "Page", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "This clicked page will open " + item + "Page", Toast.LENGTH_LONG).show();
            }
            drawerLayout.closeDrawers();
            return true;
        });

        // Find the RecyclerView in the layout
        recyclerView = findViewById(R.id.rvCart);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);

        btnCheckout = findViewById(R.id.btnCheckout);

        MyCart.MyCartNetworkTask myCartNetworkTask = new MyCart.MyCartNetworkTask();
        myCartNetworkTask.execute();
        // Create a list of cart items (Replace this with your actual data)
        cartItemList = createCartItemList();

        // Create and set up the adapter for the RecyclerView
        myCartAdapter = new MyCartAdapter(cartItemList, tvTotalPrice, btnCheckout, this::onCartItemRemoved);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(myCartAdapter);
        double totalPrice = myCartAdapter.calculateTotalPrice();
        if (myCartAdapter.isCartEmpty()) {
            // Cart is empty, hide the checkout button
            btnCheckout.setVisibility(View.GONE);
            // Hide the total price TextView
            tvTotalPrice.setVisibility(View.GONE);
            // Display the empty cart message in the center
            Toast toast = Toast.makeText(this, "Your cart is empty. Please explore our exciting products. Thank you for using our application", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP, 0, 16);
            toast.show();
        } else {
            // Cart is not empty, show the checkout button and total price TextView
            btnCheckout.setVisibility(View.VISIBLE);
            tvTotalPrice.setVisibility(View.VISIBLE);
            // Calculate the total price and update the total price TextView
            tvTotalPrice.setText("Total Price: $" + String.format("%.2f", totalPrice));
            btnCheckout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MyCart.this, Checkout.class);
                    startActivity(intent);
                }


            });
        }
    }

    // Method to create a sample list of cart items (Replace this with your actual data source)
    private List<CartItem> createCartItemList() {
        List<CartItem> cartItemList = new ArrayList<>();
        cartItemList.add(new CartItem("iPhone 12", "$10.99", R.drawable.apple12, "this is details"));
        // Add more cart items as needed
        return cartItemList;
    }

    @Override
    public void onCartItemRemoved(double totalPrice) {
        if (myCartAdapter.isCartEmpty()) {
            // Cart is empty, hide the checkout button
            btnCheckout.setVisibility(View.GONE);
            // Hide the total price TextView
            tvTotalPrice.setVisibility(View.GONE);
            // Display the empty cart message in the center
            Toast toast = Toast.makeText(this, "Your cart is empty. Please explore our exciting products. Thank you for using our application", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP, 0, 16);
            toast.show();

        } else {
            // Cart is not empty, show the checkout button and total price TextView
            btnCheckout.setVisibility(View.VISIBLE);
            tvTotalPrice.setVisibility(View.VISIBLE);
            // Calculate the total price and update the total price TextView
            tvTotalPrice.setText("Total Price: $" + String.format("%.2f", totalPrice));
        }

    }

    private class MyCartNetworkTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            OkHttpClient client = new OkHttpClient();

            // Modify the URL
            String url = "http://10.0.0.64/yourprice/getmyCart.php";

            // Create a JSON object to include the user ID in the request body
            UserDetails userDetails = UserManager.getInstance().getUserDetails();
            if (userDetails != null) {
                int userId = userDetails.getId();

                JSONObject requestBody = new JSONObject();
                try {
                    requestBody.put("user_id", userId);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // Create a request with the JSON body
                Request request = new Request.Builder()
                        .url(url)
                        .post(RequestBody.create(MediaType.parse("application/json"), requestBody.toString()))
                        .build();


                try {
                    Response response = client.newCall(request).execute();

                    if (response.isSuccessful()) {
                        return response.body().string();
                    } else {
                        Log.e("Network Error", "Response not successful: " + response.code());
                    }
                } catch (IOException e) {
                    Log.e("Network Error", "IOException: " + e.getMessage());
                    e.printStackTrace();
                }
            } else {
            }
            return null;
        }


        @Override
        protected void onPostExecute(String jsonData) {
            if (jsonData != null) {
                try {
                    JSONArray jsonArray = new JSONArray(jsonData);
                    cartItemList.clear();  // Clear existing items from itemList

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String name = jsonObject.getString("name");
                        String price = jsonObject.getString("price");
                        String imageResourceName = jsonObject.getString("imageResource");
                        String details = jsonObject.getString("details");

                        int resourceId = getResources().getIdentifier(imageResourceName, "drawable", getPackageName());

                        CartItem item = new CartItem(name, price, resourceId, details);
                        cartItemList.add(item);
                    }

                    // Notify the adapter that data has changed
                    myCartAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.d("JSON Data", "JSON Data is null");
                Toast.makeText(MyCart.this, "Network Error", Toast.LENGTH_SHORT).show();
            }
        }
    }


}