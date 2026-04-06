package com.ims.bff.common.dao;

import com.ims.bff.common.component.SupportsType;

public interface TypedDao<K, T> extends Dao<T>, SupportsType<K> {
}
