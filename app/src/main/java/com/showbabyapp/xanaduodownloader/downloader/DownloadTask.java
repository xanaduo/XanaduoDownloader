package com.showbabyapp.xanaduodownloader.downloader;

import android.os.Handler;
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
    private Handler handler;

    public DownloadTask(DownloadInfo downloadInfo, Handler handler) {
        this.downloadInfo = downloadInfo;
        this.handler = handler;
    }

    public void resume() {
    }

    public void pause() {
        this.paused = true;
    }

    public void cancel() {
        this.canceled = true;
    }

    /**
     * 开始下载
     */
    private void start() {
        downloadInfo.totalLength = 1024 * 50;
        downloadInfo.status = DownloadInfo.DownloadStatus.downloading;
        downloadInfo.state = DownloadInfo.DownloadStatus.downloading.ordinal();
        notifyUpdate(100, downloadInfo);

        for (int i = downloadInfo.progress; i < downloadInfo.totalLength; ) {
            //防止用户快速不停的点击
            SystemClock.sleep(300);
            if (paused || canceled) {
                downloadInfo.status = paused ? DownloadInfo.DownloadStatus.paused : DownloadInfo.DownloadStatus.cancelled;
                downloadInfo.state = paused ? DownloadInfo.DownloadStatus.paused.ordinal() : DownloadInfo.DownloadStatus.cancelled.ordinal();
                handler.obtainMessage(100, downloadInfo).sendToTarget();
                return;
            }
            i += 1024;
            downloadInfo.progress += 1024;
            handler.obtainMessage(100, downloadInfo).sendToTarget();
        }
        downloadInfo.status = DownloadInfo.DownloadStatus.completed;
        downloadInfo.state = DownloadInfo.DownloadStatus.completed.ordinal();
        notifyUpdate(DownloadService.HANDLER_WHAT, downloadInfo);
    }

    @Override
    public void run() {
        start();
    }

    /**
     * 更新进度
     *
     * @param what
     * @param downloadInfo
     */
    public void notifyUpdate(int what, DownloadInfo downloadInfo) {
        handler.obtainMessage(what, downloadInfo).sendToTarget();
    }
}
