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

public class ServicePlayMusic extends Service{
    MediaPlayer mediaPlayer;
    private static int[] mRawID = {R.raw.songone, R.raw.songtwo, R.raw.songthree};
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Bundle bundle = intent.getExtras();
        int current = bundle.getInt("currentIndexSong");
        PlaySong(current);
        Toast.makeText(this, current + " ", Toast.LENGTH_SHORT).show();
        return flags;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void PlaySong(int IDTrack) {
        //mediaPlayer.reset();
        mediaPlayer = MediaPlayer.create(this, mRawID[IDTrack]);
        mediaPlayer.start();
    }


}
