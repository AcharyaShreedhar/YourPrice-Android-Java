package com.shree.yourprice;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class WishlistActivity extends AppCompatActivity {

    private RecyclerView rvWishlist;
    private List<Item> wishlistItems;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    WishlistAdapter wishlistAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist);

        // Find the Toolbar and set it as the support action bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set the title for the action bar (optional)
        getSupportActionBar().setTitle("Wishlist");

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
                startActivity(new Intent(this, MyCart.class));
            } else if (itemId == R.id.mywishlist) {
                // Start the WishlistActivity
//                startActivity(new Intent(this, WishlistActivity.class));
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

        rvWishlist = findViewById(R.id.rvWishlist);
        rvWishlist.setLayoutManager(new LinearLayoutManager(this));
        WishlistNetworkTask wishlistNetworkTask = new WishlistNetworkTask();
        wishlistNetworkTask.execute();

        // Dummy data for wishlist items (you should replace this with your actual data)
        wishlistItems = getWishlistItems();

        // Create the adapter and set it to the RecyclerView
        wishlistAdapter = new WishlistAdapter(wishlistItems);
        rvWishlist.setAdapter(wishlistAdapter);
    }

    // Replace this with your actual data retrieval logic (e.g., fetching from a database)
    private List<Item> getWishlistItems() {
        List<Item> items = new ArrayList<>();

        // Add sample wishlist items

        items.add(new Item(1, "iPhone 18", "$14.99", R.drawable.apple7,
                "Welcome to the world of the iPhone 18, a device designed to elevate your mobile experience. With the A20 Bionic chip and Neural Engine, this smartphone delivers blazing-fast performance and enhanced AI capabilities. The redesigned camera system with Cinematic mode allows you to shoot professional-quality videos. The ProMotion XDR display with True Tone offers incredible color accuracy. Embrace innovation with the iPhone 18."));
        return items;
    }

    private class WishlistNetworkTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            OkHttpClient client = new OkHttpClient();

            // Modify the URL
            String url = "http://10.0.0.64/yourprice/getWishlist.php";

            // Create a JSON object to include the user ID in the request body
            UserDetails userDetails = UserManager.getInstance().getUserDetails();
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
            return null;
        }


        @Override
        protected void onPostExecute(String jsonData) {
            if (jsonData != null) {

                Toast.makeText(WishlistActivity.this, "Jsondata" + jsonData, Toast.LENGTH_SHORT).show();
                try {
                    JSONArray jsonArray = new JSONArray(jsonData);
                    wishlistItems.clear();  // Clear existing items from itemList

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String name = jsonObject.getString("name");
                        String price = jsonObject.getString("price");
                        String imageResourceName = jsonObject.getString("imageResource");
                        String details = jsonObject.getString("details");

                        int resourceId = getResources().getIdentifier(imageResourceName, "drawable", getPackageName());

                        Item item = new Item(name, price, resourceId, details);
                        wishlistItems.add(item);
                    }

                    // Notify the adapter that data has changed
                    wishlistAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.d("JSON Data", "JSON Data is null");
                Toast.makeText(WishlistActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
