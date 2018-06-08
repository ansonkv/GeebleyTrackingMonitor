package net.fan360.geebleymarker.MapView

import android.util.Log

import com.pubnub.api.PubNub
import com.pubnub.api.callbacks.SubscribeCallback
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult

import net.fan360.geebleymarker.util.JsonUtil

import java.util.LinkedHashMap

class LocationPublishPnCallback(private val locationMapAdapter: LocationPublishMapAdapter, private val watchChannel: String) : SubscribeCallback() {

    override fun status(pubnub: PubNub, status: PNStatus) {
        Log.d(TAG, "status: " + status.toString())
    }

    override fun message(pubnub: PubNub, message: PNMessageResult) {
        if (message.channel != watchChannel) {
            return
        }

        try {
            Log.d(TAG, "message: " + message.toString())

            val newLocation = JsonUtil.fromJson<LinkedHashMap<*, *>>(message.message.toString(), LinkedHashMap<*, *>::class)
            locationMapAdapter.locationUpdated(newLocation)
        } catch (e: Exception) {
            throw RuntimeException(e)
        }

    }

    override fun presence(pubnub: PubNub, presence: PNPresenceEventResult) {
        if (presence.channel != watchChannel) {
            return
        }

        Log.d(TAG, "presence: " + presence.toString())
    }

    companion object {
        private val TAG = "LocationPublishPnCallback"
    }
}
