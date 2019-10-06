package com.dev.thrwat_zidan.recording_aplication;

import android.Manifest;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.dev.thrwat_zidan.recording_aplication.Adapters.MyTabAdapter;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tabs)
    PagerSlidingTabStrip tabs;
    @BindView(R.id.pager)
    ViewPager viewPager;
    @BindView(R.id.container)
    FrameLayout container;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                ).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {
                /* ... */
                if (report.areAllPermissionsGranted()) {

                    viewPager.setAdapter(new MyTabAdapter(getSupportFragmentManager()));
                    tabs.setViewPager(viewPager);
                    setSupportActionBar(toolbar);

                } else
                    Toast.makeText(MainActivity.this, "Please Accept All Permissions", Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                /* ... */
            }
        }).check();


    }
}
