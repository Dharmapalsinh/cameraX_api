package com.example.videoplayer

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.dharmapal.camerax_api.databinding.ActivityMain2Binding
import com.dharmapal.camerax_api.databinding.ActivityMainBinding


class MainActivity2 : AppCompatActivity() {

    private lateinit var binding: ActivityMain2Binding
    private lateinit var videoView: VideoView
    var handler: Handler? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        handler = Handler()

        videoView = binding.videoView
        val uri=intent.getStringExtra("uri")
        Log.d("tagabc",uri.toString())
        videoView.setVideoURI(uri!!.toUri())
        updateSeekBarProgress();

        binding.button.setOnClickListener {
            finish()
        }


        binding.ibPlay.setOnClickListener {
            play()
        }
        binding.ibPause.setOnClickListener {
            pause()
        }
        binding.ibForward.setOnClickListener {
            forward_10_sec()
        }
        binding.ibReply.setOnClickListener {
            reply_10_sec()
        }

        binding.seekBar.setOnSeekBarChangeListener(onSeekBarChangeListener);

//        binding.button.setOnClickListener {
//
//            var intent = Intent(Intent.ACTION_GET_CONTENT,MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
//            startActivityForResult(intent, 101)
//
//        }

    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        if (resultCode == Activity.RESULT_OK && data != null) {
//            if (requestCode == 101) {
//
//                var uri: Uri
//                var selectImage  = data.data!!
//
//                if (selectImage != null) {
//
//
//
//
//                }
//            }
//        }
//    }

    private fun pause() {
        if(videoView.isPlaying) {
            videoView.pause()
            binding.ibPause.visibility = View.INVISIBLE
            binding.ibPlay.visibility = View.VISIBLE
        }
    }

    private fun play() {
        if(!videoView.isPlaying)
        {
            videoView.start()
            binding.ibPlay.visibility = View.INVISIBLE
            binding.ibPause.visibility = View.VISIBLE
        }

    }

    private fun forward_10_sec() {
        if (videoView.canSeekForward())
            videoView.seekTo(videoView.currentPosition + 5000)

    }

    private fun reply_10_sec() {
        if (videoView.canSeekBackward())
            videoView.seekTo(videoView.currentPosition - 5000)
    }

    private fun updateSeekBarProgress() {
        this.runOnUiThread(updateSeekBar)
    }

    private val updateSeekBar: Runnable = object : Runnable {
        override fun run() {
            binding.seekBar.max = videoView.duration / 1000
            binding.seekBar.progress = videoView.currentPosition / 1000
            binding.seekBar.postDelayed(this, 500)

            val min = binding.seekBar.max /60
            val sec = binding.seekBar.max % 60

            val cMin =  binding.seekBar.progress / 60
            val cSec =  binding.seekBar.progress % 60

            Log.d("time", "${min.toString()}, ${sec.toString()}")

            binding.tvDue.text = "$cMin : $cSec / $min : $sec "

        }
    }


    private var onSeekBarChangeListener: OnSeekBarChangeListener = object : OnSeekBarChangeListener {
        override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
            if (b) {
                videoView.seekTo(i * 1000)
            }
        }

        override fun onStartTrackingTouch(seekBar: SeekBar) {
            handler?.removeCallbacks(updateSeekBar)
            pause()
        }

        override fun onStopTrackingTouch(seekBar: SeekBar) {
            videoView.seekTo(seekBar.progress * 1000)
            play()
            updateSeekBarProgress()
        }
    }

}