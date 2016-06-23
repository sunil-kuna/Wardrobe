package com.jojodelivery.wardrobe.DataDefitions;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by asus on 24-06-2016.
 */
public class Cloth implements Parcelable{
    String type;
    String id;
    Bitmap image;

    public String getType() {
        return type;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public static final Parcelable.Creator<Cloth> CREATOR = new Parcelable.Creator<Cloth>() {
        public Cloth createFromParcel(Parcel in) {
            return new Cloth(in);
        }
        public Cloth[] newArray(int size) {
            return new Cloth[size];
        }
    };
    public Cloth() {}

    private Cloth(Parcel in) {
        readFromParcel(in);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    public void readFromParcel(Parcel in) {
        this.id = in.readString();
        this.type = in.readString();
        this.image = in.readParcelable(Bitmap.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel out, int i) {
        out.writeString(this.id);
        out.writeString(this.type);
        this.image.writeToParcel(out,i);
    }
}
