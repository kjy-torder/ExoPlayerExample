package com.ddeuda.exoplayerexample

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.exoplayer2.DefaultRenderersFactory
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.database.ExoDatabaseProvider
import com.google.android.exoplayer2.database.StandaloneDatabaseProvider
import com.google.android.exoplayer2.offline.DownloadHelper
import com.google.android.exoplayer2.offline.DownloadRequest
import com.google.android.exoplayer2.offline.DownloadService
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.upstream.cache.CacheDataSource
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor
import com.google.android.exoplayer2.upstream.cache.NoOpCacheEvictor
import com.google.android.exoplayer2.upstream.cache.SimpleCache
import java.io.File
import java.net.URI

class MainViewModel : ViewModel() {
    companion object {

    }

    private val tag = "MainViewModel"
    private var exoPlayer: ExoPlayer? = null
    val source = "https://www.learningcontainer.com/wp-content/uploads/2020/05/sample-mp4-file.mp4"

    override fun onCleared() {
        Log.e(tag, "onCleared")

        exoPlayer?.apply {
            release()
            exoPlayer = null
        }
        super.onCleared()
    }

    fun getPlayer() = exoPlayer

    fun initPlayer(context: Context) {
        Log.e(tag, "initPlayer")

        if (exoPlayer == null) {
            val request = DownloadRequest.Builder(source, Uri.parse(source)).build()

//            DownloadService.start(context, MediaDownloadService::class.java)
//            DownloadService.startForeground(context, MediaDownloadService::class.java)
//            DownloadService.sendAddDownload(context, MediaDownloadService::class.java, request, false)

            val cacheSourceFactory = Cache(context)
            val mediaItem = MediaItem.fromUri(Uri.parse(source))
            val factory = ProgressiveMediaSource.Factory(cacheSourceFactory.cacheDataSourceFactory)
            val mediaSource = factory.createMediaSource(mediaItem)
            exoPlayer = ExoPlayer.Builder(context).build()
                .apply { setMediaSource(mediaSource) }
//            exoPlayer?.setMediaSource(mediaSource)
        }
    }

    fun showCacheDir(v: View) {
        File("${v.context.cacheDir}/media/exo/4").listFiles().forEach {
            Log.e(tag, "$it, ${it.length()}")
        }

        v.context.externalCacheDir?.listFiles()?.forEach {
            Log.e(tag, "$it, ${it.length()}")
        }
    }
}