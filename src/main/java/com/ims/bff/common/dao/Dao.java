package com.ims.bff.common.dao;

public interface Dao<T> {

    T save(T payload);
}
