package com.wlu.tourguys.project;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;

public class ProfileSettingsFragment extends Fragment {

    private ImageView profileImage, cameraIcon;
    private EditText nameField, emailField, passwordField, mobileField, cityField, countryField;
    private Button saveButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the fragment layout
        return inflater.inflate(R.layout.fragment_profile_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize views
        profileImage = view.findViewById(R.id.profile_image);
        cameraIcon = view.findViewById(R.id.camera_icon);
        nameField = view.findViewById(R.id.name_field);
        emailField = view.findViewById(R.id.email_field);
        passwordField = view.findViewById(R.id.password_field);
        mobileField = view.findViewById(R.id.mobile_field);
        cityField = view.findViewById(R.id.city_field);
        countryField = view.findViewById(R.id.country_field);
        saveButton = view.findViewById(R.id.save_button);

        // Set default data (optional)
        nameField.setText("Dan");
        emailField.setText("dan@example.com");
        cityField.setText("Delhi");
        countryField.setText("India");

        // Save button click listener
        saveButton.setOnClickListener(v -> {
            String name = nameField.getText().toString();
            String email = emailField.getText().toString();
            String password = passwordField.getText().toString();
            String mobile = mobileField.getText().toString();
            String city = cityField.getText().toString();
            String country = countryField.getText().toString();

            // Validate the fields
            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Snackbar.make(v, "Please fill out all required fields", Snackbar.LENGTH_LONG).show();
                return;
            }

            // Perform save logic here (e.g., send to server or save locally)
            Toast.makeText(requireContext(), "Profile saved successfully!", Toast.LENGTH_SHORT).show();
        });

        // Camera icon click listener
        cameraIcon.setOnClickListener(v -> {
            // Handle profile picture update (open gallery or camera)
            Toast.makeText(requireContext(), "Update profile picture!", Toast.LENGTH_SHORT).show();
        });
    }
}
