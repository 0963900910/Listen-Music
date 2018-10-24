package com.example.quangtran.listenmusic.View;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.example.quangtran.listenmusic.R;

public class ServicePlayMusic extends IntentService{
    private static final String TAG = "ServicePlayMusic";
    MediaPlayer mediaPlayer;
    private static int[] mRawID = {R.raw.songone, R.raw.songtwo, R.raw.songthree};
    public ServicePlayMusic() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Bundle bundle = intent.getExtras();
        int current = bundle.getInt("currentTime");
        int total = bundle.getInt("totalTime");
        int index = bundle.getInt("idTrack");
        PlaySong(index,current,total);
    }

    private void PlaySong(int IDTrack,int Current,int Total) {
        //mediaPlayer.reset();
        mediaPlayer = MediaPlayer.create(this, mRawID[IDTrack]);
        mediaPlayer.seekTo(Current + 1000);
        mediaPlayer.start();
    }
}
