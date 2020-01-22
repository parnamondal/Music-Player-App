package com.example.mp3player;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.PorterDuff.Mode;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.p003v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import java.io.File;
import java.util.ArrayList;

public class Playeractivity extends AppCompatActivity {
    static MediaPlayer myMediaPlayer;
    Button btn_next;
    Button btn_pause;
    Button btn_previous;
    ArrayList<File> mysongs;
    int position;
    String sname;
    SeekBar songSeekbar;
    TextView songTextLabel;
    Thread updateseekBar;

    /* access modifiers changed from: protected */
    @SuppressLint({"NewApi"})
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C0459R.layout.activity_playeractivity);
        this.btn_next = (Button) findViewById(C0459R.C0461id.next);
        this.btn_pause = (Button) findViewById(C0459R.C0461id.pause);
        this.btn_previous = (Button) findViewById(C0459R.C0461id.previous);
        this.songTextLabel = (TextView) findViewById(C0459R.C0461id.songname);
        this.songSeekbar = (SeekBar) findViewById(C0459R.C0461id.seekbar);
        getSupportActionBar().setTitle((CharSequence) "Now Playing");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        this.updateseekBar = new Thread() {
            public void run() {
                int totalDuration = Playeractivity.myMediaPlayer.getDuration();
                int currentPosition = 0;
                while (currentPosition < totalDuration) {
                    try {
                        sleep(400);
                        currentPosition = Playeractivity.myMediaPlayer.getCurrentPosition();
                        Playeractivity.this.songSeekbar.setProgress(currentPosition);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        MediaPlayer mediaPlayer = myMediaPlayer;
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            myMediaPlayer.release();
        }
        Intent i = getIntent();
        Bundle bundle = i.getExtras();
        this.mysongs = bundle.getParcelableArrayList("songs");
        this.sname = ((File) this.mysongs.get(this.position)).getName().toString();
        this.songTextLabel.setText(i.getStringExtra("songname"));
        this.songTextLabel.setSelected(true);
        this.position = bundle.getInt("pos", 0);
        myMediaPlayer = MediaPlayer.create(getApplicationContext(), Uri.parse(((File) this.mysongs.get(this.position)).toString()));
        myMediaPlayer.start();
        this.songSeekbar.setMax(myMediaPlayer.getDuration());
        this.updateseekBar.start();
        this.songSeekbar.getProgressDrawable().setColorFilter(getResources().getColor(C0459R.color.colorPrimary), Mode.MULTIPLY);
        this.songSeekbar.getThumb().setColorFilter(getResources().getColor(C0459R.color.colorPrimary), Mode.SRC_IN);
        this.songSeekbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                Playeractivity.myMediaPlayer.seekTo(seekBar.getProgress());
            }
        });
        this.btn_pause.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Playeractivity.this.songSeekbar.setMax(Playeractivity.myMediaPlayer.getDuration());
                if (Playeractivity.myMediaPlayer.isPlaying()) {
                    Playeractivity.this.btn_pause.setBackgroundResource(C0459R.C0460drawable.iconplay);
                    Playeractivity.myMediaPlayer.pause();
                    return;
                }
                Playeractivity.this.btn_pause.setBackgroundResource(C0459R.C0460drawable.iconpause);
                Playeractivity.myMediaPlayer.start();
            }
        });
        this.btn_next.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Playeractivity.myMediaPlayer.stop();
                Playeractivity.myMediaPlayer.release();
                Playeractivity playeractivity = Playeractivity.this;
                playeractivity.position = (playeractivity.position + 1) % Playeractivity.this.mysongs.size();
                Playeractivity.myMediaPlayer = MediaPlayer.create(Playeractivity.this.getApplicationContext(), Uri.parse(((File) Playeractivity.this.mysongs.get(Playeractivity.this.position)).toString()));
                Playeractivity playeractivity2 = Playeractivity.this;
                playeractivity2.sname = ((File) playeractivity2.mysongs.get(Playeractivity.this.position)).getName().toString();
                Playeractivity.this.songTextLabel.setText(Playeractivity.this.sname);
                Playeractivity.myMediaPlayer.start();
            }
        });
        this.btn_previous.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                Playeractivity.myMediaPlayer.stop();
                Playeractivity.myMediaPlayer.release();
                Playeractivity playeractivity = Playeractivity.this;
                playeractivity.position = (playeractivity.position + -1 < 0 ? Playeractivity.this.mysongs.size() : Playeractivity.this.position) - 1;
                Playeractivity.myMediaPlayer = MediaPlayer.create(Playeractivity.this.getApplicationContext(), Uri.parse(((File) Playeractivity.this.mysongs.get(Playeractivity.this.position)).toString()));
                Playeractivity playeractivity2 = Playeractivity.this;
                playeractivity2.sname = ((File) playeractivity2.mysongs.get(Playeractivity.this.position)).getName().toString();
                Playeractivity.this.songTextLabel.setText(Playeractivity.this.sname);
                Playeractivity.myMediaPlayer.start();
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == 16908332) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
