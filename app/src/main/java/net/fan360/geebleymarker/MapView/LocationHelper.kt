package net.fan360.geebleymarker.MapView


import android.content.Context
import android.location.Location
import android.os.Bundle
import android.util.Log

import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices

import net.fan360.geebleymarker.util.JsonUtil

class LocationHelper(context: Context, private val mLocationListener: LocationListener) : GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private val mGoogleApiClient: GoogleApiClient

    init {
        this.mGoogleApiClient = GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build()

        this.mGoogleApiClient.connect()
    }

    fun connect() {
        this.mGoogleApiClient.connect()
    }

    fun disconnect() {
        this.mGoogleApiClient.disconnect()
    }

    override fun onConnected(bundle: Bundle?) {
        try {
            val lastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient)

            if (lastLocation != null) {
                onLocationChanged(lastLocation)
            }
        } catch (e: SecurityException) {
            Log.v("locationDenied", e.message)
        }

        try {
            val locationRequest = LocationRequest.create().setInterval(5000)

            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locationRequest, this)
        } catch (e: SecurityException) {
            Log.v("locationDenied", e.message)
        }

    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {
        Log.v("googlePlayDenied", connectionResult.toString())
    }

    override fun onLocationChanged(location: Location) {
        try {
            Log.v("locationChanged", JsonUtil.asJson(location))
        } catch (e: Exception) {
            throw RuntimeException(e)
        }

        mLocationListener.onLocationChanged(location)
    }

    override fun onConnectionSuspended(i: Int) {
        mLocationListener.onLocationChanged(null)
    }
}