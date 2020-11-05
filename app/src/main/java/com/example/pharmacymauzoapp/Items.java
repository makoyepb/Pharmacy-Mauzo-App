package com.example.pharmacymauzoapp;

public class Items {
    private String itemname;
    private String itembuyingprice;
    private String itemsellprice;
    private String itemquantity;
    private String itembarcode;


    public Items() {

    }

    public Items(String itemname, String itembuyingprice, String itemsellprice, String itemquantity, String itembarcode) {
        this.itemname = itemname;
        this.itembuyingprice = itembuyingprice;
        this.itemsellprice = itemsellprice;
        this.itemquantity = itemquantity;
        this.itembarcode = itembarcode;
    }

    public String getItemname() {
        return itemname;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
    }

    public String getItembuyingprice() {
        return itembuyingprice;
    }

    public void setItembuyingprice(String itembuyingprice) {
        this.itembuyingprice = itembuyingprice;
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
}
