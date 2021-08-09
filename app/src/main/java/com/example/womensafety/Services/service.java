package com.example.womensafety.Services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.Locale;

public class service extends Service {
    String listeningMsg;
    long startListeningTime;
    long pauseAndSpeakTime;
    boolean speechResultFound = false;
    boolean onReadyForSpeech = false;
    boolean continuousSpeechRecognition = true;

    final static int ERROR_TIMEOUT = 5000;
    final static int AUDIO_BEEP_DISABLED_TIMEOUT = 30000;
    final static int MAX_PAUSE_TIME = 500;
    static int PARTIAL_DELAY_TIME = 500;

    private static final int REQUEST_AUDIO_PERMISSIONS = 100;

    SpeechRecognizer speechRecognizer;
    Intent speechIntent;
    private AudioManager audioManager;
    private Handler restartDroidSpeech = new Handler();
    private Handler speechPartialResult = new Handler();

    @Override
    public void onStart(Intent intent, int startId)
    {
        super.onStart(intent, startId);

        //  LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver, new IntentFilter("SENDMSG"));
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        setRecognitionProgressMsg("");
        startSpeechRecognition();
      /*  mRegistrationBroadcastReceiver = new BroadcastReceiver()
        {
            @Override
            public void onReceive(Context context, Intent intent)
            {
                // checking for type intent filter
                if(intent.getAction().equals("SENDMSG"))
                {
                    String string = intent.getStringExtra("message");
                    Toast.makeText(getApplicationContext(), string,Toast.LENGTH_LONG).show();
                    stopSelf();
                }
            }
        };*/



        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        // LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
    }

    private void initSpeechProperties()
    {
        // Initializing the droid speech recognizer
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);

        // Initializing the speech intent
        speechIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, this.getPackageName());
        speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        speechIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);
        speechIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 5);

        // Initializing the audio Manager
        audioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
    }

    // Mutes (or) un mutes the audio
    private void muteAudio(Boolean mute)
    {
        try
        {
            // mute (or) un mute audio based on status
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            {
                audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, mute ? AudioManager.ADJUST_MUTE : AudioManager.ADJUST_UNMUTE, 0);
            }
            else
            {
                audioManager.setStreamMute(AudioManager.STREAM_MUSIC, mute);
            }
        }
        catch (Exception e)
        {
            if(audioManager == null) return;

            // un mute the audio if there is an exception
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            {
                audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_UNMUTE, 0);
            }
            else
            {
                audioManager.setStreamMute(AudioManager.STREAM_MUSIC, false);
            }
        }
    }


    private void cancelSpeechOperations()
    {
        if (speechRecognizer != null)
        {
            speechRecognizer.cancel();
        }
    }


    private void closeSpeech()
    {
        if (speechRecognizer != null)
        {
            speechRecognizer.destroy();
        }

        // Removing the partial result callback handler if applicable
        speechPartialResult.removeCallbacksAndMessages(null);

        // If audio beep was muted, enabling it again
        muteAudio(true);//f
    }

    private void setRecognitionProgressMsg(String msg)
    {
        if(msg != null)
        {
            //tv_result.setText(msg);

            Toast.makeText(getApplicationContext(),msg, Toast.LENGTH_SHORT).show();
            if(msg.contains("deactivate dnd") )
            {
                //   stopSelf();
            }
        }
    }

    private void restartDroidSpeechRecognition()
    {
        restartDroidSpeech.postDelayed(new Runnable() {

            @Override
            public void run()
            {
                startSpeechRecognition();
            }

        }, MAX_PAUSE_TIME);
    }


    public void startSpeechRecognition()
    {
        // listeningMsg="";
        setRecognitionProgressMsg(listeningMsg);

        startListeningTime = System.currentTimeMillis();
        pauseAndSpeakTime = startListeningTime;
        speechResultFound = false;

        if(speechRecognizer == null || speechIntent == null || audioManager == null)
        {
            // Initializing the droid speech properties if found not initialized
            initSpeechProperties();
        }

        speechRecognizer.setRecognitionListener(new RecognitionListener()
        {
            @Override
            public void onReadyForSpeech(Bundle params)
            {
                // If audio beep was muted, enabling it again
                muteAudio(true);//f
                onReadyForSpeech = true;
            }

            @Override
            public void onBeginningOfSpeech()
            {

            }

            @Override
            public void onRmsChanged(float rmsdB)
            {

            }

            @Override
            public void onBufferReceived(byte[] buffer)
            {

            }

            @Override
            public void onEndOfSpeech()
            {

            }

            @Override
            public void onError(int error)
            {
                long duration = System.currentTimeMillis() - startListeningTime;

                // If duration is less than the "error timeout" as the system didn't try listening to the user speech so ignoring
                if(duration < ERROR_TIMEOUT && error == SpeechRecognizer.ERROR_NO_MATCH && !onReadyForSpeech)
                    return;

                if(onReadyForSpeech && duration < AUDIO_BEEP_DISABLED_TIMEOUT)
                {
                    // Disabling audio beep if less than "audio beep disabled timeout", as it will be
                    // irritating for the user to hear the beep sound again and again
                    muteAudio(true);
                }
                else
                {
                    // If audio beep was muted, enabling it again
                    muteAudio(true);//f
                }

                if(error == SpeechRecognizer.ERROR_NO_MATCH || error == SpeechRecognizer.ERROR_SPEECH_TIMEOUT || error == SpeechRecognizer.ERROR_AUDIO)
                {
                    // Restart droid speech recognition
                    restartDroidSpeechRecognition();
                }
            }

            @Override
            public void onResults(Bundle results)
            {
                if(speechResultFound)
                    return;

                speechResultFound = true;

                // If audio beep was muted, enabling it again
                muteAudio(true);//f

                Boolean valid = (results != null && results.containsKey(SpeechRecognizer.RESULTS_RECOGNITION) &&
                        results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION) != null &&
                        results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION).size() > 0 &&
                        !results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION).get(0).trim().isEmpty());

                if(valid)
                {
                    // Getting the droid speech final result
                    String speechFinalResult = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION).get(0);

                    setRecognitionProgressMsg(speechFinalResult);

                    // Start droid speech recognition again
                    startSpeechRecognition();
                }
                else
                {
                    // No match found, restart droid speech recognition
                    restartDroidSpeechRecognition();
                }
            }

            @Override
            public void onPartialResults(Bundle partialResults)
            {
                if(speechResultFound) return;

                Boolean valid = (partialResults != null && partialResults.containsKey(SpeechRecognizer.RESULTS_RECOGNITION) &&
                        partialResults.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION) != null &&
                        partialResults.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION).size() > 0 &&
                        !partialResults.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION).get(0).trim().isEmpty());

                if(valid)
                {
                    final String liveSpeechResult = partialResults.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION).get(0);

                    // Setting the progress message
                    setRecognitionProgressMsg(liveSpeechResult);

                    if((System.currentTimeMillis() - pauseAndSpeakTime) > MAX_PAUSE_TIME)
                    {
                        speechResultFound = true;

                        speechPartialResult.postDelayed(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                // Closing droid speech operations
                                closeSpeech();

                                setRecognitionProgressMsg(liveSpeechResult);

                                if(continuousSpeechRecognition)
                                {
                                    // Start droid speech recognition again
                                    startSpeechRecognition();
                                }
                                else
                                {
                                    // Closing the droid speech operations
                                    closeSpeechOperations();
                                }
                            }

                        }, PARTIAL_DELAY_TIME);
                    }
                    else
                    {
                        pauseAndSpeakTime = System.currentTimeMillis();
                    }
                }
                else
                {
                    pauseAndSpeakTime = System.currentTimeMillis();
                }
            }

            @Override
            public void onEvent(int eventType, Bundle params)
            {

            }
        });

        // Canceling any running droid speech operations, before listening
        cancelSpeechOperations();

        // Start Listening
        speechRecognizer.startListening(speechIntent);
    }

    public void closeSpeechOperations()
    {
        setRecognitionProgressMsg("");

        closeSpeech();
    }

}
