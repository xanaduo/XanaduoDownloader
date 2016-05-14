package com.showbabyapp.xanaduodownloader.downloader;

import android.os.SystemClock;

/**
 * Created by 秀宝-段誉 on 2016/5/13 12:49.
 * <p/>
 * 1、如果服务器不支持断点下载就用单线程下载单任务下载
 * 2、如果支持则使用多线程多任务下载
 */
public class DownloadTask implements Runnable {
    private DownloadInfo downloadInfo;
    private boolean paused;
    private boolean canceled;

    public DownloadTask(DownloadInfo downloadInfo) {
        this.downloadInfo = downloadInfo;
    }

    public void resume() {
    }

    public void pause() {
        this.paused = true;
    }

    public void cancel() {
        this.canceled = true;
    }

    private void start() {
        downloadInfo.status = DownloadInfo.DownloadStatus.downloading;
        downloadInfo.totalLength = 1024 * 100;
        DataChanger.getInstance().postStatus(downloadInfo);
        for (int i = downloadInfo.progress; i < downloadInfo.totalLength; ) {
            //防止用户快速不停的点击
            SystemClock.sleep(300);
            if (paused || canceled) {
                downloadInfo.status = paused ? DownloadInfo.DownloadStatus.pause : DownloadInfo.DownloadStatus.cancel;
                DataChanger.getInstance().postStatus(downloadInfo);
                return;
            }
            i += 1024;
            downloadInfo.progress += 1024;
            DataChanger.getInstance().postStatus(downloadInfo);
        }
        downloadInfo.status = DownloadInfo.DownloadStatus.completed;
        DataChanger.getInstance().postStatus(downloadInfo);
    }

    @Override
    public void run() {
        start();
    }
}
