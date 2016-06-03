package com.showbabyapp.xanaduodownloader.downloader;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by 秀宝-段誉 on 2016/5/13 12:49.
 *
 * 观察者
 */
public abstract class DataWatcher implements Observer {

    @Override
    public void update(Observable observable, Object data) {
        if (data instanceof DownloadInfo) {
            notifyUpdate((DownloadInfo) data);
        }
    }

    protected abstract void notifyUpdate(DownloadInfo downloadInfo);
}
