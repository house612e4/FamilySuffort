package com.mtstudio.familysupport

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.Path
import android.os.Build
import android.view.accessibility.AccessibilityEvent
import androidx.core.app.NotificationCompat
import io.socket.client.IO
import io.socket.client.Socket
import org.json.JSONObject

class FamilySupportService : AccessibilityService() {

    private lateinit var mSocket: Socket
    private val SERVER_URL = "https://mt-universal-hub.onrender.com"
    private val SECRET_KEY = "MT_STUDIO_2026"
    private val CHANNEL_ID = "family_support_channel"
    private val NOTIFICATION_ID = 1001

    override fun onServiceConnected() {
        super.onServiceConnected()
        showPersistentNotification()
        connectToServer()
    }

    private fun showPersistentNotification() {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Family Support",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "পরিবারের সদস্য এই ডিভাইস দেখছেন"
            }
            notificationManager.createNotificationChannel(channel)
        }

        val stopIntent = Intent(this, StopServiceReceiver::class.java)
        val stopPendingIntent = PendingIntent.getBroadcast(
            this, 0, stopIntent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("🔴 রিমোট সাপোর্ট চলছে")
            .setContentText("আপনার পরিবারের সদস্য এই ফোন দেখছেন ও সাহায্য করছেন")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setOngoing(true)
            .addAction(android.R.drawable.ic_delete, "সংযোগ বন্ধ করুন", stopPendingIntent)
            .build()

        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    private fun connectToServer() {
        val options = IO.Options().apply {
            auth = mapOf("token" to SECRET_KEY)
        }
        mSocket = IO.socket(SERVER_URL, options)
        mSocket.connect()

        mSocket.on("child-execute-command") { args ->
            if (args.isNotEmpty()) {
                val cmd = args[0] as JSONObject
                val action = cmd.getString("action")
                val percentX = cmd.getDouble("x")
                val percentY = cmd.getDouble("y")

                val displayMetrics = resources.displayMetrics
                val actualX = (percentX * displayMetrics.widthPixels).toFloat()
                val actualY = (percentY * displayMetrics.heightPixels).toFloat()

                if (action == "click") {
                    simulateClick(actualX, actualY)
                }
            }
        }
    }

    private fun simulateClick(x: Float, y: Float) {
        val p = Path().apply { moveTo(x, y) }
        val gestureBuilder = GestureDescription.Builder()
        gestureBuilder.addStroke(GestureDescription.StrokeDescription(p, 0, 50))
        dispatchGesture(gestureBuilder.build(), null, null)
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {}
    override fun onInterrupt() {}

    override fun onDestroy() {
        super.onDestroy()
        if (::mSocket.isInitialized) mSocket.disconnect()
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(NOTIFICATION_ID)
    }
}
