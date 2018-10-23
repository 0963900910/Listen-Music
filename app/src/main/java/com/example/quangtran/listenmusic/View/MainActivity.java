package com.example.quangtran.listenmusic.View;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.quangtran.listenmusic.R;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static int[] mRawID = {R.raw.songone, R.raw.songtwo, R.raw.songthree};
    private static int mcurrentSongIndex = 0;
    MediaPlayer mediaPlayer;
    ImageButton mImagePlay, mImageNext, mImageBack;

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

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imagePlay:
                if (mediaPlayer.isPlaying()) {
                    if (mediaPlayer != null) {
                        mediaPlayer.pause();
                        break;
                    }

                } else {
                    if (mediaPlayer != null) {
                        mediaPlayer.start();
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
        //mediaPlayer.pause();
        StartService();
    }

    private void StartService() {
        Intent startService = new Intent(this, ServicePlayMusic.class);
        startService.putExtra("currentIndexSong", mcurrentSongIndex);
        startService(startService);
    }
}
