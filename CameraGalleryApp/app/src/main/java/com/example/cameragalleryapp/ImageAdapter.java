package com.example.cameragalleryapp;

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
    private OnImageLongClickListener longClickListener;

    // Interface to handle the delete action when an image is held down
    public interface OnImageLongClickListener {
        void onImageLongClick(File imageFile, int position);
    }

    public ImageAdapter(List<File> imageFiles, OnImageLongClickListener longClickListener) {
        this.imageFiles = imageFiles;
        this.longClickListener = longClickListener;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // This links to the item_image.xml file we just created
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        File file = imageFiles.get(position);

        // Load the image file into the ImageView
        holder.imageView.setImageURI(Uri.fromFile(file));

        // Listen for a long press to trigger deletion
        holder.itemView.setOnLongClickListener(v -> {
            longClickListener.onImageLongClick(file, position);
            return true;
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