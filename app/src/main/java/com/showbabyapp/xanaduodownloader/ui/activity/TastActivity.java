package com.showbabyapp.xanaduodownloader.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.showbabyapp.xanaduodownloader.R;
import com.showbabyapp.xanaduodownloader.common.log.Trace;
import com.showbabyapp.xanaduodownloader.downloader.DataWatcher;
import com.showbabyapp.xanaduodownloader.downloader.DownloadInfo;
import com.showbabyapp.xanaduodownloader.downloader.DownloadManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016-05-15.
 */
public class TastActivity extends Activity {

    private ListView lv_content;
    private DownloadManager downloadManager;
    private List<DownloadInfo> downloadInfos = new ArrayList<>();
    private TaskAdapter adapter;

    /**
     * 观察者
     */
    private DataWatcher watcher = new DataWatcher() {


        @Override
        protected void notifyUpdate(DownloadInfo downloadInfo) {
            int index = downloadInfos.indexOf(downloadInfo);
            if (index != -1) {
                downloadInfos.remove(index);
                downloadInfos.add(index, downloadInfo);
                adapter.notifyDataSetChanged();
            }
            Trace.d(downloadInfo.toString());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_task);
        lv_content = (ListView) findViewById(R.id.lv_content);


        init();
    }

    private void init() {
        downloadInfos.add(new DownloadInfo(1, "http://e.hiphotos.baidu.com/image/pic/item/314e251f95cad1c8037ed8c97b3e6709c83d5112.jpg"));
        downloadInfos.add(new DownloadInfo(2, "http://s2.sinaimg.cn/mw690/b454a913tx6BsJh5dHr81&690"));
        downloadInfos.add(new DownloadInfo(3, "http://g.hiphotos.baidu.com/image/h%3D200/sign=dd31f62ab6119313d843f8b055390c10/35a85edf8db1cb13c72604fbd954564e93584b8e.jpg"));
        downloadInfos.add(new DownloadInfo(4, "http://h.hiphotos.baidu.com/image/pic/item/f703738da97739121c70e72dfa198618367ae22c.jpg"));
        downloadInfos.add(new DownloadInfo(5, "http://e.hiphotos.baidu.com/image/pic/item/9345d688d43f8794485f9f27d01b0ef41bd53a13.jpg"));

        this.downloadManager = DownloadManager.getInstance(this);

        adapter = new TaskAdapter(this, downloadInfos);
        lv_content.setAdapter(adapter);
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

    private class TaskAdapter extends BaseAdapter {
        private Context context;
        private List<DownloadInfo> downloadInfos = new ArrayList<>();

        public TaskAdapter(Context context, List<DownloadInfo> downloadInfos) {
            this.downloadInfos = downloadInfos;
            this.context = context;
        }

        @Override
        public int getCount() {
            return downloadInfos.size();
        }

        @Override
        public Object getItem(int position) {
            return downloadInfos.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder h;
            if (convertView == null) {
                convertView = View.inflate(context, R.layout.layout_item, null);
                h = new ViewHolder(convertView);
                convertView.setTag(h);
            } else
                h = (ViewHolder) convertView.getTag();
            h.initData(downloadInfos.get(position));
            return convertView;
        }

    }

    private class ViewHolder implements View.OnClickListener {
        private TextView txt_progress;
        private Button btn_download;
        private DownloadInfo downloadInfo;

        public ViewHolder(View convertView) {
            txt_progress = (TextView) convertView.findViewById(R.id.txt_progress);
            btn_download = (Button) convertView.findViewById(R.id.btn_download);
            btn_download.setOnClickListener(this);
        }

        public void initData(DownloadInfo downloadInfo) {
            this.downloadInfo = downloadInfo;
            txt_progress.setText(downloadInfo.status(downloadInfo.status)
                    + "-" + downloadInfo.url);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_download:
                    if (this.downloadInfo.status == DownloadInfo.DownloadStatus.idle) {
                        downloadManager.start(downloadInfo);
                    } else if (this.downloadInfo.status == DownloadInfo.DownloadStatus.downloading) {
                        downloadManager.pause(downloadInfo);
                    } else if (this.downloadInfo.status == DownloadInfo.DownloadStatus.paused) {
                        downloadManager.resume(downloadInfo);
                    }
                    break;
            }
        }
    }
}
