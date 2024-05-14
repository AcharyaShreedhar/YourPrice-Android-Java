package com.shree.yourprice;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class MyCartAdapter extends RecyclerView.Adapter<MyCartAdapter.CartViewHolder> {

    private List<CartItem> cartItemList;
    private TextView tvTotalPrice, tvEmptyCartMessage;
    private Button btnCheckout;

    private OnCartItemRemovedListener onCartItemRemovedListener;
    private boolean isCartEmpty = false;

    public MyCartAdapter(List<CartItem> cartItemList, TextView tvTotalPrice, Button btnCheckout, OnCartItemRemovedListener onCartItemRemovedListener) {
        this.cartItemList = cartItemList;
        this.tvTotalPrice = tvTotalPrice;
        this.btnCheckout = btnCheckout;
        this.onCartItemRemovedListener = onCartItemRemovedListener;
        updateEmptyStatus();
    }

    public MyCartAdapter(List<CartItem> cartItemList, OnCartItemRemovedListener listener) {
        this.cartItemList = cartItemList;
        this.onCartItemRemovedListener = listener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        if (!cartItemList.isEmpty() && position >= 0 && position < cartItemList.size()) {
            updateTotalPrice();
            CartItem cartItem = cartItemList.get(position);

            holder.tvItemName.setText(cartItem.getProductName());
            holder.tvItemPrice.setText("Price: " + cartItem.getProductPrice());
//            holder.tvItemQuantity.setText("Quantity: " + cartItem.getQuantity());

            // Load the product image using Glide or any other image loading library
            Glide.with(holder.itemView.getContext())
                    .load(cartItem.getProductImage())
                    .into(holder.imgItem);

            holder.btnRemove.setOnClickListener(v -> {
                // Remove the item from the cart
                cartItemList.remove(position);
                notifyItemRemoved(position);
                updateTotalPrice();
            });
        }
    }

    private void updateTotalPrice() {
        double totalPrice = calculateTotalPrice(); // Calculate the total price

        // Notify the activity with the updated total price
        onCartItemRemovedListener.onCartItemRemoved(totalPrice);

        // Update the empty status after notifying the activity
        updateEmptyStatus();
    }

    public double calculateTotalPrice() {
        double totalPrice = 0;
        for (CartItem cartItem : cartItemList) {
            totalPrice += Double.parseDouble(cartItem.getProductPrice().substring(1));
        }
        return totalPrice;
    }

    private void updateEmptyStatus() {
        boolean isCartEmpty = cartItemList.isEmpty();
        if (isCartEmpty) {
            // Set the empty cart message
//            btnCheckout.setVisibility(View.GONE);
            tvTotalPrice.setVisibility(View.GONE);
        } else {
            tvTotalPrice.setText("Total Price: $" + calculateTotalPrice());
        }
    }

    public boolean isCartEmpty() {
        return cartItemList.isEmpty();
    }


    @Override
    public int getItemCount() {
        // Return 1 when the cart is empty to display the empty cart message
        return cartItemList.size();
    }


    public static class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView imgItem;
        TextView tvItemName, tvItemPrice, tvItemQuantity;
        ImageView btnRemove;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            imgItem = itemView.findViewById(R.id.imgItem);
            tvItemName = itemView.findViewById(R.id.tvItemName);
            tvItemPrice = itemView.findViewById(R.id.tvItemPrice);
            tvItemQuantity = itemView.findViewById(R.id.tvItemQuantity);
            btnRemove = itemView.findViewById(R.id.ivRemove);
        }
    }
}
