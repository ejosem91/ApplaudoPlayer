@file:Suppress("DEPRECATION")

package com.example.joseramirez.applaudoplayer.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.MediaPlayer
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.support.annotation.RequiresApi
import android.support.v4.app.NotificationCompat
import com.example.joseramirez.applaudoplayer.R

class MyAudioService : Service() {

    /** TODO: After migrating to androidx, use NotificationManagerCompat
     * We need to avoid targeting specific versions (@RequiresApi(Build.VERSION_CODES.O))
     * or in the worst case, provide an alternate scenario.
     */
    private lateinit var notificationManager: NotificationManager
    private val binder = LocalBinder()
    private var mediaPlayer: MediaPlayer? = null

    //notifications declarations
    inner class LocalBinder : Binder() {
        // Return this instance of LocalService so clients can call public methods
        fun getService(): MyAudioService = this@MyAudioService
    }

    override fun onBind(intent: Intent): IBinder? {
        return binder
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val flagIntent = intent?.action

        if (flagIntent.equals(PAUSE)) {
            pausePlayer()
        } else if (flagIntent.equals(PLAY)) {
            createPlayer()
        }

        return START_NOT_STICKY
    }

    @RequiresApi(Build.VERSION_CODES.O)
    //Initializer my Media Player
    fun createPlayer() {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(this, R.raw.eminem)
            notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            setConfigsNotify()
            createNotificationChannel(
                CHANNEL_ID,
                ACTION,
                DESCRIPTION
            )
        }
        mediaPlayer?.start()
    }

    fun pausePlayer() {
        mediaPlayer?.pause()
    }

    private fun setConfigsNotify() {

        val playIntent = Intent(this, this::class.java).apply {
            action = PLAY
        }

        val playPendingIntent: PendingIntent =
            PendingIntent.getService(this, 0, playIntent, 0)

        val pauseIntent = Intent(this, this::class.java).apply {
            action = PAUSE
        }

        val pausePendingIntent: PendingIntent =
            PendingIntent.getService(this, 0, pauseIntent, 0)


        val builder = NotificationCompat.Builder(this,
            CHANNEL_ID
        )
            .setSmallIcon(R.drawable.ic_info)
            .setContentTitle(NAME)
            .setContentText(SONG)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(playPendingIntent)
            .addAction(R.drawable.ic_play, getString(R.string.play), playPendingIntent)
            .addAction(R.drawable.ic_pause, getString(R.string.pause), pausePendingIntent)
        notificationManager.notify(NOTIFICATION_ID, builder.build())

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(
        id: String?,
        name: String?,
        description: String?
    ) {
        val importance = NotificationManager.IMPORTANCE_LOW
        val channel = NotificationChannel(id, name, importance)
        channel.description = description
        channel.enableLights(true)
        channel.lightColor = Color.RED
        channel.enableVibration(true)
        channel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
        notificationManager.createNotificationChannel(channel)
    }

    companion object {
        const val NOTIFICATION_ID = 20
        const val DESCRIPTION = "Play"
        const val ACTION = "Notify"
        const val SONG = "Lose your self"
        const val NAME = "APPLAUDOFY"
        const val CHANNEL_ID = "channelPlayer"
        const val PAUSE = "PAUSE"
        const val PLAY = "PLAY"
    }

}
