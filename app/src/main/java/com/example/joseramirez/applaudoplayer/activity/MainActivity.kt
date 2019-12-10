package com.example.joseramirez.applaudoplayer.activity

import android.annotation.TargetApi
import android.app.NotificationManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import com.example.joseramirez.applaudoplayer.R
import com.example.joseramirez.applaudoplayer.service.MyAudioService

@TargetApi(Build.VERSION_CODES.O)
class MainActivity : AppCompatActivity() {
    lateinit var notificationManager: NotificationManager

    lateinit var btnPlay: Button
    lateinit var btnPause: Button
    private lateinit var btnDetails: Button

    private lateinit var audioService: MyAudioService
    private var bound: Boolean = false


    private val connection = object : ServiceConnection {

        @RequiresApi(Build.VERSION_CODES.O)
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            val binder = service as MyAudioService.LocalBinder
            audioService = binder.getService()
            bound = true
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            bound = false
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnPlay = findViewById(R.id.btnPlay)
        btnPause = findViewById(R.id.btnPause)
        btnDetails = findViewById(R.id.detailsbtn)

        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        Intent(this, MyAudioService::class.java).also { intent ->
            bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }

        btnPlay.setOnClickListener {
            if (bound) {
                audioService.createPlayer()
            }
        }

        btnPause.setOnClickListener {
            if (bound) {
                audioService.pausePlayer()
            }
        }

        btnDetails.setOnClickListener {
            val intentDetails = Intent(this, DetailsActivity::class.java)
            startActivity(intentDetails)
        }
    }

    override fun onDestroy() {
        if (bound) {
            unbindService(connection)
        }
        super.onDestroy()
    }


}
