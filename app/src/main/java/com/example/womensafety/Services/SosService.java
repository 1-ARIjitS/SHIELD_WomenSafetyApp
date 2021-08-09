package com.example.womensafety.Services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.womensafety.Activities.AdminActivity;
import com.example.womensafety.R;
import com.maxwell.speechrecognition.OnSpeechRecognitionListener;
import com.maxwell.speechrecognition.OnSpeechRecognitionPermissionListener;
import com.maxwell.speechrecognition.SpeechRecognition;

import java.util.ArrayList;

public class SosService extends Service {

    SpeechRecognizer speechRecognizer;
    String sos_trigger_message;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        /*speechRecognizer=SpeechRecognizer.createSpeechRecognizer(this);
        Intent speechRecognizerIntent=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");
        speechRecognizer.startListening(speechRecognizerIntent);*/
    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        /*speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {

            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float rmsdB) {

            }

            @Override
            public void onBufferReceived(byte[] buffer) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int error) {

            }

            @Override
            public void onResults(Bundle results) {

            }

            @Override
            public void onPartialResults(Bundle partialResults) {
                ArrayList<String> data =partialResults.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                sos_trigger_message=data.get(0);
            }

            @Override
            public void onEvent(int eventType, Bundle params) {

            }
        });

        speechRecognizer.stopListening();*/

        //notification services
        createNotificationChannel();
        Intent intent1=new Intent(this,AdminActivity.class);
        /*intent1.putExtra("sos_msg",sos_trigger_message);
        Log.d("sosMsg",sos_trigger_message);*/
        PendingIntent pendingIntent=PendingIntent.getActivity(this,0,intent1,0);

        Notification notification=new NotificationCompat.Builder(this,"channelId1")
                .setContentTitle("SHIELD SOS SERVICE")
                .setContentText("Speak HELP 3 Times To Trigger The SOS Message")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(1,notification);


        return START_STICKY;
    }

    private void createNotificationChannel()
    {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
        {
            NotificationChannel notificationChannel=new NotificationChannel(
                    "channelId1",
                    "SHIELD SOS SERVICE",
                    NotificationManager.IMPORTANCE_DEFAULT
            );

            NotificationManager manager=getSystemService(NotificationManager.class);
            manager.createNotificationChannel(notificationChannel);
        }
    }

/*
    @Override
    public void onReadyForSpeech(Bundle params) {

    }

    @Override
    public void onBeginningOfSpeech() {

    }

    @Override
    public void onRmsChanged(float rmsdB) {

    }

    @Override
    public void onBufferReceived(byte[] buffer) {

    }

    @Override
    public void onEndOfSpeech() {

    }

    @Override
    public void onError(int error) {

    }

    @Override
    public void onResults(Bundle results) {
        ArrayList<String> data =results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        sos_trigger_message=data.get(0);
        if(sos_trigger_message.equals("help help help"))
        {
            startActivity(new Intent(this,AdminActivity.class));
        }
    }

    @Override
    public void onPartialResults(Bundle partialResults) {

    }

    @Override
    public void onEvent(int eventType, Bundle params) {

    }*/
}
