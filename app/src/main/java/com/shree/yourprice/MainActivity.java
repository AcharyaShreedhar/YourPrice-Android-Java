package com.shree.yourprice;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ListAdapter listAdapter;
    private List<Item> itemList;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private UserManager userManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userManager = UserManager.getInstance();
        NetworkTask networkTask = new NetworkTask();
        networkTask.execute();

        // Find the Toolbar and set it as the support action bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set the title for the action bar (optional)
        getSupportActionBar().setTitle("Latest iPhones");

        drawerLayout = findViewById(R.id.drawerLayout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);

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
            int itemId = item.getItemId();

            if (itemId == R.id.home) {
                Toast.makeText(this, "This clicked page will open " + item + "Page", Toast.LENGTH_LONG).show();
            } else if (itemId == R.id.products) {
//                startActivity(new Intent(this, MainActivity.class));
            } else if (itemId == R.id.stores) {
                startActivity(new Intent(this, Stores.class));
            } else if (itemId == R.id.mycart) {
                startActivity(new Intent(this, MyCart.class));
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

        recyclerView = findViewById(R.id.rvList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        itemList = createItemList(); // Replace this with your actual data

        listAdapter = new ListAdapter(itemList, this, this);
        recyclerView.setAdapter(listAdapter);

    }

    private class NetworkTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            OkHttpClient client = new OkHttpClient();
            String url = "http://10.0.0.64/yourprice/getPhones.php";

            Request request = new Request.Builder()
                    .url(url)
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
                try {
                    JSONArray jsonArray = new JSONArray(jsonData);
                    itemList.clear();  // Clear existing items from itemList

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        int id = Integer.parseInt(jsonObject.getString("id"));
                        String name = jsonObject.getString("name");
                        String price = jsonObject.getString("price");
                        String imageResourceName = jsonObject.getString("imageResource");
                        String details = jsonObject.getString("details");

                        int resourceId = getResources().getIdentifier(imageResourceName, "drawable", getPackageName());

                        Item item = new Item(id, name, price, resourceId, details);
                        itemList.add(item);
                    }

                    // Notify the adapter that data has changed
                    listAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.d("JSON Data", "JSON Data is null");
                Toast.makeText(MainActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
            }
        }
    }


    // Method to create a sample list of items (Replace this with your actual data source)
    private List<Item> createItemList() {
        List<Item> itemList = new ArrayList<>();

        itemList.add(new Item(1, "iPhone 12", "$10.99", R.drawable.apple1,
                "The iPhone 12 is a flagship smartphone from Apple that offers a perfect blend of sleek design and powerful performance. With its A14 Bionic chip and advanced camera system, you can capture stunning photos and record high-quality videos. The Super Retina XDR display brings your content to life, and the 5G capability ensures seamless connectivity. Experience the best of Apple's technology with the iPhone 12."));

        return itemList;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        UserDetails userDetails = UserManager.getInstance().getUserDetails();

        // Handle hamburger menu icon click events
        if (itemId == R.id.action_login) {
            if (userDetails == null) {
                startActivity(new Intent(this, Login.class));
            } else {
                // Handle logout logic here
                UserManager.getInstance().setUserDetails(null);
                startActivity(new Intent(this, Login.class));
            }
            return true;
        } else if (itemId == R.id.action_cart) {
            startActivity(new Intent(this, MyCart.class));
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }

    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the state of the ActionBarDrawerToggle after the activity has been created
        actionBarDrawerToggle.syncState();
    }
}
