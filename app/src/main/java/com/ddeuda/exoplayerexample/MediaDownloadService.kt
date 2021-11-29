package com.ddeuda.exoplayerexample

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.content.Context
import com.google.android.exoplayer2.database.StandaloneDatabaseProvider
import com.google.android.exoplayer2.offline.Download
import com.google.android.exoplayer2.offline.DownloadManager
import com.google.android.exoplayer2.offline.DownloadService
import com.google.android.exoplayer2.scheduler.PlatformScheduler
import com.google.android.exoplayer2.scheduler.Scheduler
import com.google.android.exoplayer2.ui.DownloadNotificationHelper
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.upstream.cache.NoOpCacheEvictor
import com.google.android.exoplayer2.upstream.cache.SimpleCache
import com.google.android.exoplayer2.util.NotificationUtil
import java.io.File
import java.lang.Exception
import java.util.concurrent.Executors

class MediaDownloadService : DownloadService(
    FOREGROUND_NOTIFICATION_ID,
    DEFAULT_FOREGROUND_NOTIFICATION_UPDATE_INTERVAL
) {
    companion object {
        const val JOB_ID = 1
        const val FOREGROUND_NOTIFICATION_ID = 1
    }

    override fun getDownloadManager(): DownloadManager {
        val databaseProvider = StandaloneDatabaseProvider(this)
        val downloadCache = Cache.cacheInstance(context = this)

        val dataSourceFactory = DefaultHttpDataSource.Factory()
        val downloadExecutor = Runnable::run

        val downloadNotificationHelper = DownloadNotificationHelper(
            this,
            "sadasdasd",
        )
        val downloadManager = DownloadManager(
            this,
            databaseProvider,
            downloadCache,
            dataSourceFactory,
            downloadExecutor
        )

        downloadManager.addListener(
            TerminalStateNotificationHelper(
                this@MediaDownloadService,
                downloadNotificationHelper,
                FOREGROUND_NOTIFICATION_ID + 1
            )
        )

        return downloadManager
    }

    override fun getScheduler(): Scheduler {
        return PlatformScheduler(this, JOB_ID)
    }

    override fun getForegroundNotification(
        downloads: MutableList<Download>,
        notMetRequirements: Int
    ): Notification {
        return DownloadNotificationHelper(this, "미디어 다운로드 서비스")
            .buildProgressNotification(
                this,
                R.drawable.ic_launcher_foreground,
                null,
                null,
                downloads,
                notMetRequirements
            )
    }
}

class TerminalStateNotificationHelper(
    private val context: Context,
    private val notificationHelper: DownloadNotificationHelper,
    firstNotificationId: Int
) : DownloadManager.Listener {

    private var nextNotificationId: Int = firstNotificationId

    @SuppressLint("WrongConstant")
    override fun onDownloadChanged(
        downloadManager: DownloadManager,
        download: Download,
        finalException: Exception?
    ) {
//        super.onDownloadChanged(downloadManager, download, finalException)
        val notification: Notification = when (download.state) {
            Download.STATE_COMPLETED -> {
                notificationHelper.buildDownloadCompletedNotification(
                    context,
                    R.drawable.ic_launcher_foreground,
                    null,
                    "성공"
                )
            }
            Download.STATE_FAILED -> {
                notificationHelper.buildDownloadCompletedNotification(
                    context,
                    R.drawable.ic_launcher_foreground,
                    null,
                    "실패"
                )
            }
            else -> {
                return
            }
        }
        NotificationUtil.setNotification(context, nextNotificationId++, notification)
    }
}
