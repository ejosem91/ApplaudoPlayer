package com.example.joseramirez.applaudoplayer

import android.content.res.Configuration
import android.media.MediaPlayer
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button

class MainActivity : AppCompatActivity() {

    private lateinit var mediaPlayer: MediaPlayer
    lateinit var btnPlay    : Button
    lateinit var btnPause   : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnPlay = findViewById(R.id.btnPlay)
        btnPause = findViewById(R.id.btnPause)

        mediaPlayer = MediaPlayer.create(this ,R.raw.eminem)

        btnPlay.setOnClickListener {
            mediaPlayer.start()
        }

        btnPause.setOnClickListener {
            mediaPlayer.pause()
        }
    }
    




}
