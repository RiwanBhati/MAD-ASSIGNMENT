package com.example.cameragalleryapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    Button btnTakePhoto, btnChooseFolder;
    RecyclerView recyclerViewGallery;

    ImageAdapter imageAdapter;
    List<File> imageList;
    File currentPhotoFile;
    Uri currentPhotoUri;

    // 1. Ask for Camera Permission safely
    ActivityResultLauncher<String> requestCameraPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    dispatchTakePictureIntent();
                } else {
                    Toast.makeText(this, "Camera permission is required to take photos", Toast.LENGTH_SHORT).show();
                }
            });

    // 2. Launch Camera and wait for the result
    ActivityResultLauncher<Uri> takePictureLauncher =
            registerForActivityResult(new ActivityResultContracts.TakePicture(), success -> {
                if (success) {
                    loadImagesFromFolder(); // Refresh the grid to show the new photo
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnTakePhoto = findViewById(R.id.btnTakePhoto);
        btnChooseFolder = findViewById(R.id.btnChooseFolder);
        recyclerViewGallery = findViewById(R.id.recyclerViewGallery);

        // Set up the Grid to show 3 pictures per row
        recyclerViewGallery.setLayoutManager(new GridLayoutManager(this, 3));
        imageList = new ArrayList<>();

        // Set up our Adapter and define what happens on a Long Click (Delete)
        imageAdapter = new ImageAdapter(imageList, (imageFile, position) -> {
            showDeleteDialog(imageFile, position);
        });
        recyclerViewGallery.setAdapter(imageAdapter);

        // Take Photo Button Logic
        btnTakePhoto.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent();
            } else {
                requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA);
            }
        });

        // Folder Button Logic (Loads all saved photos from this app's folder)
        btnChooseFolder.setOnClickListener(v -> loadImagesFromFolder());

        // Automatically load existing images when the app opens
        loadImagesFromFolder();
    }

    // Creates a secure file and opens the camera
    private void dispatchTakePictureIntent() {
        try {
            // Create a unique file name using the current date and time
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
            String imageFileName = "JPEG_" + timeStamp + "_";
            File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

            currentPhotoFile = File.createTempFile(imageFileName, ".jpg", storageDir);

            // Get a secure URI using the FileProvider we defined in the Manifest
            currentPhotoUri = FileProvider.getUriForFile(this,
                    getApplicationContext().getPackageName() + ".provider",
                    currentPhotoFile);

            takePictureLauncher.launch(currentPhotoUri);

        } catch (Exception e) {
            Toast.makeText(this, "Error setting up the camera", Toast.LENGTH_SHORT).show();
        }
    }

    // Scans our folder and pushes the images into the grid
    private void loadImagesFromFolder() {
        imageList.clear();
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        if (storageDir != null && storageDir.exists()) {
            File[] files = storageDir.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.getName().endsWith(".jpg")) {
                        imageList.add(file);
                    }
                }
            }
        }

        imageAdapter.notifyDataSetChanged();
        if (imageList.isEmpty()) {
            Toast.makeText(this, "No photos in gallery yet!", Toast.LENGTH_SHORT).show();
        }
    }

    // Shows a confirmation pop-up before deleting
    private void showDeleteDialog(File imageFile, int position) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.confirm_delete_title)
                .setMessage(R.string.confirm_delete_message)
                .setPositiveButton(R.string.yes, (dialog, which) -> {
                    if (imageFile.delete()) {
                        imageList.remove(position);
                        imageAdapter.notifyItemRemoved(position);
                        Toast.makeText(this, "Image Deleted", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(R.string.no, null)
                .show();
    }
}