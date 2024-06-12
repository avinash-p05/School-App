package com.example.schoolapp;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HomeFragment extends Fragment {
    private RecyclerView recyclerView;
    private EventAdapter eventAdapter;
    private DatabaseReference databaseReference;
    private ProgressBar progress;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        progress = view.findViewById(R.id.progressBar2);
        recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize EventAdapter with an empty list and an OnItemClickListener
        eventAdapter = new EventAdapter(new ArrayList<>(), getContext(), new EventAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Event event) {
                // Handle item click
            }
        });

        recyclerView.setAdapter(eventAdapter);

        // Show the progress bar
        progress.setVisibility(View.VISIBLE);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("events");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Event> eventList = new ArrayList<>();

                // ...

                for (DataSnapshot eventSnapshot : dataSnapshot.getChildren()) {
                    // Retrieve data from Firebase
                    String eventId = eventSnapshot.getKey();
                    String eventName = eventSnapshot.child("eventName").getValue(String.class);
                    String eventDescription = eventSnapshot.child("eventDescription").getValue(String.class);
                    String eventDate = eventSnapshot.child("eventDate").getValue(String.class);
                    String imageUrl = eventSnapshot.child("imageUrl").getValue(String.class);
                    Boolean deleted = eventSnapshot.child("deleted").getValue(Boolean.class);

                    // Check if the event is marked as deleted
                    if (!deleted) {
                        // Create an Event object and add it to the list
                        Event event = new Event(eventId, eventName, eventDescription, eventDate, imageUrl, true);
                        eventList.add(event);
                    }
                }


                Collections.reverse(eventList);
                // Set the event list to the adapter
                eventAdapter.setEventList(eventList);

                // Hide the progress bar after 3 seconds
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progress.setVisibility(View.GONE);
                    }
                }, 1500); // 3 seconds
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle database error, if needed
                progress.setVisibility(View.GONE); // Ensure the progress bar is hidden on error
            }
        });

        return view;
    }
}
