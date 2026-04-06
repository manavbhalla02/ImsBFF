package com.ims.bff.common.executor;

public interface Executor<T, E> {

    T execute(E context);
}
