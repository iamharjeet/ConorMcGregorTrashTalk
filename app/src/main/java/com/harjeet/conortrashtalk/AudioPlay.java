package com.harjeet.conortrashtalk;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.view.View;

public class AudioPlay {
    public static MediaPlayer mediaPlayer;
    public static boolean isplayingAudio=false;
    public static MediaMetadataRetriever mmr = new MediaMetadataRetriever();
    public static View view;


    public static int playAudioRandom(Context c, int id){
        mediaPlayer = MediaPlayer.create(c,id);
        if(mediaPlayer!= null && !mediaPlayer.isPlaying())
        {
            isplayingAudio=true;
            mediaPlayer.start();

            int milliSecond = mediaPlayer.getDuration();
            return milliSecond;
        }
        return -1;
    }

    public static int playAudio(Context c, int id, View v){
        view = v;
        mediaPlayer = MediaPlayer.create(c,id);
        if(mediaPlayer!= null && !mediaPlayer.isPlaying())
        {
            isplayingAudio=true;
            mediaPlayer.start();

            int milliSecond = mediaPlayer.getDuration();
            return milliSecond;
        }
        return -1;
    }
    public static void stopAudio(){
        if(view!= null){
            view.setVisibility(View.INVISIBLE);
        }
        isplayingAudio=false;
        if(mediaPlayer!= null){
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
