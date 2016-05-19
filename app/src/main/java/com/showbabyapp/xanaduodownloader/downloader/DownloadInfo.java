package com.showbabyapp.xanaduodownloader.downloader;

import android.os.Parcel;
import android.os.Parcelable;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by 秀宝-段誉 on 2016/5/13 13:10.
 */
@Table(name = "DownloadInfo", onCreated = "CREATE UNIQUE INDEX index_name ON DownloadInfo(did,name)")
public class DownloadInfo implements Parcelable {

    public static final int MAX_TASK = 2;
    public static final String KEY_DOWNLOAD_ACTION = "KEY_DOWNLOAD_ACTION";
    public static final int VALUE_DOWNLOAD_ACTION_START = 0;
    public static final int VALUE_DOWNLOAD_ACTION_PAUSE = 1;
    public static final int VALUE_DOWNLOAD_ACTION_RESUME = 2;
    public static final int VALUE_DOWNLOAD_ACTION_CANCEL = 3;
    public static final int VALUE_DOWNLOAD_ACTION_PAUSE_ALL = 4;
    public static final int VALUE_DOWNLOAD_ACTION_RECOVER_ALL = 5;

    @Column(name = "id", isId = true)
    public int id;
    @Column(name = "did")
    public int did;
    @Column(name = "name")
    public String name;
    @Column(name = "url")
    public String url;
    @Column(name = "state")
    public int state;
    @Column(name = "status")
    public DownloadStatus status = DownloadStatus.idle;
    @Column(name = "progress")
    public int progress;
    @Column(name = "totalLength")
    public int totalLength;

    public DownloadInfo() {

    }

    public DownloadInfo(int did, String url) {
        this.did = did;
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

    public String status(int state) {
        switch (state) {
            case 0:
                return "空闲";
            case 1:
                return "等待";
            case 2:
                return "下载";
            case 3:
                return "暂停";
            case 4:
                return "继续";
            case 5:
                return "取消";
            default:
                return "完成";

        }
    }

    @Override
    public String toString() {
        return "DownloadInfo{" +
                "did=" + did +
                ", progress=" + progress +
                ", totalLength=" + totalLength +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        //TODO 重写hashCode是因为我们需要通过did来比较，认为是同一个对象
        return this.hashCode() == o.hashCode();
    }

    @Override
    public int hashCode() {
        return did;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.did);
        dest.writeString(this.name);
        dest.writeString(this.url);
        dest.writeInt(this.status == null ? -1 : this.status.ordinal());
        dest.writeInt(this.progress);
        dest.writeInt(this.totalLength);
    }

    protected DownloadInfo(Parcel in) {
        this.did = in.readInt();
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
