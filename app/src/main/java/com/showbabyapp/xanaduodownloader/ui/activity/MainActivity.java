package com.showbabyapp.xanaduodownloader.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.showbabyapp.xanaduodownloader.R;
import com.showbabyapp.xanaduodownloader.common.log.Trace;
import com.showbabyapp.xanaduodownloader.downloader.DataWatcher;
import com.showbabyapp.xanaduodownloader.downloader.DownloadInfo;
import com.showbabyapp.xanaduodownloader.downloader.DownloadManager;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_download;

    private DownloadManager downloadManager;
    private DownloadInfo downloadInfo;

    /**
     * 观察者
     */
    private DataWatcher watcher = new DataWatcher() {
        @Override
        protected void notifyUpdate(DownloadInfo downloadInfo) {
            MainActivity.this.downloadInfo = downloadInfo;
            Trace.d(downloadInfo.toString());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        initView();
        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        downloadManager = DownloadManager.getInstance(this);
    }

    /**
     * 初始化控件
     */
    private void initView() {
        btn_download = (Button) findViewById(R.id.btn_download);
        btn_download.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (this.downloadInfo == null) {
            downloadInfo = new DownloadInfo();
            downloadInfo.id = 1;
            downloadInfo.name = "古生物";
            downloadInfo.url = "http://c.hiphotos.baidu.com/image/pic/item/0d338744ebf81a4c037521c4d52a6059252da67c.jpg";
        }

        switch (v.getId()) {
            case R.id.btn_download:
                if (this.downloadInfo.status == DownloadInfo.DownloadStatus.idle) {
                    downloadManager.start(downloadInfo);
                } else if (this.downloadInfo.status == DownloadInfo.DownloadStatus.downloading) {
                    downloadManager.pause(downloadInfo);
                } else if (this.downloadInfo.status == DownloadInfo.DownloadStatus.paused) {
                    downloadManager.resume(downloadInfo);
                }
                refreshView(downloadInfo.status(this.downloadInfo.status));
                break;
        }
    }

    private void refreshView(String txt) {
        btn_download.setText(txt);
    }

    /**
     * 注册观察者
     */
    @Override
    protected void onResume() {
        super.onResume();
        downloadManager.addObserver(watcher);
    }

    /**
     * 注销观察者
     */
    @Override
    protected void onPause() {
        super.onPause();
        downloadManager.deleteObserver(watcher);
    }
}
