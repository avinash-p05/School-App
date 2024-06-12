// FacultyDetailsActivity.java
package com.example.schoolapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class FacultyDetails extends AppCompatActivity {

    private Faculty Faculty;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_details);

        // Retrieve the Faculty details from the intent
        Faculty = getIntent().getParcelableExtra("Faculty");

        if (Faculty != null) {
            // Set the Faculty details in the UI
            TextView FacultyNameTextView = findViewById(R.id.facultyNameTextView);
            TextView dateTextView = findViewById(R.id.dateTextView1);
            ImageView FacultyImageView = findViewById(R.id.facultyImageView);
            TextView descriptionTextView = findViewById(R.id.designationTextView);
            Button deleteButton = findViewById(R.id.removeButton);
            FacultyNameTextView.setText(Faculty.getFacultyName());
            dateTextView.setText("Date- "+Faculty.getFacultyDate());
            descriptionTextView.setText("Designation - "+Faculty.getFacultyDescription());


            // Load the image using Picasso
            Picasso.get().load(Faculty.getImageUrl()).into(FacultyImageView);

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onDeleteButtonClick();
                }
            });
        }
    }



    public void onDeleteButtonClick() {
        // Check if the Faculty and FacultyId are not null
        if (Faculty != null && Faculty.getFacultyId() != null) {
            // Get a reference to the original Faculty location
            DatabaseReference originalFacultyRef = FirebaseDatabase.getInstance().getReference().child("faculty").child(Faculty.getFacultyId());

            // Set deleted to true
            Faculty.setDeleted(true);

            // Update the Faculty in the original location
            originalFacultyRef.setValue(Faculty, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    if (databaseError == null) {
                        // Display success message
                        Toast.makeText(FacultyDetails.this, "Faculty deleted successfully", Toast.LENGTH_SHORT).show();

                        // Close the activity
                        finish();
                    } else {
                        // Handle the case where updating the Faculty failed
                        Toast.makeText(FacultyDetails.this, "Unable to delete the Faculty", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            // Handle the case where Faculty or Faculty ID is null
            Toast.makeText(this, "Unable to delete the Faculty", Toast.LENGTH_SHORT).show();
        }
    }



}
