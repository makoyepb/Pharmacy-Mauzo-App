package com.example.pharmacymauzoapp;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Medicine {
    private String itemname;
    private String itemsellprice;
    private String itemquantity;
    private String itembarcode;
    private String amount;
    private  String transdate;

    public Medicine() {
    }

    public Medicine(String itemname, String itemsellprice, String itemquantity, String itembarcode, String amount, String transdate) {
        this.itemname = itemname;
        this.itemsellprice = itemsellprice;
        this.itemquantity = itemquantity;
        this.itembarcode = itembarcode;
        this.amount = amount;
        this.transdate = transdate;
    }

    public String getItemname() {
        return itemname;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
    }

    public String getItemsellprice() {
        return itemsellprice;
    }

    public void setItemsellprice(String itemsellprice) {
        this.itemsellprice = itemsellprice;
    }

    public String getItemquantity() {
        return itemquantity;
    }

    public void setItemquantity(String itemquantity) {
        this.itemquantity = itemquantity;
    }

    public String getItembarcode() {
        return itembarcode;
    }

    public void setItembarcode(String itembarcode) {
        this.itembarcode = itembarcode;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTransdate() {
        return transdate;
    }

    public void setTransdate(String transdate) {
        this.transdate = transdate;
    }
}

