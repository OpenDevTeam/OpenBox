package com.open.sdk.studyoscommon.database;

import java.util.HashMap;
import java.util.List;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;


public class DBCodeGenerate {

    /**
     * 数据库版本
     */
    private int dbVersion = 101;
    /**
     * 生成代码所在的包路径
     */
    private String codePkg = "com.open.studyos.db.autocode";

    /**
     * 成功生成代码
     */
    public static final String SUCCESS = "SUCCESS";

    /**
     * 生成代码失败
     */
    public static final String FAILURE = "FAILURE";

    private Schema schema = null;

    /**
     * 一个Entity代表有一个表，以此类推
     * String:表对应的对象名，即className
     */
    private HashMap<String, Entity> entityList = null;

    /**
     * 一个Property代表一个字段
     * 第一个String:表对应的对象名，即className
     * 第二个String:对象里面的属性名
     */
    private HashMap<String, HashMap<String, Property>> propertyList = null;

    private static DBCodeGenerate instance = null;

    private DBCodeGenerate() {
        schema = new Schema(dbVersion, codePkg);
        // 使下次代码生成的时候不覆盖keep内的代码
        schema.enableKeepSectionsByDefault();
        entityList = new HashMap<String, Entity>();
        propertyList = new HashMap<String, HashMap<String, Property>>();
    }

    public static DBCodeGenerate getInstance() {
        if (null == instance) {
            synchronized (DBCodeGenerate.class) {
                if (null == instance) {
                    instance = new DBCodeGenerate();
                }
            }
        }
        return instance;
    }

    /**
     * 获取所有表实体
     */
    public HashMap<String, Entity> getEntityList() {
        return entityList;
    }

    /**
     * 获取所有表实体里面的字段
     * 通过获取此结果可以设置多表关联，如1对n
     */
    public HashMap<String, HashMap<String, Property>> getPropertyList() {
        return propertyList;
    }

    /**
     * 设置表及其字段
     */
    public void initTables(List<DBTable> tableList) {
        Entity entity = null;
        HashMap<String, Property> propertyHashMap = null;
        for (DBTable dbtable : tableList) {
            entity = schema.addEntity(dbtable.getTableObject());
            entity.setTableName(dbtable.getTableName());
            entityList.put(dbtable.getTableObject(), entity);
            propertyHashMap = new HashMap<String, Property>();
            for (DBField dbField : dbtable.getFieldList()) {
                Property property = null;
                if (dbField.isPrimaryKey() || dbField.isAutoIncrement()) {
                    if (dbField.isPrimaryKey() && !dbField.isAutoIncrement()) {
                        property = entity.addProperty(dbField.getFieldType(), dbField.getFieldName()).primaryKey().getProperty();
                    } else if (dbField.isAutoIncrement() && !dbField.isPrimaryKey()) {
                        property = entity.addProperty(dbField.getFieldType(), dbField.getFieldName()).autoincrement().getProperty();
                    } else {
                        property = entity.addProperty(dbField.getFieldType(), dbField.getFieldName()).primaryKey().autoincrement().getProperty();
                    }
                } else {
                    property = entity.addProperty(dbField.getFieldType(), dbField.getFieldName()).getProperty();
                }
                propertyHashMap.put(dbField.getFieldName(), property);
            }
            propertyList.put(dbtable.getTableObject(), propertyHashMap);
        }
    }

    /**
     * 生成代码
     *
     * @param outputPath 代码存放的绝对路径
     * @return 返回是否生成成功
     */
    public String doGenerate(String outputPath) {
        if (schema.getEntities() == null || schema.getEntities().size() == 0) {
            return FAILURE;
        }
        try {
            new DaoGenerator().generateAll(schema, outputPath);
            return SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return FAILURE;
        }
    }
}
