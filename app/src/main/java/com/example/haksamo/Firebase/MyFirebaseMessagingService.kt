package com.example.haksamo.Firebase


import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.haksamo.MainActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

/** 푸시 알림으로 보낼 수 있는 메세지는 2가지
 * 1. Notification: 앱이 실행중(포그라운드)일 떄만 푸시 알림이 옴
 * 2. Data: 실행중이거나 백그라운드(앱이 실행중이지 않을때) 알림이 옴 -> TODO: 대부분 사용하는 방식 */
class MyFirebaseMessagingService : FirebaseMessagingService() {

    private val TAG = "로그"

    // Firebase가 해당 디바이스에 새로운 토큰을 부여할 때마다 자동으로 호출되는 함수
    // Token은 주로 최초 설치하거나, 데이터를 삭제하고 앱에 진입하게 되면 발급
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG, "Refreshed token: $token")

        // 토큰 값을 따로 저장
        //val pref = this.getSharedPreferences("token", Context.MODE_PRIVATE)
        //val editor = pref.edit()
        //editor.putString("token", token).apply()
        //editor.commit()
    }

    // 서버에서 토큰을 이용해 POST 요청 메시지를 보내면, Firebase 서버에서 디바이스로 메시지를 전송.
    //메시지 받을 때마다 자동으로 호출되는 함수 , 백그라운드 알림 (data 타입)
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        //받은 remoteMessage의 값 출력해보기. 데이터메세지 / 알림메세지
        Log.d(TAG, "Message data : ${remoteMessage.data}")
        Log.d(TAG, "Message notification : ${remoteMessage.notification}")
        //Log.d(TAG, "Message notification ttt: ${remoteMessage.notification!!.title}, ${remoteMessage.notification!!.title}"
        //

        if (isNotificationPermissionGranted()) {
            // 알림 권한이 허용된 경우에만 알림을 보냅니다.
            if (remoteMessage.data != null) {
                sendNotification(remoteMessage)
            } else {
                Log.d(TAG, "수신 에러: Notification이 비어있습니다.")
            }
        } else {
            Log.d(TAG, "알림 권한이 거부되었습니다.")
        }
    }

    private fun sendNotification(remoteMessage: RemoteMessage) {
        Log.d(TAG, "sendNotification 함수 실행됨")

        // 데이터 페이로드에서 title과 body 추출
        val dataPayload = remoteMessage.data
        val title = dataPayload["title"]
        val body = dataPayload["body"]


        // 알림을 클릭하면 해당 화면으로 이동
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) // 이 액티비티를 최상위로 하여 이동
        val pendingIntent = PendingIntent.getActivity(
            this, 1, intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_MUTABLE
        )

        // FCM 메시지의 데이터를 모두 가져와서 액티비티로 전달
        for (key in remoteMessage.data.keys) {
            intent.putExtra(key, remoteMessage.data.getValue(key))
        }

        //channel 설정
        val channelId = "500"      // 알림 채널 이름
        val channelName = "MainChannel"
        val channelDescription = "channelDescription"
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // 알램 채널 생성 (API 26부터 - 오레오 버전 이후)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH // 중요도를 HIGH로 설정
            ).apply {
                description = channelDescription
            }
            notificationManager.createNotificationChannel(channel)
        }

        // 알림 소리
        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        // 알림 생성
        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle(title) // 제목
            .setContentText(body)   // 내용
            .setSmallIcon(android.R.drawable.ic_dialog_info)    // 아이콘 설정
            .setAutoCancel(true) // 클릭시 삭제
            .setPriority(NotificationCompat.PRIORITY_HIGH )
            .setSound(soundUri)
            .setContentIntent(pendingIntent)

        // 알림 표시
        notificationManager.notify(500, notification.build())
    }

    private fun isNotificationPermissionGranted(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { // 오레오 버전 - API 26
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            // 티라미수 이하 버전에서는 항상 권한이 허용된 것
            true
        }
    }

}