package com.mint.gallery;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lushijie on 2018/4/20.
 */

public class ScanHelper {
    private static ScanHelper instance;
    private final List<Image> imageList = new ArrayList<>();
    public static ScanHelper getInstance() {
        if (instance == null) {
            instance = new ScanHelper();
        }
        return instance;
    }
    private ScanHelper(){

    }

    public void loadImagesFromMediaStore(final ScanCallback scanCallback){
        new Thread(new Runnable() {
            @Override
            public void run() {
                scanCallback.onScanStart();
                imageList.clear();
                ContentResolver contentResolver = GalleryApplication.getAppContext().getContentResolver();
                String[] columns = { MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID, MediaStore.Images.Media.TITLE,
                        MediaStore.Images.Media.DISPLAY_NAME };

                Cursor cursor = contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,columns,null,null,null);
                if (cursor.moveToFirst()){
                    do {
                        int id = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Media._ID));
                        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                        String title = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.TITLE));
                        Image image = new Image(id,path,title);
                        imageList.add(image);

                        if(scanCallback != null){
                            scanCallback.onScannedImage(image);
                        }
                    }while (cursor.moveToNext());
                }
                cursor.close();
                if(scanCallback != null){
                    scanCallback.onScanFinish();
                }
            }
        }).start();
    }

    public List<Image> getImageList() {
        return imageList;
    }

    public interface ScanCallback {
        void onScanStart();
        void onScannedImage(Image image);
        void onScanFinish();
    }
}
