package com.ddeuda.exoplayerexample

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.exoplayer2.DefaultRenderersFactory
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.database.ExoDatabaseProvider
import com.google.android.exoplayer2.database.StandaloneDatabaseProvider
import com.google.android.exoplayer2.offline.DownloadHelper
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.upstream.cache.CacheDataSource
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor
import com.google.android.exoplayer2.upstream.cache.NoOpCacheEvictor
import com.google.android.exoplayer2.upstream.cache.SimpleCache
import java.io.File

class MainViewModel : ViewModel() {
    companion object {
        private var cache: SimpleCache? = null
        private var leastRecentlyUsedCacheEvictor: LeastRecentlyUsedCacheEvictor? = null
        private var exoDatabaseProvider: ExoDatabaseProvider? = null
        private val cacheSize: Long = 512.megaByte()
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

        val cacheDataSourceFactory = CacheDataSourceFactory(
            context,
            1.gigaByte(),
            512.megaByte()
        )

        if (exoPlayer == null) {
            val c =
                CacheDataSource.Factory()
                    .setCache(CacheDataSourceFactory.getInstance(context, 256.megaByte()))
                    .setUpstreamDataSourceFactory(DefaultHttpDataSource.Factory())

            exoPlayer = ExoPlayer.Builder(context).setMediaSourceFactory(
                DefaultMediaSourceFactory(c)
            ).build()
            exoPlayer?.setMediaSource(
                ProgressiveMediaSource.Factory(c).createMediaSource(
                    MediaItem.fromUri(
                        Uri.parse(source)
                    )
                )
            )
        }
    }
}