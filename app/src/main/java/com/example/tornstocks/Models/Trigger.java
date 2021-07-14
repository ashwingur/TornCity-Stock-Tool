package com.example.tornstocks.Models;


import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "trigger_table")
public class Trigger implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private int ID;
    private int stock_id;
    private String acronym;
    private float trigger_price;
    private float creation_price;
    private boolean is_above; // If true then trigger is above current price

    public Trigger(int stock_id, String acronym, float trigger_price, float creation_price) {
        this.stock_id = stock_id;
        this.acronym = acronym;
        this.trigger_price = trigger_price;
        this.creation_price = creation_price;
        this.is_above = trigger_price > creation_price;
    }

    protected Trigger(Parcel in) {
        ID = in.readInt();
        stock_id = in.readInt();
        acronym = in.readString();
        trigger_price = in.readFloat();
        creation_price = in.readFloat();
        is_above = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(ID);
        dest.writeInt(stock_id);
        dest.writeString(acronym);
        dest.writeFloat(trigger_price);
        dest.writeFloat(creation_price);
        dest.writeByte((byte) (is_above ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Trigger> CREATOR = new Creator<Trigger>() {
        @Override
        public Trigger createFromParcel(Parcel in) {
            return new Trigger(in);
        }

        @Override
        public Trigger[] newArray(int size) {
            return new Trigger[size];
        }
    };

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setTrigger_price(float trigger_price) {
        this.trigger_price = trigger_price;
        this.is_above = trigger_price > creation_price;
    }

    public void setCreation_price(float creation_price) {
        this.creation_price = creation_price;
        this.is_above = trigger_price > creation_price;
    }

    public void setIs_above(boolean is_above) {
        this.is_above = is_above;
    }

    public int getID() {
        return ID;
    }

    public int getStock_id() {
        return stock_id;
    }

    public String getAcronym() {
        return acronym;
    }

    public float getTrigger_price() {
        return trigger_price;
    }

    public float getCreation_price() {
        return creation_price;
    }

    public boolean isIs_above() { return is_above; }

    @Override
    public String toString() {
        return "Trigger{" +
                "ID=" + ID +
                ", stock_id=" + stock_id +
                ", acronym='" + acronym + '\'' +
                ", trigger_price=" + trigger_price +
                ", creation_price=" + creation_price +
                ", is_above=" + is_above +
                '}';
    }

}
