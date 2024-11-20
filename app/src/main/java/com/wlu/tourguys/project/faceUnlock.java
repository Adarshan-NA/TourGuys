package com.wlu.tourguys.project;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
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
import com.google.mlkit.vision.face.FaceDetection;
import com.google.mlkit.vision.face.FaceDetector;
import com.google.mlkit.vision.face.FaceDetectorOptions;
import com.google.mlkit.vision.face.Face;

public class faceUnlock extends AppCompatActivity {

    private PreviewView previewView;
    private Button unlockButton;
    private FaceDetectorOptions highAccuracyOpts;

    // Placeholder for the user's stored image
    private Bitmap userImage;

    // Flag to prevent repeated "No face detected" toast messages
    private boolean isFaceDetectedToastShown = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_face_unlock);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.faceUnlock), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Load the user's image from resources or external storage
        userImage = BitmapFactory.decodeResource(getResources(), R.drawable.user_image); // replace with your image resource

        // Initialize views
        unlockButton = findViewById(R.id.unlockButton);
        previewView = findViewById(R.id.previewView);

        // Initially disable the unlock button
        unlockButton.setEnabled(false);

        // Request camera permission
        requestCameraPermission();

        // Initialize face detector options
        highAccuracyOpts = new FaceDetectorOptions.Builder()
                .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
                .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
                .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
                .build();

        // Initialize back button
        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(faceUnlock.this, signUp.class);
            startActivity(intent);
            finish(); // Optional: close this activity
        });

        unlockButton.setOnClickListener(v -> {
            // Check if the detected face matches before navigating to MainActivity
            if (unlockButton.isEnabled()) {
                Intent intent = new Intent(faceUnlock.this, Welcome.class);
                startActivity(intent);
                finish(); // Optional: close this activity
            } else {
                // Show a toast message if the face didn't match
//                Toast.makeText(faceUnlock.this, "Face didn't match", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private void startFaceUnlock() {
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
                        if (faces.size() > 0) {
                            // Reset the toast flag if a face is detected
                            isFaceDetectedToastShown = false;

                            // Check if the detected face matches the user's face
                            boolean faceMatched = false; // Track if a match is found
                            for (Face face : faces) {
                                // Here, you need to implement a method to compare the detected face with userImage
                                if (isFaceMatching(face, userImage)) {
                                    unlockButton.setEnabled(true); // Enable button if face matches
                                    Toast.makeText(faceUnlock.this, "Face matched! You can unlock.", Toast.LENGTH_SHORT).show();
                                    faceMatched = true;
                                    break; // Exit loop once a match is found
                                }
                            }
                            if (!faceMatched) {
                                // Show a toast message if the face didn't match
                                Toast.makeText(faceUnlock.this, "Face didn't match, please try again.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            unlockButton.setEnabled(false); // Disable button if no face detected
                            // Show the "No face detected" toast only if it hasn't been shown yet
                            if (!isFaceDetectedToastShown) {
                                Toast.makeText(faceUnlock.this, "No face detected, please position your face in front of the camera.", Toast.LENGTH_SHORT).show();
                                isFaceDetectedToastShown = true; // Set the flag to true to prevent future toasts
                            }
                        }
                        image.close();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(faceUnlock.this, "Face detection failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        image.close();
                    });
        });

        CameraSelector cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA;
        cameraProvider.bindToLifecycle(this, cameraSelector, imageAnalysis, preview);
    }

    private boolean isFaceMatching(Face detectedFace, Bitmap userImage) {
        // Implement face recognition logic to compare detectedFace with userImage
        // This can involve extracting features from the detected face and comparing them with userImage features
        // Return true if the faces match, false otherwise

        // For simplicity, this is a stub. You'll need to integrate a face recognition method.
        return false; // Change this to your matching logic
    }

    private void requestCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1001);
        } else {
            startFaceUnlock(); // Start camera if permission is already granted
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1001) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startFaceUnlock(); // Start camera if permission is granted
            } else {
                Toast.makeText(this, "Camera permission is required for face unlock", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
