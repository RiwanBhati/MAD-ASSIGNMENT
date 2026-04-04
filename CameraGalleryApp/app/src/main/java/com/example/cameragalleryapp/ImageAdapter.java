package com.example.cameragalleryapp;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.io.File;
import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

    private List<File> imageFiles;

    public ImageAdapter(List<File> imageFiles) {
        this.imageFiles = imageFiles;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        File file = imageFiles.get(position);
        holder.imageView.setImageURI(Uri.fromFile(file));

        // Requirement C: On clicking an image, open the Details page and pass the file path
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), ImageDetailsActivity.class);
            intent.putExtra("IMAGE_PATH", file.getAbsolutePath());
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return imageFiles.size();
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageViewGallery);
        }
    }
}