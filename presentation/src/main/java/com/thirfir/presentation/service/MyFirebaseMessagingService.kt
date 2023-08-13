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
import com.thirfir.presentation.view.post.PostListActivity
import com.thirfir.presentation.R

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        val currentBody = remoteMessage.data["body"]
        val prefNotice = this.getSharedPreferences("lastBody", Context.MODE_PRIVATE)
        val lastBody = prefNotice.getString("lastBody", "null")

        // 여러개의 키워드가 공지 하나의 타이틀에 모두 포함되어 있을경우 한번만 알림
        if(currentBody != lastBody) {
            if(remoteMessage.data.isNotEmpty()){
                sendNotification(remoteMessage)
            }

            val editor = prefNotice.edit()
            editor.putString("lastBody", remoteMessage.data["body"]).apply()
            editor.commit()
        }
    }

    private fun sendNotification(remoteMessage: RemoteMessage) {
        val uniId: Int = (System.currentTimeMillis() / 7).toInt()
        val intent = Intent(this, PostListActivity::class.java)
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
            .setContentTitle("공지사항")
            .setContentText(remoteMessage.data["title"])
            .setAutoCancel(true)
            .setSound(soundUri)
            .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if ( notificationManager.getNotificationChannel(channelId) == null ) {
            val channel = NotificationChannel(channelId, "Notice", NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(uniId, notificationBuilder.build())
    }
}