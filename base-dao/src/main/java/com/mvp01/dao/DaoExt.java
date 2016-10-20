package com.mvp01.dao;

import com.mongodb.DBObject;
import com.mongodb.WriteResult;
import com.mongodb.util.JSON;
import com.mvp01.common.utils.AppGsonUtil;
import com.mvp01.domain.BaseBean;
import com.mvp01.domain.UniqueValue;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.lang.reflect.InvocationTargetException;
import java.util.*;


/**
 * Created by wenjie on 16/4/11.
 */
@Repository
public class DaoExt {
    @Autowired
    protected MongoTemplate mongoTemplate;

    public ObjectId toObjectId(String id) {
        return new ObjectId(id);
    }

    public <T extends BaseBean> T convert(Class<T> clss, DBObject dbObject) {
        return mongoTemplate.getConverter().read(clss, dbObject);
    }

    public <T extends BaseBean> List<String> findIds(List<T> list) {
        if (list != null && list.size() > 0) {
            List<String> ids = new ArrayList<>();
            for (T t : list) {
                ids.add(t.get_id());
            }
            return ids;
        }
        return Collections.EMPTY_LIST;
    }

    public <T extends BaseBean> Map<String, T> toMap(List<T> list) {
        if (list != null && list.size() > 0) {
            Map<String, T> map = new HashMap<String, T>();
            for (T t : list) {
                map.put(t.get_id(), t);
            }
            return map;
        }
        return Collections.EMPTY_MAP;
    }

    public <T extends BaseBean> List<T> findAlLAsList(List<String> ids, Class clss, String... fields) {
        Query query = Query.query(Criteria.where(BaseBean.ID).in(ids));
        handleFields(query, fields);
        return mongoTemplate.find(query, clss);
    }

    public <T extends BaseBean> Map<String, T> findAll(List<String> ids, Class clss, String... fields) {
        return findAll(ids, null, clss, fields);
    }

    public <T extends BaseBean> Map<String, T> findAll(List<String> ids, Query query, Class clss, String... fields) {
        if (ids == null || ids.isEmpty()) {
            return Collections.EMPTY_MAP;
        }
        if (query == null) {
            query = Query.query(Criteria.where(BaseBean.ID).in(ids));
        } else {
            query.addCriteria(Criteria.where(BaseBean.ID).in(ids));
        }

        handleFields(query, fields);
        List<T> list = mongoTemplate.find(query, clss);
        if (list != null && list.size() > 0) {
            Map<String, T> map = new HashMap<String, T>();
            for (T t : list) {
                map.put(t.get_id(), t);
            }
            return map;
        }
        return Collections.EMPTY_MAP;
    }

    public <T extends BaseBean> List<T> findAll(Class clss, String... fields) {
        if (fields != null && fields.length > 0) {
            Query query = new Query();
            handleFields(query, fields);
            return mongoTemplate.find(query, clss);
        }
        return mongoTemplate.findAll(clss);
    }

    public WriteResult remove(String id, Class clss) {
        return mongoTemplate.remove(generateQuery(id), clss);
    }

    public <T extends BaseBean> WriteResult remove(T query) {
        return mongoTemplate.remove(generateQuery(query), query.getClass());
    }

    public <T extends BaseBean> void insert(T t) {
        mongoTemplate.insert(t);
    }

    public <T extends BaseBean> boolean exists(T query) {
        return mongoTemplate.exists(generateQuery(query), query.getClass());
    }

    public <T extends BaseBean> T findOne(String id, Class clss, String... fields) {
        Query query1 = generateQuery(id);
        handleFields(query1, fields);
        return (T) mongoTemplate.findOne(query1, clss);
    }

    public <T extends BaseBean> T findOne(T query, String... fields) {
        Query query1 = generateQuery(query);
        handleFields(query1, fields);
        return (T) mongoTemplate.findOne(query1, query.getClass());
    }

    public <T extends BaseBean> List<T> find(T query, String... fields) {
        Query query1 = generateQuery(query);
        handleFields(query1, fields);
        return (List<T>) mongoTemplate.find(query1, query.getClass());
    }

    public UniqueValue findUniqueValue(String valueId, Class clss, String valueType) {
        if (StringUtils.isBlank(valueId) || StringUtils.isBlank(valueType) || clss == null) {
            return null;
        }
        UniqueValue uniqueValue = new UniqueValue();
        uniqueValue.setValueId(valueId);
        uniqueValue.setCollectionName(mongoTemplate.getCollectionName(clss));
        uniqueValue.setValueType(valueType);
        return findOne(uniqueValue);
    }

    public WriteResult updateUniqueValue(String valueId, Class clss, String valueType, Object value) {
        if (StringUtils.isBlank(valueId) || StringUtils.isBlank(valueType) || clss == null) {
            return null;
        }

        String collectinName = mongoTemplate.getCollectionName(clss);

        UniqueValue uniqueValueQuery = new UniqueValue();
        uniqueValueQuery.setValueId(valueId);
        uniqueValueQuery.setCollectionName(collectinName);
        uniqueValueQuery.setValueType(valueType);

        if (value == null) {
            return remove(uniqueValueQuery);
        } else {
            UniqueValue uniqueValueUpdate = new UniqueValue();
            uniqueValueUpdate.setValueId(valueId);
            uniqueValueUpdate.setCollectionName(collectinName);
            uniqueValueUpdate.setValueType(valueType);
            uniqueValueUpdate.setValue(value);
            return upsert(uniqueValueQuery, uniqueValueUpdate);
        }
    }

    public <T extends BaseBean> void save(T save) {
        if (StringUtils.isBlank(save.get_id())) {
            insert(save);
        } else {
            update(save);
        }
    }

    public <T extends BaseBean> WriteResult upsert(T query, T update) {
        return mongoTemplate.upsert(generateQuery(query), generateUpdate(update), query.getClass());
    }

    public <T extends BaseBean> WriteResult update(T query, T update) {
        return mongoTemplate.updateFirst(generateQuery(query), generateUpdate(update), query.getClass());
    }

    public <T extends BaseBean> WriteResult update(T update) {
        return mongoTemplate.updateFirst(generateQuery(update.get_id()), generateUpdate(update), update.getClass());
    }

    public <T extends BaseBean> T findAndModify(T query, T update) {
        return (T) mongoTemplate.findAndModify(generateQuery(query), generateUpdate(update), update.getClass());
    }

    public <T extends BaseBean> T findAndModify(T update) {
        return (T) mongoTemplate.findAndModify(generateQuery(update.get_id()), generateUpdate(update), update.getClass());
    }

    public <T extends BaseBean> long count(T q) {
        return mongoTemplate.count(generateQuery(q), q.getClass());
    }

    protected void handleFields(Query query, String... fields) {
        if (query != null && fields != null && fields.length > 0) {
            for (String field : fields) {
                query.fields().include(field);
            }
        }
    }

    protected static <T extends BaseBean> Update generateUpdate(T updateT) {
        if (false) {
            DBObject dbObject = (DBObject) JSON.parse(AppGsonUtil.toJson(updateT));
            Update update = new Update();
            Iterator<String> iterator = dbObject.keySet().iterator();
            while (iterator.hasNext()) {
                String key = iterator.next();
                update.set(key, dbObject.get(key));
            }

            return update;
        }
        Update update = new Update();
        toUpdate(update, "", updateT);
        return update;
    }

    protected static <T extends BaseBean> Query generateQuery(String id) {
        Query query = Query.query(Criteria.where(BaseBean.ID).is(id));
        return query;
    }

    protected static <T extends BaseBean> Query generateQuery(T q) {
        Query query = new Query();
        toQuery(query, "", q);
        return query;
    }

    private static void toUpdate(Update update, String parentKey, BaseBean parent) {
        try {
            Map<String, Object> upateMap = PropertyUtils.describe(parent);

            if (upateMap != null) {
                Iterator<Map.Entry<String, Object>> iterator = upateMap.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<String, Object> entry = iterator.next();
                    Class clss = PropertyUtils.getPropertyType(parent, entry.getKey());
                    if (int.class.equals(clss) ||
                            long.class.equals(clss) ||
                            float.class.equals(clss) ||
                            double.class.equals(clss) ||
                            char.class.equals(clss) ||
                            boolean.class.equals(clss) ||
                            byte.class.equals(clss) ||
                            short.class.equals(clss)) {
                        throw new IllegalArgumentException("不能使用基本类型做参数");
                    }
                    if ("class".equals(entry.getKey())) {
                    } else if (BaseBean.ID.equals(entry.getKey())) {
                    } else if (entry.getValue() instanceof BaseBean) {
                        toUpdate(update, parentKey + entry.getKey() + ".", (BaseBean) entry.getValue());
                    } else if (entry.getValue() != null) {
                        update.set(parentKey + entry.getKey(), entry.getValue());
                    }
                }
            }

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    private static void toQuery(Query query, String parentKey, BaseBean parent) {
        try {
            Map<String, Object> upateMap = PropertyUtils.describe(parent);

            if (upateMap != null) {
                Iterator<Map.Entry<String, Object>> iterator = upateMap.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<String, Object> entry = iterator.next();
                    Class clss = PropertyUtils.getPropertyType(parent, entry.getKey());
                    if (int.class.equals(clss) ||
                            long.class.equals(clss) ||
                            float.class.equals(clss) ||
                            double.class.equals(clss) ||
                            char.class.equals(clss) ||
                            boolean.class.equals(clss) ||
                            byte.class.equals(clss) ||
                            short.class.equals(clss)) {
                        throw new IllegalArgumentException("不能使用基本类型做参数");
                    }
                    if ("class".equals(entry.getKey())) {
                    } else if (entry.getValue() instanceof BaseBean) {
                        toQuery(query, parentKey + entry.getKey() + ".", (BaseBean) entry.getValue());
                    } else if (entry.getValue() != null) {
                        query.addCriteria(Criteria.where(parentKey + entry.getKey()).is(entry.getValue()));
                    }
                }
            }

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }


//    public Object exeScript(String file, Object... params) {
//        String script = readScript(file);
//        if (StringUtils.isBlank(script)) {
//            return null;
//        }
//
//        for (int i = 0; i < params.length; i++) {
//            params[i] = mongoTemplate.getConverter().convertToMongoType(params[i]);
//        }
//
//        ScriptOperations scriptOperations = mongoTemplate.scriptOps();
//        ExecutableMongoScript executableMongoScript = new ExecutableMongoScript(script);
//        return scriptOperations.execute(executableMongoScript, params);
//    }
//
//    public String readScript(String file) {
//        DefaultResourceLoader loader = new DefaultResourceLoader();
//        Resource resource = loader.getResource("classpath:/mongodb-script/" + file);
//        if (resource.exists()) {
//            try {
//                return FileUtils.readFileToString(resource.getFile());
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        return "";
//    }
//
//    public <T extends BaseBean> List<T> convert(Class<T> clss, BasicDBList list) {
//        if (list == null) {
//            return null;
//        }
//        List<T> newList = new ArrayList<T>();
//        for (Object dbObject : list) {
//            newList.add(convert(clss, (DBObject) dbObject));
//        }
//        return newList;
//    }
//
}
