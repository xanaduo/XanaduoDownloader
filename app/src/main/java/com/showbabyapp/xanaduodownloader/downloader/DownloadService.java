package com.showbabyapp.xanaduodownloader.downloader;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created by 秀宝-段誉 on 2016/5/13 12:49.
 */
public class DownloadService extends Service {
    private Map<Integer, DownloadTask> taskMap = new HashMap<>();
    private DownloadTask task;
    private ExecutorService cachedThreadPool;
    /**
     * 任务队列，控制下载的数量
     */
    private LinkedBlockingDeque<DownloadInfo> deque = new LinkedBlockingDeque<>();
    public static final int HANDLER_WHAT = 100;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == HANDLER_WHAT) {
                DownloadInfo downloadInfo = (DownloadInfo) msg.obj;
                switch (downloadInfo.status) {
                    case cancelled:
                    case completed:
                    case paused:
                        removeTask(downloadInfo);
                        checkNextTask();
                        break;
                }
                DataChanger.getInstance().postStatus(downloadInfo);
            }
        }

    };

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
                addTask(downloadInfo);
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
            case DownloadInfo.VALUE_DOWNLOAD_ACTION_PAUSE_ALL:
                pauseAllDownload();
                break;
            case DownloadInfo.VALUE_DOWNLOAD_ACTION_RECOVER_ALL:
                recoverAllDownload();
                break;
        }
    }

    /**
     * 暂停所有的下载
     */
    private void pauseAllDownload() {
        //暂停正在下载中的任务
        for (Map.Entry<Integer, DownloadTask> entry : taskMap.entrySet()) {
            entry.getValue().pause();
        }
        taskMap.clear();
        //暂停队列中的任务
        while (deque.iterator().hasNext()) {
            DownloadInfo downloadInfo = deque.poll();
            downloadInfo.status = DownloadInfo.DownloadStatus.paused;
            //TODO 效率有待改进
            DataChanger.getInstance().postStatus(downloadInfo);
        }
    }

    /**
     * 恢复所有
     */
    private void recoverAllDownload() {
        List<DownloadInfo> downloadInfos = DataChanger.getInstance().queryAllRecoverableInfos();
        if (downloadInfos != null && downloadInfos.size() > 0) {
            for (DownloadInfo info : downloadInfos)
                addTask(info);
        }
    }

    /**
     * 取消
     *
     * @param downloadInfo
     */
    private void cancelDownload(DownloadInfo downloadInfo) {
        task = taskMap.remove(downloadInfo.id);
        if (task != null) {
            task.cancel();
        } else {
            downloadInfo.status = DownloadInfo.DownloadStatus.cancelled;
            deque.remove(downloadInfo);
            DataChanger.getInstance().postStatus(downloadInfo);
        }
    }

    /**
     * 恢复
     *
     * @param downloadInfo
     */
    private void resumeDownload(DownloadInfo downloadInfo) {
        addTask(downloadInfo);
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
        else {
            downloadInfo.status = DownloadInfo.DownloadStatus.paused;
            deque.remove(downloadInfo);
            DataChanger.getInstance().postStatus(downloadInfo);
        }
    }

    /**
     * 开始下载
     *
     * @param downloadInfo
     */
    private void startDownload(DownloadInfo downloadInfo) {
        DownloadTask task = new DownloadTask(downloadInfo, handler);
        taskMap.put(downloadInfo.id, task);
        cachedThreadPool.execute(task);
    }

    /**
     * 添加任务，不过为了防止太多的任务我们需要对任务进行限制
     *
     * @param downloadInfo
     */
    private void addTask(DownloadInfo downloadInfo) {
        //如果超过最大任务数就添加到等待队列中
        if (taskMap.size() > downloadInfo.MAX_TASK) {
            deque.offer(downloadInfo);
            downloadInfo.status = DownloadInfo.DownloadStatus.waiting;
            DataChanger.getInstance().postStatus(downloadInfo);
        } else {
            startDownload(downloadInfo);
        }
    }

    /**
     * 检查下一个任务
     *
     */
    private void checkNextTask() {
        DownloadInfo newInfo = deque.poll();
        if (newInfo != null)
            startDownload(newInfo);
    }

    private void removeTask(DownloadInfo downloadInfo) {
        task = taskMap.remove(downloadInfo.id);
        if (task != null) {
            switch (downloadInfo.status) {
                case paused:
                    task.pause();
                    break;
                case cancelled:
                    task.cancel();
                    break;
            }

        } else {
            deque.remove(downloadInfo);
            DataChanger.getInstance().postStatus(downloadInfo);
        }
    }
}