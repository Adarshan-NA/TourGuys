package com.wlu.tourguys.project;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.face.Face;
import com.google.mlkit.vision.face.FaceDetection;
import com.google.mlkit.vision.face.FaceDetector;
import com.google.mlkit.vision.face.FaceDetectorOptions;

public class verifyHuman extends AppCompatActivity {

    private PreviewView previewView;
    private FaceDetectorOptions highAccuracyOpts;

    // Flag to prevent repeated "No face detected" toast messages
    private boolean isFaceDetectedToastShown = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_verify_human);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.verifyHuman), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        previewView = findViewById(R.id.previewView);

        // Request camera permission
        requestCameraPermission();

        // Initialize face detector options
        highAccuracyOpts = new FaceDetectorOptions.Builder()
                .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
                .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
                .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
                .build();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private void startFaceDetection() {
        ProcessCameraProvider.getInstance(this).addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = ProcessCameraProvider.getInstance(this).get();
                startCamera(cameraProvider);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Error starting camera: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }, ContextCompat.getMainExecutor(this));
    }

    private void startCamera(ProcessCameraProvider cameraProvider) {
        Preview preview = new Preview.Builder().build();
        preview.setSurfaceProvider(previewView.getSurfaceProvider());

        ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build();

        FaceDetector detector = FaceDetection.getClient(highAccuracyOpts);

        imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(this), image -> {
            @SuppressLint("UnsafeOptInUsageError")
            InputImage inputImage = InputImage.fromMediaImage(image.getImage(), image.getImageInfo().getRotationDegrees());

            detector.process(inputImage)
                    .addOnSuccessListener(faces -> {
                        if (!faces.isEmpty()) {
                            // Reset the toast flag if a face is detected
                            isFaceDetectedToastShown = false;

                            // If a face is detected, navigate back to signUp activity
//                            Toast.makeText(verifyHuman.this, "Face detected!", Toast.LENGTH_SHORT).show();
                            navigateToSignUp();
                        } else {
                            // Show the "No face detected" toast only if it hasn't been shown yet
                            if (!isFaceDetectedToastShown) {
                                Toast.makeText(verifyHuman.this, "No face detected, please position your face in front of the camera.", Toast.LENGTH_SHORT).show();
                                isFaceDetectedToastShown = true; // Set the flag to true to prevent future toasts
                            }
                        }
                        image.close();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(verifyHuman.this, "Face detection failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        image.close();
                    });
        });

        CameraSelector cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA;
        cameraProvider.bindToLifecycle(this, cameraSelector, imageAnalysis, preview);
    }

    private void navigateToSignUp() {
        Intent intent = new Intent(this, signUp.class);
        startActivity(intent);
        finish();
    }

    private void requestCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1001);
        } else {
            startFaceDetection(); // Start camera if permission is already granted
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1001) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startFaceDetection(); // Start camera if permission is granted
            } else {
                Toast.makeText(this, "Camera permission is required for face detection", Toast.LENGTH_SHORT).show();
            }
        }
    }
}