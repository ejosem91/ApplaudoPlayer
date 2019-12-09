@file:Suppress("DEPRECATION")

package com.example.joseramirez.applaudoplayer

import android.app.*
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.support.annotation.RequiresApi
import android.support.v4.app.NotificationCompat


class MyAudioService : Service() {

    private val packageNameProject = "com.example.joseramirez.applaudoplayer"
    private val binder = LocalBinder()
    private var mediaPlayer: MediaPlayer? = null

    //notifications declarations
    private var notificationManager : NotificationManager? = null

    inner class LocalBinder : Binder() {
        // Return this instance of LocalService so clients can call public methods
        fun getService(): MyAudioService = this@MyAudioService
    }

    override fun onBind(intent: Intent): IBinder? {
        return binder
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_NOT_STICKY
    }

    @RequiresApi(Build.VERSION_CODES.O)
    //Initializer my Media Player
    fun createPlayer() {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(this, R.raw.eminem)
            notificationManager= getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            createNotificationChannel(packageNameProject, "Listen Music", "Playing Song")
        }
        mediaPlayer?.start()
    }

    fun pausePlayer() {
        mediaPlayer?.pause()
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(id : String , name : String , description: String) {
        val notification = NotificationCompat.Builder(this)
            .setContentTitle(name)
            .setContentText(description)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setChannelId(id)
            .setPriority(NotificationManager.IMPORTANCE_DEFAULT)
            .build()
        notificationManager?.notify(101, notification)
    }

}
