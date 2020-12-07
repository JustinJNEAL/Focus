package com.justinjneal.focus;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
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

public class BreakTimerTenActivity extends AppCompatActivity {
	ProgressBar timeProgress;
	CountDownTimer breakTimer;
	TextView txtTime;
	// long timeMS = 600000;
	// For testing
	long timeMS = 10000;
	private SoundPool sound;
	private int chimes;
	private Vibrator vibrator;
	private VibrationEffect vibrationEffect;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_10_breaktimer);

		vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			AudioAttributes audioAttributes = new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_ALARM).setContentType(AudioAttributes.CONTENT_TYPE_UNKNOWN).build();
			sound = new SoundPool.Builder().setMaxStreams(1).setAudioAttributes(audioAttributes).build();
		} else {
			sound = new SoundPool(1, AudioManager.STREAM_ALARM, 0);
		}
		chimes = sound.load(this, R.raw.chimes, 1);
		timeProgress = findViewById(R.id.timer_progressBar);
		txtTime = (TextView) findViewById(R.id.progress_time);
		timeProgress.setProgress((int) timeMS);
		startBreak();
	}

	public void startBreak() {
		breakTimer = new CountDownTimer(timeMS, 1000) {
			@Override
			public void onTick(long millisUntilFinished) {
				updateProgress(millisUntilFinished);
				updateClock(millisUntilFinished);
			}

			@Override
			public void onFinish() {
				updateProgress(0);
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
					vibrationEffect = VibrationEffect.createOneShot(6000, VibrationEffect.EFFECT_HEAVY_CLICK);
					vibrator.vibrate(vibrationEffect);
				} else {
					vibrator.vibrate(6000);
				}
				sound.play(chimes, 1, 1, 0, 0, 1);
				txtTime.setText("Breath. You did great.");
				Handler handler = new Handler();
				handler.postDelayed(new Runnable() {
					@Override
					public void run() {
						returnHome();
					}
				}, 6000);
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

	public void returnHome() {
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
	}
}