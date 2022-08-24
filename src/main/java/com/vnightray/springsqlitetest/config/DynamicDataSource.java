package com.vnightray.springsqlitetest.config;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DynamicDataSource extends AbstractRoutingDataSource {

    private volatile static DynamicDataSource dynamicDataSource;

    /**
     * 数据源存储用map
     */
    private final Map<Object, Object> datasourceMap = new ConcurrentHashMap<>();

    /**
     * 当前线程数据源key
     */
    private final ThreadLocal<String> contextHolder = new ThreadLocal<>();

    @Override
    protected Object determineCurrentLookupKey() {
        return contextHolder.get();
    }

    /**
     * 动态数据源构造方法
     * @param defaultDataSource
     */
    public DynamicDataSource(DataSource defaultDataSource) {
        //存入主数据源
        datasourceMap.put("master",defaultDataSource);
        //设定目标数据源map
        setTargetDataSources(datasourceMap);
        //设定默认数据源
        setDefaultTargetDataSource(defaultDataSource);
        dynamicDataSource = this;
    }


    /**
     * 判断当前数据源是否存在
     *
     * @param dataBaseKey
     * @return
     */
    public static boolean isExistDataSource(String dataBaseKey) {
        if (dynamicDataSource != null) {
            return dynamicDataSource.getDataSourceMap().containsKey(dataBaseKey);
        } else {
            return false;
        }
    }


    /**
     * 切换数据源
     *
     * @param key
     */
    public static void setDataSourceKey(String key) {
        if (dynamicDataSource != null) {
            dynamicDataSource.getContextHolder().set(key);
            dynamicDataSource.afterPropertiesSet();
        }
    }

    /**
     * 获取当前数据源
     *
     * @return
     */
    public static String getDataSourceKey() {
        if (dynamicDataSource != null) {
            return dynamicDataSource.getContextHolder().get();
        } else {
            return null;
        }
    }


    /**
     * 切换到默认数据源
     */
    public static void clearDataSourceKey() {
        if (dynamicDataSource != null) {
            dynamicDataSource.getContextHolder().remove();
        }
    }

    /**
     * 根据数据源key设置数据源
     *
     * @param datatBase
     * @return
     */
    public static void addDataSource(String datatBase, DataSource dataSource) {
        if (dynamicDataSource == null) {
            return;
        }
        // 没有数据源时添加数据源,有数据源直接使用
        if (!isExistDataSource(datatBase)) {
            // 新增数据源
            dynamicDataSource.getDataSourceMap().put(datatBase, dataSource);
        }
        // 切换数据源
        checkoutDataSource(datatBase);
    }

    /**
     * 切换数据源
     *
     * @param databaseKey
     */
    public static void checkoutDataSource(String databaseKey) {
        if (dynamicDataSource != null) {
            // 切换数据源
            setDataSourceKey(databaseKey);
            dynamicDataSource.afterPropertiesSet();
        }
    }

    public Map<Object, Object> getDataSourceMap() {
        return datasourceMap;
    }

    public ThreadLocal<String> getContextHolder() {
        return contextHolder;
    }


}
