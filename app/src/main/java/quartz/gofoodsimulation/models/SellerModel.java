package quartz.gofoodsimulation.models;

import android.location.Location;
import android.util.Log;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by kened on 4/9/2017.
 */

public class SellerModel implements Serializable {
    public static SellerModel seller;

    private long id;
    private String name;
    private String phone;
    private String address;
    private String coverPhoto;
    private String category;
    private int jumlahOrder;
    private String time;
    private Date joinDate;
    private double latitude;
    private double longitude;
    private float range;

    public SellerModel(String name, String phone, String address,
                       String coverPhoto, String category, int jumlahOrder,
                       String time, Date joinDate, double latitude, double longitude) {
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.coverPhoto = coverPhoto;
        this.category = category;
        this.jumlahOrder = jumlahOrder;
        this.time = time;
        this.joinDate = joinDate;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public SellerModel() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public int getStatus() {
        String[] sTime = time.split(" - ");
        String open_time = sTime[0];
        String closed_time = sTime[1];
        int open_hour = Integer.valueOf(open_time.split("\\.")[0]) * 60;
        int open_minute = Integer.valueOf(open_time.split("\\.")[1]) + open_hour;
        int closed_hour = Integer.valueOf(closed_time.split("\\.")[0]) * 60;
        int closed_minute = Integer.valueOf(closed_time.split("\\.")[1]) + closed_hour;
        int now_hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY) * 60;
        int now_minute = Calendar.getInstance().get(Calendar.MINUTE) + now_hour;

        int status;
        if (open_minute <= now_minute && now_minute <= closed_minute) {
            status = 1;
        } else {
            status = 0;
        }
        return status;
    }

    public Date getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(Date joinDate) {
        this.joinDate = joinDate;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCoverPhoto() {
        return coverPhoto;
    }

    public void setCoverPhoto(String coverPhoto) {
        this.coverPhoto = coverPhoto;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getJumlahOrder() {
        return jumlahOrder;
    }

    public void setJumlahOrder(int jumlahOrder) {
        this.jumlahOrder = jumlahOrder;
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

    public float getRange() {
        return range;
    }

    public void setRange(Location phoneLocation) {
        Location sellerLocation = new Location("");
        sellerLocation.setLatitude(this.getLatitude());
        sellerLocation.setLongitude(this.getLongitude());
        float distance = 0;
        try {
            distance = phoneLocation.distanceTo(sellerLocation) / 1000;
        } catch (Exception e) {
            Log.e("Location", e.getMessage());
        }
        this.range = distance;
    }
}
