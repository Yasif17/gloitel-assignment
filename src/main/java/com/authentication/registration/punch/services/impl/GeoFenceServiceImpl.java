package com.authentication.registration.punch.services.impl;

import com.authentication.registration.punch.services.GeoFenceService;
import org.springframework.stereotype.Service;

@Service
public class GeoFenceServiceImpl implements GeoFenceService {

    private final double EARTH_RADIUS = 6371000;// in meter
    private final double ALLOWED_DISTANCE = 300;

    @Override
    public boolean isWithinGeoFence(double userLatitude, double userLongitude, double campLatitude, double campLongitude) {
        double userCurrentDistance = calculateDistance(userLatitude,userLongitude,campLatitude,campLongitude);
        return userCurrentDistance <= ALLOWED_DISTANCE;
    }

    @Override
    public double calculateDistance(double latitude1, double longitude1, double latitude2, double longitude2) {
        double dLat = Math.toRadians(latitude2 - latitude1);
        double dLon = Math.toRadians(longitude2 - longitude1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(latitude1)) * Math.cos(Math.toRadians(latitude2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS * c; // distance in meters
    }
}
