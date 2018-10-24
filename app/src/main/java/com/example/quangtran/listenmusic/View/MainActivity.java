package com.example.quangtran.listenmusic.View;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quangtran.listenmusic.R;
import com.example.quangtran.listenmusic.Util.Util;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {
    private static int[] mRawID = {R.raw.songone, R.raw.songtwo, R.raw.songthree};
    private static int mcurrentSongIndex = 0;
    ProgressBar mProgressMusic;
    MediaPlayer mediaPlayer;
    ImageButton mImagePlay, mImageNext, mImageBack;
    TextView mCurrentTime, mTotalTime;
    Util mUtil = new Util();
    android.os.Handler handler = new android.os.Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mImageBack = findViewById(R.id.imageBack);
        mImagePlay = findViewById(R.id.imagePlay);
        mImageNext = findViewById(R.id.imageNext);
        mImagePlay.setOnClickListener(this);
        mImageBack.setOnClickListener(this);
        mImageNext.setOnClickListener(this);
        mediaPlayer = MediaPlayer.create(this, mRawID[0]);
        mCurrentTime = findViewById(R.id.textview_CurrentTime);
        mTotalTime = findViewById(R.id.textview_TotalTime);
        mProgressMusic = findViewById(R.id.SeekBarMusic);
        Runnable mUpdateTime = (new Runnable() {
            @Override
            public void run() {
                long currentTime = mediaPlayer.getCurrentPosition();
                long totalTime = mediaPlayer.getDuration();
                mCurrentTime.setText(mUtil.TotalTime(currentTime));
                mTotalTime.setText(mUtil.TotalTime(totalTime));
                //update progressbar
                int progress = (int) (Util.getProgressPercentage(currentTime, totalTime));
                mProgressMusic.setProgress(progress);
                handler.postDelayed(this, 100);
            }
        });
        mUpdateTime.run();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imagePlay:
                if (mediaPlayer.isPlaying()) {
                    if (mediaPlayer != null) {
                        mediaPlayer.pause();
                        mImagePlay.setImageResource(R.drawable.ic_pause);
                        break;
                    }

                } else {
                    if (mediaPlayer != null) {
                        mediaPlayer.start();
                        mImagePlay.setImageResource(R.drawable.ic_play_button);
                        break;

                    }
                }
                break;

            case R.id.imageBack:
                if (mcurrentSongIndex > 0) {
                    mcurrentSongIndex = mcurrentSongIndex - 1;
                    PlaySong(mcurrentSongIndex);
                } else {
                    mcurrentSongIndex = mRawID.length - 1;
                    PlaySong(mcurrentSongIndex);
                }
                break;

            case R.id.imageNext:
                if (mcurrentSongIndex < mRawID.length - 1) {
                    mcurrentSongIndex = mcurrentSongIndex + 1;
                    PlaySong(mcurrentSongIndex);
                } else {
                    mcurrentSongIndex = 0;
                    PlaySong(mcurrentSongIndex);

                }
                break;
        }
    }

    private void PlaySong(int IDTrack) {
        mediaPlayer.reset();
        mediaPlayer = MediaPlayer.create(this, mRawID[IDTrack]);
        mediaPlayer.start();

        buildNotification();

        mProgressMusic.setProgress(0);
        mProgressMusic.setMax(100);


    }

    @Override
    protected void onResume() {
        super.onResume();
        StopService();
    }

    private void StopService() {
        Intent startService = new Intent(this, ServicePlayMusic.class);
        stopService(startService);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mediaPlayer.pause();
        StartService();
    }

    private void StartService() {
        Intent startService = new Intent(this, ServicePlayMusic.class);
        startService.putExtra("currentTime", mediaPlayer.getCurrentPosition());
        startService.putExtra("totalTime", mediaPlayer.getDuration());
        startService.putExtra("idTrack", mcurrentSongIndex);

        startService(startService);

    }

    private void buildNotification() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(), 1, intent, 0);
        Notification.Builder builder = new Notification.Builder(this)
                .setSmallIcon(R.drawable.music_icon)
                .setContentTitle("Media Artist")
                .setContentText("thuong thuc am nhac")
                .setDeleteIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, builder.build());
    }

    private void updateProgressBar() {

    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        int totalDuration = mediaPlayer.getDuration();
        int progress = mProgressMusic.getProgress();
        int current = Util.progressToTimer(progress, totalDuration);
        mediaPlayer.seekTo(current);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
