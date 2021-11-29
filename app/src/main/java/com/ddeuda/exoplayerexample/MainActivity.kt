package com.ddeuda.exoplayerexample

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.ddeuda.exoplayerexample.CacheDataSourceFactory.Companion.getInstance
import com.ddeuda.exoplayerexample.databinding.ActivityMainBinding
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.database.DatabaseProvider
import com.google.android.exoplayer2.database.ExoDatabaseProvider
import com.google.android.exoplayer2.database.StandaloneDatabaseProvider
import com.google.android.exoplayer2.offline.DownloadHelper.createMediaSource
import com.google.android.exoplayer2.offline.DownloadManager
import com.google.android.exoplayer2.offline.DownloadRequest
import com.google.android.exoplayer2.offline.DownloadService
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.upstream.cache.CacheDataSource
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor
import com.google.android.exoplayer2.upstream.cache.NoOpCacheEvictor
import com.google.android.exoplayer2.upstream.cache.SimpleCache
import com.google.android.exoplayer2.util.Util
import java.io.File
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {
    private val tag = "MainActivity"
    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var downloadManager: DownloadManager

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.e(tag, "onCreate")

        super.onCreate(savedInstanceState)

        mainViewModel = ViewModelProvider(this@MainActivity).get(MainViewModel::class.java)
        binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
            .apply {
                lifecycleOwner = this@MainActivity

                viewModel = mainViewModel.apply {
                    initPlayer(context = this@MainActivity)
                }

                playerView.apply {
                    player = mainViewModel.getPlayer()
                }
            }
    }

    override fun onStart() {
        Log.e(tag, "onStart")

        super.onStart()
    }

    override fun onRestart() {
        Log.e(tag, "onRestart")

        super.onRestart()
    }

    override fun onResume() {
        Log.e(tag, "onResume")

        super.onResume()
        mainViewModel.getPlayer()?.apply {
            prepare()
            play()
        }
    }

    override fun onPause() {
        Log.e(tag, "onPause")

        mainViewModel.getPlayer()?.apply {
            pause()
        }
        super.onPause()
    }

    override fun onStop() {
        Log.e(tag, "onStop")

        mainViewModel.getPlayer()?.apply {
            stop()
        }
        super.onStop()
    }

    override fun onDestroy() {
        Log.e(tag, "onDestroy")

        super.onDestroy()
    }
}