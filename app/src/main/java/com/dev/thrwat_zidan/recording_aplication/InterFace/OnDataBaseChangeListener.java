package com.dev.thrwat_zidan.recording_aplication.InterFace;

import com.dev.thrwat_zidan.recording_aplication.Models.RecodingItem;

public interface OnDataBaseChangeListener {

    void onNewDatabaseEntryAdded(RecodingItem recodingItem);
}
