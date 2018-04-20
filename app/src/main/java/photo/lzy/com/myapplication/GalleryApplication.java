package photo.lzy.com.myapplication;

import android.app.Application;
import android.content.Context;

/**
 * Created by lushijie on 2018/4/20.
 */

public class GalleryApplication extends Application {
    private static Context appContext;

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = this;

    }

    public static Context getAppContext(){
        return appContext;
    }
}
