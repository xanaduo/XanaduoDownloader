package com.showbabyapp.xanaduodownloader.downloader;

import android.content.Context;
import android.os.Environment;

import org.xutils.DbManager;

import java.io.File;

/**
 * Created by 秀宝-段誉 on 2016-05-18 22:11.
 */
public class DownloadDbManger {

    public static final String SDCARD_PATH = Environment.getExternalStorageDirectory() + File.separator;
    private String dbPath = SDCARD_PATH + "Android" + File.separator + "data" + File.separator;
    public static DbManager.DaoConfig daoConfig;

    private DownloadDbManger(Context context) {
        // 不设置dbDir时, 默认存储在app的私有目录.
        // "sdcard"的写法并非最佳实践, 这里为了简单, 先这样写了.
        daoConfig = new DbManager.DaoConfig()
                .setDbName(context.getPackageName() + ".db")
                // 不设置dbDir时, 默认存储在app的私有目录.
                .setDbDir(new File(dbPath + context.getPackageName() + File.separator)) // "sdcard"的写法并非最佳实践, 这里为了简单, 先这样写了.
                .setDbVersion(1)
                .setDbOpenListener(new DbManager.DbOpenListener() {
                    @Override
                    public void onDbOpened(DbManager db) {
                        // 开启WAL, 对写入加速提升巨大
                        db.getDatabase().enableWriteAheadLogging();
                    }
                })
                .setDbUpgradeListener(new DbManager.DbUpgradeListener() {
                    @Override
                    public void onUpgrade(DbManager db, int oldVersion, int newVersion) {
                        // TODO: ...
                        // db.addColumn(...);
                        // db.dropTable(...);
                        // ...
                        // or
                        // db.dropDb();
                    }
                });
    }

    /**
     * 单例双重判断
     *
     * @param context
     * @return
     */
    public static void getInstance(Context context) {
        if (daoConfig == null) {
            synchronized (DownloadDbManger.class) {
                if (daoConfig == null)
                    new DownloadDbManger(context);
            }
        }
    }

}
