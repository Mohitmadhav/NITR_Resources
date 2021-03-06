package github.sachin2dehury.nitrresources.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import github.sachin2dehury.nitrresources.R
import github.sachin2dehury.nitrresources.component.AppCore
import github.sachin2dehury.nitrresources.component.AppJobs
import github.sachin2dehury.nitrresources.dialog.RenameDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AppUploadService : Service() {

    private lateinit var files: ArrayList<String>
    private var item = 0
    private var common = false

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val channel = "NITR Resources Upload Service"
        val notificationManager = NotificationManagerCompat.from(this)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channel, channel, NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.WHITE
            notificationChannel.enableVibration(false)
            notificationManager.createNotificationChannel(notificationChannel)
        }

        val notification = NotificationCompat.Builder(this, channel).apply {
            priority = NotificationCompat.PRIORITY_DEFAULT
            setSmallIcon(R.mipmap.ic_launcher)
            setContentTitle("Uploading Files")
            setProgress(100, 0, false)
        }

        notificationManager.notify(AppCore.REQUEST_CODE_UPLOAD_SERVICE, notification.build())

        CoroutineScope(Dispatchers.IO).launch {
            intent!!.apply {
                files = getStringArrayListExtra("Files")!!
                item = getIntExtra("Index", 0)
                common = getBooleanExtra("isCommon", false)

            }
            AppJobs.uploadDoc(files, RenameDialog.doc, item, common).invokeOnCompletion {
                notificationManager.cancel(AppCore.REQUEST_CODE_UPLOAD_SERVICE)
            }
        }.invokeOnCompletion {
            stopSelf()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        val intent = Intent(this, AppUploadService::class.java).apply {
            putStringArrayListExtra("Files", files)
            putExtra("Index", item)
        }
        startService(intent)
        super.onTaskRemoved(rootIntent)
    }
}