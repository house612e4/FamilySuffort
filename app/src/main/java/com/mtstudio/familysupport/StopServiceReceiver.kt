package com.mtstudio.familysupport

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Settings

class StopServiceReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        // Accessibility Settings এ নিয়ে যাবে যেখানে user নিজে service বন্ধ করতে পারবে
        val settingsIntent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(settingsIntent)
    }
}
