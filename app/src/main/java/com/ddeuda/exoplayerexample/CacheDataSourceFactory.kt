package com.ddeuda.exoplayerexample

import android.content.Context
import com.google.android.exoplayer2.database.DatabaseProvider
import com.google.android.exoplayer2.database.ExoDatabaseProvider
import com.google.android.exoplayer2.database.StandaloneDatabaseProvider
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSource
import com.google.android.exoplayer2.upstream.FileDataSource
import com.google.android.exoplayer2.upstream.cache.CacheDataSink
import com.google.android.exoplayer2.upstream.cache.CacheDataSource
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor
import com.google.android.exoplayer2.upstream.cache.SimpleCache
import java.io.File

class CacheDataSourceFactory(
    private val context: Context,
    private val maxCacheSize: Long,
    private val maxFileSize: Long
) : DataSource.Factory {
    companion object {
        private var simpleCache: SimpleCache? = null

        fun getInstance(context: Context, maxCacheSize: Long): SimpleCache {
            if (simpleCache == null) {
                simpleCache = SimpleCache(
                    File(context.cacheDir, "media"),
                    LeastRecentlyUsedCacheEvictor(maxCacheSize),
                    StandaloneDatabaseProvider(context)
                )
            }
            return simpleCache as SimpleCache
        }
    }

    override fun createDataSource(): DataSource {
        return CacheDataSource(
            getInstance(context, maxCacheSize),
            getDefaultDataSource(),
            FileDataSource(),
            CacheDataSink(getInstance(context, maxCacheSize), maxFileSize),
            CacheDataSource.FLAG_BLOCK_ON_CACHE or CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR,
            null
        )
    }

    private fun getDefaultDataSource(): DefaultDataSource {
        return DefaultDataSource.Factory(context).createDataSource()
    }
}