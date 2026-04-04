package com.example.cameragalleryapp;

import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ImageDetailsActivity extends AppCompatActivity {

    ImageView detailImageView;
    TextView tvImageName, tvImagePath, tvImageSize, tvImageDate;
    Button btnDeleteImage;
    File imageFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_details);

        detailImageView = findViewById(R.id.detailImageView);
        tvImageName = findViewById(R.id.tvImageName);
        tvImagePath = findViewById(R.id.tvImagePath);
        tvImageSize = findViewById(R.id.tvImageSize);
        tvImageDate = findViewById(R.id.tvImageDate);
        btnDeleteImage = findViewById(R.id.btnDeleteImage);

        // Receive the path from the Adapter
        String path = getIntent().getStringExtra("IMAGE_PATH");
        if (path != null) {
            imageFile = new File(path);
            displayImageDetails();
        }

        // Requirement C(ii): Delete button with confirmation
        btnDeleteImage.setOnClickListener(v -> showDeleteConfirmation());
    }

    private void displayImageDetails() {
        detailImageView.setImageURI(Uri.fromFile(imageFile));

        // Requirement C(i): Set text details
        tvImageName.setText("Name: " + imageFile.getName());
        tvImagePath.setText("Path: " + imageFile.getAbsolutePath());

        long fileSizeInKB = imageFile.length() / 1024;
        tvImageSize.setText("Size: " + fileSizeInKB + " KB");

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.getDefault());
        String dateString = sdf.format(new Date(imageFile.lastModified()));
        tvImageDate.setText("Date Taken: " + dateString);
    }

    private void showDeleteConfirmation() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.confirm_delete_title)
                .setMessage(R.string.confirm_delete_message)
                .setPositiveButton(R.string.yes, (dialog, which) -> {
                    if (imageFile.exists() && imageFile.delete()) {
                        Toast.makeText(this, "Image Deleted", Toast.LENGTH_SHORT).show();
                        finish(); // This closes the detail screen and returns to the Gallery
                    } else {
                        Toast.makeText(this, "Failed to delete", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(R.string.no, null)
                .show();
    }
}