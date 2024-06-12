// EditEventActivity.java
package com.example.schoolapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditEventActivity extends AppCompatActivity {

    private EditText eventNameEditText;
    private EditText descriptionEditText;
    private Button saveButton,discard;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);
        eventNameEditText = findViewById(R.id.editEventNameEditText);
        descriptionEditText = findViewById(R.id.editDescriptionEditText);
        InputFilter[] filters = new InputFilter[1];
        filters[0] = new InputFilter.LengthFilter(250);
        descriptionEditText.setFilters(filters);
        saveButton = findViewById(R.id.saveButton);
        discard = findViewById(R.id.discardButton);

        // Retrieve the event details from the intent
        Event event = getIntent().getParcelableExtra("event");

        if (event != null) {
            // Set the current event details in the edit text fields
            eventNameEditText.setText(event.getEventName());
            descriptionEditText.setText(event.getEventDescription());
        }
        discard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(EditEventActivity.this, "Changes Discarded!!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSaveButtonClick(event);
            }
        });
    }

    private void onSaveButtonClick(Event event) {
        // Retrieve the updated event details from the edit text fields
        String updatedEventName = eventNameEditText.getText().toString().trim();
        String updatedDescription = descriptionEditText.getText().toString().trim();

        // Check if the updated details are not empty
        if (!updatedEventName.isEmpty() && !updatedDescription.isEmpty()) {
            // Create an updated event object
            Event updatedEvent = new Event(
                    event.getEventId(),
                    updatedEventName,
                    updatedDescription,
                    event.getEventDate(),
                    event.getImageUrl(),
                    event.isDeleted()
            );

            // Update the event in the Firebase Realtime Database
            DatabaseReference eventRef = FirebaseDatabase.getInstance().getReference().child("events").child(event.getEventId());
            eventRef.setValue(updatedEvent);

            // Display success message
            Toast.makeText(this, "Event updated successfully", Toast.LENGTH_SHORT).show();

            // Close the activity
            finish();
        } else {
            // Display an error message if the fields are empty
            Toast.makeText(this, "Please enter event details", Toast.LENGTH_SHORT).show();
        }
    }
}
