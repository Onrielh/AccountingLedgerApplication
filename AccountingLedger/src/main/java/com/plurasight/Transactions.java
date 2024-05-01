package com.plurasight;

public class Transactions {
    private String date;
    private String time;
    private String description;
    private String vendor;
    private float amount;
    public Transactions(String date, String time, String description, String vendor, float amount){
        this.date = date;
        this.time = time;
        this.description = description;
        this.vendor = vendor;
        this.amount = amount;
    }
    @Override
    public String toString() {
        return (date + "|" + time + "|" + description + "|"  + vendor + "|"  + amount + "\n");
                }
    }


