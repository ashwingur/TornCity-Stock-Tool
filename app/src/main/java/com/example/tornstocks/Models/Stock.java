package com.example.tornstocks.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Comparator;

public class Stock implements Parcelable {
    // This class implements Parcelable so it can be sent through intent bundles

    private int stock_id;
    private String name;
    private String acronym;
    private float current_price;
    // Add more if required


    public Stock(int stock_id, String name, String acronym, float current_price) {
        this.stock_id = stock_id;
        this.name = name;
        this.acronym = acronym;
        this.current_price = current_price;
    }

    // Getters
    public int getStock_id() { return stock_id; }

    public String getName() { return name; }

    public String getAcronym() { return acronym; }

    public float getCurrent_price() { return current_price; }

    protected Stock(Parcel in) {
        stock_id = in.readInt();
        name = in.readString();
        acronym = in.readString();
        current_price = in.readFloat();
    }

    public static Comparator<Stock> StockPriceComparator = new Comparator<Stock>() {
        @Override
        public int compare(Stock s1, Stock s2) {
            return (int) (s2.current_price - s1.current_price);
        }
    };

    public static final Creator<Stock> CREATOR = new Creator<Stock>() {
        @Override
        public Stock createFromParcel(Parcel in) {
            return new Stock(in);
        }

        @Override
        public Stock[] newArray(int size) {
            return new Stock[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(stock_id);
        dest.writeString(name);
        dest.writeString(acronym);
        dest.writeFloat(current_price);
    }

    @Override
    public String toString() {
        return "Stock{" +
                "stock_id=" + stock_id +
                ", name='" + name + '\'' +
                ", acronym='" + acronym + '\'' +
                ", current_price=" + current_price +
                '}';
    }
}
