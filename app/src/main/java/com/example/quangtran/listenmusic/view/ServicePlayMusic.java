package com.example.quangtran.listenmusic.view;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.example.quangtran.listenmusic.R;

public class ServicePlayMusic extends Service {
    private static int mCurrentSongIndex = 0;
    private static final int START_ID = 1;
    private final IBinder mIBinder = new LocalBinder();
    public MediaPlayer mediaPlayer;
    private static int[] mRawID = {R.raw.songone
            , R.raw.songtwo
            , R.raw.songthree
            , R.raw.songfour};

    public ServicePlayMusic() {
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mIBinder;
    }

    public class LocalBinder extends Binder {
        public ServicePlayMusic getService() {
            return ServicePlayMusic.this;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //kiem tra so lan service khoi tao lai
        if (startId == START_ID) {
            mediaPlayer = MediaPlayer.create(this, mRawID[0]);

        }
        return START_STICKY;
    }

    public void playSong(int id) {
        if (mediaPlayer != null) {
            mediaPlayer.reset();
        }
        mediaPlayer = MediaPlayer.create(this, mRawID[id]);
        mediaPlayer.start();
    }

    public void play() {
        if (mediaPlayer.isPlaying()) {
            if (mediaPlayer != null) {
                mediaPlayer.pause();
            }
        } else {
            if (mediaPlayer != null) {
                mediaPlayer.start();
            }
        }
    }

    public void nextSong() {
        if (mCurrentSongIndex < (mRawID.length - 1)) {
            playSong(mCurrentSongIndex + 1);
            mCurrentSongIndex = mCurrentSongIndex + 1;
        } else {
            playSong(0);
            mCurrentSongIndex = 0;
        }
    }

    public void backSong() {
        if (mCurrentSongIndex > 0) {
            playSong(mCurrentSongIndex - 1);
            mCurrentSongIndex = mCurrentSongIndex - 1;
        } else {
            //play last song
            playSong(mRawID.length - 1);
            mCurrentSongIndex = mRawID.length - 1;
        }
    }


    public static void setCurrentSongIndex(int mcurrentSongIndex) {
        ServicePlayMusic.mCurrentSongIndex = mcurrentSongIndex;
    }

    public static int getCurrentSongIndex() {
        return mCurrentSongIndex;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopSelf();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return true;
    }
}
