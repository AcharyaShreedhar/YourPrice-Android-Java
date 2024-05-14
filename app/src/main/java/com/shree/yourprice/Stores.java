package com.shree.yourprice;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Stores extends AppCompatActivity {

    private RecyclerView recyclerView;
    private StoreAdapter storeAdapter;
    private List<Store> storeList;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stores);
        // Find the Toolbar and set it as the support action bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set the title for the action bar (optional)
        getSupportActionBar().setTitle("Recommended Stores");

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
                startActivity(new Intent(this, MainActivity.class));
            } else if (itemId == R.id.stores) {
//                startActivity(new Intent(this, Stores.class));
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

        recyclerView = findViewById(R.id.rvStores);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        storeList = new ArrayList<>();
        storeAdapter = new StoreAdapter(storeList);
        recyclerView.setAdapter(storeAdapter);

        NetworkTask networkTask = new NetworkTask();
        networkTask.execute();
    }

    private class NetworkTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            OkHttpClient client = new OkHttpClient();
            String url = "http://10.0.0.64/yourprice/getStores.php";  // Replace with your PHP API URL

            Request request = new Request.Builder()
                    .url(url)
                    .build();

            try {
                Response response = client.newCall(request).execute();
                return response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String jsonData) {
            if (jsonData != null) {
                updateStoreData(jsonData);
            } else {
                Snackbar.make(recyclerView, "Network error", Snackbar.LENGTH_LONG).show();
            }
        }
    }

    private void updateStoreData(String jsonData) {
        try {
            JSONArray jsonArray = new JSONArray(jsonData);
            List<Store> newStoreList = new ArrayList<>();
            double brantfordLat = 43.1448; // Brantford's latitude
            double brantfordLon = -80.2644; // Brantford's longitude

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String name = jsonObject.getString("name");
                String address = jsonObject.getString("address");
                float rating = (float) jsonObject.getDouble("rating");
                double storeLat = jsonObject.getDouble("latitude"); // Store's latitude
                double storeLon = jsonObject.getDouble("longitude");


                newStoreList.add(new Store(name, rating, address, storeLat, storeLon));
            }


            storeList.clear();
            storeList.addAll(newStoreList);
            sortByRating();
            storeAdapter.notifyDataSetChanged();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_stores, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.action_sort_rating) {
            sortByRating();
            return true;
        } else if (itemId == R.id.action_sort_location) {
            sortByLocation();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void sortByRating() {
        // Sort the storeList by rating in ascending order
        Collections.sort(storeList, (store1, store2) -> Float.compare((float) store2.getRating(), (float) store1.getRating()));

        storeAdapter.notifyDataSetChanged();
    }

    private void sortByLocation() {

        // Request user's location using Location Services
//        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
//        Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//
//        Toast.makeText(this, "lastnu-----------"+lastKnownLocation, Toast.LENGTH_LONG).show();
//        if (lastKnownLocation != null) {
//            double userLat = lastKnownLocation.getLatitude();
//            double userLon = lastKnownLocation.getLongitude();
        double userLat = 43.1488;
        double userLon = -80.2644;

        // Sorting logic based on user's location
        Collections.sort(storeList, (store1, store2) -> {
            double distance1 = HaversineDistanceCalculator.calculateDistance(userLat, userLon, store1.getLatitude(), store1.getLongitude());
            double distance2 = HaversineDistanceCalculator.calculateDistance(userLat, userLon, store2.getLatitude(), store2.getLongitude());
            return Double.compare(distance1, distance2);
        });

        storeAdapter.notifyDataSetChanged();

    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the state of the ActionBarDrawerToggle after the activity has been created
        actionBarDrawerToggle.syncState();
    }
}
