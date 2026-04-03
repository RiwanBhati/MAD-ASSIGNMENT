package com.example.play;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
public class MainActivity extends AppCompatActivity {

    Button btnOpenAudio, btnAudioPlay, btnAudioPause, btnAudioStop, btnAudioRestart;
    TextView tvAudioStatus;
    MediaPlayer mediaPlayer;
    Uri audioUri;

    EditText etVideoUrl;
    Button btnLoadVideoUrl, btnVideoPlay, btnVideoPause, btnVideoStop, btnVideoRestart;
    VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        btnOpenAudio = findViewById(R.id.btnOpenAudio);
        btnAudioPlay = findViewById(R.id.btnAudioPlay);
        btnAudioPause = findViewById(R.id.btnAudioPause);
        btnAudioStop = findViewById(R.id.btnAudioStop);
        btnAudioRestart = findViewById(R.id.btnAudioRestart);
        tvAudioStatus = findViewById(R.id.tvAudioStatus);


        etVideoUrl = findViewById(R.id.etVideoUrl);
        btnLoadVideoUrl = findViewById(R.id.btnLoadVideoUrl);
        btnVideoPlay = findViewById(R.id.btnVideoPlay);
        btnVideoPause = findViewById(R.id.btnVideoPause);
        btnVideoStop = findViewById(R.id.btnVideoStop);
        btnVideoRestart = findViewById(R.id.btnVideoRestart);
        videoView = findViewById(R.id.videoView);


        ActivityResultLauncher<Intent> audioPickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        audioUri = result.getData().getData();
                        tvAudioStatus.setText("Audio File Loaded!");
                        setupMediaPlayer();
                    }
                }
        );

        btnOpenAudio.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("audio/*"); // Only show audio files
            audioPickerLauncher.launch(intent);
        });

        btnAudioPlay.setOnClickListener(v -> {
            if (mediaPlayer != null && !mediaPlayer.isPlaying()) mediaPlayer.start();
        });

        btnAudioPause.setOnClickListener(v -> {
            if (mediaPlayer != null && mediaPlayer.isPlaying()) mediaPlayer.pause();
        });

        btnAudioStop.setOnClickListener(v -> {
            if (mediaPlayer != null) {
                mediaPlayer.pause();
                mediaPlayer.seekTo(0); // Safest way to "stop" without crashing the media state
            }
        });

        btnAudioRestart.setOnClickListener(v -> {
            if (mediaPlayer != null) {
                mediaPlayer.seekTo(0);
                mediaPlayer.start();
            }
        });

        // ================= VIDEO LOGIC =================

        btnLoadVideoUrl.setOnClickListener(v -> {
            String url = etVideoUrl.getText().toString();
            if (url.isEmpty()) {
                Toast.makeText(this, "Please enter a URL", Toast.LENGTH_SHORT).show();
                return;
            }
            Uri videoUri = Uri.parse(url);
            videoView.setVideoURI(videoUri);
            Toast.makeText(this, "Video Loading... Press Play", Toast.LENGTH_SHORT).show();
        });

        btnVideoPlay.setOnClickListener(v -> videoView.start());

        btnVideoPause.setOnClickListener(v -> {
            if (videoView.isPlaying()) videoView.pause();
        });

        btnVideoStop.setOnClickListener(v -> {
            videoView.pause();
            videoView.seekTo(0);
        });

        btnVideoRestart.setOnClickListener(v -> {
            videoView.seekTo(0);
            videoView.start();
        });
    }

    // Helper method to prepare the audio file once selected
    private void setupMediaPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.release(); // Clear any old audio
        }
        mediaPlayer = MediaPlayer.create(this, audioUri);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Clean up resources when the app is closed
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}