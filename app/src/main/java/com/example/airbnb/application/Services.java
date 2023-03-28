package com.example.airbnb.application;

import com.example.airbnb.persistence.AirbnbDB;

public class Services {

    public static AirbnbDB airbnb = null;

    public static synchronized AirbnbDB getAirbnbDB(){
        if (airbnb == null) {
            airbnb = new AirbnbDB(Main.getDBPathName());
        }
        return airbnb;
    }
}
