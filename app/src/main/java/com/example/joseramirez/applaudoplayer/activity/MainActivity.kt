package com.example.joseramirez.applaudoplayer.activity

import android.annotation.TargetApi
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

    private lateinit var btnPlay: Button
    private lateinit var btnPause: Button
    private lateinit var btnDetails: Button

    private lateinit var audioService: MyAudioService
    private var bound: Boolean = false

    //Connection service
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
        btnPlay = findViewById(R.id.btn_play)
        btnPause = findViewById(R.id.btn_pause)
        btnDetails = findViewById(R.id.btn_details)

        createServiceIntent()
        // listener  for Play Music
        btnPlay.setOnClickListener {
            if (bound) {
                audioService.createPlayer()
            }
        }
        // listener  for Pause Music
        btnPause.setOnClickListener {
            if (bound) {
                audioService.pausePlayer()
            }
        }
        //Listener for new activity Details
        btnDetails.setOnClickListener {
            val intentDetails = Intent(this, DetailsActivity::class.java)
            startActivity(intentDetails)
        }
    }

    //Method intent for service
    private fun createServiceIntent(){
        Intent(this, MyAudioService::class.java).also { intent ->
            bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }
    }

    override fun onDestroy() {
        if (bound) {
            unbindService(connection)
        }
        super.onDestroy()
    }

}
