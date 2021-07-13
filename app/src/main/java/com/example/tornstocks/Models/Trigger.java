package com.example.tornstocks.Models;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "trigger_table")
public class Trigger {

    @PrimaryKey(autoGenerate = true)
    private int ID;
    private int stock_id;
    private String acronym;
    private float trigger_price;
    private float creation_price;

    public Trigger(int stock_id, String acronym, float trigger_price, float creation_price) {
        this.stock_id = stock_id;
        this.acronym = acronym;
        this.trigger_price = trigger_price;
        this.creation_price = creation_price;
    }

    public void setID(int ID) {
        this.ID = ID;
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

    @Override
    public String toString() {
        return "Trigger{" +
                "ID=" + ID +
                ", stock_id=" + stock_id +
                ", acronym='" + acronym + '\'' +
                ", trigger_price=" + trigger_price +
                ", creation_price=" + creation_price +
                '}';
    }
}
