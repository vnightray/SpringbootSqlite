package com.vnightray.springsqlitetest.utils;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.sqlite.JDBC;

import javax.sql.DataSource;

public class SqliteBuilder {

    private String filePath;

    private String url;

    public static SqliteBuilder create(){
        return new SqliteBuilder();
    }

    public SqliteBuilder filePath(String filePath){
        this.filePath = filePath;
        return this;
    }

    public SqliteBuilder url(String url) {
        this.url = url;
        return this;
    }

    public DataSource build() {
        if (StringUtils.isNoneEmpty(url)) {
            DruidDataSource ds = new DruidDataSource();
            ds.setUrl(url);
            ds.setDriverClassName(JDBC.class.getName());
            ds.setInitialSize(8);
            ds.setMaxActive(20);
            ds.setMaxWait(6000);
            ds.setMinIdle(1);
            return ds;
        }

        if (StringUtils.isNotEmpty(filePath)) {
            StringBuilder sb = new StringBuilder();
            sb.append("jdbc:sqlite:").append(filePath);

            DruidDataSource ds = new DruidDataSource();
            ds.setUrl(sb.toString());
            ds.setDriverClassName(JDBC.class.getName());
            return ds;
        }
        return DataSourceBuilder.create().build();
    }
}
