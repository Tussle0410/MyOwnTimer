package com.tussle.myowntimer.service

import android.app.Service
import android.content.Intent
import android.os.IBinder

class ForcedTerminationService : Service() {
    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        stopSelf()
    }
}