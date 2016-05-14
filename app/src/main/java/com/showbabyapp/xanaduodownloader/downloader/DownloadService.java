package com.showbabyapp.xanaduodownloader.downloader;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by 秀宝-段誉 on 2016/5/13 12:49.
 */
public class DownloadService extends Service {
    private Map<String, DownloadTask> taskMap = new HashMap<>();
    private DownloadTask task;
    private ExecutorService cachedThreadPool;

    @Override
    public void onCreate() {
        super.onCreate();
        cachedThreadPool = Executors.newCachedThreadPool();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            DownloadInfo downloadInfo = intent.getParcelableExtra(DownloadInfo.class.getSimpleName());
            int action = intent.getIntExtra(DownloadInfo.KEY_DOWNLOAD_ACTION, -1);
            doAction(action, downloadInfo);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 执行
     *
     * @param downloadInfo
     */
    private void doAction(int action, DownloadInfo downloadInfo) {
        switch (action) {
            case DownloadInfo.VALUE_DOWNLOAD_ACTION_START:
                startDownload(downloadInfo);
                break;
            case DownloadInfo.VALUE_DOWNLOAD_ACTION_PAUSE:
                pauseDownload(downloadInfo);
                break;
            case DownloadInfo.VALUE_DOWNLOAD_ACTION_RESUME:
                resumeDownload(downloadInfo);
                break;
            case DownloadInfo.VALUE_DOWNLOAD_ACTION_CANCEL:
                cancelDownload(downloadInfo);
                break;
        }
    }

    /**
     * 取消
     *
     * @param downloadInfo
     */
    private void cancelDownload(DownloadInfo downloadInfo) {
        task = taskMap.remove(downloadInfo.id);
        if (task != null)
            task.cancel();
    }

    /**
     * 恢复
     *
     * @param downloadInfo
     */
    private void resumeDownload(DownloadInfo downloadInfo) {
        startDownload(downloadInfo);
    }

    /**
     * 暂停
     *
     * @param downloadInfo
     */
    private void pauseDownload(DownloadInfo downloadInfo) {
        task = taskMap.remove(downloadInfo.id);
        if (task != null)
            task.pause();
    }

    /**
     * 开始下载
     *
     * @param downloadInfo
     */
    private void startDownload(DownloadInfo downloadInfo) {
        DownloadTask task = new DownloadTask(downloadInfo);
        taskMap.put(downloadInfo.id, task);
        cachedThreadPool.execute(task);
    }

}
