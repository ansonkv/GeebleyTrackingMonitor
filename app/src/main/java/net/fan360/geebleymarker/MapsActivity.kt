package net.fan360.geebleymarker

import android.app.Activity
import android.support.v4.app.FragmentActivity
import android.os.Bundle
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.pubnub.api.PNConfiguration
import com.pubnub.api.PubNub

import net.fan360.geebleymarker.MapView.LocationHelper
import net.fan360.geebleymarker.MapView.LocationPublishMapAdapter
import net.fan360.geebleymarker.MapView.LocationPublishPnCallback
import net.fan360.geebleymarker.constants.AppConstants
import java.util.Arrays

class MapsActivity : FragmentActivity(), OnMapReadyCallback {
    private val TAG = "MapsActivity"
    private var mMap: GoogleMap? = null
    private var mPubnub_DataStream: PubNub? = null
    private var config: PNConfiguration? = null
    private val locationHelper: LocationHelper? = null
    private val userName: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        initPubNub()
    }

    private fun initPubNub() {
        this.config = PNConfiguration()

        config!!.publishKey = AppConstants.PUBNUB_PUBLISH_KEY
        config!!.subscribeKey = AppConstants.PUBNUB_SUBSCRIBE_KEY
        config!!.isSecure = false
        this.mPubnub_DataStream = PubNub(config)
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        this.mPubnub_DataStream!!.addListener(LocationPublishPnCallback(LocationPublishMapAdapter(this as Activity, mMap!!), AppConstants.CHANNEL_NAME))
        this.mPubnub_DataStream!!.subscribe().channels(Arrays.asList(AppConstants.CHANNEL_NAME)).execute()

    }


}
