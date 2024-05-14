package com.shree.yourprice;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class WishlistAdapter extends RecyclerView.Adapter<WishlistAdapter.WishlistViewHolder> {

    private List<Item> wishlistItems;
    private Context context;

    public WishlistAdapter(List<Item> wishlistItems) {
        this.wishlistItems = wishlistItems;
    }

    @NonNull
    @Override
    public WishlistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.wishlist_item_layout, parent, false);
        return new WishlistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WishlistViewHolder holder, int position) {
        Item item = wishlistItems.get(position);

        // Bind wishlist item data to the views
        holder.tvName.setText(item.getName());
        holder.tvPrice.setText(item.getPrice());

        // Use Glide library to load the image into the ImageView
        Glide.with(context)
                .load(item.getImageResource())
                .placeholder(R.drawable.iphone14) // Placeholder image while loading
                .into(holder.imgItem);
    }

    @Override
    public int getItemCount() {
        return wishlistItems.size();
    }

    public static class WishlistViewHolder extends RecyclerView.ViewHolder {
        ImageView imgItem;
        TextView tvName;
        TextView tvPrice;

        public WishlistViewHolder(@NonNull View itemView) {
            super(itemView);
            imgItem = itemView.findViewById(R.id.imgItem);
            tvName = itemView.findViewById(R.id.tvName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
        }
    }
}
