package com.haritha_kh.tickingclocksound;

import android.content.Context;
import android.media.AudioManager;
import android.widget.SeekBar;

public class SeekBarMethod {

    public void setSeekBar(SeekBar seekBar,AudioManager audioManager){

        //get max volume
        int maxVolume=audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

        //get current volume
        int currVolume=audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

        //create connection between AudioManager and SeekBar
        seekBar.setMax(maxVolume);
        seekBar.setProgress(currVolume);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,i,0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}
