package com.mvp01.domain;

import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by wenjie on 16/5/11.
 */
@Document
@CompoundIndexes({
        @CompoundIndex(name = "collectionName_valueType_value", def = "{'collectionName': 1, 'valueType': 1, 'value': 1}", unique = true)
})
public class UniqueValue extends BaseBean {
    @Indexed
    private String valueId;
    private String collectionName;
    private String valueType;
    private Object value;


    public String getValueId() {
        return valueId;
    }

    public void setValueId(String valueId) {
        this.valueId = valueId;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getValueType() {
        return valueType;
    }

    public void setValueType(String valueType) {
        this.valueType = valueType;
    }

    public String getCollectionName() {
        return collectionName;
    }

    public void setCollectionName(String collectionName) {
        this.collectionName = collectionName;
    }
}
