package com.dev.thrwat_zidan.recording_aplication.Fragments;

import android.app.Dialog;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.TextView;

import com.dev.thrwat_zidan.recording_aplication.Models.RecodingItem;
import com.dev.thrwat_zidan.recording_aplication.R;
import com.melnykov.fab.FloatingActionButton;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
import butterknife.BindView;
import butterknife.ButterKnife;

public class PlayBackFragment extends DialogFragment {

    private RecodingItem item;
    private Handler handler=new Handler();
    private MediaPlayer mediaPlayer;

    private boolean isPLaying = false;

    long min = 0;
    long sec = 0;

    @BindView(R.id.seekbar)
    SeekBar seekBar;
    @BindView(R.id.file_name_txt)
    TextView file_name_txt;
    @BindView(R.id.media_player_view)
    CardView media_player_view;
    @BindView(R.id.current_progress_txt)
    TextView current_progress_txt;
    @BindView(R.id.fab_play)
    FloatingActionButton fab_play;
    @BindView(R.id.file_length_txt)
    TextView file_length_txt;





    //#1
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        item = (RecodingItem) getArguments().getSerializable("item");
        min = TimeUnit.MILLISECONDS.toMinutes(item.getLength());
        sec = TimeUnit.MILLISECONDS.toSeconds(item.getLength()) - TimeUnit.MINUTES.toSeconds(min);


    }


    //#2
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_playback, null);
        ButterKnife.bind(this, view);


        seekBarValue();

        fab_play.setOnClickListener(v -> {
            onPlay(isPLaying);
            isPLaying = !isPLaying;
        });

        file_name_txt.setText(item.getName());
        file_length_txt.setText(String.format("%02d:%02d",min,sec));


        builder.setView(view);

        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        return builder.create();
    }

    private void onPlay(boolean isPLaying) {

        if (!isPLaying) {
            if (mediaPlayer == null) {
                startPlaying();
            }
        }else{
            pausePlaying();
        }

    }

    private void pausePlaying() {

        fab_play.setImageResource(R.drawable.ic_play_arrow_white_24dp);
        handler.removeCallbacks(mRunnable);
        mediaPlayer.pause();
    }

    private void startPlaying() {

        try {
            fab_play.setImageResource(R.drawable.ic_pause_white_24dp);
            mediaPlayer=new MediaPlayer();
            mediaPlayer.setDataSource(item.getPath());
            mediaPlayer.prepare();
            seekBar.setMax(mediaPlayer.getDuration());
        } catch (IOException e) {
            e.printStackTrace();
        }

        mediaPlayer.setOnPreparedListener(mp -> mp.start());
        mediaPlayer.setOnCompletionListener(mp -> stopPlaying());

        updateSeekBar();

        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    private void seekBarValue() {

        ColorFilter colorFilter = new LightingColorFilter(getResources().getColor(R.color.colorPrimary),
                getResources().getColor(R.color.colorPrimary));

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mediaPlayer != null && fromUser) {
                    mediaPlayer.seekTo(progress);
                    handler.removeCallbacks(mRunnable);

                    long minutes = TimeUnit.MILLISECONDS.toMinutes(mediaPlayer.getCurrentPosition());
                    long seconds = TimeUnit.MILLISECONDS.toSeconds(mediaPlayer.getCurrentPosition()) -
                            TimeUnit.MINUTES.toSeconds(minutes);

                    current_progress_txt.setText(String.format("%02d:%02d", minutes, seconds));

                    updateSeekBar();
                } else if (mediaPlayer == null && fromUser) {
                    prepareMediaPlayerFromPoint(progress);
                    updateSeekBar();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void prepareMediaPlayerFromPoint(int progress) {

        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(item.getPath());
            mediaPlayer.prepare();
            seekBar.setMax(mediaPlayer.getDuration());
            mediaPlayer.seekTo(progress);
            mediaPlayer.setOnCompletionListener(mp -> stopPlaying());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void stopPlaying() {

        fab_play.setImageResource(R.drawable.ic_play_arrow_white_24dp);
        handler.removeCallbacks(mRunnable);
        mediaPlayer.stop();
        mediaPlayer.reset();
        mediaPlayer.release();
        mediaPlayer = null;

        seekBar.setProgress(seekBar.getMax());
        isPLaying = !isPLaying;

        current_progress_txt.setText(file_length_txt.getText());
        seekBar.setProgress(seekBar.getMax());



    }


    private Runnable mRunnable=new Runnable() {
    @Override
    public void run() {
        if (mediaPlayer != null) {

            int mCurrentPosition = mediaPlayer.getCurrentPosition();
            seekBar.setProgress(mCurrentPosition);

            long minute = TimeUnit.MILLISECONDS.toMinutes(mCurrentPosition);
            long second = TimeUnit.MILLISECONDS.toSeconds(mCurrentPosition) - TimeUnit.MINUTES.toSeconds(minute);


            current_progress_txt.setText(String.format("%02d:%02d", minute, second));

            updateSeekBar();
        }
    }
};


    private void updateSeekBar() {
        handler.postDelayed(mRunnable, 1000);
    }
}
