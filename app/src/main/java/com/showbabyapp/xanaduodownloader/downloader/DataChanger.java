package com.showbabyapp.xanaduodownloader.downloader;

import java.util.Observable;

/**
 * Created by 秀宝-段誉 on 2016/5/13 12:49.
 * <p>
 * 监测数据变化的
 */
public class DataChanger extends Observable {
    private static DataChanger dataChanger;

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
        setChanged();
        notifyObservers(downloadInfo);
    }
}
