package com.wlu.tourguys.project;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.face.FaceDetection;
import com.google.mlkit.vision.face.FaceDetector;
import com.google.mlkit.vision.face.FaceDetectorOptions;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class verifyHuman extends AppCompatActivity {

    private static final int CAMERA_PERMISSION_REQUEST_CODE = 1001;
    private static final String TAG = "verifyHuman";

    private PreviewView previewView;
    private FaceDetectorOptions highAccuracyOpts;
    private ProcessCameraProvider cameraProvider;
    private Camera camera;
    private ExecutorService cameraExecutor;

    private Button backButton;
    private boolean isFaceDetectedToastShown = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_human);

        previewView = findViewById(R.id.previewView);
        backButton = findViewById(R.id.backButton);

        cameraExecutor = Executors.newSingleThreadExecutor();

        backButton.setOnClickListener(v -> finish()); // Return to the previous activity

        highAccuracyOpts = new FaceDetectorOptions.Builder()
                .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
                .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
                .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
                .build();

        if (hasCameraPermission()) {
            startCamera();
        } else {
            requestCameraPermission();
        }
    }

    private boolean hasCameraPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
    }

    private void startCamera() {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        cameraProviderFuture.addListener(() -> {
            try {
                cameraProvider = cameraProviderFuture.get();
                bindCameraUseCases();
            } catch (Exception e) {
                Log.e(TAG, "Error starting camera: ", e);
                Toast.makeText(this, "Error starting camera: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }, ContextCompat.getMainExecutor(this));
    }

    private void bindCameraUseCases() {
        if (cameraProvider == null) return;

        cameraProvider.unbindAll();

        Preview preview = new Preview.Builder().build();
        preview.setSurfaceProvider(previewView.getSurfaceProvider());

        ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build();

        FaceDetector detector = FaceDetection.getClient(highAccuracyOpts);

        imageAnalysis.setAnalyzer(cameraExecutor, image -> {
            try {
                @SuppressLint("UnsafeOptInUsageError")
                InputImage inputImage = InputImage.fromMediaImage(image.getImage(), image.getImageInfo().getRotationDegrees());
                detector.process(inputImage)
                        .addOnSuccessListener(faces -> {
                            if (!faces.isEmpty()) {
                                setResult(RESULT_OK); // Indicate verification success
                                finish(); // Close the activity and return to sign-up
                            } else if (!isFaceDetectedToastShown) {
                                Toast.makeText(this, "No face detected, please position your face in front of the camera.", Toast.LENGTH_SHORT).show();
                                isFaceDetectedToastShown = true;
                            }
                            image.close();
                        })
                        .addOnFailureListener(e -> {
                            Log.e(TAG, "Face detection failed: ", e);
                            Toast.makeText(this, "Face detection failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            image.close();
                        });
            } catch (Exception e) {
                Log.e(TAG, "Image analysis error: ", e);
                image.close();
            }
        });

        CameraSelector cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA;

        try {
            camera = cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageAnalysis);
        } catch (Exception e) {
            Log.e(TAG, "Camera binding failed: ", e);
            Toast.makeText(this, "Error binding camera: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCamera();
            } else {
                Toast.makeText(this, "Camera permission is required for face detection.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (cameraProvider != null) {
            cameraProvider.unbindAll();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cameraProvider != null) {
            cameraProvider.unbindAll();
        }
        if (cameraExecutor != null) {
            cameraExecutor.shutdown();
        }
    }
}






//package com.wlu.tourguys.project;
//
//import android.Manifest;
//import android.annotation.SuppressLint;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.camera.core.CameraSelector;
//import androidx.camera.core.ImageAnalysis;
//import androidx.camera.core.Preview;
//import androidx.camera.lifecycle.ProcessCameraProvider;
//import androidx.camera.view.PreviewView;
//import androidx.core.app.ActivityCompat;
//import androidx.core.content.ContextCompat;
//
//import com.google.common.util.concurrent.ListenableFuture;
//import com.google.mlkit.vision.common.InputImage;
//import com.google.mlkit.vision.face.Face;
//import com.google.mlkit.vision.face.FaceDetection;
//import com.google.mlkit.vision.face.FaceDetector;
//import com.google.mlkit.vision.face.FaceDetectorOptions;
//
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//
//public class verifyHuman extends AppCompatActivity {
//
//    private static final int CAMERA_PERMISSION_REQUEST_CODE = 1001;
//    private static final String TAG = "verifyHuman";
//
//    private PreviewView previewView;
//    private FaceDetectorOptions highAccuracyOpts;
//    private ProcessCameraProvider cameraProvider;
//    private ExecutorService cameraExecutor;
//
//    private Button backButton;
//
//    private boolean isFaceDetectedToastShown = false;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_verify_human);
//
//        previewView = findViewById(R.id.previewView);
//        backButton = findViewById(R.id.backButton);
//
//        cameraExecutor = Executors.newSingleThreadExecutor();
//
//        backButton.setOnClickListener(v -> {
//            Intent intent = new Intent(verifyHuman.this, signUp.class);
//            startActivity(intent);
//            finish();
//        });
//
//        highAccuracyOpts = new FaceDetectorOptions.Builder()
//                .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
//                .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
//                .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
//                .build();
//
//        if (hasCameraPermission()) {
//            startCamera();
//        } else {
//            requestCameraPermission();
//        }
//    }
//
//    private boolean hasCameraPermission() {
//        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
//    }
//
//    private void requestCameraPermission() {
//        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
//    }
//
//    private void startCamera() {
//        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(this);
//        cameraProviderFuture.addListener(() -> {
//            try {
//                cameraProvider = cameraProviderFuture.get();
//                bindCameraUseCases();
//            } catch (Exception e) {
//                Log.e(TAG, "Error starting camera: ", e);
//                Toast.makeText(this, "Error starting camera: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        }, ContextCompat.getMainExecutor(this));
//    }
//
//    private void bindCameraUseCases() {
//        if (cameraProvider == null) return;
//
//        cameraProvider.unbindAll();
//
//        Preview preview = new Preview.Builder().build();
//        preview.setSurfaceProvider(previewView.getSurfaceProvider());
//
//        ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
//                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
//                .build();
//
//        FaceDetector detector = FaceDetection.getClient(highAccuracyOpts);
//
//        imageAnalysis.setAnalyzer(cameraExecutor, image -> {
//            @SuppressLint("UnsafeOptInUsageError")
//            InputImage inputImage = InputImage.fromMediaImage(image.getImage(), image.getImageInfo().getRotationDegrees());
//            detector.process(inputImage)
//                    .addOnSuccessListener(faces -> {
//                        if (!faces.isEmpty()) {
//                            isFaceDetectedToastShown = false;
//                            navigateToSignUp();
//                        } else if (!isFaceDetectedToastShown) {
//                            Toast.makeText(this, "No face detected, please position your face in front of the camera.", Toast.LENGTH_SHORT).show();
//                            isFaceDetectedToastShown = true;
//                        }
//                        image.close();
//                    })
//                    .addOnFailureListener(e -> {
//                        Log.e(TAG, "Face detection failed: ", e);
//                        Toast.makeText(this, "Face detection failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//                        image.close();
//                    });
//        });
//
//        CameraSelector cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA;
//
//        try {
//            cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageAnalysis);
//        } catch (Exception e) {
//            Log.e(TAG, "Camera binding failed: ", e);
//            Toast.makeText(this, "Error binding camera: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    private void navigateToSignUp() {
//        Intent intent = new Intent(this, signUp.class);
//        startActivity(intent);
//        finish();
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                startCamera();
//            } else {
//                Toast.makeText(this, "Camera permission is required for face detection.", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        if (cameraProvider != null) {
//            cameraProvider.unbindAll();
//        }
//        if (cameraExecutor != null) {
//            cameraExecutor.shutdown();
//        }
//    }
//}
//
//
//
//
////package com.wlu.tourguys.project;
////
////import android.Manifest;
////import android.annotation.SuppressLint;
////import android.content.Intent;
////import android.content.pm.PackageManager;
////import android.os.Bundle;
////import android.view.View;
////import android.widget.Button;
////import android.widget.Toast;
////
////import androidx.activity.EdgeToEdge;
////import androidx.annotation.NonNull;
////import androidx.appcompat.app.AppCompatActivity;
////import androidx.camera.core.CameraSelector;
////import androidx.camera.core.ImageAnalysis;
////import androidx.camera.core.Preview;
////import androidx.camera.lifecycle.ProcessCameraProvider;
////import androidx.camera.view.PreviewView;
////import androidx.core.app.ActivityCompat;
////import androidx.core.content.ContextCompat;
////import androidx.core.graphics.Insets;
////import androidx.core.view.ViewCompat;
////import androidx.core.view.WindowInsetsCompat;
////
////import com.google.mlkit.vision.common.InputImage;
////import com.google.mlkit.vision.face.Face;
////import com.google.mlkit.vision.face.FaceDetection;
////import com.google.mlkit.vision.face.FaceDetector;
////import com.google.mlkit.vision.face.FaceDetectorOptions;
////
////public class verifyHuman extends AppCompatActivity {
////    Button backButtton;
////
////    private PreviewView previewView;
////    private FaceDetectorOptions highAccuracyOpts;
////
////    // Flag to prevent repeated "No face detected" toast messages
////    private boolean isFaceDetectedToastShown = false;
////
////    @Override
////    protected void onCreate(Bundle savedInstanceState) {
////        super.onCreate(savedInstanceState);
////        EdgeToEdge.enable(this);
////        setContentView(R.layout.activity_verify_human);
////
////        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.verifyHuman), (v, insets) -> {
////            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
////            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
////            return insets;
////        });
////
////        previewView = findViewById(R.id.previewView);
////
////        // Request camera permission
////        requestCameraPermission();
////
////        // Initialize face detector options
////        highAccuracyOpts = new FaceDetectorOptions.Builder()
////                .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
////                .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
////                .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
////                .build();
////
////
////        backButtton = findViewById(R.id.backButton);
////        backButtton.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                // Navigate to FaceUnlockActivity
////                Intent intent = new Intent(verifyHuman.this, signUp.class);
////                startActivity(intent);
////                // Apply the slide-in and slide-out animations
////                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
////            }
////        });
////    }
////
////    @Override
////    public void finish() {
////        super.finish();
//////        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
////    }
////
////    private void startFaceDetection() {
////        ProcessCameraProvider.getInstance(this).addListener(() -> {
////            try {
////                ProcessCameraProvider cameraProvider = ProcessCameraProvider.getInstance(this).get();
////                startCamera(cameraProvider);
////            } catch (Exception e) {
////                e.printStackTrace();
////                Toast.makeText(this, "Error starting camera: " + e.getMessage(), Toast.LENGTH_SHORT).show();
////            }
////        }, ContextCompat.getMainExecutor(this));
////    }
////
////    private void startCamera(ProcessCameraProvider cameraProvider) {
////        Preview preview = new Preview.Builder().build();
////        preview.setSurfaceProvider(previewView.getSurfaceProvider());
////
////        ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
////                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
////                .build();
////
////        FaceDetector detector = FaceDetection.getClient(highAccuracyOpts);
////
////        imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(this), image -> {
////            @SuppressLint("UnsafeOptInUsageError")
////            InputImage inputImage = InputImage.fromMediaImage(image.getImage(), image.getImageInfo().getRotationDegrees());
////
////            detector.process(inputImage)
////                    .addOnSuccessListener(faces -> {
////                        if (!faces.isEmpty()) {
////                            // Reset the toast flag if a face is detected
////                            isFaceDetectedToastShown = false;
////
////                            // If a face is detected, navigate back to signUp activity
//////                            Toast.makeText(verifyHuman.this, "Face detected!", Toast.LENGTH_SHORT).show();
////                            navigateToSignUp();
////                        } else {
////                            // Show the "No face detected" toast only if it hasn't been shown yet
////                            if (!isFaceDetectedToastShown) {
////                                Toast.makeText(verifyHuman.this, "No face detected, please position your face in front of the camera.", Toast.LENGTH_SHORT).show();
////                                isFaceDetectedToastShown = true; // Set the flag to true to prevent future toasts
////                            }
////                        }
////                        image.close();
////                    })
////                    .addOnFailureListener(e -> {
////                        Toast.makeText(verifyHuman.this, "Face detection failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
////                        image.close();
////                    });
////        });
////
////        CameraSelector cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA;
////        cameraProvider.bindToLifecycle(this, cameraSelector, imageAnalysis, preview);
////    }
////
////    private void navigateToSignUp() {
////        Intent intent = new Intent(this, signUp.class);
////        startActivity(intent);
////        finish();
////    }
////
////    private void requestCameraPermission() {
////        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
////                != PackageManager.PERMISSION_GRANTED) {
////            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1001);
////        } else {
////            startFaceDetection(); // Start camera if permission is already granted
////        }
////    }
////
////    @Override
////    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
////        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
////        if (requestCode == 1001) {
////            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
////                startFaceDetection(); // Start camera if permission is granted
////            } else {
////                Toast.makeText(this, "Camera permission is required for face detection", Toast.LENGTH_SHORT).show();
////            }
////        }
////    }
////}