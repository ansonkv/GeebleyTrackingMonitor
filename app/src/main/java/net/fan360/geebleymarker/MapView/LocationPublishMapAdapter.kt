package net.fan360.geebleymarker.MapView

import android.app.Activity
import android.util.Log

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class LocationPublishMapAdapter(private val activity: Activity, private val map: GoogleMap) {

    private var marker: Marker? = null

    fun locationUpdated(newLocation: Map<String, String>) {
        if (newLocation.containsKey("latitude") && newLocation.containsKey("longitude")) {
            val lat = newLocation["latitude"]
            val lng = newLocation["longitude"]

            doUiUpdate(LatLng(java.lang.Double.parseDouble(lat), java.lang.Double.parseDouble(lng)))
        } else {
            Log.w(TAG, "message ignored: " + newLocation.toString())
        }
    }

    private fun doUiUpdate(location: LatLng) {
        activity.runOnUiThread {
            if (marker != null) {
                marker!!.setPosition(location)
            } else {
                marker = map.addMarker(MarkerOptions().position(location))
            }
            map.moveCamera(CameraUpdateFactory.newLatLng(location))
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(location.latitude, location.longitude), 18.0f))
        }
    }

    companion object {
        private val TAG = "LocationPublishMapAdapter"
    }
}
