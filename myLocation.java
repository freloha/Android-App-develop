package com.example.astp;

public class myLocation {
    public double Longitude[];
    public double Latitude[];
    public int total;
    public myLocation(){
        Longitude = new double[50];
        Latitude = new double[50];
        total = 0;
    }

    public void getLatLon(double lat, double lon, int counter){
        Latitude[counter] = lat;
        Longitude[counter] = lon;
        total++;
    }

    public double getLat(int i){
        return Latitude[i];
    }

    public double getLon(int i){
        return Longitude[i];
    }

    public int getCounter(){
        return total;
    }
}
