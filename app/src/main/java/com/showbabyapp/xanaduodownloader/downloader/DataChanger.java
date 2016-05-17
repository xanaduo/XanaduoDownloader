package com.showbabyapp.xanaduodownloader.downloader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;

/**
 * Created by 秀宝-段誉 on 2016/5/13 12:49.
 * <p>
 * 监测数据变化的
 */
public class DataChanger extends Observable {
    private static DataChanger dataChanger;
    //使用LinkedHashMap是因为链表增删快
    private HashMap<Integer, DownloadInfo> pauseMap;

    public DataChanger() {
        pauseMap = new LinkedHashMap<>();
    }

    /**
     * 单例双重判断
     *
     * @return
     */
    public static DataChanger getInstance() {
        if (dataChanger == null) {
            synchronized (DataChanger.class) {
                if (dataChanger == null)
                    dataChanger = new DataChanger();
            }
        }
        return dataChanger;
    }

    /**
     * 发送状态
     *
     * @param downloadInfo
     */
    public void postStatus(DownloadInfo downloadInfo) {
        //所有的操作数据都被保存起来，以便下次使用
        pauseMap.put(downloadInfo.id, downloadInfo);
        setChanged();
        notifyObservers(downloadInfo);
    }

    /**
     * 查询所有被暂停的任务
     *
     * @return
     */
    public List<DownloadInfo> queryAllRecoverableInfos() {
        List<DownloadInfo> downloadInfos = new ArrayList<>();
        for (Map.Entry<Integer, DownloadInfo> entry : pauseMap.entrySet()) {
            if (entry.getValue().status == DownloadInfo.DownloadStatus.paused)
                downloadInfos.add(entry.getValue());
        }
        return downloadInfos;
    }
}
