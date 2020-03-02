package com.fahimshahrierrasel.collectionnotifier.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Bin implements Parcelable
{

    @SerializedName("button")
    @Expose
    private int button;
    @SerializedName("led")
    @Expose
    private int led;
    @SerializedName("count")
    @Expose
    private int count;
    @SerializedName("current_level")
    @Expose
    private int currentLevel;
    @SerializedName("depth")
    @Expose
    private int depth;
    @SerializedName("echo_pin")
    @Expose
    private int echoPin;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("last_cleaned")
    @Expose
    private String lastCleaned;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("notify_level")
    @Expose
    private int notifyLevel;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("trig_pin")
    @Expose
    private int trigPin;
    @SerializedName("tuned")
    @Expose
    private boolean tuned;
    public final static Parcelable.Creator<Bin> CREATOR = new Creator<Bin>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Bin createFromParcel(Parcel in) {
            return new Bin(in);
        }

        public Bin[] newArray(int size) {
            return (new Bin[size]);
        }

    }
            ;

    protected Bin(Parcel in) {
        this.button = ((int) in.readValue((int.class.getClassLoader())));
        this.count = ((int) in.readValue((int.class.getClassLoader())));
        this.currentLevel = ((int) in.readValue((int.class.getClassLoader())));
        this.depth = ((int) in.readValue((int.class.getClassLoader())));
        this.echoPin = ((int) in.readValue((int.class.getClassLoader())));
        this.id = ((String) in.readValue((String.class.getClassLoader())));
        this.lastCleaned = ((String) in.readValue((String.class.getClassLoader())));
        this.latitude = ((String) in.readValue((String.class.getClassLoader())));
        this.longitude = ((String) in.readValue((String.class.getClassLoader())));
        this.name = ((String) in.readValue((String.class.getClassLoader())));
        this.notifyLevel = ((int) in.readValue((int.class.getClassLoader())));
        this.status = ((String) in.readValue((String.class.getClassLoader())));
        this.trigPin = ((int) in.readValue((int.class.getClassLoader())));
        this.led = ((int) in.readValue((int.class.getClassLoader())));
        this.tuned = ((boolean) in.readValue((boolean.class.getClassLoader())));
    }

    /**
     * No args constructor for use in serialization
     *
     */
    public Bin() {
    }

    /**
     *
     * @param id
     * @param echoPin
     * @param count
     * @param status
     * @param button
     * @param tuned
     * @param name
     * @param currentLevel
     * @param trigPin
     * @param longitude
     * @param notifyLevel
     * @param latitude
     * @param lastCleaned
     * @param depth
     */
    public Bin(int button, int count,int led, int currentLevel, int depth, int echoPin, String id, String lastCleaned, String latitude, String longitude, String name, int notifyLevel, String status, int trigPin, boolean tuned) {
        super();
        this.button = button;
        this.count = count;
        this.currentLevel = currentLevel;
        this.depth = depth;
        this.echoPin = echoPin;
        this.id = id;
        this.lastCleaned = lastCleaned;
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
        this.notifyLevel = notifyLevel;
        this.status = status;
        this.trigPin = trigPin;
        this.tuned = tuned;
        this.led = led;
    }

    public int getButton() {
        return button;
    }

    public void setButton(int button) {
        this.button = button;
    }
    public int getLed() {
        return led;
    }

    public void setLed(int led) {
        this.led = led;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(int currentLevel) {
        this.currentLevel = currentLevel;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public int getEchoPin() {
        return echoPin;
    }

    public void setEchoPin(int echoPin) {
        this.echoPin = echoPin;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLastCleaned() {
        return lastCleaned;
    }

    public void setLastCleaned(String lastCleaned) {
        this.lastCleaned = lastCleaned;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNotifyLevel() {
        return notifyLevel;
    }

    public void setNotifyLevel(int notifyLevel) {
        this.notifyLevel = notifyLevel;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTrigPin() {
        return trigPin;
    }

    public void setTrigPin(int trigPin) {
        this.trigPin = trigPin;
    }

    public boolean isTuned() {
        return tuned;
    }

    public void setTuned(boolean tuned) {
        this.tuned = tuned;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(button);
        dest.writeValue(count);
        dest.writeValue(currentLevel);
        dest.writeValue(depth);
        dest.writeValue(echoPin);
        dest.writeValue(id);
        dest.writeValue(lastCleaned);
        dest.writeValue(latitude);
        dest.writeValue(longitude);
        dest.writeValue(name);
        dest.writeValue(notifyLevel);
        dest.writeValue(status);
        dest.writeValue(trigPin);
        dest.writeValue(tuned);
        dest.writeValue(led);
    }

    public int describeContents() {
        return 0;
    }

}