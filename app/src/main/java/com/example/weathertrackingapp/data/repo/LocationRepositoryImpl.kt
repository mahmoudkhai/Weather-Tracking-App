//package com.example.weathertrackingapp.data.repo
//
//import android.content.Context
//import android.location.Location
//import com.example.weathertrackingapp.domain.LocationRepository
//
//internal class LocationRepositoryImpl() : LocationRepository {
//
//    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
//}
//
//override fun getLocation(context: Context): Location? {
//    // Implementation to get the location
//    if (ActivityCompat.checkSelfPermission(
//            context,
//            Manifest.permission.ACCESS_FINE_LOCATION
//        ) == PackageManager.PERMISSION_GRANTED
//    ) {
//        return null
//    }
//}