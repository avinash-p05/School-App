package com.example.schoolapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class FacultyAdapter extends RecyclerView.Adapter<FacultyAdapter.FacultyViewHolder> {

    private List<Faculty> FacultyList;
    private OnItemClickListener listener;
    private Context context;

    // Constructor to initialize the FacultyList and listener
    public FacultyAdapter(List<Faculty> FacultyList, Context context, OnItemClickListener listener) {
        this.FacultyList = FacultyList;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FacultyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.facultyitem, parent, false);
        return new FacultyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FacultyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Faculty Faculty = FacultyList.get(position);

        // Bind data to views in the ViewHolder
        holder.FacultyNameTextView.setText(Faculty.getFacultyName());
        holder.dateTextView.setText(Faculty.getFacultyDate());

        // Set a click listener for the item
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the onItemClick method of the listener
                if (listener != null) {
                    listener.onItemClick(FacultyList.get(position));
                    openDetailActivity(FacultyList.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return FacultyList.size();
    }

    // Method to update the FacultyList in the adapter
    public void setFacultyList(List<Faculty> FacultyList) {
        this.FacultyList = FacultyList;
        notifyDataSetChanged();
    }

    // ViewHolder class
    public static class FacultyViewHolder extends RecyclerView.ViewHolder {
        TextView FacultyNameTextView;
        TextView dateTextView;

        public FacultyViewHolder(@NonNull View itemView) {
            super(itemView);
            FacultyNameTextView = itemView.findViewById(R.id.FacultyNameTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
        }
    }

    // Interface for click Facultys
    public interface OnItemClickListener {
        void onItemClick(Faculty Faculty);
    }

    private void openDetailActivity(Faculty Faculty) {
        Intent intent = new Intent(context, FacultyDetails.class); // Replace with your detail activity class
        intent.putExtra("Faculty", (Parcelable) Faculty); // Pass the Faculty object to the detail activity
        context.startActivity(intent);
    }
}
