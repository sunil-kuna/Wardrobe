package com.jojodelivery.wardrobe.DataDefitions;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by asus on 24-06-2016.
 */
public class Favourite  implements Parcelable {

    String shirtId;
    String trousersId;

    public String getShirtId() {
        return shirtId;
    }

    public void setShirtId(String shirtId) {
        this.shirtId = shirtId;
    }

    public String getTrousersId() {
        return trousersId;
    }

    public void setTrousersId(String trousersId) {
        this.trousersId = trousersId;
    }

    @Override
    public String toString() {
        return "Favourite{" +
                "shirtId='" + shirtId + '\'' +
                ", trousersId='" + trousersId + '\'' +
                '}';
    }

    public static final Parcelable.Creator<Favourite> CREATOR = new Parcelable.Creator<Favourite>() {
        public Favourite createFromParcel(Parcel in) {
            return new Favourite(in);
        }
        public Favourite[] newArray(int size) {
            return new Favourite[size];
        }
    };
    public Favourite() {}

    private Favourite(Parcel in) {
        readFromParcel(in);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    public void readFromParcel(Parcel in) {
        this.shirtId = in.readString();
        this.trousersId = in.readString();
    }

    @Override
    public void writeToParcel(Parcel out, int i) {
        out.writeString(this.shirtId);
        out.writeString(this.trousersId);
    }
}
