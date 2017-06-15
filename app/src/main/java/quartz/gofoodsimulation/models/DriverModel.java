package quartz.gofoodsimulation.models;

import android.location.Location;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by sxio on 06-Jun-17.
 */

public class DriverModel {
    public static int cancelId = 0;

    private int id;
    private String name;
    private String phone;
    private double latitude;
    private double longitude;
    private double range;

    public DriverModel() {
    }

    public DriverModel(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }

    public DriverModel(String name, String phone, double latitude, double longitude) {
        this.name = name;
        this.phone = phone;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public LatLng getLatLng() {
        return new LatLng(this.latitude, this.longitude);
    }

    public double getRange() {
        return range;
    }

    public void setRange(Location sellerLocation) {
        Location driverLocation = new Location("");
        driverLocation.setLatitude(this.getLatitude());
        driverLocation.setLongitude(this.getLongitude());
        float distance = 0;
        try {
            distance = sellerLocation.distanceTo(driverLocation) / 1000;
        } catch (Exception e) {
            Log.e("Location", e.getMessage());
        }
        this.range = distance;
    }
}
