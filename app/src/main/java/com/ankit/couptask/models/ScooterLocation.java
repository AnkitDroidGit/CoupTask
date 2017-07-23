package com.ankit.couptask.models;

import com.google.gson.annotations.SerializedName;

/**
 * @author by Ankit Kumar (ankitdroiddeveloper@gmail.com).
 */

public class ScooterLocation {
    @SerializedName("lng")
    private double longitude;
    @SerializedName("lat")
    private double latitude;

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}
