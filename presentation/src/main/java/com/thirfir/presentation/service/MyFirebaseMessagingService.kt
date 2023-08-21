package com.thirfir.presentation.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.thirfir.domain.usecase.SettingsUseCase
import com.thirfir.presentation.R
import com.thirfir.presentation.view.post.PostActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class MyFirebaseMessagingService : FirebaseMessagingService() {

    @Inject
    lateinit var useCase: SettingsUseCase

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        runBlocking {
            val settings = useCase.getSettings()
                .stateIn(
                    scope = CoroutineScope(Dispatchers.IO)
                ).value

            if ( settings.allowNotification ) {
                check(remoteMessage)
            }
        }
    }

    private fun check(remoteMessage: RemoteMessage) {
        val currentPostID = remoteMessage.data["post_id"]
        val prefNotice = this.getSharedPreferences("lastPostID", Context.MODE_PRIVATE)
        val lastPostID = prefNotice.getString("lastPostID", "null")

        if(lastPostID != currentPostID ) {
            if(remoteMessage.data.isNotEmpty()){
                sendNotification(remoteMessage)
            }

            val editor = prefNotice.edit()
            editor.putString("lastPostID", remoteMessage.data["post_id"]).apply()
            editor.commit()
        }
    }

    private fun sendNotification(remoteMessage: RemoteMessage) {
        val uniId: Int = (System.currentTimeMillis() / 7).toInt()
        val intent = Intent(this, PostActivity::class.java)
        intent.apply {
            putExtra("bulletin", remoteMessage.data["bulletin"]) // 게시판 번호 intent로 전달 String
            putExtra("post_id", remoteMessage.data["post_id"]) // 포스트 고유번호 intent로 전달 String

            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
        val pendingIntent = PendingIntent.getActivity(this, uniId, intent, PendingIntent.FLAG_IMMUTABLE)

        val channelId = getString(R.string.firebase_notification_channel_id)
        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_settings_36)
            .setContentTitle(remoteMessage.notification!!.title)
            .setContentText(remoteMessage.notification!!.body)
            .setAutoCancel(true)
            .setSound(soundUri)
            .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if ( notificationManager.getNotificationChannel(channelId) == null ) {
            val channel = NotificationChannel(channelId, "구독 공지 알림", NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(uniId, notificationBuilder.build())
    }

    override fun onDestroy() {
        super.onDestroy()

    }
}