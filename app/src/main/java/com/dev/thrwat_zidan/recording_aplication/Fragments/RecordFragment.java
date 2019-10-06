package com.dev.thrwat_zidan.recording_aplication.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import com.dev.thrwat_zidan.recording_aplication.Database.DBHelper;
import com.dev.thrwat_zidan.recording_aplication.Models.RecodingItem;
import com.dev.thrwat_zidan.recording_aplication.R;
import com.dev.thrwat_zidan.recording_aplication.Serviecs.RecordingService;
import com.melnykov.fab.FloatingActionButton;

import java.io.File;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RecordFragment extends Fragment {

    @BindView(R.id.chronometer)
    Chronometer chronometer;
    @BindView(R.id.recording_status_txt)
    TextView recording_status_txt;
    @BindView(R.id.btn_record)
    FloatingActionButton btn_record;
    @BindView(R.id.btn_pasue)
    Button btn_pause;
    @BindView(R.id.noti_counter_badge)
    TextView noti_counter_badge;

    private boolean mStartRecording = true;
    private boolean mPauseRecording = true;

    private long timeWhenPaused = 0;
    DBHelper dbHelper;
    boolean isFileRecorded;

    ArrayList<RecodingItem> recodingItemArrayList;


//  #1
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }



    //   #2
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View recordView = inflater.from(container.getContext()).inflate(R.layout.fragment_record, container, false);
        ButterKnife.bind(this, recordView);

        return recordView;
    }

//    #3
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dbHelper = new DBHelper(getContext());
        recodingItemArrayList = dbHelper.getAllAudioFiles();

        if (recodingItemArrayList.size()>0  )
            noti_counter_badge.setText(String.valueOf(recodingItemArrayList.size()));
        else
            noti_counter_badge.setText("0");
        btn_pause.setVisibility(View.GONE);
        btn_record.setColorPressed(getResources().getColor(R.color.colorPrimary));

    }

    @OnClick(R.id.btn_record)
    public void recordAudio() {
        onRecord(mStartRecording);
        mStartRecording = !mStartRecording;

    }


    private void onRecord(boolean start) {
        Intent intent = new Intent(getActivity(), RecordingService.class);

        if (start) {
            btn_record.setImageResource(R.drawable.ic_stop_black_24dp);
            Toast.makeText(getContext(), "Recoding Started", Toast.LENGTH_SHORT).show();

            File file = new File(Environment.getExternalStorageDirectory() + "/MySoundRec");
            if (!file.exists()) {
                file.mkdir();
            }

            chronometer.setBase(SystemClock.elapsedRealtime());
            chronometer.start();

            getActivity().startService(intent);

            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            recording_status_txt.setText("Recording....");
        }
        else{
            recodingItemArrayList = dbHelper.getAllAudioFiles();
            if (recodingItemArrayList.size()>0  )
            noti_counter_badge.setText(String.valueOf(recodingItemArrayList.size()+1));
            else
                noti_counter_badge.setText("0");

            btn_record.setImageResource(R.drawable.ic_mic_black_24dp);
            chronometer.stop();
            chronometer.setBase(SystemClock.elapsedRealtime());
            timeWhenPaused = 0;
            recording_status_txt.setText("Tap the button to start recording...");
            getActivity().stopService(intent);
        }
    }
}
