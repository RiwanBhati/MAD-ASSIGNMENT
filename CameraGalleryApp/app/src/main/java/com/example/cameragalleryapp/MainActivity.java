package com.example.cameragalleryapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
    TextView tvCurrentFolder;
    RecyclerView recyclerViewGallery;

    ImageAdapter imageAdapter;
    List<File> imageList;

    // Default folder name when app starts
    String currentFolderName = "DefaultGallery";
    File currentPhotoFile;
    Uri currentPhotoUri;

    ActivityResultLauncher<String> requestCameraPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) dispatchTakePictureIntent();
                else Toast.makeText(this, "Camera permission required", Toast.LENGTH_SHORT).show();
            });

    ActivityResultLauncher<Uri> takePictureLauncher =
            registerForActivityResult(new ActivityResultContracts.TakePicture(), success -> {
                if (success) loadImagesFromFolder();
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnTakePhoto = findViewById(R.id.btnTakePhoto);
        btnChooseFolder = findViewById(R.id.btnChooseFolder);
        tvCurrentFolder = findViewById(R.id.tvCurrentFolder);
        recyclerViewGallery = findViewById(R.id.recyclerViewGallery);

        recyclerViewGallery.setLayoutManager(new GridLayoutManager(this, 3));
        imageList = new ArrayList<>();
        imageAdapter = new ImageAdapter(imageList);
        recyclerViewGallery.setAdapter(imageAdapter);

        updateFolderDisplay();

        btnTakePhoto.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent();
            } else {
                requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA);
            }
        });

        // Requirement B: Choose a folder
        btnChooseFolder.setOnClickListener(v -> showFolderChooserDialog());
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadImagesFromFolder(); // Refresh grid when returning from details screen
    }

    private void showFolderChooserDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose or Create Folder");
        builder.setMessage("Enter the name of the folder you want to view or save photos to:");

        final EditText input = new EditText(this);
        input.setText(currentFolderName);
        builder.setView(input);

        builder.setPositiveButton("Set Folder", (dialog, which) -> {
            String newFolderName = input.getText().toString().trim();
            if (!newFolderName.isEmpty()) {
                currentFolderName = newFolderName;
                updateFolderDisplay();
                loadImagesFromFolder();
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void updateFolderDisplay() {
        tvCurrentFolder.setText("Current Folder: " + currentFolderName);
    }

    // Requirement A: Save to chosen folder
    private void dispatchTakePictureIntent() {
        try {
            File storageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), currentFolderName);
            if (!storageDir.exists()) storageDir.mkdirs();

            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
            String imageFileName = "IMG_" + timeStamp + ".jpg";

            currentPhotoFile = new File(storageDir, imageFileName);

            currentPhotoUri = FileProvider.getUriForFile(this,
                    getApplicationContext().getPackageName() + ".provider",
                    currentPhotoFile);

            takePictureLauncher.launch(currentPhotoUri);
        } catch (Exception e) {
            Toast.makeText(this, "Error setting up camera", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadImagesFromFolder() {
        imageList.clear();
        File storageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), currentFolderName);

        if (storageDir.exists()) {
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
    }
}