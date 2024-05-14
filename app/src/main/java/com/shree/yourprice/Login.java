package com.shree.yourprice;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import com.google.android.material.snackbar.Snackbar;

public class Login extends AppCompatActivity {

    private EditText etUsername, etPassword;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(view -> {
            String username = etUsername.getText().toString();
            String password = etPassword.getText().toString();

            // Call the API for user authentication
            authenticateUser(username, password);
        });
    }

    private void authenticateUser(String username, String password) {
        NetworkTask networkTask = new NetworkTask();
        networkTask.execute(username, password);
    }

    private class NetworkTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String username = params[0];
            String password = params[1];

            OkHttpClient client = new OkHttpClient();
            String apiEndpoint = "http://10.0.0.64/yourprice/getUser.php?username=" + username;

            Request request = new Request.Builder()
                    .url(apiEndpoint)
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
                // Parse JSON response and authenticate user
                try {

                    JSONObject userObject = new JSONObject(jsonData);

                    String storedPassword = userObject.optString("password"); // Replace "password" with your actual password field name
                    String enteredPassword = etPassword.getText().toString();

                    if (enteredPassword.equals(storedPassword)) {
                        UserDetails userDetails = parseUserDetails(jsonData);
                        UserManager.getInstance().setLoggedIn(true);
                        UserManager.getInstance().setUserDetails(userDetails);
                        String message = "This is a Snackbar message";
                        runOnUiThread(() -> showSnackbar(message));
                        // Password matches, login successful
                        Toast.makeText(Login.this, "Login successful", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Login.this, MainActivity.class));
                        finish(); // Close the login screen
                    } else {
                        // Password doesn't match, show error message
                        Toast.makeText(Login.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(Login.this, "Network Error", Toast.LENGTH_SHORT).show();
            }
        }


        private UserDetails parseUserDetails(String jsonData) {
            try {
                JSONObject userObject = new JSONObject(jsonData);
                int id = userObject.getInt("id");
                String name = userObject.getString("name");
                String username = userObject.getString("username");
                String contact = userObject.getString("contact");
                String address = userObject.getString("address");

                return new UserDetails(id, name, username, contact, address);
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }

        private void showSnackbar(String message) {
            View rootView = findViewById(android.R.id.content);
            Snackbar snackbar = Snackbar.make(rootView, message, Snackbar.LENGTH_LONG);
            snackbar.show();
        }
    }


}
