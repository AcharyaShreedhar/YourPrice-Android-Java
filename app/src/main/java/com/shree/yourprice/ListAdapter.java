package com.shree.yourprice;

import static android.util.Log.d;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ItemViewHolder> {

    private List<Item> itemList;
    private Context context; // Add a context variable
    private Activity activity;


    public ListAdapter(List<Item> itemList, Context context, Activity activity) {
        this.itemList = itemList;
        this.context = context; // Initialize the context
        this.activity = activity;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_layout, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Item item = itemList.get(position);
        holder.imgItem.setImageResource(item.getImageResource());
        holder.tvName.setText(item.getName());
        holder.tvPrice.setText(item.getPrice());
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        ImageView imgItem;
        TextView tvName;
        TextView tvPrice;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            imgItem = itemView.findViewById(R.id.imgItem);
            tvName = itemView.findViewById(R.id.tvName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            // Find the ImageView for the three-dot menu
            ImageView ivThreeDot = itemView.findViewById(R.id.ivThreeDot);

            ivThreeDot.setOnClickListener(v -> {
                // Create a PopupMenu
                PopupMenu popupMenu = new PopupMenu(context, v);
                MenuInflater inflater = popupMenu.getMenuInflater();
                inflater.inflate(R.menu.product_menu, popupMenu.getMenu());

                // Set the click listener for menu items
                popupMenu.setOnMenuItemClickListener(item -> {
                    int itemId = item.getItemId();

                    if (itemId == R.id.menu_view_details) {
                        openViewDetails();
                        return true;
                    } else if (itemId == R.id.menu_add_to_wishlist) {
                        addToWishlist();
                        return true;
                    } else if (itemId == R.id.menu_add_to_cart) {
                        addToCart();
                        return true;
                    } else {
                        return false;
                    }
                });

                // Show the PopupMenu
                popupMenu.show();
            });


        }


        private void openViewDetails() {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                // Get the item from the itemList based on the position
                Item item = itemList.get(position);


                // Create an Intent to start the DetailsViewActivity
                Intent intent = new Intent(context, ItemDetailsActivity.class);

                // Pass the details of the clicked item to the DetailsViewActivity
                intent.putExtra("productName", item.getName());
                intent.putExtra("productPrice", item.getPrice());
                intent.putExtra("productImage", item.getImageResource());
                intent.putExtra("productDescription", item.getDetails());

                // Add other details of the product to the intent

                // Start the DetailsViewActivity
                context.startActivity(intent);
            }
        }

        private void addToWishlist() {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                // Get the item from the itemList based on the position
                Item item = itemList.get(position);

                // Here, you can send the user ID and phone ID to the server
                UserDetails userDetails = UserManager.getInstance().getUserDetails();
                int userId = userDetails.getId();
                int phoneId = item.getId(); // Replace with the method to get the phone ID from your Item class

                // Create a JSON object to include the user ID and phone ID in the request body
                JSONObject requestBody = new JSONObject();
                try {
                    requestBody.put("user_id", userId);
                    requestBody.put("phone_id", phoneId);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // Create a request with the JSON body
                OkHttpClient client = new OkHttpClient();
                String url = "http://10.0.0.64/yourprice/addToWishlist.php";
                Request request = new Request.Builder()
                        .url(url)
                        .post(RequestBody.create(MediaType.parse("application/json"), requestBody.toString()))
                        .build();

                // Execute the request asynchronously
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                        // Handle network failure
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()) {
                            String responseBody = response.body().string();
                            // Process the response, e.g., show a Toast or update UI
                            activity.runOnUiThread(() -> {
                                Toast.makeText(context, responseBody, Toast.LENGTH_SHORT).show();
                            });
                        } else {
                            // Handle unsuccessful response
                        }
                    }
                });
            }
        }

        private void addToCart() {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                // Get the item from the itemList based on the position
                Item item = itemList.get(position);

                // Here, you can send the user ID and phone ID to the server
                UserDetails userDetails = UserManager.getInstance().getUserDetails();
                int userId = userDetails.getId();
                int phoneId = item.getId(); // Replace with the method to get the phone ID from your Item class

                // Create a JSON object to include the user ID and phone ID in the request body
                JSONObject requestBody = new JSONObject();
                try {
                    requestBody.put("user_id", userId);
                    requestBody.put("phone_id", phoneId);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // Create a request with the JSON body
                OkHttpClient client = new OkHttpClient();
                String url = "http://10.0.0.64/yourprice/addToCart.php";
                Request request = new Request.Builder()
                        .url(url)
                        .post(RequestBody.create(MediaType.parse("application/json"), requestBody.toString()))
                        .build();

                // Execute the request asynchronously
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                        // Handle network failure
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()) {
                            String responseBody = response.body().string();
                            // Process the response, e.g., show a Toast or update UI
                            activity.runOnUiThread(() -> {
                                Toast.makeText(context, responseBody, Toast.LENGTH_SHORT).show();
                            });
                        } else {
                            // Handle unsuccessful response
                        }
                    }
                });
            }
        }

    }
}
