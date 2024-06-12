package com.example.schoolapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class UploadImageFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;

    private EditText eventNameEditText;
    private EditText descriptionEditText;
    private ImageView imageView;
    private ProgressBar progressBar;
    private Uri imageUri;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_upload_image, container, false);

        eventNameEditText = view.findViewById(R.id.eventname);
        descriptionEditText = view.findViewById(R.id.description);
        InputFilter[] filters = new InputFilter[1];
        filters[0] = new InputFilter.LengthFilter(250);
        descriptionEditText.setFilters(filters);
        imageView = view.findViewById(R.id.imageView);
        progressBar = view.findViewById(R.id.progressBar3);
        progressBar.setVisibility(View.INVISIBLE);
        Button chooseImageButton = view.findViewById(R.id.chooseimg);
        Button submitButton = view.findViewById(R.id.submit);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("events");
        storageReference = FirebaseStorage.getInstance().getReference().child("event_images");

        chooseImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadEvent();
            }
        });

        return view;
    }

    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            Picasso.get().load(imageUri).into(imageView);
        }
    }

    private void uploadEvent() {
        String eventName = eventNameEditText.getText().toString().trim();
        String description = descriptionEditText.getText().toString().trim();

        // Validate fields
        if (eventName.isEmpty() || description.isEmpty() || imageUri == null) {
            Toast.makeText(getContext(), "Please fill in all fields and select an image", Toast.LENGTH_SHORT).show();
            return; // Stop execution if any field is empty
        }

        progressBar.setVisibility(View.VISIBLE);

        StorageReference fileReference = storageReference.child(System.currentTimeMillis() + ".jpg");

        fileReference.putFile(imageUri)
                .addOnCompleteListener(new OnCompleteListener<com.google.firebase.storage.UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<com.google.firebase.storage.UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            fileReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    if (task.isSuccessful()) {
                                        Uri downloadUri = task.getResult();
                                        Boolean deleted = false;
                                        long currentTimeMillis = System.currentTimeMillis();
                                        String eventDate = formatDate(currentTimeMillis);
                                        String eventId = databaseReference.push().getKey();

                                        Event event = new Event(eventId, eventName, description, eventDate, downloadUri.toString(), deleted);

                                        // Save the event to Firebase Realtime Database
                                        databaseReference.child(eventId).setValue(event);

                                        // Clear input fields
                                        eventNameEditText.setText("");
                                        descriptionEditText.setText("");
                                        imageView.setImageResource(android.R.color.transparent);

                                        // Display success message
                                        Toast.makeText(getContext(), "Event uploaded successfully", Toast.LENGTH_SHORT).show();
                                    } else {
                                        // Handle failure to get download URL
                                        Toast.makeText(getContext(), "Failed to get download URL", Toast.LENGTH_SHORT).show();
                                    }

                                    progressBar.setVisibility(View.GONE);
                                }
                            });
                        } else {
                            // Handle failure to upload file
                            Toast.makeText(getContext(), "Upload failed", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle failure to upload file
                        Toast.makeText(getContext(), "Upload failed", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                });
    }

    private String formatDate(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy hh:mm a", Locale.getDefault());
        return sdf.format(new Date(timestamp));
    }
}
