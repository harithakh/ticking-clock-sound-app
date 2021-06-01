package com.haritha_kh.tickingclocksound;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextClock;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    CountDownTimer cTimer = null;
    private boolean startStop = true;
    SeekBar seekBarVolume;
    AudioManager audioManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button startButton = findViewById(R.id.start_button);
        Button stopButton = findViewById(R.id.stop_button);
        Button runInBgButton = findViewById(R.id.run_in_bg_button);

        TextClock textClock = findViewById(R.id.the_clock);

        seekBarVolume = findViewById(R.id.volume_seekBar);
        //to set volume seekbar
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        SeekBarMethod seekBarMethod = new SeekBarMethod();
        seekBarMethod.setSeekBar(seekBarVolume, audioManager);

        startButton.setOnClickListener(view -> {

            setTexColorChangeOnClick(startButton);

            if (startStop)
                startAtRightTime(textClock);

            startStop = false;
        });

        stopButton.setOnClickListener(view -> {
            setTexColorChangeOnClick(stopButton);
            cancelMainTimer();
        });

        runInBgButton.setOnClickListener(view -> {
            if (!startStop) {
                setTexColorChangeOnClick(runInBgButton);
                runInBackground();
            } else {
                CharSequence text = "Start ticking to run the app in background";
                Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
            }
        });

    }

    //this method is for changing text color when clicks a button
    private void setTexColorChangeOnClick(Button b) {
        try {
            b.setTextColor(Color.DKGRAY);
           Handler h = new Handler(Looper.getMainLooper());
            h.postDelayed(() -> b.setTextColor(Color.WHITE), 100);
        } catch (Exception ignored) {

        }
    }

    @Override
    public void onBackPressed() {
        cancelMainTimer();
        super.onBackPressed();
    }

    //by overriding this method, it shows 3 dot menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.three_dot_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //meka override karala thama thith 3e menu eke items select karahama mokada wenne kiyala kiyanne
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.settings_menu_button:
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                cancelMainTimer();
                startActivity(intent);
                return true;
            case R.id.rate_us_menu_button:
                final String appPackageName = getPackageName();
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
                return true;
            case R.id.privacy_policy_menu_button:
                try {
                    Intent privacyPolicyIntent=new Intent(Intent.ACTION_VIEW,Uri.parse("https://harithasapps.blogspot.com/2021/05/ticking-clock-sound-pro-privacy-policy.html"));
                    startActivity(privacyPolicyIntent);
                }catch (Exception e){
                    Toast.makeText(this, "No application can handle this request."
                            + " Please install a web browser",  Toast.LENGTH_LONG).show();
                }
                return true;
            case R.id.about_menu_button:
                openAboutDialog();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    //About dialog eka pennana box eka open wenne me method eken
    private void openAboutDialog() {
        DialogClass dialogClass = new DialogClass();
        dialogClass.show(getSupportFragmentManager(), "about app");
    }

    //this runInBackground method is called when back button is pressed
    public void runInBackground() {
        moveTaskToBack(true);
    }

    //start the timer function. it has a countdown timer
    public void startMainTimer() {

        //this reads values from preferences
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        String soundClipNameValues = sp.getString("sound_clips_preferences", "1");
        String tickingDurationValues = sp.getString("ticking_durations_preferences", "2");

        int a;
        long countDownInterval = 1000L;
        switch (soundClipNameValues) {
            case "1":
                a = R.raw.clock_ticking_01;
                break;
            case "2":
                a = R.raw.clock_ticking_02;
                countDownInterval = 2000L;
                break;
            case "3":
                a = R.raw.watch_tick;
                break;
            case "4":
                a = R.raw.large_clock_ticking_01;
                countDownInterval = 2000L;
                break;
            case "5":
                a = R.raw.grandfather_clock_tick;
                countDownInterval = 2000L;
                break;
            case "6":
                a = R.raw.mantel_clock_ticking;
                countDownInterval = 2500L;
                break;
            case "7":
                a = R.raw.pendulum_clock_ticking;
                break;
            case "8":
                a = R.raw.big_old_clock_ticking;
                countDownInterval = 2000L;
                break;
            case "9":
                a = R.raw.clock_ticking_03;
                countDownInterval = 2000L;
                break;

            default:
                a = R.raw.clock_ticking_01;
                //throw new IllegalStateException("Unexpected value: " + soundClipNameValues);
        }

        long b;
        switch (tickingDurationValues) {
            case "2": //2 means 2 minutes=120000 millie seconds
                b = 120000;
                break;
            case "5":
                b = 300000;
                break;
            case "10":
                b = 600000;
                break;
            case "20":
                b = 1200000;
                break;
            case "30":
                b = 1800000;
                break;
            case "60":
                b = 3600000;
                break;
            case "120":
                b = 7200000;
                break;
            case "180":
                b = 10800000;
                break;
            case "360":
                b = 21600000;
                break;
            case "720":
                b = 43200000;
                break;
            default:
                b = 60000;
        }

        final MediaPlayer mp = MediaPlayer.create(MainActivity.this, a);

        cTimer = new CountDownTimer(b, countDownInterval) {
            @Override
            public void onTick(long l) {
                mp.start();
            }

            //onFinish calls when countdown is over
            @Override
            public void onFinish() {
                startStop = true;
            }
        }.start();
    }

    //cancel timer. this should be in the stop button
    void cancelMainTimer() {
        if (cTimer != null)
            cTimer.cancel();

        startStop = true;
    }

    //hari welawata sound play wenda patan ganne meken
    public void startAtRightTime(TextClock theTextClock) {

        try {
            /*If 24 hours clock is enabled in the phone,if block executes.if not else block calls.
              What happens in this method is, when startAtRightTime is called, the time which is
              in the textClock is copied to a string called "first".
              Then after 10 milliseconds again the time is captured.
              if both times are same,startMainTimer() is called.
              Else ,again startAtRightTime() is called as a recursion method until two times
              are different.
             */
            if (theTextClock.is24HourModeEnabled()) {
                Handler mHandler = new Handler(Looper.getMainLooper());
                String first = theTextClock.getText().toString();

                mHandler.postDelayed(() -> {
                    if (!first.equals(theTextClock.getText().toString())) {
                        startMainTimer();
                    } else {
                        startAtRightTime(theTextClock);
                    }
                }, 10);
            } else {
                startMainTimer();
            }
        } catch (Exception e) {
            startMainTimer();
            //e.printStackTrace();
        }

    }


}