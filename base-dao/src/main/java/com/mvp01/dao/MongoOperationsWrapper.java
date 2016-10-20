package com.mvp01.dao;

import com.mongodb.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.mongodb.core.*;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.data.mongodb.core.mapreduce.GroupBy;
import org.springframework.data.mongodb.core.mapreduce.GroupByResults;
import org.springframework.data.mongodb.core.mapreduce.MapReduceOptions;
import org.springframework.data.mongodb.core.mapreduce.MapReduceResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.NearQuery;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.util.CloseableIterator;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Created by wenjie on 16/4/11.
 */
public abstract class MongoOperationsWrapper implements MongoOperations {
    @Autowired
    protected MongoTemplate mongoTemplate;

    @Override
    public String getCollectionName(Class<?> aClass) {
        return mongoTemplate.getCollectionName(aClass);
    }

    @Override
    public CommandResult executeCommand(String s) {
        return mongoTemplate.executeCommand(s);
    }

    @Override
    public CommandResult executeCommand(DBObject dbObject) {
        return mongoTemplate.executeCommand(dbObject);
    }

    @Override
    @Deprecated
    public CommandResult executeCommand(DBObject dbObject, int i) {
        return mongoTemplate.executeCommand(dbObject, i);
    }

    @Override
    public CommandResult executeCommand(DBObject dbObject, ReadPreference readPreference) {
        return mongoTemplate.executeCommand(dbObject, readPreference);
    }

    @Override
    public void executeQuery(Query query, String s, DocumentCallbackHandler documentCallbackHandler) {
        mongoTemplate.executeQuery(query, s, documentCallbackHandler);
    }

    @Override
    public <T> T execute(DbCallback<T> dbCallback) {
        return mongoTemplate.execute(dbCallback);
    }

    @Override
    public <T> T execute(Class<?> aClass, CollectionCallback<T> collectionCallback) {
        return mongoTemplate.execute(aClass, collectionCallback);
    }

    @Override
    public <T> T execute(String s, CollectionCallback<T> collectionCallback) {
        return mongoTemplate.execute(s, collectionCallback);
    }

    @Override
    @Deprecated
    public <T> T executeInSession(DbCallback<T> dbCallback) {
        return mongoTemplate.executeInSession(dbCallback);
    }

    @Override
    public <T> CloseableIterator<T> stream(Query query, Class<T> aClass) {
        return mongoTemplate.stream(query, aClass);
    }

    @Override
    public <T> DBCollection createCollection(Class<T> aClass) {
        return mongoTemplate.createCollection(aClass);
    }

    @Override
    public <T> DBCollection createCollection(Class<T> aClass, CollectionOptions collectionOptions) {
        return mongoTemplate.createCollection(aClass, collectionOptions);
    }

    @Override
    public DBCollection createCollection(String s) {
        return mongoTemplate.createCollection(s);
    }

    @Override
    public DBCollection createCollection(String s, CollectionOptions collectionOptions) {
        return mongoTemplate.createCollection(s, collectionOptions);
    }

    @Override
    public Set<String> getCollectionNames() {
        return mongoTemplate.getCollectionNames();
    }

    @Override
    public DBCollection getCollection(String s) {
        return mongoTemplate.getCollection(s);
    }

    @Override
    public <T> boolean collectionExists(Class<T> aClass) {
        return mongoTemplate.collectionExists(aClass);
    }

    @Override
    public boolean collectionExists(String s) {
        return mongoTemplate.collectionExists(s);
    }

    @Override
    public <T> void dropCollection(Class<T> aClass) {
        mongoTemplate.dropCollection(aClass);
    }

    @Override
    public void dropCollection(String s) {
        mongoTemplate.dropCollection(s);
    }

    @Override
    public IndexOperations indexOps(String s) {
        return mongoTemplate.indexOps(s);
    }

    @Override
    public IndexOperations indexOps(Class<?> aClass) {
        return mongoTemplate.indexOps(aClass);
    }

    @Override
    public ScriptOperations scriptOps() {
        return mongoTemplate.scriptOps();
    }

    @Override
    public <T> List<T> findAll(Class<T> aClass) {
        return mongoTemplate.findAll(aClass);
    }

    @Override
    public <T> List<T> findAll(Class<T> aClass, String s) {
        return mongoTemplate.findAll(aClass, s);
    }

    @Override
    public <T> GroupByResults<T> group(String s, GroupBy groupBy, Class<T> aClass) {
        return mongoTemplate.group(s, groupBy, aClass);
    }

    @Override
    public <T> GroupByResults<T> group(Criteria criteria, String s, GroupBy groupBy, Class<T> aClass) {
        return mongoTemplate.group(criteria, s, groupBy, aClass);
    }

    @Override
    public <O> AggregationResults<O> aggregate(TypedAggregation<?> typedAggregation, String s, Class<O> aClass) {
        return mongoTemplate.aggregate(typedAggregation, s, aClass);
    }

    @Override
    public <O> AggregationResults<O> aggregate(TypedAggregation<?> typedAggregation, Class<O> aClass) {
        return mongoTemplate.aggregate(typedAggregation, aClass);
    }

    @Override
    public <O> AggregationResults<O> aggregate(Aggregation aggregation, Class<?> aClass, Class<O> aClass1) {
        return mongoTemplate.aggregate(aggregation, aClass, aClass1);
    }

    @Override
    public <O> AggregationResults<O> aggregate(Aggregation aggregation, String s, Class<O> aClass) {
        return mongoTemplate.aggregate(aggregation, s, aClass);
    }

    @Override
    public <T> MapReduceResults<T> mapReduce(String s, String s1, String s2, Class<T> aClass) {
        return mongoTemplate.mapReduce(s, s1, s2, aClass);
    }

    @Override
    public <T> MapReduceResults<T> mapReduce(String s, String s1, String s2, MapReduceOptions mapReduceOptions, Class<T> aClass) {
        return mongoTemplate.mapReduce(s, s1, s2, mapReduceOptions, aClass);
    }

    @Override
    public <T> MapReduceResults<T> mapReduce(Query query, String s, String s1, String s2, Class<T> aClass) {
        return mongoTemplate.mapReduce(query, s, s1, s2, aClass);
    }

    @Override
    public <T> MapReduceResults<T> mapReduce(Query query, String s, String s1, String s2, MapReduceOptions mapReduceOptions, Class<T> aClass) {
        return mongoTemplate.mapReduce(query, s, s1, s2, mapReduceOptions, aClass);
    }

    @Override
    public <T> GeoResults<T> geoNear(NearQuery nearQuery, Class<T> aClass) {
        return mongoTemplate.geoNear(nearQuery, aClass);
    }

    @Override
    public <T> GeoResults<T> geoNear(NearQuery nearQuery, Class<T> aClass, String s) {
        return mongoTemplate.geoNear(nearQuery, aClass, s);
    }

    @Override
    public <T> T findOne(Query query, Class<T> aClass) {
        return mongoTemplate.findOne(query, aClass);
    }

    @Override
    public <T> T findOne(Query query, Class<T> aClass, String s) {
        return mongoTemplate.findOne(query, aClass, s);
    }

    @Override
    public boolean exists(Query query, String s) {
        return mongoTemplate.exists(query, s);
    }

    @Override
    public boolean exists(Query query, Class<?> aClass) {
        return mongoTemplate.exists(query, aClass);
    }

    @Override
    public boolean exists(Query query, Class<?> aClass, String s) {
        return mongoTemplate.exists(query, aClass, s);
    }

    @Override
    public <T> List<T> find(Query query, Class<T> aClass) {
        return mongoTemplate.find(query, aClass);
    }

    @Override
    public <T> List<T> find(Query query, Class<T> aClass, String s) {
        return mongoTemplate.find(query, aClass, s);
    }

    @Override
    public <T> T findById(Object o, Class<T> aClass) {
        return mongoTemplate.findById(o, aClass);
    }

    @Override
    public <T> T findById(Object o, Class<T> aClass, String s) {
        return mongoTemplate.findById(o, aClass, s);
    }

    @Override
    public <T> T findAndModify(Query query, Update update, Class<T> aClass) {
        return mongoTemplate.findAndModify(query, update, aClass);
    }

    @Override
    public <T> T findAndModify(Query query, Update update, Class<T> aClass, String s) {
        return mongoTemplate.findAndModify(query, update, aClass, s);
    }

    @Override
    public <T> T findAndModify(Query query, Update update, FindAndModifyOptions findAndModifyOptions, Class<T> aClass) {
        return mongoTemplate.findAndModify(query, update, findAndModifyOptions, aClass);
    }

    @Override
    public <T> T findAndModify(Query query, Update update, FindAndModifyOptions findAndModifyOptions, Class<T> aClass, String s) {
        return mongoTemplate.findAndModify(query, update, findAndModifyOptions, aClass, s);
    }

    @Override
    public <T> T findAndRemove(Query query, Class<T> aClass) {
        return mongoTemplate.findAndRemove(query, aClass);
    }

    @Override
    public <T> T findAndRemove(Query query, Class<T> aClass, String s) {
        return mongoTemplate.findAndRemove(query, aClass, s);
    }

    @Override
    public long count(Query query, Class<?> aClass) {
        return mongoTemplate.count(query, aClass);
    }

    @Override
    public long count(Query query, String s) {
        return mongoTemplate.count(query, s);
    }

    @Override
    public long count(Query query, Class<?> aClass, String s) {
        return mongoTemplate.count(query, aClass, s);
    }

    @Override
    public void insert(Object o) {
        mongoTemplate.insert(o);
    }

    @Override
    public void insert(Object o, String s) {
        mongoTemplate.insert(o, s);
    }

    @Override
    public void insert(Collection<? extends Object> collection, Class<?> aClass) {
        mongoTemplate.insert(collection, aClass);
    }

    @Override
    public void insert(Collection<? extends Object> collection, String s) {
        mongoTemplate.insert(collection, s);
    }

    @Override
    public void insertAll(Collection<? extends Object> collection) {
        mongoTemplate.insertAll(collection);
    }

    @Override
    public void save(Object o) {
        mongoTemplate.save(o);
    }

    @Override
    public void save(Object o, String s) {
        mongoTemplate.save(o, s);
    }

    @Override
    public WriteResult upsert(Query query, Update update, Class<?> aClass) {
        return mongoTemplate.upsert(query, update, aClass);
    }

    @Override
    public WriteResult upsert(Query query, Update update, String s) {
        return mongoTemplate.upsert(query, update, s);
    }

    @Override
    public WriteResult upsert(Query query, Update update, Class<?> aClass, String s) {
        return mongoTemplate.upsert(query, update, aClass, s);
    }

    @Override
    public WriteResult updateFirst(Query query, Update update, Class<?> aClass) {
        return mongoTemplate.updateFirst(query, update, aClass);
    }

    @Override
    public WriteResult updateFirst(Query query, Update update, String s) {
        return mongoTemplate.updateFirst(query, update, s);
    }

    @Override
    public WriteResult updateFirst(Query query, Update update, Class<?> aClass, String s) {
        return mongoTemplate.updateFirst(query, update, aClass, s);
    }

    @Override
    public WriteResult updateMulti(Query query, Update update, Class<?> aClass) {
        return mongoTemplate.updateMulti(query, update, aClass);
    }

    @Override
    public WriteResult updateMulti(Query query, Update update, String s) {
        return mongoTemplate.updateMulti(query, update, s);
    }

    @Override
    public WriteResult updateMulti(Query query, Update update, Class<?> aClass, String s) {
        return mongoTemplate.updateMulti(query, update, aClass, s);
    }

    @Override
    public WriteResult remove(Object o) {
        return mongoTemplate.remove(o);
    }

    @Override
    public WriteResult remove(Object o, String s) {
        return mongoTemplate.remove(o, s);
    }

    @Override
    public WriteResult remove(Query query, Class<?> aClass) {
        return mongoTemplate.remove(query, aClass);
    }

    @Override
    public WriteResult remove(Query query, Class<?> aClass, String s) {
        return mongoTemplate.remove(query, aClass, s);
    }

    @Override
    public WriteResult remove(Query query, String s) {
        return mongoTemplate.remove(query, s);
    }

    @Override
    public <T> List<T> findAllAndRemove(Query query, String s) {
        return mongoTemplate.findAllAndRemove(query, s);
    }

    @Override
    public <T> List<T> findAllAndRemove(Query query, Class<T> aClass) {
        return mongoTemplate.findAllAndRemove(query, aClass);
    }

    @Override
    public <T> List<T> findAllAndRemove(Query query, Class<T> aClass, String s) {
        return mongoTemplate.findAllAndRemove(query, aClass, s);
    }

    @Override
    public MongoConverter getConverter() {
        return mongoTemplate.getConverter();
    }
}
