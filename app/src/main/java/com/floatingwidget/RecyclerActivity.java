package com.floatingwidget;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import java.util.List;

/**
 * Created by Mayk on 9/27/2017.
 */

public class RecyclerActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager recyclerViewLayoutManager;
    Toolbar toolbar;
    private List<AppGS> appList;
    ImageButton OK;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_view);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Passing the column number 1 to show online one column in each row.
        recyclerViewLayoutManager = new GridLayoutManager(RecyclerActivity.this, 1);

        recyclerView.setLayoutManager(recyclerViewLayoutManager);

        adapter = new MyAppAdapter(RecyclerActivity.this, new ApkInfoExtractor(RecyclerActivity.this).GetAllInstalledApkInfo());

        recyclerView.setAdapter(adapter);
        OK = (ImageButton) findViewById(R.id.recycler_ok);
        OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });
    }
}


