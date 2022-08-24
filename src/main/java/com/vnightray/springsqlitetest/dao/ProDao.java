package com.vnightray.springsqlitetest.dao;

import com.vnightray.springsqlitetest.domain.ProEntity;

import java.util.List;

public interface ProDao {

    List<ProEntity> list();

    List<ProEntity> query(ProEntity proEntity);

    ProEntity findById(int id);

    int add(ProEntity proEntity);

    int delAll();

    int del(Integer id);
}
