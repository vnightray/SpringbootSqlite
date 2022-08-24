package com.vnightray.springsqlitetest.domain;

import java.time.LocalDateTime;

public class ProEntity {

    private Integer id;

    private String databaseKey;

    private String databaseUrl;

    private LocalDateTime createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDatabaseKey() {
        return databaseKey;
    }

    public void setDatabaseKey(String databaseKey) {
        this.databaseKey = databaseKey;
    }

    public String getDatabaseUrl() {
        return databaseUrl;
    }

    public void setDatabaseUrl(String databaseUrl) {
        this.databaseUrl = databaseUrl;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "ProEntity{" +
                "id=" + id +
                ", databaseKey='" + databaseKey + '\'' +
                ", databaseUrl='" + databaseUrl + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}

