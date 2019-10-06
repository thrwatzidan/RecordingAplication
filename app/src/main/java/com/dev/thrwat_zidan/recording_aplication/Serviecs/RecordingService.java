package com.dev.thrwat_zidan.recording_aplication.Serviecs;

import android.app.Service;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.IBinder;
import android.widget.Toast;

import com.dev.thrwat_zidan.recording_aplication.Database.DBHelper;
import com.dev.thrwat_zidan.recording_aplication.Models.RecodingItem;

import java.io.File;
import java.io.IOException;

import androidx.annotation.Nullable;

public class RecordingService extends Service {

    MediaRecorder mediaRecorder;
    long mStartingTieMills = 0;
    long mElapsedTime = 0;

    File file;
    String fileName;
    DBHelper dbHelper;

    @Override
    public void onCreate() {
        super.onCreate();

        dbHelper = new DBHelper(getApplicationContext());
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startRecoding();
        return START_STICKY;
    }

    private void startRecoding() {

        Long tsLong = System.currentTimeMillis() / 1000;
        String ts = tsLong.toString();


        fileName = "audio_" + ts;
        file=new File(Environment.getExternalStorageDirectory()+"/MySoundRec/"+fileName+".mp3");
        mediaRecorder=new MediaRecorder();

        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mediaRecorder.setOutputFile(file.getAbsolutePath());
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        mediaRecorder.setAudioChannels(1);

        try {

            mediaRecorder.prepare();
            mediaRecorder.start();
            mStartingTieMills = System.currentTimeMillis();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void stopRecording() {
        mediaRecorder.stop();
        mElapsedTime = (System.currentTimeMillis() - mStartingTieMills);
        mediaRecorder.release();
        Toast.makeText(getApplicationContext(), "Recoding saved", Toast.LENGTH_SHORT).show();

        //add to dataBase

        RecodingItem recodingItem = new RecodingItem(fileName, file.getAbsolutePath(), mElapsedTime, System.currentTimeMillis());

        dbHelper.addRecoding(recodingItem);
    }

    @Override
    public void onDestroy() {

        if (mediaRecorder != null) {
            stopRecording();
        }
        super.onDestroy();
    }

}
