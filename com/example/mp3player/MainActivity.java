package com.example.mp3player;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.p003v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView Mysonglist;
    String[] items;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C0459R.layout.activity_main);
        this.Mysonglist = (ListView) findViewById(C0459R.C0461id.MysongList);
        runtimePermission();
    }

    public void runtimePermission() {
        Dexter.withActivity(this).withPermission("android.permission.READ_EXTERNAL_STORAGE").withListener(new PermissionListener() {
            public void onPermissionGranted(PermissionGrantedResponse response) {
                MainActivity.this.display();
            }

            public void onPermissionDenied(PermissionDeniedResponse response) {
            }

            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                token.continuePermissionRequest();
            }
        }).check();
    }

    public ArrayList<File> findsong(File file) {
        File[] files;
        ArrayList<File> arrayList = new ArrayList<>();
        for (File singleFile : file.listFiles()) {
            if (singleFile.isDirectory() && !singleFile.isHidden()) {
                arrayList.addAll(findsong(singleFile));
            } else if (singleFile.getName().endsWith(".mp3") || singleFile.getName().endsWith(".wav")) {
                arrayList.add(singleFile);
            }
        }
        return arrayList;
    }

    /* access modifiers changed from: 0000 */
    public void display() {
        final ArrayList<File> mysongs = findsong(Environment.getExternalStorageDirectory());
        this.items = new String[mysongs.size()];
        for (int i = 0; i < mysongs.size(); i++) {
            this.items[i] = ((File) mysongs.get(i)).getName().toString().replace(".mp3", "").replace(".wav", "");
        }
        this.Mysonglist.setAdapter(new ArrayAdapter<>(this, 17367043, this.items));
        this.Mysonglist.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String songName = MainActivity.this.Mysonglist.getItemAtPosition(i).toString();
                MainActivity mainActivity = MainActivity.this;
                mainActivity.startActivity(new Intent(mainActivity.getApplicationContext(), Playeractivity.class).putExtra("songs", mysongs).putExtra("songname", songName).putExtra("pos", i));
            }
        });
    }
}
