package com.showbabyapp.xanaduodownloader;

import android.app.Application;

import com.showbabyapp.xanaduodownloader.downloader.DownloadDbManger;
import com.showbabyapp.xanaduodownloader.downloader.DownloadManager;

import org.xutils.x;

/**
 * Created by 秀宝-段誉 on 2016-05-18 22:17.
 */
public class App extends Application {
    public static DownloadManager downloadManager;

    @Override
    public void onCreate() {
        super.onCreate();
        DownloadDbManger.getInstance(this);
        x.Ext.init(this);
        this.downloadManager = DownloadManager.getInstance(this);
    }
}
