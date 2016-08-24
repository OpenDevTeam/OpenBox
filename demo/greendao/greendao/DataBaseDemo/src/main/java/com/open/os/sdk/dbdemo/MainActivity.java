package com.open.studyos.sdk.dbdemo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.open.studyos.db.autocode.BookInfo;
import com.open.studyos.db.autocode.BookInfoDao;
import com.open.studyos.db.autocode.UserInfo;
import com.open.studyos.db.autocode.UserInfoDao;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import de.greenrobot.dao.query.QueryBuilder;

public class MainActivity extends Activity implements View.OnClickListener {

    BookInfoDao bookInfoDao = null;
    UserInfoDao userInfoDao = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);

        bookInfoDao = DBHelper.getInstance(this).getBookDao();
        userInfoDao = DBHelper.getInstance(this).getUserDao();

        findViewById(R.id.insert_button).setOnClickListener(this);
        findViewById(R.id.update_button).setOnClickListener(this);
        findViewById(R.id.query_button).setOnClickListener(this);
        findViewById(R.id.delete_button).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.insert_button) {
            exeInsert();
        } else if (v.getId() == R.id.update_button) {
            exeUpdate();
        } else if (v.getId() == R.id.query_button) {
            exeQuery();
        } else if (v.getId() == R.id.delete_button) {
            exeDelete();
        }
    }

    private void exeInsert() {
        List<UserInfo> userInfoList = new ArrayList<UserInfo>();

        UserInfo zhangSan = new UserInfo();
        zhangSan.setUserId(UUID.randomUUID().toString().replace("-", ""));
        zhangSan.setUserName("张三");
        zhangSan.setUserPwd("123");
        userInfoList.add(zhangSan);

        UserInfo liSi = new UserInfo();
        liSi.setUserId(UUID.randomUUID().toString().replace("-", ""));
        liSi.setUserName("李四");
        liSi.setUserPwd("123456");
        userInfoList.add(liSi);

        userInfoDao.insertInTx(userInfoList);

        List<BookInfo> bookList = new ArrayList<BookInfo>();
        BookInfo bookInfoa = new BookInfo();
        bookInfoa.setUserId(zhangSan.getUserId());
        bookInfoa.setBookName("明朝那些事儿");
        bookInfoa.setBookId(UUID.randomUUID().toString().replace("-", ""));
        bookList.add(bookInfoa);
        BookInfo bookInfob = new BookInfo();
        bookInfob.setUserId(zhangSan.getUserId());
        bookInfob.setBookName("白鹿原");
        bookInfob.setBookId(UUID.randomUUID().toString().replace("-", ""));
        bookList.add(bookInfob);

        bookInfoDao.insertInTx(bookList);
    }

    private void exeUpdate() {
        UserInfo userInfo = userInfoDao.load("3692cd24423042f58c2ef81f8c3906c0");
        userInfo.setUserName("冯小刚");
        userInfo.setUserPwd("123456789");
        userInfoDao.update(userInfo);
    }

    private void exeQuery() {
        QueryBuilder queryBuilder = userInfoDao.queryBuilder();
        queryBuilder.where(UserInfoDao.Properties.UserName.like("%n%"), UserInfoDao.Properties.UserPwd.like("%123%"));
        queryBuilder.orderDesc(UserInfoDao.Properties.UserId);
        queryBuilder.build();
        List<UserInfo> userInfoList = queryBuilder.list();
        Log.i("hecp", "userInfoList.size=" + userInfoList.size());
        for (UserInfo userBean : userInfoList) {
            Log.i("hecp", userBean.getUserName() + " ,拥有的书籍数=" + userBean.getBookInfoList().size());
            Log.i("hecp", new Gson().toJson(userBean.getBookInfoList()));
        }
    }

    private void exeDelete() {
        // 根据Id删除
        userInfoDao.deleteByKey("4011c8a859a843029de9770bee064afe");
    }
}


