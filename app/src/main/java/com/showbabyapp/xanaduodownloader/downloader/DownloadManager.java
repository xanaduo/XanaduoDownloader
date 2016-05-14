package com.showbabyapp.xanaduodownloader.downloader;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

/**
 * Created by 秀宝-段誉 on 2016/5/13.
 * <p/>
 * 由它来控制整个下载的，而不是直接去启动Service
 */
public class DownloadManager {
    private static DownloadManager downloadManager;
    private final Context context;

    private DownloadManager(Context context) {
        this.context = context;
    }

    /**
     * 单例双重判断
     *
     * @param context
     * @return
     */
    public static DownloadManager getInstance(Context context) {
        if (downloadManager == null) {
            synchronized (DownloadManager.class) {
                if (downloadManager == null)
                    downloadManager = new DownloadManager(context);
            }
        }
        return downloadManager;
    }

    /**
     * 开始下载
     * @param downloadInfo
     */
    public void start(DownloadInfo downloadInfo) {
        loadIntent(downloadInfo, DownloadInfo.VALUE_DOWNLOAD_ACTION_START);
    }

    /**
     * 暂停
     * @param downloadInfo
     */
    public void pause(DownloadInfo downloadInfo) {
        loadIntent(downloadInfo, DownloadInfo.VALUE_DOWNLOAD_ACTION_PAUSE);
    }

    /**
     * 恢复
     * @param downloadInfo
     */
    public void resume(DownloadInfo downloadInfo) {
        loadIntent(downloadInfo, DownloadInfo.VALUE_DOWNLOAD_ACTION_RESUME);
    }

    /**
     * 取消
     * @param downloadInfo
     */
    public void cancel(DownloadInfo downloadInfo) {
        loadIntent(downloadInfo, DownloadInfo.VALUE_DOWNLOAD_ACTION_CANCEL);
    }

    /**
     * 加载服务
     *
     * @param downloadInfo
     * @return
     */
    @NonNull
    private void loadIntent(DownloadInfo downloadInfo, int action) {
        Intent intent = new Intent(context, DownloadService.class);
        intent.putExtra(DownloadInfo.class.getSimpleName(), downloadInfo);
        intent.putExtra(DownloadInfo.KEY_DOWNLOAD_ACTION, action);
        context.startService(intent);
    }

    /**
     * 添加观察者
     *
     * @param watcher
     */
    public void addObserver(DataWatcher watcher) {
        DataChanger.getInstance().addObserver(watcher);
    }

    /**
     * 删除观察者
     *
     * @param watcher
     */
    public void deleteObserver(DataWatcher watcher) {
        DataChanger.getInstance().deleteObserver(watcher);
    }
}
