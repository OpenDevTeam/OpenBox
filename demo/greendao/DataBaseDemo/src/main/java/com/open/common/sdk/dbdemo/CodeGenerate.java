package com.eebbk.studyos.sdk.dbdemo;

import android.util.LruCache;

import com.eebbk.sdk.studyoscommon.database.DBCodeGenerate;
import com.eebbk.sdk.studyoscommon.database.DBField;
import com.eebbk.sdk.studyoscommon.database.DBTable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.PropertyType;
import de.greenrobot.daogenerator.ToMany;

/**
 * 文  件：CodeGenerate.java
 * 公  司：步步高教育电子
 * 日  期：2016/6/27  20:33
 * 作  者：HeChangPeng
 */
public class CodeGenerate {

    private static final String USER_CLASS_NAME = "UserInfo";
    private static final String USER_TABLE_NAME = "user_table";

    private static final String BOOK_CLASS_NAME = "BookInfo";
    private static final String BOOK_TABLE_NAME = "book_table";

    public static void main(String[] args) {
        generateCode();
    }

    private static void generateCode() {
        List<DBTable> tableList = new ArrayList<DBTable>();

        /*********** 用户信息表 ***********/
        DBTable userTable = new DBTable();
        userTable.setTableObject(USER_CLASS_NAME);
        userTable.setTableName(USER_TABLE_NAME);

        List<DBField> userFields = new ArrayList<DBField>();
        DBField userId = new DBField(PropertyType.String, "userId", true, false);
        userFields.add(userId);
        DBField userName = new DBField(PropertyType.String, "userName", false, false);
        userFields.add(userName);
        DBField userPwd = new DBField(PropertyType.String, "userPwd", false, false);
        userFields.add(userPwd);

        userTable.setFieldList(userFields);
        tableList.add(userTable);
        /*********** End *****************/

        /*********** 书籍信息表 ***********/
        DBTable bookTable = new DBTable();
        bookTable.setTableObject(BOOK_CLASS_NAME);
        bookTable.setTableName(BOOK_TABLE_NAME);

        List<DBField> bookFields = new ArrayList<DBField>();
        DBField bookId = new DBField(PropertyType.String, "bookId", true, false);
        bookFields.add(bookId);
        DBField bookName = new DBField(PropertyType.String, "bookName", false, false);
        bookFields.add(bookName);

        bookTable.setFieldList(bookFields);
        tableList.add(bookTable);
        /*********** End *****************/


        DBCodeGenerate.getInstance().initTables(tableList);


        // 此处可进行多表关联的设置
        HashMap<String, Entity> entityList = DBCodeGenerate.getInstance().getEntityList();
        HashMap<String, HashMap<String, Property>> propertyList = DBCodeGenerate.getInstance().getPropertyList();
        HashMap<String, Property> userPropertyList = propertyList.get(USER_CLASS_NAME);
        HashMap<String, Property> bookPropertyList = propertyList.get(BOOK_CLASS_NAME);
        Entity userEntity = entityList.get(USER_CLASS_NAME);
        Entity bookEntity = entityList.get(BOOK_CLASS_NAME);

        // 一对一
        /*Property foreignKey = userEntity.addProperty(PropertyType.String, "appId").getProperty();
        ToOne toOne=userEntity.addToOne(appEntity, foreignKey);*/
        // 一对多
        Property foreignKey = bookEntity.addProperty(PropertyType.String, "userId").getProperty();
        ToMany toMany = userEntity.addToMany(bookEntity, foreignKey);
        LruCache<String, String> cache = new LruCache<String, String>(((int) (Runtime.getRuntime().maxMemory() / 1024)) / 4);


        // 最终生成代码
        DBCodeGenerate.getInstance().doGenerate("E:/java");
    }
}