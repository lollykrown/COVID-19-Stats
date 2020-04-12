package com.lollykrown.coronavirusstats.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "corona", indices = {@Index(value = "countryName", unique = true)})
public class Corona {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;

    @ColumnInfo(name = "countryName")
    private String countryName;

    @ColumnInfo(name = "cases")
    private int cases;

    @ColumnInfo(name = "deaths")
    private String deaths;

    @ColumnInfo(name = "total_recovered")
    private String total_recovered;

    @ColumnInfo(name = "newCases")
    private String newCases;

    @ColumnInfo(name = "newDeaths")
    private int newDeaths;

    @ColumnInfo(name = "seriousCritical")
    private String seriousCritical;


    public Corona(int id, String countryName, int cases, String deaths, String total_recovered, String newCases, int newDeaths, String seriousCritical) {
        this.id = id;
        this.countryName = countryName;
        this.cases = cases;
        this.deaths = deaths;
        this.total_recovered = total_recovered;
        this.newCases = newCases;
        this.newDeaths = newDeaths;
        this.seriousCritical = seriousCritical;
    }

    @Ignore
    public Corona(String countryName, int cases, String deaths, String total_recovered, String newCases, int newDeaths, String seriousCritical) {
        this.countryName = countryName;
        this.cases = cases;
        this.deaths = deaths;
        this.total_recovered = total_recovered;
        this.newCases = newCases;
        this.newDeaths = newDeaths;
        this.seriousCritical = seriousCritical;
    }

    @Ignore
    public Corona(){
    }

    @NonNull
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getCountryName() {
        return countryName;
    }
    public int getCases() {
        return cases;
    }
    public String getDeaths() {
        return deaths;
    }
    public String getTotal_recovered() {
        return total_recovered;
    }
    public String getNewCases() {
        return newCases;
    }
    public int getNewDeaths() {
        return newDeaths;
    }
    public String getSeriousCritical() {
        return seriousCritical;
    }
}
