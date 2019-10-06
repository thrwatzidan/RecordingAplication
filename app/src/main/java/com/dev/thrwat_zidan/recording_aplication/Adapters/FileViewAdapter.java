package com.dev.thrwat_zidan.recording_aplication.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dev.thrwat_zidan.recording_aplication.Database.DBHelper;
import com.dev.thrwat_zidan.recording_aplication.Fragments.PlayBackFragment;
import com.dev.thrwat_zidan.recording_aplication.InterFace.OnDataBaseChangeListener;
import com.dev.thrwat_zidan.recording_aplication.Models.RecodingItem;
import com.dev.thrwat_zidan.recording_aplication.R;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class FileViewAdapter extends RecyclerView.Adapter<FileViewAdapter.MyViewHolder> implements OnDataBaseChangeListener {

    private Context context;
    private ArrayList<RecodingItem> recodingItemsList;
    private LinearLayoutManager manager;
    DBHelper dbHelper;

    public FileViewAdapter(Context context, ArrayList<RecodingItem> recodingItems, LinearLayoutManager manager) {
        this.context = context;
        this.recodingItemsList = recodingItems;
        this.manager = manager;
        DBHelper.setOnDataBaseChangeListener(this);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view   =LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_file_card, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        RecodingItem recodingItem = recodingItemsList.get(position);

        long min = TimeUnit.MILLISECONDS.toMinutes(recodingItem.getLength());
        long sec = TimeUnit.MILLISECONDS.toSeconds(recodingItem.getLength()) - TimeUnit.MINUTES.toSeconds(min);
        holder.file_length_txt.setText(String.format("%02d:%02d",min,sec));

        holder.file_name_txt.setText(recodingItem.getName());

        holder.file_time_added.setText(DateUtils.formatDateTime(context,recodingItem.getTime_added(),
                DateUtils.FORMAT_SHOW_DATE|DateUtils.FORMAT_NUMERIC_DATE|DateUtils.FORMAT_SHOW_TIME
                |DateUtils.FORMAT_SHOW_YEAR
                ));

    }

    @Override
    public int getItemCount() {
        return recodingItemsList.size();
    }

    @Override
    public void onNewDatabaseEntryAdded(RecodingItem recodingItem) {
        recodingItemsList.add(recodingItem);
        notifyItemInserted(recodingItemsList.size() -1);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.file_name_txt)
        TextView file_name_txt;
        @BindView(R.id.file_length_txt)
        TextView file_length_txt;
        @BindView(R.id.file_time_added)
        TextView file_time_added;
        @BindView(R.id.card_item)
        CardView card_item;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            card_item.setOnClickListener(v -> {

                PlayBackFragment playBackFragment = new PlayBackFragment();

                Bundle b = new Bundle();

                b.putSerializable("item", recodingItemsList.get(getAdapterPosition()));
                playBackFragment.setArguments(b);


                FragmentTransaction fragmentTransaction = ((FragmentActivity) context)
                        .getSupportFragmentManager()
                        .beginTransaction();

                playBackFragment.show(fragmentTransaction,"dialog_playback");
            });
        }
    }
}
