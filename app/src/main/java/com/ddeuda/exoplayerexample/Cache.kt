package com.ddeuda.exoplayerexample

import android.content.Context
import android.util.Log
import com.google.android.exoplayer2.database.ExoDatabaseProvider
import com.google.android.exoplayer2.database.StandaloneDatabaseProvider
import com.google.android.exoplayer2.upstream.DefaultDataSource
import com.google.android.exoplayer2.upstream.FileDataSource
import com.google.android.exoplayer2.upstream.cache.CacheDataSink
import com.google.android.exoplayer2.upstream.cache.CacheDataSource
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor
import com.google.android.exoplayer2.upstream.cache.SimpleCache
import java.io.File

class Cache(context: Context) {
    companion object {
        private var cache: SimpleCache? = null

        fun cacheInstance(context: Context): SimpleCache {
            if(cache == null) {
                val exoCacheDir = File("${context.cacheDir}/media", "exo")
                val evictor = LeastRecentlyUsedCacheEvictor(1000000000)
                cache = SimpleCache(exoCacheDir, evictor, StandaloneDatabaseProvider(context))
            }
            return cache as SimpleCache
        }

    }

    private val upstreamDataSourceFactory = DefaultDataSource.Factory(context)

    val cacheDataSourceFactory = CacheDataSource.Factory()
            .setCache(cacheInstance(context))
            .setUpstreamDataSourceFactory(upstreamDataSourceFactory)
            .setCacheReadDataSourceFactory(FileDataSource.Factory())
            .setCacheWriteDataSinkFactory(
                CacheDataSink.Factory()
                    .setCache(cacheInstance(context))
                    .setFragmentSize(CacheDataSink.DEFAULT_FRAGMENT_SIZE)
            )
            .setFlags(CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR or CacheDataSource.FLAG_BLOCK_ON_CACHE)
            .setEventListener(object : CacheDataSource.EventListener {
                override fun onCachedBytesRead(cacheSizeBytes: Long, cachedBytesRead: Long) {
                    Log.e("Cache", "onCachedBytesRead. cacheSizeBytes:$cacheSizeBytes, cachedBytesRead: $cachedBytesRead")
                }

                override fun onCacheIgnored(reason: Int) {
                    Log.e("Cache", "onCacheIgnored. reason:$reason")
                }
            })

}