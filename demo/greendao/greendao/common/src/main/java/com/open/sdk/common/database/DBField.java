package com.open.sdk.studyoscommon.database;

import de.greenrobot.daogenerator.PropertyType;

public class DBField {

    private PropertyType fieldType;
    private String fieldName;
    private boolean isPrimaryKey;
    private boolean isAutoIncrement;

    public DBField() {
    }

    public boolean isPrimaryKey() {
        return isPrimaryKey;
    }

    public void setIsPrimaryKey(boolean isPrimaryKey) {
        this.isPrimaryKey = isPrimaryKey;
    }

    public PropertyType getFieldType() {
        return fieldType;
    }

    public void setFieldType(PropertyType fieldType) {
        this.fieldType = fieldType;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public boolean isAutoIncrement() {
        return isAutoIncrement;
    }

    public void setIsAutoIncrement(boolean isAutoIncrement) {
        this.isAutoIncrement = isAutoIncrement;
    }

    public DBField(PropertyType fieldType, String fieldName, boolean isPrimaryKey, boolean isAutoIncrement) {
        this.fieldType = fieldType;
        this.fieldName = fieldName;
        this.isPrimaryKey = isPrimaryKey;
        this.isAutoIncrement = isAutoIncrement;
    }
}
