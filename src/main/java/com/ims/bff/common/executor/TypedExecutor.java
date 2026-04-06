package com.ims.bff.common.executor;

import com.ims.bff.common.component.SupportsType;

public interface TypedExecutor<K, T, E> extends Executor<T, E>, SupportsType<K> {
}
