package com.example.schoolapp;


import android.os.Parcel;
import android.os.Parcelable;

public class Faculty implements Parcelable {
    private String FacultyId; // Unique identifier
    private String FacultyName;
    private String FacultyDescription;
    private String FacultyDate;
    private boolean deleted;
    private String imageUrl;

    // Default constructor required for calls to DataSnapshot.getValue(Faculty.class)
    public Faculty() {
    }

    public Faculty(String FacultyId, String FacultyName, String FacultyDescription, String FacultyDate, String imageUrl,Boolean deleted) {
        this.FacultyId = FacultyId;
        this.FacultyName = FacultyName;
        this.FacultyDescription = FacultyDescription;
        this.FacultyDate = FacultyDate;
        this.imageUrl = imageUrl;
        this.deleted = deleted;
    }

    public String getFacultyId() {
        return FacultyId;
    }
    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public void setFacultyId(String FacultyId) {
        this.FacultyId = FacultyId;
    }

    public String getFacultyName() {
        return FacultyName;
    }

    public void setFacultyName(String FacultyName) {
        this.FacultyName = FacultyName;
    }

    public String getFacultyDescription() {
        return FacultyDescription;
    }

    public void setFacultyDescription(String FacultyDescription) {
        this.FacultyDescription = FacultyDescription;
    }

    public String getFacultyDate() {
        return FacultyDate;
    }

    public void setFacultyDate(String FacultyDate) {
        this.FacultyDate = FacultyDate;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    // Parcelable implementation
    protected Faculty(Parcel in) {
        FacultyId = in.readString();
        FacultyName = in.readString();
        FacultyDescription = in.readString();
        FacultyDate = in.readString();
        imageUrl = in.readString();
    }

    public static final Creator<Faculty> CREATOR = new Parcelable.Creator<Faculty>() {
        @Override
        public Faculty createFromParcel(Parcel in) {
            return new Faculty(in);
        }

        @Override
        public Faculty[] newArray(int size) {
            return new Faculty[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(FacultyId);
        dest.writeString(FacultyName);
        dest.writeString(FacultyDescription);
        dest.writeString(FacultyDate);
        dest.writeString(imageUrl);
    }
}

