package com.example.quangtran.listenmusic.view;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.quangtran.listenmusic.R;
import com.example.quangtran.listenmusic.util.Util;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
        , SeekBar.OnSeekBarChangeListener {
    private static final String TITLE_NOTIFICATION = "Media Artist";
    private static final String CONTENT_NOTIFICATION = "thuong thuc am nhac";
    private static final int REQUEST_CODE = 1;
    private static final int NOTIFICATION_ID = 1;
    private static final int PROGRESS_START = 0;
    private static final int PROGRESS_MAX = 100;
    private SeekBar mProgressMusic;
    private ImageButton mImagePlay, mImageNext, mImageBack;
    private TextView mCurrentTime, mTotalTime;
    private Util mUtil;
    private android.os.Handler mHandler = new android.os.Handler();
    private ServicePlayMusic mServicePlayMusic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mImageBack = findViewById(R.id.image_back);
        mImagePlay = findViewById(R.id.image_play);
        mImageNext = findViewById(R.id.image_next);
        mImagePlay.setOnClickListener(this);
        mImageBack.setOnClickListener(this);
        mImageNext.setOnClickListener(this);
        mCurrentTime = findViewById(R.id.text_current_time);
        mTotalTime = findViewById(R.id.text_total_time);
        mProgressMusic = findViewById(R.id.seek_bar_music);
        mProgressMusic.setProgress(PROGRESS_START);
        mProgressMusic.setMax(PROGRESS_MAX);
        mProgressMusic.setOnSeekBarChangeListener(this);
        startService();
        buildNotification();
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            ServicePlayMusic.LocalBinder binder = (ServicePlayMusic.LocalBinder) iBinder;
            mServicePlayMusic = binder.getService();
            updateUI();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            unbindService(mConnection);
        }
    };

    private void startService() {
        Intent startService = new Intent(this, ServicePlayMusic.class);
        bindService(startService, mConnection, BIND_AUTO_CREATE);
        startService(startService);
    }

    private void buildNotification() {
        Intent intentNotification = new Intent(getApplicationContext(), MainActivity.class);
        PendingIntent pendingIntent = PendingIntent
                .getService(getApplicationContext(), REQUEST_CODE, intentNotification, 0);
        Notification.Builder builder = new Notification.Builder(this)
                .setSmallIcon(R.drawable.music_icon)
                .setContentTitle(TITLE_NOTIFICATION)
                .setContentText(CONTENT_NOTIFICATION)
                .setDeleteIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager)
                getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    private void updateUI() {
        if (mServicePlayMusic.mediaPlayer != null) {
            updateProgressBar();
            if (mServicePlayMusic.mediaPlayer.isPlaying()) {
                mImagePlay.setImageResource(R.drawable.ic_pause);
            } else {
                mImagePlay.setImageResource(R.drawable.ic_play_button);
            }
        }
    }

    private Runnable mUpdateTimes = (new Runnable() {
        @Override
        public void run() {
            mUtil = new Util();
            long currentTime = mServicePlayMusic.mediaPlayer.getCurrentPosition();
            long totalTime = mServicePlayMusic.mediaPlayer.getDuration();
            mCurrentTime.setText(mUtil.TotalTime(currentTime));
            mTotalTime.setText(mUtil.TotalTime(totalTime));
            //update progressbar
            int progress = (int) (Util.getProgressPercentage(currentTime, totalTime));
            mProgressMusic.setProgress(progress);
            if (mProgressMusic.getProgress() == mProgressMusic.getMax()) {
                mServicePlayMusic.nextSong();
            }
            mHandler.postDelayed(this, PROGRESS_MAX);//de quy
        }
    });

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_play:
                updatePlay();
                break;
            case R.id.image_back:
                mServicePlayMusic.backSong();
                mImagePlay.setImageResource(R.drawable.ic_pause);
                break;

            case R.id.image_next:
                mServicePlayMusic.nextSong();
                mImagePlay.setImageResource(R.drawable.ic_pause);
                break;
            default:
                break;
        }
    }

    private void updatePlay() {
        if (mServicePlayMusic.mediaPlayer.isPlaying()) {
            mServicePlayMusic.play();
            mImagePlay.setImageResource(R.drawable.ic_play_button);
        } else {
            mServicePlayMusic.play();
            mImagePlay.setImageResource(R.drawable.ic_pause);
        }
    }

    private void updateProgressBar() {
        mHandler.postDelayed(mUpdateTimes, PROGRESS_MAX);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        mHandler.removeCallbacks(mUpdateTimes);
        if (mServicePlayMusic.mediaPlayer != null) {
            int totalDuration = mServicePlayMusic.mediaPlayer.getDuration();
            int progress = mProgressMusic.getProgress();
            int current = Util.progressToTimer(progress, totalDuration);
            mServicePlayMusic.mediaPlayer.seekTo(current);
            updateProgressBar();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        NotificationManager notificationManager = (NotificationManager)
                getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(NOTIFICATION_ID);
    }
}
