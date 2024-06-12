package com.example.schoolapp;

import android.annotation.SuppressLint;
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

public class DisplayFaculty extends Fragment {
    private RecyclerView recyclerView;
    private FacultyAdapter FacultyAdapter;
    private DatabaseReference databaseReference;
    private ProgressBar progress;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_display_faculty, container, false);
        progress = view.findViewById(R.id.progressBar5);
        recyclerView = view.findViewById(R.id.recyclerview1);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize FacultyAdapter with an empty list and an OnItemClickListener
        FacultyAdapter = new FacultyAdapter(new ArrayList<>(), getContext(), new FacultyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Faculty Faculty) {
                // Handle item click
            }
        });

        recyclerView.setAdapter(FacultyAdapter);

        // Show the progress bar
        progress.setVisibility(View.VISIBLE);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("faculty");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Faculty> FacultyList = new ArrayList<>();

                // ...

                for (DataSnapshot FacultySnapshot : dataSnapshot.getChildren()) {
                    // Retrieve data from Firebase
                    String FacultyId = FacultySnapshot.getKey();
                    String FacultyName = FacultySnapshot.child("facultyName").getValue(String.class);
                    String FacultyDescription = FacultySnapshot.child("facultyDescription").getValue(String.class);
                    String FacultyDate = FacultySnapshot.child("facultyDate").getValue(String.class);
                    String imageUrl = FacultySnapshot.child("imageUrl").getValue(String.class);
                    Boolean deleted = FacultySnapshot.child("deleted").getValue(Boolean.class);

                    // Check if the Faculty is marked as deleted
                    if (!deleted) {
                        // Create an Faculty object and add it to the list
                        Faculty Faculty = new Faculty(FacultyId, FacultyName, FacultyDescription, FacultyDate, imageUrl, true);
                        FacultyList.add(Faculty);
                    }
                }


                Collections.reverse(FacultyList);
                // Set the Faculty list to the adapter
                FacultyAdapter.setFacultyList(FacultyList);

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
