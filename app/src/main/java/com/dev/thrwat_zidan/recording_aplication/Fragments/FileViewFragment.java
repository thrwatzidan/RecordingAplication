package com.dev.thrwat_zidan.recording_aplication.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dev.thrwat_zidan.recording_aplication.Adapters.FileViewAdapter;
import com.dev.thrwat_zidan.recording_aplication.Database.DBHelper;
import com.dev.thrwat_zidan.recording_aplication.Models.RecodingItem;
import com.dev.thrwat_zidan.recording_aplication.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class FileViewFragment extends Fragment {

    @BindView(R.id.recycler_files)
    RecyclerView recycler_files;

    DBHelper dbHelper;

    ArrayList<RecodingItem> recodingItemArrayList;

    private FileViewAdapter adapter;

//#1
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    //#2

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.from(container.getContext()).inflate(R.layout.frament_file_viewer, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    //#3

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dbHelper = new DBHelper(getContext());
        recycler_files.setHasFixedSize(true);

        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setReverseLayout(true);
        manager.setStackFromEnd(true);
        recycler_files.setHasFixedSize(true);
        recycler_files.addItemDecoration(new DividerItemDecoration(getContext(),manager.getOrientation()));
        manager.setOrientation(LinearLayoutManager.VERTICAL);

        recycler_files.setLayoutManager(manager);
        recodingItemArrayList = dbHelper.getAllAudioFiles();

        if (recodingItemArrayList == null) {
            Toast.makeText(getContext(), "No Audio File", Toast.LENGTH_SHORT).show();
        }else{
            adapter = new FileViewAdapter(getActivity(), recodingItemArrayList, manager);
                    recycler_files.setAdapter(adapter);
        }
    }
}
