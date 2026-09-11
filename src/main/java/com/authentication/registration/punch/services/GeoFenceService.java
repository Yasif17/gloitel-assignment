package com.authentication.registration.punch.services;

public interface GeoFenceService {

    public boolean isWithinGeoFence(double userLatitude,double userLongitude,double campLatitude,double campLongitude);

    public double calculateDistance(double latitude1,double longitude1,double latitude2,double longitude);

}
