package com.example.mohamed.anticheating;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Mohamed on 26/02/2017.
 */
public class ManualNotification implements Serializable,Parcelable {
    public int id;
    private String image ;
    private String header;
    private String content;
    private String time;
    private String video_url;
    private String status;
    private boolean is_automatic;
    private boolean seen;
    private String key;




    public boolean is_automatic() {
        return is_automatic;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getVideo_url() {
        return video_url;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean getIs_automatic() {
        return is_automatic;
    }

    public void setIs_automatic(boolean is_automatic) {
        this.is_automatic = is_automatic;
    }

    public static Creator<ManualNotification> getCREATOR() {
        return CREATOR;
    }

    public ManualNotification(){}

    private ManualNotification(Parcel in) {
        this.seen = false;
        this.id = in.readInt();
        this.image = in.readString();
        this.header = in.readString();
        this.content = in.readString();
        this.time = in.readString();
        this.video_url = in.readString();
        this.status = in.readString();
        //this.is_automatic = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.image);
        dest.writeString(this.header);
        dest.writeString(this.content);
        dest.writeString(this.time);
        dest.writeString(this.video_url);
        dest.writeString(this.status);
        //dest.writeInt(this.is_automatic);
    }

    public static final Parcelable.Creator<ManualNotification> CREATOR = new Parcelable.Creator<ManualNotification>() {
        public ManualNotification createFromParcel(Parcel in) {
            return new ManualNotification(in);
        }

        public ManualNotification[] newArray(int size) {
            return new ManualNotification[size];
        }
    };

}
