package com.justinjneal.focus;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class FocusTimerActivity extends AppCompatActivity {
    ProgressBar timeProgress;
    CountDownTimer focusTimer;
    TextView txtTime;
    // long timeMS = 1500000;
    // For testing
    long timeMS = 25000;
    private SoundPool sound;
    private int gong;
    private Vibrator vibrator;
    private VibrationEffect vibrationEffect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_focustimer);

        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_ALARM).setContentType(AudioAttributes.CONTENT_TYPE_UNKNOWN).build();
            sound = new SoundPool.Builder().setMaxStreams(1).setAudioAttributes(audioAttributes).build();
        } else {
            sound = new SoundPool(1, AudioManager.STREAM_ALARM, 0);
        }
        gong = sound.load(this, R.raw.gong, 1);
        timeProgress = findViewById(R.id.timer_progressBar);
        txtTime = (TextView) findViewById(R.id.progress_time);
        timeProgress.setProgress((int) timeMS);
        startFocus();

    }

    public void startFocus() {
        focusTimer = new CountDownTimer(timeMS, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                updateProgress(millisUntilFinished);
                updateClock(millisUntilFinished);
            }

            @Override
            public void onFinish() {
                updateProgress(0);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrationEffect = VibrationEffect.createOneShot(4000, VibrationEffect.EFFECT_HEAVY_CLICK);
                    vibrator.vibrate(vibrationEffect);
                } else {
                    vibrator.vibrate(4000);
                }
                sound.play(gong, 1, 1, 0, 0, 1);
                txtTime.setText("Relax. It's break time.");
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(FocusTimerActivity.this);
                String breakTime = preferences.getString("sshort", "");
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (breakTime.matches("5")) {
                            startBreakFive();
                        } else if (breakTime.matches("10")) {
                            startBreakTen();
                        } else {
                            Toast.makeText(FocusTimerActivity.this, ("\ud83d\ude27") + "Error retrieving Break Time setting...", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, 4000);
            }
        }.start();
    }

    public void updateClock(long ms) {
        int minutes = (int) ms / 60000;
        int seconds = (int) ms % 60000 / 1000;

        StringBuffer clock = new StringBuffer();
        clock.append(minutes + ":");
        if (seconds < 10) {
            clock.append("0");
        }
        clock.append(seconds);
        txtTime.setText(clock);
    }

    public void updateProgress(long ms) {
        timeProgress.setProgress((int) ms);
    }

    public void startBreakFive() {
        Intent intent = new Intent(this, BreakTimerFiveActivity.class);
        startActivity(intent);
    }

    public void startBreakTen() {
        Intent intent = new Intent(this, BreakTimerTenActivity.class);
        startActivity(intent);
    }

}