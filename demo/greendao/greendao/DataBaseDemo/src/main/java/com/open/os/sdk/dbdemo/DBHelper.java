package com.open.studyos.sdk.dbdemo;

import android.content.Context;

import com.open.studyos.db.autocode.BookInfoDao;
import com.open.studyos.db.autocode.DaoMaster;
import com.open.studyos.db.autocode.DaoSession;
import com.open.studyos.db.autocode.UserInfoDao;

/**
 * 文  件：DBHelper.java
 * 公  司：步步高教育电子
 * 日  期：2016/6/27  10:46
 * 作  者：HeChangPeng
 */

public class DBHelper {
    private static final String DB_NAME = "testdb.db";
    private static DaoMaster daoMaster = null;
    private static DaoSession daoSession = null;
    private UserInfoDao userDao = null;
    private BookInfoDao bookDao = null;
    private static DBHelper instance = null;

    private DBHelper(Context context) {
        DaoSession daoSession = getDaoSession(context);
        userDao = daoSession.getUserInfoDao();
        bookDao = daoSession.getBookInfoDao();
    }

    public static DBHelper getInstance(Context context) {
        if (null == instance) {
            synchronized (DBHelper.class) {
                if (null == instance) {
                    instance = new DBHelper(context.getApplicationContext());
                }
            }
        }
        return instance;
    }

    /*
      * 取得DaoMaster
      *
      * @param context
      * @return
      * */
    public DaoMaster getDaoMaster(Context context) {
        if (daoMaster == null) {
            DaoMaster.OpenHelper helper = new DaoMaster.DevOpenHelper(context,
                    DB_NAME, null);
            daoMaster = new DaoMaster(helper.getWritableDatabase());
        }
        return daoMaster;
    }

    /*
     * 取得DaoSession
     *
     * @param context
     * @return
     */
    public DaoSession getDaoSession(Context context) {
        if (daoSession == null) {
            if (daoMaster == null) {
                daoMaster = getDaoMaster(context);
            }
            daoSession = daoMaster.newSession();
        }
        return daoSession;
    }

    public UserInfoDao getUserDao() {
        return userDao;
    }

    public BookInfoDao getBookDao() {
        return bookDao;
    }
}
