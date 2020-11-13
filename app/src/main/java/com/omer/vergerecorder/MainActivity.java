package com.omer.vergerecorder;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

@RequiresApi(api = Build.VERSION_CODES.Q)
public class MainActivity extends AppCompatActivity {

    private Button btnRecStart, btnRecStop,btnListSongs;
    Chronometer timer;
    private TextView txtLoad;

    private ProgressBar prgLevel;
    private MediaRecorder mRecord;
    private MediaPlayer mPlay;

    private static final String LOG_TAG = "AudioRecording";
    private static String mFileName = null;
    public static final int REQUEST_AUDIO_PERMISSION_CODE  = 1;


    private void init()
    {
        //btnListSongs = findViewById(R.id.btnSongs);
        btnRecStart = findViewById(R.id.btnRecStart);
        btnRecStop = findViewById(R.id.btnRecStop);
        timer = findViewById(R.id.timer);
        txtLoad = findViewById(R.id.txtLoad);
        txtLoad.setVisibility(View.INVISIBLE);
        btnRecStop.setEnabled(false);
        String FileName = new SimpleDateFormat("yyyyMMddHHmm").format(new Date());

        mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();

        mFileName += "/Records/" + FileName +".3gp";

        btnRecStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(CheckPermissions())
                {
                    Toast.makeText(MainActivity.this, "Running", Toast.LENGTH_SHORT).show();
                    btnRecStop.setEnabled(true);
                    btnRecStart.setEnabled(false);

                    timer.setBase(SystemClock.elapsedRealtime());
                    timer.start();


                    txtLoad.setVisibility(View.VISIBLE);
                    mRecord = new MediaRecorder();
                    mRecord.setAudioSource(MediaRecorder.AudioSource.MIC);
                    mRecord.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                    mRecord.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                    mRecord.setOutputFile(mFileName);
                    try {
                        mRecord.prepare();
                    }catch (IOException e){
                        Log.e(LOG_TAG, "prepare() failed");
                    }
                    mRecord.start();
                }else
                {
                    RequestPermissions();
                }
            }
        });

        btnRecStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Recorded at /Records folder", Toast.LENGTH_LONG).show();
                btnRecStop.setEnabled(false);
                timer.stop();
                txtLoad.setVisibility(View.INVISIBLE);
                btnRecStart.setEnabled(true);
                mRecord.stop();
                mRecord.release();
                mRecord = null;
            }
        });

        /*btnListSongs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),ListenActivity.class));
            }
        });*/

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_AUDIO_PERMISSION_CODE:
                if (grantResults.length> 0) {
                    boolean permissionToRecord = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean permissionToStore = grantResults[1] ==  PackageManager.PERMISSION_GRANTED;
                    if (permissionToRecord && permissionToStore) {
                        Toast.makeText(getApplicationContext(), "Permission Granted", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(),"Permission Denied",Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    public boolean CheckPermissions() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), RECORD_AUDIO);
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
    }
    private void RequestPermissions() {
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{RECORD_AUDIO, WRITE_EXTERNAL_STORAGE}, REQUEST_AUDIO_PERMISSION_CODE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }
}
