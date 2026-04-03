package com.example.sensordataapp;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

// We add 'implements SensorEventListener' so this activity can listen to hardware changes
public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private TextView tvAccelerometer, tvLight, tvProximity;
    private SensorManager sensorManager;
    private Sensor accelerometer, lightSensor, proximitySensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 1. Link to XML UI
        tvAccelerometer = findViewById(R.id.tvAccelerometer);
        tvLight = findViewById(R.id.tvLight);
        tvProximity = findViewById(R.id.tvProximity);

        // 2. Initialize the Sensor Manager
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        if (sensorManager != null) {
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
            proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        }

        // 3. Warn the user if a sensor is missing
        if (accelerometer == null) tvAccelerometer.setText("Accelerometer not available");
        if (lightSensor == null) tvLight.setText("Light sensor not available");
        if (proximitySensor == null) tvProximity.setText("Proximity sensor not available");
    }

    // Register listeners when the app is open and visible
    @Override
    protected void onResume() {
        super.onResume();
        if (accelerometer != null) {
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        }
        if (lightSensor != null) {
            sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
        if (proximitySensor != null) {
            sensorManager.registerListener(this, proximitySensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    // Unregister listeners when the app goes to the background to save battery
    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    // This method fires every time the hardware detects a change
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            tvAccelerometer.setText(String.format("X: %.2f\nY: %.2f\nZ: %.2f", x, y, z));

        } else if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
            tvLight.setText(event.values[0] + " lx");

        } else if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
            tvProximity.setText(event.values[0] + " cm");
        }
    }

    // We must include this method when implementing SensorEventListener, even if empty
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}