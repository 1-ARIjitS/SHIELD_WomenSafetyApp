package com.example.womensafety.Services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.womensafety.Activities.AdminActivity;
import com.example.womensafety.R;

public class SosService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /*@Override
    public void onDestroy() {
        stopForeground(true);
        stopSelf();
        super.onDestroy();
    }*/

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        createNotificationChannel();

        Intent intent1=new Intent(this, AdminActivity.class);
        PendingIntent pendingIntent=PendingIntent.getActivity(this,0,intent1,0);

        Notification notification=new NotificationCompat.Builder(this,"channel1")
                .setContentTitle("SHIELD SOS SERVICE")
                .setContentText("SHIELD SOS Service Is Running press the power Button 3 times to send an SOS message to your next to kin")
                .setSmallIcon(R.drawable.vig)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(1,notification);

        return START_STICKY;
    }

    private void createNotificationChannel() {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
        {
            NotificationChannel notificationChannel=new NotificationChannel("channel1","SOS Service", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager=getSystemService(NotificationManager.class);
            manager.createNotificationChannel(notificationChannel);
        }
    }
}
