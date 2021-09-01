package com.skywaet.vtbmarket.dao;

import com.skywaet.vtbmarket.model.BaseEntity;

import java.util.List;

public interface BaseDao<T extends BaseEntity> {
    List<T> findAll();

    T findById(Long id);

    T create(T newEntity);

    T update(Long id, T newEntity);

    void delete(Long id);

    void deleteAll();

}
