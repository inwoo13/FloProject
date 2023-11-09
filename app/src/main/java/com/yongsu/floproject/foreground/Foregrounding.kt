package com.yongsu.floproject.foreground

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.yongsu.floproject.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class Foregrounding : Service(){

    // Foreground가 사용하는 notification이 Channel을 사용한다.
    // 해당 Channel에 오는 notification을 그 Channel에서 사용하게 해줘야 한다.
    val CHANNEL_ID = "FGSEX" // ForegroundServiceExample => 그냥 설정해준거(의미는 x)
    val NOTI_ID = 99

    private var currentCount = 0

    private lateinit var notification: NotificationCompat.Builder

    fun createNotificationChannel(){
        Log.d("servicesss", "channel 생성")

        // SDK Version이 O 이상일 때만 사용 가능
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            // 3번째거는 중요도. 중요도가 높을수록 알림의 강도가 달라짐
            val serviceChannel = NotificationChannel(CHANNEL_ID, "FOREGROUNDD", NotificationManager.IMPORTANCE_HIGH)
            // notificationManager를 systemService를 통해서 생성
            val manager = getSystemService(NotificationManager::class.java)
            // 내 Channel은 여기서 쓰겠다고 알려주는 거임
            manager.createNotificationChannel(serviceChannel)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("servicesss", "스타트")
        createNotificationChannel()
        Log.d("servicesss", "생성 성공")
        // 추가적인 옵션
        notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("이것은 FLO앱 입니다.ㅋㅋ")
            .setContentText("이것은 내용을 작성하는 란입니다.")
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setProgress(1000, 0, false)
        Log.d("servicesss", "notification 생성")

        // Foreground 시작
        startForeground(NOTI_ID, notification.build())
        Log.d("servicesss", "시작됨")

        CoroutineScope(Dispatchers.IO).launch {
            for (i in 1..1000) {
                delay(1000L)
                currentCount = i
                updateNotification()
            }
        }

        return super.onStartCommand(intent, flags, startId)
    }

    private fun updateNotification() {
        notification.setProgress(1000, currentCount, false)
        startForeground(1, notification.build())
    }

    override fun onBind(intent: Intent): IBinder {
        return Binder()
    }
}