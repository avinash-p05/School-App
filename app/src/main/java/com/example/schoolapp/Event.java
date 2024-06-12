package com.example.schoolapp;

import android.os.Parcel;
import android.os.Parcelable;

public class Event implements Parcelable {
    private String eventId; // Unique identifier
    private String eventName;
    private String eventDescription;
    private String eventDate;
    private boolean deleted;
    private String imageUrl;

    // Default constructor required for calls to DataSnapshot.getValue(Event.class)
    public Event() {
    }

    public Event(String eventId, String eventName, String eventDescription, String eventDate, String imageUrl,Boolean deleted) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.eventDescription = eventDescription;
        this.eventDate = eventDate;
        this.imageUrl = imageUrl;
        this.deleted = deleted;
    }

    public String getEventId() {
        return eventId;
    }
    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    // Parcelable implementation
    protected Event(Parcel in) {
        eventId = in.readString();
        eventName = in.readString();
        eventDescription = in.readString();
        eventDate = in.readString();
        imageUrl = in.readString();
    }

    public static final Creator<Event> CREATOR = new Parcelable.Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(eventId);
        dest.writeString(eventName);
        dest.writeString(eventDescription);
        dest.writeString(eventDate);
        dest.writeString(imageUrl);
    }
}
