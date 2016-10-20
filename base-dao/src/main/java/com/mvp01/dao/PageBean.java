package com.mvp01.dao;

import com.mvp01.common.exception.ParamException;
import com.mvp01.domain.BaseBean;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResult;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.NearQuery;
import org.springframework.data.mongodb.core.query.Query;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gezhizheng on 15/10/11
 */
public class PageBean<T extends BaseBean> {
    //需要设置的参数
    private int pageSize = 10;//每页大小
    private int page = 1;//页码
    private List<T> list;

    //不需要设置的参数
    private int totalDataCount;//数据总条数
    private int totalPage;//总页数
    private int dataCount;//当前获取到得数据条数
    private boolean hasNext = false;//是否有下一页
    private boolean hasPrevious = false;//是否有前一页
    private String pageMsg = "";

    public PageBean() {
    }

    public PageBean(Integer pageSize, Integer page) {
        if (pageSize == null) {
            pageSize = 10;
        }

        if (page == null) {
            page = 1;
        }

        if (pageSize == 0) {
            pageSize = 10;
        }

        if (page <= 0) {
            page = 1;
        }

        this.pageSize = pageSize;
        this.page = page;

    }

    public void execute(Query query, Class<T> clazz, MongoTemplate mongoTemplate) {
        totalDataCount = (int) mongoTemplate.count(query, clazz);
        //查询数据条数
        totalPage = totalDataCount % pageSize == 0 ? totalDataCount / pageSize : totalDataCount / pageSize + 1;
        int skip = (page - 1) * pageSize;
        query.skip(skip).limit(this.getPageSize());
        list = mongoTemplate.find(query, clazz);
        dataCount = list.size();
        if (page > 1) {
            this.hasPrevious = true;
        }
        if (page < totalPage) {
            this.hasNext = true;
        }
        if (totalPage <= 1) {
            this.hasNext = false;
            this.hasPrevious = false;
        }
//        this.pageMsg = "共" + totalDataCount + "条,共" + totalPage + "页,当前" + page + "页" ;
        StringBuilder pageMsgBuilder = new StringBuilder();
        pageMsgBuilder.append("共")
                .append(totalDataCount)
                .append("条,共")
                .append(totalPage)
                .append("页,当前")
                .append(page)
                .append("页");
        pageMsg = pageMsgBuilder.toString();
    }

    public void execute(NearQuery nearQuery, Query query, Class<T> clazz, MongoTemplate mongoTemplate) {
        totalDataCount = (int) mongoTemplate.count(query,clazz);
        //查询数据条数
        totalPage = totalDataCount % pageSize == 0 ? totalDataCount / pageSize : totalDataCount / pageSize + 1;
        int skip = (page - 1) * pageSize;
        nearQuery.query(query);
        nearQuery.skip(skip).num(this.getPageSize()+skip);
//        int skip = (page - 1) * pageSize;
//        query.skip(skip).limit(this.getPageSize());
        GeoResults<T> geoResults = mongoTemplate.geoNear(nearQuery,clazz);
        list = new ArrayList<T>();
        for (GeoResult geoResult : geoResults) {
            Distance distance = geoResult.getDistance();
            T t = (T) geoResult.getContent();
            t.setExtValue("distance",distance.getValue());
            list.add(t);
        }

//        GeoNearOperation geoNearOperation = Aggregation.geoNear(nearQuery, "distance");
//        TypedAggregation<T> typedAggregation = TypedAggregation.newAggregation(clazz, geoNearOperation,Aggregation.skip(skip));
//        AggregationResults<BasicDBObject> aggregationResults = mongoTemplate.aggregate(typedAggregation, BasicDBObject.class);

        dataCount = list.size();
        if (page > 1) {
            this.hasPrevious = true;
        }
        if (page < totalPage) {
            this.hasNext = true;
        }
        if (totalPage <= 1) {
            this.hasNext = false;
            this.hasPrevious = false;
        }
//        this.pageMsg = "共" + totalDataCount + "条,共" + totalPage + "页,当前" + page + "页" ;
        StringBuilder pageMsgBuilder = new StringBuilder();
        pageMsgBuilder.append("共")
                .append(totalDataCount)
                .append("条,共")
                .append(totalPage)
                .append("页,当前")
                .append(page)
                .append("页");
        pageMsg = pageMsgBuilder.toString();
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getDataCount() {
        return dataCount;
    }

    public List<T> getList() {
        if (list == null) {
            throw new ParamException("请设置数据列表参数：list");
        }
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public int getPage() {
        return page;
    }

    public int getTotalDataCount() {
        return totalDataCount;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public boolean isHasNext() {
        return hasNext;
    }

    public boolean isHasPrevious() {
        return hasPrevious;
    }

    public String getPageMsg() {
        return pageMsg;
    }

}
