package com.showbabyapp.xanaduodownloader.downloader;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by 秀宝-段誉 on 2016/5/13 13:10.
 */
public class DownloadInfo implements Parcelable {

    public static final String KEY_DOWNLOAD_ACTION = "KEY_DOWNLOAD_ACTION";
    public static final int VALUE_DOWNLOAD_ACTION_START = 0;
    public static final int VALUE_DOWNLOAD_ACTION_PAUSE = 1;
    public static final int VALUE_DOWNLOAD_ACTION_RESUME = 2;
    public static final int VALUE_DOWNLOAD_ACTION_CANCEL = 3;
    public static final int MAX_TASK = 2;

    public int id;
    public String name;
    public String url;
    public DownloadStatus status = DownloadStatus.idle;
    public int progress;
    public int totalLength;

    public DownloadInfo() {

    }

    public DownloadInfo(int id, String url) {
        this.id = id;
        this.url = url;
    }

    /**
     * 下载状态
     */
    public enum DownloadStatus {
        idle,
        waiting,
        downloading,
        paused,
        resumed,
        cancelled,
        completed
    }

    public String status(DownloadStatus status) {
        switch (status) {
            case idle:
                return "空闲";
            case waiting:
                return "等待";
            case downloading:
                return "下载";
            case paused:
                return "暂停";
            case resumed:
                return "继续";
            case cancelled:
                return "取消";
            default:
                return "完成";

        }
    }

    @Override
    public String toString() {
        return "DownloadInfo{" +
                "id=" + id +
                ", progress=" + progress +
                ", totalLength=" + totalLength +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        //TODO 重写hashCode是因为我们需要通过id来比较，认为是同一个对象
        return this.hashCode() == o.hashCode();
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.url);
        dest.writeInt(this.status == null ? -1 : this.status.ordinal());
        dest.writeInt(this.progress);
        dest.writeInt(this.totalLength);
    }

    protected DownloadInfo(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.url = in.readString();
        int tmpStatus = in.readInt();
        this.status = tmpStatus == -1 ? null : DownloadStatus.values()[tmpStatus];
        this.progress = in.readInt();
        this.totalLength = in.readInt();
    }

    public static final Parcelable.Creator<DownloadInfo> CREATOR = new Parcelable.Creator<DownloadInfo>() {
        @Override
        public DownloadInfo createFromParcel(Parcel source) {
            return new DownloadInfo(source);
        }

        @Override
        public DownloadInfo[] newArray(int size) {
            return new DownloadInfo[size];
        }
    };
}
