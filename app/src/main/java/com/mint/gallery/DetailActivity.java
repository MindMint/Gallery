package com.mint.gallery;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import java.lang.ref.WeakReference;

import photo.lzy.com.Gallery.R;

/**
 * Created by lushijie on 2018/4/20.
 */

public class DetailActivity extends AppCompatActivity {
    public final static String EXTRA_IMAGE_INDEX = "extra_image_index";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        int index = getIntent().getIntExtra(EXTRA_IMAGE_INDEX,0);
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(new ImagePageAdapter(this));
        viewPager.setCurrentItem(index);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish(); // back button
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private static class ImagePageAdapter extends PagerAdapter{
        private WeakReference<DetailActivity> detailActivityWeakReference;
        public ImagePageAdapter(DetailActivity detailActivity) {
            detailActivityWeakReference = new WeakReference<DetailActivity>(detailActivity);
        }

        @Override
        public int getCount() {
            return ScanHelper.getInstance().getImageList().size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View contentView = LayoutInflater.from(container.getContext()).inflate(R.layout.item_image_detail_list,container,false);
            container.addView(contentView);
            ImageView imageDetail = contentView.findViewById(R.id.image_detail);
            imageDetail.setImageBitmap(getBitmapByImage(ScanHelper.getInstance().getImageList().get(position)));
            if(detailActivityWeakReference.get() != null){
                detailActivityWeakReference.get().setTitle(ScanHelper.getInstance().getImageList().get(position).getTitle());
            }

            return contentView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    private static Bitmap getBitmapByImage(Image image){
        Bitmap bitmap = BitmapCache.getInstance().getBitmap(image.getPath());
        if(bitmap == null){
            WindowManager manager = (WindowManager) GalleryApplication.getAppContext().getSystemService(Context.WINDOW_SERVICE);
            DisplayMetrics outMetrics = new DisplayMetrics();
            manager.getDefaultDisplay().getMetrics(outMetrics);
            int screenWidth = outMetrics.widthPixels;
            int screenHeight = outMetrics.heightPixels;

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            bitmap = BitmapFactory.decodeFile(image.getPath(),options);
            options.inJustDecodeBounds = false;
            options.inSampleSize = calculateInSampleSize(options,screenWidth,screenHeight);
            bitmap = BitmapFactory.decodeFile(image.getPath(),options);
            BitmapCache.getInstance().putBitmap(image.getPath(),bitmap);
        }
        return bitmap;
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int width = options.outWidth;
        final int height = options.outHeight;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            //使用需要的宽高的最大值来计算比率
            final int suitedValue = reqHeight > reqWidth ? reqHeight : reqWidth;
            final int heightRatio = Math.round((float) height / (float) suitedValue);
            final int widthRatio = Math.round((float) width / (float) suitedValue);

            inSampleSize = heightRatio > widthRatio ? heightRatio : widthRatio;//用最大
        }

        return inSampleSize;
    }
}
