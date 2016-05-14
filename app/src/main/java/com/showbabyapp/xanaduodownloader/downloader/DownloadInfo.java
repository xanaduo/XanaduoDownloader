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

    public String id;
    public String name;
    public String url;
    public DownloadStatus status = DownloadStatus.waiting;

    /**
     * 下载状态
     */
    public enum DownloadStatus {
        waiting,
        downloading,
        pause,
        resume,
        cancel,
        completed
    }

    public String status(DownloadStatus status) {
        switch (status) {
            case waiting:
                return "等待";
            case downloading:
                return "下载";
            case pause:
                return "暂停";
            case resume:
                return "继续";
            case cancel:
                return "取消";
            default:
                return "完成";

        }
    }

    public int progress;
    public int totalLength;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.url);
        dest.writeInt(this.status == null ? -1 : this.status.ordinal());
        dest.writeInt(this.progress);
        dest.writeInt(this.totalLength);
    }

    public DownloadInfo() {
    }

    protected DownloadInfo(Parcel in) {
        this.id = in.readString();
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

    @Override
    public String toString() {
        return "DownloadInfo{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", status=" + status +
                ", progress=" + progress +
                ", totalLength=" + totalLength +
                '}';
    }
}
