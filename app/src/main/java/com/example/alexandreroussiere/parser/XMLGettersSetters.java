package com.example.alexandreroussiere.parser;

/**
 * Created by Alexandre Roussière on 22/02/2016.
 */
import java.util.ArrayList;
import android.util.Log;


/**
 *  This class contains all getter and setter methods to set and retrieve data.
 *
 **/
public class XMLGettersSetters {

    private ArrayList<String> title = new ArrayList<String>();
    private ArrayList<String> artist = new ArrayList<String>();
    private ArrayList<String> country = new ArrayList<String>();
    private ArrayList<String> company = new ArrayList<String>();
    private ArrayList<String> price = new ArrayList<String>();
    private ArrayList<String> year = new ArrayList<String>();
    private ArrayList<String> soldOut = new ArrayList<String>();

    public ArrayList<String> getSoldOut() { return soldOut;}

    public void setSoldOut(String str){
        this.soldOut.add(str);
        Log.i("CD is soldout? " , str);
    }

    //Count how many CDs are sold out
    public int CountCdSoldOut(){

        int result = 0;
        //We go through the array and look for "yes"
        for(int i=0;i<soldOut.size();i++) {
            if (soldOut.get(i).equalsIgnoreCase("yes"))
                result++;
        }
        return result;
    }


    public ArrayList<String> getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company.add(company);
        Log.i("This is the company:", company);
    }


    public ArrayList<String> getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price.add(price);
        Log.i("This is the price:", price);
    }

    public ArrayList<String> getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year.add(year);
        Log.i("This is the year:", year);
    }

    public ArrayList<String> getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title.add(title);
        Log.i("This is the title:", title);
    }

    public ArrayList<String> getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist.add(artist);
        Log.i("This is the artist:", artist);
    }

    public ArrayList<String> getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country.add(country);
        Log.i("This is the country:", country);
    }

}
