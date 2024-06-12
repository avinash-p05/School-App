// EventDetailsActivity.java
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

public class EventDetailsActivity extends AppCompatActivity {

    private Event event;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        // Retrieve the event details from the intent
        event = getIntent().getParcelableExtra("event");

        if (event != null) {
            // Set the event details in the UI
            TextView eventNameTextView = findViewById(R.id.eventNameTextView);
            TextView dateTextView = findViewById(R.id.dateTextView);
            ImageView eventImageView = findViewById(R.id.eventImageView);
            TextView descriptionTextView = findViewById(R.id.descriptionTextView);
            Button deleteButton = findViewById(R.id.deleteButton);

            eventNameTextView.setText(event.getEventName());
            dateTextView.setText("Date- "+event.getEventDate());
            descriptionTextView.setText(event.getEventDescription());
            Button editButton = findViewById(R.id.editButton);
            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onEditButtonClick();
                }
            });

            // Load the image using Picasso
            Picasso.get().load(event.getImageUrl()).into(eventImageView);

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onDeleteButtonClick();
                }
            });
        }
    }



    private void onEditButtonClick() {
        // Open the EditEventActivity and pass the current event details
        Intent intent = new Intent(EventDetailsActivity.this , EditEventActivity.class);
        intent.putExtra("event",event);
        startActivity(intent);
        finish();
    }

    public void onDeleteButtonClick() {
        // Check if the event and eventId are not null
        if (event != null && event.getEventId() != null) {
            // Get a reference to the original event location
            DatabaseReference originalEventRef = FirebaseDatabase.getInstance().getReference().child("events").child(event.getEventId());

            // Set deleted to true
            event.setDeleted(true);

            // Update the event in the original location
            originalEventRef.setValue(event, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    if (databaseError == null) {
                        // Display success message
                        Toast.makeText(EventDetailsActivity.this, "Event deleted successfully", Toast.LENGTH_SHORT).show();

                        // Close the activity
                        finish();
                    } else {
                        // Handle the case where updating the event failed
                        Toast.makeText(EventDetailsActivity.this, "Unable to delete the event", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            // Handle the case where event or event ID is null
            Toast.makeText(this, "Unable to delete the event", Toast.LENGTH_SHORT).show();
        }
    }



}
