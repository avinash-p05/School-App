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

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    private List<Event> eventList;
    private OnItemClickListener listener;
    private Context context;

    // Constructor to initialize the eventList and listener
    public EventAdapter(List<Event> eventList, Context context, OnItemClickListener listener) {
        this.eventList = eventList;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.uploadeditem, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Event event = eventList.get(position);

        // Bind data to views in the ViewHolder
        holder.eventNameTextView.setText("✨"+event.getEventName()+"✨");
        holder.dateTextView.setText(event.getEventDate());

        // Set a click listener for the item
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the onItemClick method of the listener
                if (listener != null) {
                    listener.onItemClick(eventList.get(position));
                    openDetailActivity(eventList.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    // Method to update the eventList in the adapter
    public void setEventList(List<Event> eventList) {
        this.eventList = eventList;
        notifyDataSetChanged();
    }

    // ViewHolder class
    public static class EventViewHolder extends RecyclerView.ViewHolder {
        TextView eventNameTextView;
        TextView dateTextView;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            eventNameTextView = itemView.findViewById(R.id.eventNameTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
        }
    }

    // Interface for click events
    public interface OnItemClickListener {
        void onItemClick(Event event);
    }

    private void openDetailActivity(Event event) {
        Intent intent = new Intent(context, EventDetailsActivity.class); // Replace with your detail activity class
        intent.putExtra("event", (Parcelable) event); // Pass the event object to the detail activity
        context.startActivity(intent);
    }
}
