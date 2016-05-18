package com.showbabyapp.xanaduodownloader;

import android.app.Application;

import com.showbabyapp.xanaduodownloader.downloader.DownloadDbManger;

/**
 * Created by 秀宝-段誉 on 2016-05-18 22:17.
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        DownloadDbManger.getInstance(this);
    }
}
