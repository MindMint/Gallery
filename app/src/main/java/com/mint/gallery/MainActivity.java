package com.mint.gallery;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import photo.lzy.com.Gallery.R;

public class MainActivity extends AppCompatActivity implements ScanHelper.ScanCallback {
    private RecyclerView recyclerView;
    private ImageListAdapter adapter;
    private ProgressDialog progressDialog;
    private boolean isActive = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this,3);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ImageListAdapter();
        adapter.setOnItemClickListener(new ImageListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Image image, int position) {
                Intent intent = new Intent(MainActivity.this,DetailActivity.class);
                intent.putExtra(DetailActivity.EXTRA_IMAGE_INDEX,position);
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);

        ScanHelper.getInstance().loadImagesFromMediaStore(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isActive = false;
    }

    @Override
    public void onScannedImage(final Image image) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.addItem(image);
            }
        });
    }

    @Override
    public void onScanStart() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!isActive){
                    return;
                }
                if(progressDialog == null){
                    progressDialog = new ProgressDialog(MainActivity.this);
                }
                progressDialog.setCancelable(false);
                progressDialog.show();
            }
        });
    }

    @Override
    public void onScanFinish() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!isActive){
                    return;
                }
                if (progressDialog != null && progressDialog.isShowing()){
                    progressDialog.dismiss();
                    progressDialog = null;
                }
            }
        });
    }
}
